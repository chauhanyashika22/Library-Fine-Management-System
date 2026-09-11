package com.library_fine_calculation_system.service;

import com.library_fine_calculation_system.dao.TransactionDAO;
import com.library_fine_calculation_system.dao.BookDAO;
import com.library_fine_calculation_system.dao.MemberDAO;
import com.library_fine_calculation_system.model.Transaction;
import com.library_fine_calculation_system.model.Book;
import com.library_fine_calculation_system.model.Member;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {

    /**
     * Issue a book to a member
     */
    public static boolean issueBook(int bookId, int memberId) throws SQLException {
        // Validate book exists and is available
        Book book = BookDAO.getBookById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Book is not available");
        }

        // Validate member exists and is active
        Member member = MemberDAO.getMemberById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }
        if (!member.getStatus().equals("ACTIVE")) {
            throw new IllegalStateException("Member account is not active");
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setBookId(bookId);
        transaction.setMemberId(memberId);
        LocalDate issueDate = LocalDate.now();
        transaction.setIssueDate(issueDate);
        transaction.setDueDate(FineCalculationService.calculateDueDate(issueDate));
        transaction.setFineAmount(0);
        transaction.setStatus("ISSUED");

        // Create transaction and decrease available copies
        if (TransactionDAO.createTransaction(transaction)) {
            return BookService.issueBook(bookId);
        }
        return false;
    }

    /**
     * Return a book from a member
     */
    public static boolean returnBook(int transactionId) throws SQLException {
        // Get the transaction
        Transaction transaction = TransactionDAO.getTransactionById(transactionId);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found");
        }
        if (transaction.getStatus().equals("RETURNED")) {
            throw new IllegalStateException("Book is already returned");
        }

        // Calculate fine
        LocalDate returnDate = LocalDate.now();
        double fineAmount = FineCalculationService.calculateFine(transaction.getDueDate(), returnDate);

        // Update transaction
        if (TransactionDAO.returnBook(transactionId, returnDate, fineAmount)) {
            // Increase available copies
            return BookService.returnBook(transaction.getBookId());
        }
        return false;
    }

    /**
     * Get all transactions
     */
    public static List<Transaction> getAllTransactions() throws SQLException {
        return TransactionDAO.getAllTransactions();
    }

    /**
     * Get transactions by member ID
     */
    public static List<Transaction> getTransactionsByMember(int memberId) throws SQLException {
        if (memberId <= 0) {
            throw new IllegalArgumentException("Invalid member ID");
        }
        return TransactionDAO.getTransactionsByMemberId(memberId);
    }

    /**
     * Get transactions by book ID
     */
    public static List<Transaction> getTransactionsByBook(int bookId) throws SQLException {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID");
        }
        return TransactionDAO.getTransactionsByBookId(bookId);
    }

    /**
     * Get overdue transactions
     */
    public static List<Transaction> getOverdueTransactions() throws SQLException {
        return TransactionDAO.getOverdueTransactions();
    }

    /**
     * Get recent transactions
     */
    public static List<Transaction> getRecentTransactions() throws SQLException {
        return TransactionDAO.getRecentTransactions();
    }

    /**
     * Get pending returns
     */
    public static List<Transaction> getPendingReturns() throws SQLException {
        return TransactionDAO.getPendingReturns();
    }

    /**
     * Get total fines collected
     */
    public static double getTotalFinesCollected() throws SQLException {
        return TransactionDAO.getTotalFinesCollected();
    }

    /**
     * Get total pending fines
     */
    public static double getTotalPendingFines() throws SQLException {
        return TransactionDAO.getTotalPendingFines();
    }

    /**
     * Get fines for a specific member
     */
    public static double getMemberFines(int memberId) throws SQLException {
        if (memberId <= 0) {
            throw new IllegalArgumentException("Invalid member ID");
        }
        return TransactionDAO.getFinesByMemberId(memberId);
    }

    /**
     * Get transaction by ID
     */
    public static Transaction getTransactionById(int transactionId) throws SQLException {
        if (transactionId <= 0) {
            throw new IllegalArgumentException("Invalid transaction ID");
        }
        return TransactionDAO.getTransactionById(transactionId);
    }
}
