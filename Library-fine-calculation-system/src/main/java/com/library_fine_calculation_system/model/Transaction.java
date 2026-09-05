package com.library_fine_calculation_system.model;

import java.time.LocalDate;

public class Transaction {

    private int transactionId;

    private int bookId;
    private int memberId;

    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private double fineAmount;
    private String status;

    // Default Constructor
    public Transaction() {
    }

    //Constructor

    public Transaction(int bookId, LocalDate dueDate, double fineAmount,
                       LocalDate issueDate, int memberId, LocalDate returnDate,
                       String status, int transactionId) {
        this.bookId = bookId;
        this.dueDate = dueDate;
        this.fineAmount = fineAmount;
        this.issueDate = issueDate;
        this.memberId = memberId;
        this.returnDate = returnDate;
        this.status = status;
        this.transactionId = transactionId;
    }


    //Getter and Setter

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
