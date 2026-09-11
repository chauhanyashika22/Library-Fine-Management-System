package com.library_fine_calculation_system.service;

import com.library_fine_calculation_system.dao.BookDAO;
import com.library_fine_calculation_system.model.Book;

import java.sql.SQLException;
import java.util.List;

public class BookService {

    /**
     * Add a new book to the library
     */
    public static boolean addBook(Book book) throws SQLException {
        // Validation
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
        if (book.getTotalCopies() <= 0) {
            throw new IllegalArgumentException("Total copies must be greater than 0");
        }

        // Available copies should equal total copies initially
        book.setAvailableCopies(book.getTotalCopies());

        return BookDAO.addBook(book);
    }

    /**
     * Retrieve book by ID
     */
    public static Book getBookById(int bookId) throws SQLException {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID");
        }
        return BookDAO.getBookById(bookId);
    }

    /**
     * Get all books
     */
    public static List<Book> getAllBooks() throws SQLException {
        return BookDAO.getAllBooks();
    }

    /**
     * Search books by title, author, or ISBN
     */
    public static List<Book> searchBooks(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        return BookDAO.searchBooks(query.trim());
    }

    /**
     * Get books by category
     */
    public static List<Book> getBooksByCategory(String category) throws SQLException {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        return BookDAO.getBooksByCategory(category.trim());
    }

    /**
     * Update book information
     */
    public static boolean updateBook(Book book) throws SQLException {
        if (book.getBookId() <= 0) {
            throw new IllegalArgumentException("Invalid book ID");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        return BookDAO.updateBook(book);
    }

    /**
     * Delete a book
     */
    public static boolean deleteBook(int bookId) throws SQLException {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID");
        }
        return BookDAO.deleteBook(bookId);
    }

    /**
     * Check if a book is available
     */
    public static boolean isBookAvailable(int bookId) throws SQLException {
        Book book = BookDAO.getBookById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        return book.getAvailableCopies() > 0;
    }

    /**
     * Issue a book (decrease available copies)
     */
    public static boolean issueBook(int bookId) throws SQLException {
        if (!isBookAvailable(bookId)) {
            throw new IllegalStateException("Book is not available");
        }
        return BookDAO.updateAvailableCopies(bookId, -1);
    }

    /**
     * Return a book (increase available copies)
     */
    public static boolean returnBook(int bookId) throws SQLException {
        Book book = BookDAO.getBookById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        if (book.getAvailableCopies() >= book.getTotalCopies()) {
            throw new IllegalStateException("Cannot return more copies than total available");
        }
        return BookDAO.updateAvailableCopies(bookId, 1);
    }
}
