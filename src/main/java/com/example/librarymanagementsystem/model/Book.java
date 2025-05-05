package com.example.librarymanagementsystem.model;

import java.util.Date;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private Date publicationYear;
    private String category;
    private int stock;
    private byte[] image;

    public Book(String title, String author, Date publicationYear, String category, int stock, byte[] image) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.category = category;
        this.stock = stock;
        this.image = image;
    }

    public Book(int bookId, String title, String author, Date publicationYear, String category, int stock, byte[] image) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.category = category;
        this.stock = stock;
        this.image = image;
    }

    public Book() {

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

    public Date getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Date publicationYear) {
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

    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
}
