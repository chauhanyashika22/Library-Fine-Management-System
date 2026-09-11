package com.library_fine_calculation_system.service;

import com.library_fine_calculation_system.dao.MemberDAO;
import com.library_fine_calculation_system.model.Member;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MemberService {

    /**
     * Add a new member
     */
    public static boolean addMember(Member member) throws SQLException {
        // Validation
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty");
        }
        if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!isValidEmail(member.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (MemberDAO.emailExists(member.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (member.getPhone() == null || member.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        if (member.getMembershipDate() == null) {
            member.setMembershipDate(LocalDate.now());
        }
        if (member.getStatus() == null || member.getStatus().isEmpty()) {
            member.setStatus("ACTIVE");
        }

        return MemberDAO.addMember(member);
    }

    /**
     * Retrieve member by ID
     */
    public static Member getMemberById(int memberId) throws SQLException {
        if (memberId <= 0) {
            throw new IllegalArgumentException("Invalid member ID");
        }
        return MemberDAO.getMemberById(memberId);
    }

    /**
     * Get all members
     */
    public static List<Member> getAllMembers() throws SQLException {
        return MemberDAO.getAllMembers();
    }

    /**
     * Search members by name or email
     */
    public static List<Member> searchMembers(String query) throws SQLException {
        if (query == null || query.trim().isEmpty()) {
            return getAllMembers();
        }
        return MemberDAO.searchMembers(query.trim());
    }

    /**
     * Get members by status (ACTIVE, INACTIVE, SUSPENDED)
     */
    public static List<Member> getMembersByStatus(String status) throws SQLException {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        return MemberDAO.getMembersByStatus(status.trim());
    }

    /**
     * Update member details
     */
    public static boolean updateMember(Member member) throws SQLException {
        if (member.getMemberId() <= 0) {
            throw new IllegalArgumentException("Invalid member ID");
        }
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty");
        }
        if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!isValidEmail(member.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return MemberDAO.updateMember(member);
    }

    /**
     * Delete a member
     */
    public static boolean deleteMember(int memberId) throws SQLException {
        if (memberId <= 0) {
            throw new IllegalArgumentException("Invalid member ID");
        }
        return MemberDAO.deleteMember(memberId);
    }

    /**
     * Deactivate a member
     */
    public static boolean deactivateMember(int memberId) throws SQLException {
        Member member = getMemberById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }
        member.setStatus("INACTIVE");
        return updateMember(member);
    }

    /**
     * Activate a member
     */
    public static boolean activateMember(int memberId) throws SQLException {
        Member member = getMemberById(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }
        member.setStatus("ACTIVE");
        return updateMember(member);
    }

    /**
     * Validate email format
     */
    private static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Get total number of members
     */
    public static int getTotalMembers() throws SQLException {
        return MemberDAO.getTotalMembers();
    }
}
