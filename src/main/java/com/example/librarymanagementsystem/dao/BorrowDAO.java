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
    // Database connection for executing queries
    private Connection connection;

    // Constructor to initialize the DAO with a database connection
    public BorrowDAO(Connection connection) {
        this.connection = connection;
    }

    // Adds a new borrow record to the database
    public boolean addBorrow(Borrow borrow) throws SQLException {
        // SQL query to insert a borrow record
        String sql = "INSERT INTO borrow (user_id, book_id, borrow_date, due_date, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set query parameters from the Borrow object
            stmt.setInt(1, borrow.getUserId());
            stmt.setInt(2, borrow.getBookId());
            stmt.setTimestamp(3, new Timestamp(borrow.getBorrowDate().getTime()));
            stmt.setDate(4, new java.sql.Date(borrow.getDueDate().getTime()));
            stmt.setString(5, borrow.getStatus());

            // Execute insert and return true if rows were affected
            return stmt.executeUpdate() > 0;
        }
    }

    // Checks if a user has an active borrow (borrowed or overdue) for a specific book
    public boolean hasActiveBorrow(int userId, int bookId) throws SQLException {
        // SQL query to count active borrow records for a user and book
        String sql = "SELECT COUNT(*) FROM borrow WHERE user_id = ? AND book_id = ? AND status IN ('borrowed', 'overdue')";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Return true if count is greater than 0
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false; // No active borrow found
    }

    // Retrieves all active borrows (borrowed or overdue) for a user, including book details
    public List<Borrow> getActiveBorrowsByUser(int userId) throws SQLException {
        // SQL query to select active borrows with joined book details
        String sql = "SELECT b.*, bk.title, bk.author, bk.publication_year, bk.category, bk.book_image " +
                "FROM borrow b " +
                "JOIN books bk ON b.book_id = bk.book_id " +
                "WHERE b.user_id = ? AND b.status IN ('borrowed', 'overdue')";
        List<Borrow> borrows = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Create Borrow object and populate fields
                    Borrow borrow = new Borrow();
                    borrow.setBorrowId(rs.getInt("borrow_id"));
                    borrow.setUserId(rs.getInt("user_id"));
                    borrow.setBookId(rs.getInt("book_id"));
                    borrow.setBorrowDate(rs.getTimestamp("borrow_date"));
                    borrow.setDueDate(rs.getDate("due_date"));
                    borrow.setReturnDate(rs.getDate("return_date"));
                    borrow.setFineAmount(rs.getDouble("fine_amount"));
                    borrow.setStatus(rs.getString("status"));

                    // Create Book object and populate fields
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

    // Retrieves all borrow records, including book and user details
    public List<Borrow> getAllBorrows() throws SQLException {
        // SQL query to select all borrows with joined book and user details
        String sql = "SELECT b.*, bk.title, bk.author, bk.publication_year, bk.category, bk.book_image, u.full_name " +
                "FROM borrow b " +
                "JOIN books bk ON b.book_id = bk.book_id " +
                "JOIN users u ON b.user_id = u.user_id";
        List<Borrow> borrows = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Create Borrow object and populate fields
                Borrow borrow = new Borrow();
                borrow.setBorrowId(rs.getInt("borrow_id"));
                borrow.setUserId(rs.getInt("user_id"));
                borrow.setUsername(rs.getString("full_name"));
                borrow.setBookId(rs.getInt("book_id"));
                borrow.setBorrowDate(rs.getTimestamp("borrow_date"));
                borrow.setDueDate(rs.getDate("due_date"));
                borrow.setReturnDate(rs.getDate("return_date"));
                borrow.setFineAmount(rs.getDouble("fine_amount"));
                borrow.setStatus(rs.getString("status"));

                // Create Book object and populate fields
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
        return borrows;
    }

    // Retrieves a specific borrow record by ID, including book details, if active
    public Borrow getBorrowById(int borrowId) throws SQLException {
        // SQL query to select a borrow record with joined book details
        String sql = "SELECT b.*, bk.title, bk.author, bk.publication_year, bk.category, bk.book_image " +
                "FROM borrow b " +
                "JOIN books bk ON b.book_id = bk.book_id " +
                "WHERE b.borrow_id = ? AND b.status IN ('borrowed', 'overdue')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, borrowId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create Borrow object and populate fields
                    Borrow borrow = new Borrow();
                    borrow.setBorrowId(rs.getInt("borrow_id"));
                    borrow.setUserId(rs.getInt("user_id"));
                    borrow.setBookId(rs.getInt("book_id"));
                    borrow.setBorrowDate(rs.getTimestamp("borrow_date"));
                    borrow.setDueDate(rs.getDate("due_date"));
                    borrow.setReturnDate(rs.getDate("return_date"));
                    borrow.setFineAmount(rs.getDouble("fine_amount"));
                    borrow.setStatus(rs.getString("status"));

                    // Create Book object and populate fields
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPublicationYear(rs.getDate("publication_year"));
                    book.setCategory(rs.getString("category"));
                    book.setImage(rs.getBytes("book_image"));
                    borrow.setBook(book);

                    return borrow;
                }
            }
        }
        return null; // Borrow record not found or not active
    }

    // Updates the status of a borrow record, setting return date if returned
    public boolean updateBorrowStatus(int borrowId, String status) throws SQLException {
        // SQL query to update borrow status and return date
        String sql = "UPDATE borrow SET status = ?, return_date = ? WHERE borrow_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            // Set return date to current timestamp if status is "returned", else null
            stmt.setTimestamp(2, status.equals("returned") ? new Timestamp(System.currentTimeMillis()) : null);
            stmt.setInt(3, borrowId);
            // Execute update and return true if rows were affected
            return stmt.executeUpdate() > 0;
        }
    }
}