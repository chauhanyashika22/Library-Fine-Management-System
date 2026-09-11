package com.library_fine_calculation_system.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineCalculationService {

    private static final double FINE_PER_DAY = 10.0; // Rs. 10 per day
    private static final int ISSUE_PERIOD = 14; // 14 days

    /**
     * Calculate fine based on due date and return date
     * @param dueDate The due date for the book
     * @param returnDate The actual return date
     * @return Fine amount (0 if returned on time, calculated fine if late)
     */
    public static double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null) {
            // Book not yet returned, calculate based on today's date
            returnDate = LocalDate.now();
        }

        // If returned on or before due date, no fine
        if (returnDate.isBefore(dueDate) || returnDate.isEqual(dueDate)) {
            return 0.0;
        }

        // Calculate days overdue
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        return daysOverdue * FINE_PER_DAY;
    }

    /**
     * Calculate due date based on issue date
     * @param issueDate The date when book is issued
     * @return Due date (14 days from issue date)
     */
    public static LocalDate calculateDueDate(LocalDate issueDate) {
        return issueDate.plusDays(ISSUE_PERIOD);
    }

    /**
     * Check if a book is overdue
     * @param dueDate The due date for the book
     * @return true if overdue, false otherwise
     */
    public static boolean isOverdue(LocalDate dueDate) {
        return LocalDate.now().isAfter(dueDate);
    }

    /**
     * Get days remaining until due date
     * @param dueDate The due date for the book
     * @return Days remaining (negative if overdue)
     */
    public static long getDaysRemaining(LocalDate dueDate) {
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    /**
     * Get days overdue
     * @param dueDate The due date for the book
     * @return Days overdue (0 if not overdue)
     */
    public static long getDaysOverdue(LocalDate dueDate) {
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        return daysOverdue > 0 ? daysOverdue : 0;
    }

    /**
     * Get current fine for an unreturned book
     * @param dueDate The due date for the book
     * @return Current fine amount based on days overdue
     */
    public static double getCurrentFine(LocalDate dueDate) {
        if (!isOverdue(dueDate)) {
            return 0.0;
        }
        long daysOverdue = getDaysOverdue(dueDate);
        return daysOverdue * FINE_PER_DAY;
    }

    /**
     * Get fine rate per day
     * @return Fine per day in rupees
     */
    public static double getFinePerDay() {
        return FINE_PER_DAY;
    }

    /**
     * Get issue period in days
     * @return Issue period
     */
    public static int getIssuePeriod() {
        return ISSUE_PERIOD;
    }
}
