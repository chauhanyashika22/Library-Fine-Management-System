package com.library_fine_calculation_system.servlet;

import com.library_fine_calculation_system.model.Transaction;
import com.library_fine_calculation_system.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/transactions")
public class TransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");
            String memberId = request.getParameter("memberId");
            String bookId = request.getParameter("bookId");
            String transactionId = request.getParameter("id");

            if ("member".equals(action) && memberId != null) {
                List<Transaction> transactions = TransactionService.getTransactionsByMember(Integer.parseInt(memberId));
                out.println(toJsonList(transactions));
            } else if ("book".equals(action) && bookId != null) {
                List<Transaction> transactions = TransactionService.getTransactionsByBook(Integer.parseInt(bookId));
                out.println(toJsonList(transactions));
            } else if ("overdue".equals(action)) {
                List<Transaction> transactions = TransactionService.getOverdueTransactions();
                out.println(toJsonList(transactions));
            } else if ("pending".equals(action)) {
                List<Transaction> transactions = TransactionService.getPendingReturns();
                out.println(toJsonList(transactions));
            } else if ("recent".equals(action)) {
                List<Transaction> transactions = TransactionService.getRecentTransactions();
                out.println(toJsonList(transactions));
            } else if ("get".equals(action) && transactionId != null) {
                Transaction transaction = TransactionService.getTransactionById(Integer.parseInt(transactionId));
                out.println(toJson(transaction));
            } else if ("totalFines".equals(action)) {
                double totalFines = TransactionService.getTotalFinesCollected();
                out.println("{\"totalFines\":" + totalFines + "}");
            } else if ("pendingFines".equals(action)) {
                double pendingFines = TransactionService.getTotalPendingFines();
                out.println("{\"pendingFines\":" + pendingFines + "}");
            } else if ("memberFines".equals(action) && memberId != null) {
                double fines = TransactionService.getMemberFines(Integer.parseInt(memberId));
                out.println("{\"fines\":" + fines + "}");
            } else {
                List<Transaction> transactions = TransactionService.getAllTransactions();
                out.println(toJsonList(transactions));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");

            if ("issue".equals(action)) {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                int memberId = Integer.parseInt(request.getParameter("memberId"));

                if (TransactionService.issueBook(bookId, memberId)) {
                    out.println("{\"success\":true,\"message\":\"Book issued successfully\"}");
                } else {
                    out.println("{\"success\":false,\"message\":\"Failed to issue book\"}");
                }
            } else if ("return".equals(action)) {
                int transactionId = Integer.parseInt(request.getParameter("transactionId"));

                if (TransactionService.returnBook(transactionId)) {
                    out.println("{\"success\":true,\"message\":\"Book returned successfully\"}");
                } else {
                    out.println("{\"success\":false,\"message\":\"Failed to return book\"}");
                }
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"success\":false,\"error\":\"Database error: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }

    private String toJson(Transaction transaction) {
        if (transaction == null) return "null";
        return "{" +
                "\"id\":" + transaction.getTransactionId() + "," +
                "\"bookId\":" + transaction.getBookId() + "," +
                "\"memberId\":" + transaction.getMemberId() + "," +
                "\"issueDate\":\"" + transaction.getIssueDate() + "\"," +
                "\"dueDate\":\"" + transaction.getDueDate() + "\"," +
                "\"returnDate\":\"" + (transaction.getReturnDate() != null ? transaction.getReturnDate() : "") + "\"," +
                "\"fineAmount\":" + transaction.getFineAmount() + "," +
                "\"status\":\"" + transaction.getStatus() + "\"" +
                "}";
    }

    private String toJsonList(List<Transaction> transactions) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < transactions.size(); i++) {
            if (i > 0) json.append(",");
            json.append(toJson(transactions.get(i)));
        }
        json.append("]");
        return json.toString();
    }
}
