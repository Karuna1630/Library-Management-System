package com.example.librarymanagementsystem.model;

import java.util.Date;

public class Borrow {
        private int borrowId;
        private int bookId;
        private int userId;
        private Date borrowDate;
        private Date dueDate;
        private Date returnDate;
        private String status;

    public Borrow(int borrowingId, int bookId, int userId, Date borrowDate, Date dueDate, Date returnDate, String status) {
        this.borrowId = borrowingId;
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getBorrowingId() {
        return borrowId;
    }

    public void setBorrowingId(int borrowingId) {
        this.borrowId = borrowingId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "Borrowing [borrowingId=" + borrowId + ", bookId=" + bookId +
                ", userId=" + userId + ", status=" + status + "]";
    }
}
