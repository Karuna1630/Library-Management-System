package com.example.librarymanagementsystem.dao;

import com.example.librarymanagementsystem.model.Borrow;
import com.example.librarymanagementsystem.model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {
    private Connection connection;

    public BorrowDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addBorrow(Borrow borrow) throws SQLException {
        String sql = "INSERT INTO borrow (user_id, book_id, borrow_date, due_date, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, borrow.getUserId());
            stmt.setInt(2, borrow.getBookId());
            stmt.setTimestamp(3, new Timestamp(borrow.getBorrowDate().getTime()));
            stmt.setDate(4, new java.sql.Date(borrow.getDueDate().getTime()));
            stmt.setString(5, borrow.getStatus());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean hasActiveBorrow(int userId, int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM borrow WHERE user_id = ? AND book_id = ? AND status IN ('borrowed', 'overdue')";

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

    public List<Borrow> getActiveBorrowsByUser(int userId) throws SQLException {
        String sql = "SELECT b.*, bk.title, bk.author, bk.publication_year, bk.category, bk.book_image " +
                "FROM borrow b " +
                "JOIN books bk ON b.book_id = bk.book_id " +
                "WHERE b.user_id = ? AND b.status IN ('borrowed', 'overdue')";
        List<Borrow> borrows = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Borrow borrow = new Borrow();
                    borrow.setBorrowId(rs.getInt("borrow_id"));
                    borrow.setUserId(rs.getInt("user_id"));
                    borrow.setBookId(rs.getInt("book_id"));
                    borrow.setBorrowDate(rs.getTimestamp("borrow_date"));
                    borrow.setDueDate(rs.getDate("due_date"));
                    borrow.setReturnDate(rs.getDate("return_date"));
                    borrow.setFineAmount(rs.getDouble("fine_amount"));
                    borrow.setStatus(rs.getString("status"));

                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPublicationYear(rs.getDate("publication_year"));
                    book.setCategory(rs.getString("category"));
                    book.setImage(rs.getBytes("book_image"));
                    borrow.setBook(book);

                    borrows.add(borrow);
                }
            }
        }
        return borrows;
    }
}