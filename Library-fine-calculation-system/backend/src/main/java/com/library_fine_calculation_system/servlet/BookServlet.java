package com.library_fine_calculation_system.servlet;

import com.library_fine_calculation_system.model.Book;
import com.library_fine_calculation_system.service.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/books")
public class BookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");
            String query = request.getParameter("query");
            String category = request.getParameter("category");
            String bookId = request.getParameter("id");

            if ("search".equals(action) && query != null) {
                List<Book> books = BookService.searchBooks(query);
                out.println(toJsonList(books));
            } else if ("category".equals(action) && category != null) {
                List<Book> books = BookService.getBooksByCategory(category);
                out.println(toJsonList(books));
            } else if ("get".equals(action) && bookId != null) {
                Book book = BookService.getBookById(Integer.parseInt(bookId));
                out.println(toJson(book));
            } else {
                List<Book> books = BookService.getAllBooks();
                out.println(toJsonList(books));
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

            if ("add".equals(action)) {
                Book book = new Book();
                book.setTitle(request.getParameter("title"));
                book.setAuthor(request.getParameter("author"));
                book.setIsbn(request.getParameter("isbn"));
                book.setCategory(request.getParameter("category"));
                book.setTotalCopies(Integer.parseInt(request.getParameter("totalCopies")));

                if (BookService.addBook(book)) {
                    out.println("{\"success\":true,\"message\":\"Book added successfully\"}");
                } else {
                    out.println("{\"success\":false,\"message\":\"Failed to add book\"}");
                }
            } else if ("update".equals(action)) {
                Book book = new Book();
                book.setBookId(Integer.parseInt(request.getParameter("id")));
                book.setTitle(request.getParameter("title"));
                book.setAuthor(request.getParameter("author"));
                book.setIsbn(request.getParameter("isbn"));
                book.setCategory(request.getParameter("category"));
                book.setTotalCopies(Integer.parseInt(request.getParameter("totalCopies")));
                book.setAvailableCopies(Integer.parseInt(request.getParameter("availableCopies")));

                if (BookService.updateBook(book)) {
                    out.println("{\"success\":true,\"message\":\"Book updated successfully\"}");
                } else {
                    out.println("{\"success\":false,\"message\":\"Failed to update book\"}");
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            int bookId = Integer.parseInt(request.getParameter("id"));
            if (BookService.deleteBook(bookId)) {
                out.println("{\"success\":true,\"message\":\"Book deleted successfully\"}");
            } else {
                out.println("{\"success\":false,\"message\":\"Failed to delete book\"}");
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

    private String toJson(Book book) {
        if (book == null) return "null";
        return "{" +
                "\"id\":" + book.getBookId() + "," +
                "\"title\":\"" + escapeJson(book.getTitle()) + "\"," +
                "\"author\":\"" + escapeJson(book.getAuthor()) + "\"," +
                "\"isbn\":\"" + book.getIsbn() + "\"," +
                "\"category\":\"" + escapeJson(book.getCategory()) + "\"," +
                "\"totalCopies\":" + book.getTotalCopies() + "," +
                "\"availableCopies\":" + book.getAvailableCopies() +
                "}";
    }

    private String toJsonList(List<Book> books) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < books.size(); i++) {
            if (i > 0) json.append(",");
            json.append(toJson(books.get(i)));
        }
        json.append("]");
        return json.toString();
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
