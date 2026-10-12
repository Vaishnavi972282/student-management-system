package com.devops.studentmanagement;

import com.devops.studentmanagement.dto.UserRegistrationDto;
import com.devops.studentmanagement.entity.Student;
import com.devops.studentmanagement.entity.User;
import com.devops.studentmanagement.service.StudentService;
import com.devops.studentmanagement.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StudentManagementApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    // =========================================================================
    // MEMBER 2 & MEMBER 5: Authentication & Security Tests
    // =========================================================================

    @Test
    @DisplayName("TC-SEC-01: Anonymous user accessing protected /dashboard is redirected to /login")
    void anonymousUserRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("TC-SEC-02: Public login page loads with HTTP 200")
    void loginPageLoadsSuccessfully() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("TC-SEC-03: Valid login authenticates and redirects to /dashboard")
    void validLoginSucceeds() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "admin@college.edu")
                        .param("password", "admin123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @DisplayName("TC-SEC-04: Invalid password fails authentication")
    void invalidPasswordFails() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "admin@college.edu")
                        .param("password", "wrongpassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    // =========================================================================
    // MEMBER 3 & MEMBER 5: Registration Tests
    // =========================================================================

    @Test
    @DisplayName("TC-REG-01: Register valid new user")
    void registerValidUser() {
        UserRegistrationDto dto = new UserRegistrationDto("Test Candidate", "candidate@college.edu", "pass123");
        User registered = userService.registerUser(dto);

        assertNotNull(registered.getId());
        assertEquals("Test Candidate", registered.getFullName());
        assertEquals("candidate@college.edu", registered.getEmail());
        assertNotEquals("pass123", registered.getPassword(), "Password must be BCrypt encoded!");
    }

    @Test
    @DisplayName("TC-REG-02: Prevent duplicate email registration")
    void duplicateEmailRegistrationThrowsException() {
        UserRegistrationDto dto = new UserRegistrationDto("Duplicate Test", "admin@college.edu", "pass123");
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
    }

    // =========================================================================
    // MEMBER 4 & MEMBER 5: Student CRUD Tests
    // =========================================================================

    @Test
    @WithMockUser(username = "admin@college.edu", roles = {"USER"})
    @DisplayName("TC-CRUD-01: Authenticated user can view dashboard student list")
    void authenticatedUserAccessesDashboard() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("totalStudents"));
    }

    @Test
    @WithMockUser(username = "admin@college.edu", roles = {"USER"})
    @DisplayName("TC-CRUD-02: Create student via form submission")
    void createStudentViaPost() throws Exception {
        mockMvc.perform(post("/students")
                        .param("studentCode", "ME2026-999")
                        .param("fullName", "Vikram Singh")
                        .param("email", "vikram.singh@college.edu")
                        .param("department", "Mechanical Engineering")
                        .param("academicYear", "Third Year (Sem 5-6)")
                        .param("contactNumber", "9876501234")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attributeExists("successMessage"));

        assertTrue(studentService.getAllStudents().stream()
                .anyMatch(s -> "ME2026-999".equals(s.getStudentCode())));
    }

    @Test
    @DisplayName("TC-CRUD-03: Service level student update and duplicate prevention")
    void testStudentServiceOperations() {
        Student s = new Student("TEST-001", "Initial Name", "init@college.edu", "Computer Engineering", "First Year", "9999999999");
        Student saved = studentService.saveStudent(s);

        // Update
        saved.setFullName("Updated Name");
        Student updated = studentService.updateStudent(saved.getId(), saved);
        assertEquals("Updated Name", updated.getFullName());

        // Delete
        studentService.deleteStudent(saved.getId());
        assertFalse(studentService.getStudentById(saved.getId()).isPresent());
    }

    // =========================================================================
    // MEMBER 10 & MEMBER 5: Actuator Health Endpoint Test
    // =========================================================================

    @Test
    @DisplayName("TC-MON-01: Actuator health endpoint returns UP status")
    void actuatorHealthReturnsUp() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
