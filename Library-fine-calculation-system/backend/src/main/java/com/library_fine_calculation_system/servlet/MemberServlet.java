package com.library_fine_calculation_system.servlet;

import com.library_fine_calculation_system.model.Member;
import com.library_fine_calculation_system.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/members")
public class MemberServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String action = request.getParameter("action");
            String query = request.getParameter("query");
            String status = request.getParameter("status");
            String memberId = request.getParameter("id");

            if ("search".equals(action) && query != null) {
                List<Member> members = MemberService.searchMembers(query);
                out.println(toJsonList(members));
            } else if ("status".equals(action) && status != null) {
                List<Member> members = MemberService.getMembersByStatus(status);
                out.println(toJsonList(members));
            } else if ("get".equals(action) && memberId != null) {
                Member member = MemberService.getMemberById(Integer.parseInt(memberId));
                out.println(toJson(member));
            } else {
                List<Member> members = MemberService.getAllMembers();
                out.println(toJsonList(members));
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
                Member member = new Member();
                member.setName(request.getParameter("name"));
                member.setEmail(request.getParameter("email"));
                member.setPhone(request.getParameter("phone"));
                member.setMembershipDate(LocalDate.now());
                member.setStatus("ACTIVE");

                if (MemberService.addMember(member)) {
                    out.println("{\"success\":true,\"message\":\"Member added successfully\"}");
                } else {
                    out.println("{\"success\":false,\"message\":\"Failed to add member\"}");
                }
            } else if ("update".equals(action)) {
                Member member = new Member();
                member.setMemberId(Integer.parseInt(request.getParameter("id")));
                member.setName(request.getParameter("name"));
                member.setEmail(request.getParameter("email"));
                member.setPhone(request.getParameter("phone"));
                member.setStatus(request.getParameter("status"));

                if (MemberService.updateMember(member)) {
                    out.println("{\"success\":true,\"message\":\"Member updated successfully\"}");
                } else {
                    out.println("{\"success\":false,\"message\":\"Failed to update member\"}");
                }
            } else if ("deactivate".equals(action)) {
                int memberId = Integer.parseInt(request.getParameter("id"));
                if (MemberService.deactivateMember(memberId)) {
                    out.println("{\"success\":true,\"message\":\"Member deactivated successfully\"}");
                } else {
                    out.println("{\"success\":false,\"message\":\"Failed to deactivate member\"}");
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
            int memberId = Integer.parseInt(request.getParameter("id"));
            if (MemberService.deleteMember(memberId)) {
                out.println("{\"success\":true,\"message\":\"Member deleted successfully\"}");
            } else {
                out.println("{\"success\":false,\"message\":\"Failed to delete member\"}");
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

    private String toJson(Member member) {
        if (member == null) return "null";
        return "{" +
                "\"id\":" + member.getMemberId() + "," +
                "\"name\":\"" + escapeJson(member.getName()) + "\"," +
                "\"email\":\"" + member.getEmail() + "\"," +
                "\"phone\":\"" + member.getPhone() + "\"," +
                "\"membershipDate\":\"" + member.getMembershipDate() + "\"," +
                "\"status\":\"" + member.getStatus() + "\"" +
                "}";
    }

    private String toJsonList(List<Member> members) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < members.size(); i++) {
            if (i > 0) json.append(",");
            json.append(toJson(members.get(i)));
        }
        json.append("]");
        return json.toString();
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
