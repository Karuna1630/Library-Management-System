package com.example.librarymanagementsystem.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Borrow {
    private int borrowId;
    private int bookId;
    private int userId;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private String status;
    private double fineAmount;

    public Borrow(int borrowId, int bookId, int userId, Date borrowDate, Date dueDate, Date returnDate, String status) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fineAmount = 0.0; // Initialize fineAmount to 0
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
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
        this.fineAmount = calculateFine(); // Recalculate fine when return date is set
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public double calculateFine() {
        if (returnDate == null || returnDate.before(dueDate)) {
            return 0.0; // No fine "if returned on time or before the time or not returned yet"
        }

        // Calculate the number of days late
        long diffInMillies = returnDate.getTime() - dueDate.getTime();
        long daysLate = TimeUnit.MILLISECONDS.toDays(diffInMillies);

        // Define a fine rate per day late
        double fineRate = 1.0; // e.g., $1 per day

        // Calculate fine based on late days
        fineAmount = daysLate * fineRate;
        return fineAmount;
    }
    //displaying the string representation of the Borrow object
    @Override
    public String toString() {
        return "Borrowing [borrowingId=" + borrowId + ", bookId=" + bookId +
                ", userId=" + userId + ", status=" + status + ", fineAmount=" + fineAmount + "]";
    }
}
