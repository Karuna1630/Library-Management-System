package com.example.librarymanagementsystem.model;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private int publicationYear;
    private String category;
    private int stock;


    public Book(int bookId, String title, String author, int publicationYear, String category, int stock) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.category = category;
        this.stock = stock;
    }

    // Getters and Setters for each field
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Override the toString method to display all fields, including stock
    @Override
    public String toString() {
        return "Book [bookId=" + bookId + ", title=" + title + ", author=" + author +
                ", publicationYear=" + publicationYear + ", category=" + category + ", stock=" + stock + "]";
    }
}
