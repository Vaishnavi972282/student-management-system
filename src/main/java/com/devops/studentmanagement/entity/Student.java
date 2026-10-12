package com.devops.studentmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student ID/Roll Number is required")
    @Column(name = "student_code", nullable = false, unique = true)
    private String studentCode;

    @NotBlank(message = "Full Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    @NotBlank(message = "Year / Semester is required")
    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be a 10-digit phone number")
    @Column(name = "contact_number")
    private String contactNumber;

    public Student() {
    }

    public Student(String studentCode, String fullName, String email, String department, String academicYear, String contactNumber) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.academicYear = academicYear;
        this.contactNumber = contactNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
