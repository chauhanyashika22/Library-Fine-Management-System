package com.library_fine_calculation_system.dao;

import com.library_fine_calculation_system.model.Member;
import com.library_fine_calculation_system.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    /**
     * Add a new member to the database
     */
    public static boolean addMember(Member member) throws SQLException {
        String query = "INSERT INTO members (name, email, phone, membership_date, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setDate(4, Date.valueOf(member.getMembershipDate()));
            pstmt.setString(5, member.getStatus());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieve a member by ID
     */
    public static Member getMemberById(int memberId) throws SQLException {
        String query = "SELECT * FROM members WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setMembershipDate(rs.getDate("membership_date").toLocalDate());
                member.setStatus(rs.getString("status"));
                return member;
            }
        }
        return null;
    }

    /**
     * Get all members
     */
    public static List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setMembershipDate(rs.getDate("membership_date").toLocalDate());
                member.setStatus(rs.getString("status"));
                members.add(member);
            }
        }
        return members;
    }

    /**
     * Search members by name or email
     */
    public static List<Member> searchMembers(String query) throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE name LIKE ? OR email LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + query + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setMembershipDate(rs.getDate("membership_date").toLocalDate());
                member.setStatus(rs.getString("status"));
                members.add(member);
            }
        }
        return members;
    }

    /**
     * Get members by status
     */
    public static List<Member> getMembersByStatus(String status) throws SQLException {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members WHERE status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setName(rs.getString("name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setMembershipDate(rs.getDate("membership_date").toLocalDate());
                member.setStatus(rs.getString("status"));
                members.add(member);
            }
        }
        return members;
    }

    /**
     * Update member details
     */
    public static boolean updateMember(Member member) throws SQLException {
        String query = "UPDATE members SET name = ?, email = ?, phone = ?, status = ? WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getStatus());
            pstmt.setInt(5, member.getMemberId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Delete a member
     */
    public static boolean deleteMember(int memberId) throws SQLException {
        String query = "DELETE FROM members WHERE member_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, memberId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Check if member email exists
     */
    public static boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM members WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Get total member count
     */
    public static int getTotalMembers() throws SQLException {
        String query = "SELECT COUNT(*) FROM members";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
