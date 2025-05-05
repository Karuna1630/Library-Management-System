package com.example.librarymanagementsystem.dao;

import com.example.librarymanagementsystem.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection connection;

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, publication_year, category, stock, book_image) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            // Convert java.util.Date to java.sql.Date if needed
            java.sql.Date sqlDate = null;
            if (book.getPublicationYear() != null) {
                if (book.getPublicationYear() instanceof java.sql.Date) {
                    sqlDate = (java.sql.Date) book.getPublicationYear();
                } else {
                    sqlDate = new java.sql.Date(book.getPublicationYear().getTime());
                }
            }
            stmt.setDate(3, sqlDate);
            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.getStock());
            stmt.setBytes(6, book.getImage());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setBookId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Database connection is not available");
        }

        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDate("publication_year"),
                        rs.getString("category"),
                        rs.getInt("stock"),
                        rs.getBytes("book_image")
                );
                books.add(book);
            }
        }
        return books;
    }

    public Book getBookById(int bookId) throws SQLException {
        String sql = "SELECT * FROM books WHERE book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getDate("publication_year"),
                            rs.getString("category"),
                            rs.getInt("stock"),
                            rs.getBytes("book_image")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, publication_year = ?, category = ?, stock = ?, book_image = ? WHERE book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());

            // Convert java.util.Date to java.sql.Date if needed
            java.sql.Date sqlDate = null;
            if (book.getPublicationYear() != null) {
                if (book.getPublicationYear() instanceof java.sql.Date) {
                    sqlDate = (java.sql.Date) book.getPublicationYear();
                } else {
                    sqlDate = new java.sql.Date(book.getPublicationYear().getTime());
                }
            }
            stmt.setDate(3, sqlDate);

            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.getStock());
            stmt.setBytes(6, book.getImage());
            stmt.setInt(7, book.getBookId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Search books by title, author, or category
    public List<Book> searchBooks(String query) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR category LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchTerm = "%" + query + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getDate("publication_year"),
                            rs.getString("category"),
                            rs.getInt("stock"),
                            rs.getBytes("book_image")
                    );
                    books.add(book);
                }
            }
        }
        return books;
    }
}