package com.library_fine_calculation_system.dao;

import com.library_fine_calculation_system.model.Transaction;
import com.library_fine_calculation_system.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /**
     * Create a new transaction (issue a book)
     */
    public static boolean createTransaction(Transaction transaction) throws SQLException {
        String query = "INSERT INTO transactions (book_id, member_id, issue_date, due_date, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, transaction.getBookId());
            pstmt.setInt(2, transaction.getMemberId());
            pstmt.setDate(3, Date.valueOf(transaction.getIssueDate()));
            pstmt.setDate(4, Date.valueOf(transaction.getDueDate()));
            pstmt.setString(5, "ISSUED");

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieve a transaction by ID
     */
    public static Transaction getTransactionById(int transactionId) throws SQLException {
        String query = "SELECT * FROM transactions WHERE transaction_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRowToTransaction(rs);
            }
        }
        return null;
    }

    /**
     * Get all transactions
     */
    public static List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions ORDER BY issue_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        }
        return transactions;
    }

    /**
     * Get transactions by member ID
     */
    public static List<Transaction> getTransactionsByMemberId(int memberId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE member_id = ? ORDER BY issue_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        }
        return transactions;
    }

    /**
     * Get transactions by book ID
     */
    public static List<Transaction> getTransactionsByBookId(int bookId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE book_id = ? ORDER BY issue_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        }
        return transactions;
    }

    /**
     * Get transactions by status
     */
    public static List<Transaction> getTransactionsByStatus(String status) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE status = ? ORDER BY issue_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        }
        return transactions;
    }

    /**
     * Return a book and update transaction with return date and fine amount
     */
    public static boolean returnBook(int transactionId, LocalDate returnDate, double fineAmount) throws SQLException {
        String query = "UPDATE transactions SET return_date = ?, fine_amount = ?, status = 'RETURNED' " +
                "WHERE transaction_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(returnDate));
            pstmt.setDouble(2, fineAmount);
            pstmt.setInt(3, transactionId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Get overdue transactions
     */
    public static List<Transaction> getOverdueTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE status = 'ISSUED' AND due_date < CURDATE() " +
                "ORDER BY due_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        }
        return transactions;
    }

    /**
     * Get recent transactions (limit 10)
     */
    public static List<Transaction> getRecentTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions ORDER BY issue_date DESC LIMIT 10";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        }
        return transactions;
    }

    /**
     * Get pending returns (transactions that are overdue)
     */
    public static List<Transaction> getPendingReturns() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE status = 'ISSUED' ORDER BY due_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }
        }
        return transactions;
    }

    /**
     * Get total fines collected
     */
    public static double getTotalFinesCollected() throws SQLException {
        String query = "SELECT SUM(fine_amount) FROM transactions WHERE status = 'RETURNED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    /**
     * Get total pending fines
     */
    public static double getTotalPendingFines() throws SQLException {
        String query = "SELECT SUM(fine_amount) FROM transactions WHERE status = 'ISSUED' AND due_date < CURDATE()";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double result = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : result;
            }
        }
        return 0.0;
    }

    /**
     * Get fines by member
     */
    public static double getFinesByMemberId(int memberId) throws SQLException {
        String query = "SELECT SUM(fine_amount) FROM transactions WHERE member_id = ? AND status = 'ISSUED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double result = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : result;
            }
        }
        return 0.0;
    }

    /**
     * Helper method to map ResultSet row to Transaction object
     */
    private static Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setBookId(rs.getInt("book_id"));
        transaction.setMemberId(rs.getInt("member_id"));
        transaction.setIssueDate(rs.getDate("issue_date").toLocalDate());
        transaction.setDueDate(rs.getDate("due_date").toLocalDate());

        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            transaction.setReturnDate(returnDate.toLocalDate());
        }

        transaction.setFineAmount(rs.getDouble("fine_amount"));
        transaction.setStatus(rs.getString("status"));

        return transaction;
    }
}
