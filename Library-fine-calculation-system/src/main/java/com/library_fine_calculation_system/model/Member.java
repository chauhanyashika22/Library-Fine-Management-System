package com.library_fine_calculation_system.model;

import java.time.LocalDate;

public class Member {

    private int memberId;
    private String name;
    private String email;
    private String phone;
    private LocalDate membershipDate;
    private String status;

    // Default Constructor
    public Member() {
    }

    //Constructor

    public Member(String email, int memberId,
                  LocalDate membershipDate, String name,
                  String phone, String status) {
        this.email = email;
        this.memberId = memberId;
        this.membershipDate = membershipDate;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    //Getter and Setter

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
