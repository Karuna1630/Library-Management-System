package com.example.librarymanagementsystem.dao;

import com.example.librarymanagementsystem.model.Reservation;
import com.example.librarymanagementsystem.model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private Connection connection;

    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (user_id, book_id, reservation_date, status, reserved_until) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getBookId());
            stmt.setTimestamp(3, new Timestamp(reservation.getReservationDate().getTime()));
            stmt.setString(4, reservation.getStatus());
            stmt.setDate(5, new java.sql.Date(reservation.getReservedUntil().getTime()));

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Reservation> getActiveReservationsByUser(int userId) throws SQLException {
        String sql = "SELECT r.*, b.title, b.author, b.publication_year, b.category, b.book_image " +
                "FROM reservations r " +
                "JOIN books b ON r.book_id = b.book_id " +
                "WHERE r.user_id = ? AND r.status = 'active'";
        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setReservationId(rs.getInt("reservation_id"));
                    reservation.setUserId(rs.getInt("user_id"));
                    reservation.setBookId(rs.getInt("book_id"));
                    reservation.setReservationDate(rs.getTimestamp("reservation_date"));
                    reservation.setStatus(rs.getString("status"));
                    reservation.setReservedUntil(rs.getDate("reserved_until"));

                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPublicationYear(rs.getDate("publication_year"));
                    book.setCategory(rs.getString("category"));
                    book.setImage(rs.getBytes("book_image"));
                    reservation.setBook(book);

                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    public Reservation getReservationById(int reservationId) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setReservationId(rs.getInt("reservation_id"));
                    reservation.setUserId(rs.getInt("user_id"));
                    reservation.setBookId(rs.getInt("book_id"));
                    reservation.setReservationDate(rs.getTimestamp("reservation_date"));
                    reservation.setStatus(rs.getString("status"));
                    reservation.setReservedUntil(rs.getDate("reserved_until"));
                    return reservation;
                }
            }
        }
        return null;
    }

    public boolean cancelReservation(int reservationId) throws SQLException {
        String sql = "UPDATE reservations SET status = 'cancelled' WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean hasActiveReservation(int userId, int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND book_id = ? AND status = 'active'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}