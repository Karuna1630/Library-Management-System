CREATE SCHEMA IF NOT EXISTS library_db;
USE library_db;
CREATE TABLE books (
                       book_id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       publisher VARCHAR(255),
                       publication_year INT,
                       category VARCHAR(100),
);

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role INT DEFAULT 1 NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE borrowings (
                            borrowing_id INT AUTO_INCREMENT PRIMARY KEY,
                            book_id INT NOT NULL,
                            user_id INT NOT NULL,
                            borrow_date DATE NOT NULL,
                            due_date DATE NOT NULL,
                            return_date DATE,
                            status VARCHAR(20) NOT NULL DEFAULT 'borrowed',
                            FOREIGN KEY (book_id) REFERENCES books(book_id),
                            FOREIGN KEY (user_id) REFERENCES users(user_id)
);