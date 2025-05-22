package com.example.librarymanagementsystem.dao;

import com.example.librarymanagementsystem.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    // Database connection for executing queries
    private Connection connection;

    // Constructor to initialize the DAO with a database connection
    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    // Adds a new book to the database
    public boolean addBook(Book book) throws SQLException {
        // SQL query to insert a book record
        String sql = "INSERT INTO books (title, author, publication_year, category, stock, book_image) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set query parameters from the Book object
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            // Convert publication year to SQL Date, handling null or different Date types
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

            // Execute the insert query
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated book ID and set it in the Book object
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setBookId(generatedKeys.getInt(1));
                    }
                }
                return true; // Success
            }
            return false; // Insert failed
        }
    }

    // Retrieves all books from the database
    public List<Book> getAllBooks() throws SQLException {
        // Verify connection is valid
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Database connection is not available");
        }

        List<Book> books = new ArrayList<>();
        // SQL query to select all books
        String sql = "SELECT * FROM books";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Iterate through result set and create Book objects
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

    // Retrieves a book by its ID
    public Book getBookById(int bookId) throws SQLException {
        // SQL query to select a book by ID
        String sql = "SELECT * FROM books WHERE book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create and return a Book object from the result
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
        return null; // Book not found
    }

    // Updates an existing book in the database
    public boolean updateBook(Book book) throws SQLException {
        // SQL query to update a book record
        String sql = "UPDATE books SET title = ?, author = ?, publication_year = ?, category = ?, stock = ?, book_image = ? WHERE book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set query parameters from the Book object
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            // Convert publication year to SQL Date, handling null or different Date types
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

            // Execute update and return true if rows were affected
            return stmt.executeUpdate() > 0;
        }
    }

    // Deletes a book from the database by its ID
    public boolean deleteBook(int bookId) throws SQLException {
        // SQL query to delete a book by ID
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            // Execute delete and return true if rows were affected
            return stmt.executeUpdate() > 0;
        }
    }

    // Searches for books by title, author, or category
    public List<Book> searchBooks(String query) throws SQLException {
        List<Book> books = new ArrayList<>();
        // SQL query to search books with LIKE conditions
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR category LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Add wildcards to search term for partial matching
            String searchTerm = "%" + query + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through results and create Book objects
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

    // Retrieves the latest books, limited by the specified number
    public List<Book> getLatestBooks(int limit) throws SQLException {
        List<Book> books = new ArrayList<>();
        // SQL query to select latest books ordered by book_id descending
        String query = "SELECT book_id, title, author, publication_year, category, stock, book_image FROM books ORDER BY book_id DESC LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through results and create Book objects
                while (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPublicationYear(rs.getDate("publication_year"));
                    book.setCategory(rs.getString("category"));
                    book.setStock(rs.getInt("stock"));
                    book.setImage(rs.getBytes("book_image"));
                    books.add(book);
                }
            }
        }
        return books;
    }

    // Filters books based on query, category, and availability
    public List<Book> filterBooks(String query, String category, String availability) throws SQLException {
        List<Book> books = new ArrayList<>();
        // Base SQL query with dynamic conditions
        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE 1=1");

        // Append conditions based on provided filters
        if (query != null && !query.isEmpty()) {
            sql.append(" AND (title LIKE ? OR author LIKE ? OR category LIKE ?)");
        }
        if (category != null && !category.equals("All Categories")) {
            sql.append(" AND category = ?");
        }
        if (availability != null && !availability.equals("All Availability")) {
            sql.append(" AND stock > 0 AND ? = 'Available Now' OR stock = 0 AND ? = 'Currently Reserved'");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            // Set query parameters for search term
            if (query != null && !query.isEmpty()) {
                String searchTerm = "%" + query + "%";
                stmt.setString(paramIndex++, searchTerm);
                stmt.setString(paramIndex++, searchTerm);
                stmt.setString(paramIndex++, searchTerm);
            }
            // Set category parameter
            if (category != null && !category.equals("All Categories")) {
                stmt.setString(paramIndex++, category);
            }
            // Set availability parameters
            if (availability != null && !availability.equals("All Availability")) {
                stmt.setString(paramIndex++, availability);
                stmt.setString(paramIndex++, availability);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through results and create Book objects
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