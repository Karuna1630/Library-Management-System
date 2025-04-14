package com.example.librarymanagementsystem.model;

import java.sql.Timestamp;

public class Reservation {
    private int reservationId;
    private int bookId;
    private int userId;
    private Timestamp reservationDate;
    private String status;
    private String reservedUntil;

    public Reservation(int reservationId, int bookId, int userId, Timestamp reservationDate, String status, String reservedUntil) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.userId = userId;
        this.reservationDate = reservationDate;
        this.status = status;
        this.reservedUntil = reservedUntil;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(String reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    @Override
    public String toString() {
        return "Reservation [reservationId=" + reservationId + ", bookId=" + bookId + ", userId=" + userId +
                ", status=" + status + ", reservedUntil=" + reservedUntil + "]";
    }
}
