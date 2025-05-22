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

    // Constructor to initialize the database connection
    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    // Adds a new reservation to the database
    public boolean addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (user_id, book_id, reservation_date, status, reserved_until) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getBookId());
            stmt.setTimestamp(3, new Timestamp(reservation.getReservationDate().getTime()));
            stmt.setString(4, reservation.getStatus());
            stmt.setDate(5, new java.sql.Date(reservation.getReservedUntil().getTime()));

            // Returns true if the insert was successful (at least one row affected)
            return stmt.executeUpdate() > 0;
        }
    }

    // Retrieves all active reservations for a given user, including book details
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
                    // Create a new Reservation object for each record
                    Reservation reservation = new Reservation();
                    reservation.setReservationId(rs.getInt("reservation_id"));
                    reservation.setUserId(rs.getInt("user_id"));
                    reservation.setBookId(rs.getInt("book_id"));
                    reservation.setReservationDate(rs.getTimestamp("reservation_date"));
                    reservation.setStatus(rs.getString("status"));
                    reservation.setReservedUntil(rs.getDate("reserved_until"));

                    // Populate Book details associated with the reservation
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

    // Retrieves a single reservation by its ID
    public Reservation getReservationById(int reservationId) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Populate Reservation object with data from the database
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
        // Return null if no reservation is found
        return null;
    }

    // Cancels a reservation by updating its status to 'cancelled'
    public boolean cancelReservation(int reservationId) throws SQLException {
        String sql = "UPDATE reservations SET status = 'cancelled' WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            // Returns true if the update was successful
            return stmt.executeUpdate() > 0;
        }
    }

    // Checks if a user has an active reservation for a specific book
    public boolean hasActiveReservation(int userId, int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND book_id = ? AND status = 'active'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Returns true if at least one active reservation exists
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Checks if a book has any active reservations
    public boolean hasActiveReservation(int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE book_id = ? AND status = 'active'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Returns true if at least one active reservation exists for the book
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Retrieves the earliest active reservation for a specific book, including book details
    public Reservation getEarliestActiveReservation(int bookId) throws SQLException {
        String sql = "SELECT r.*, b.title, b.author, b.publication_year, b.category, b.book_image " +
                "FROM reservations r " +
                "JOIN books b ON r.book_id = b.book_id " +
                "WHERE r.book_id = ? AND r.status = 'active' " +
                "ORDER BY r.reservation_date ASC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Populate Reservation object
                    Reservation reservation = new Reservation();
                    reservation.setReservationId(rs.getInt("reservation_id"));
                    reservation.setUserId(rs.getInt("user_id"));
                    reservation.setBookId(rs.getInt("book_id"));
                    reservation.setReservationDate(rs.getTimestamp("reservation_date"));
                    reservation.setStatus(rs.getString("status"));
                    reservation.setReservedUntil(rs.getDate("reserved_until"));

                    // Populate Book details
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPublicationYear(rs.getDate("publication_year"));
                    book.setCategory(rs.getString("category"));
                    book.setImage(rs.getBytes("book_image"));
                    reservation.setBook(book);

                    return reservation;
                }
            }
        }
        // Return null if no active reservation is found
        return null;
    }

    // Marks a reservation as fulfilled by updating its status
    public boolean markReservationAsFulfilled(int reservationId) throws SQLException {
        String sql = "UPDATE reservations SET status = 'fulfilled' WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Log success for debugging purposes
                System.out.println("Reservation " + reservationId + " marked as fulfilled successfully");
                return true;
            } else {
                // Log failure if no rows were updated
                System.out.println("Failed to mark reservation " + reservationId + " as fulfilled: No rows affected");
                return false;
            }
        } catch (SQLException e) {
            // Log SQL errors for debugging
            System.err.println("SQL Error while marking reservation " + reservationId + " as fulfilled: " + e.getMessage());
            throw e;
        }
    }
}