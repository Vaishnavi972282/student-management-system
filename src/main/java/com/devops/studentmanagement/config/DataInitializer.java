package com.devops.studentmanagement.config;

import com.devops.studentmanagement.dto.UserRegistrationDto;
import com.devops.studentmanagement.entity.Student;
import com.devops.studentmanagement.service.StudentService;
import com.devops.studentmanagement.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserService userService, StudentService studentService) {
        return args -> {
            // Seed a default admin/demo user if not present
            if (!userService.existsByEmail("admin@college.edu")) {
                UserRegistrationDto admin = new UserRegistrationDto(
                        "Demo Administrator",
                        "admin@college.edu",
                        "admin123"
                );
                userService.registerUser(admin);
            }

            // Seed sample students for immediate live demo
            if (studentService.countStudents() == 0) {
                studentService.saveStudent(new Student(
                        "CS2026-001",
                        "Aarav Sharma",
                        "aarav.sharma@college.edu",
                        "Computer Engineering",
                        "Final Year (Sem 8)",
                        "9876543210"
                ));

                studentService.saveStudent(new Student(
                        "IT2026-002",
                        "Priya Patel",
                        "priya.patel@college.edu",
                        "Information Technology",
                        "Third Year (Sem 6)",
                        "9123456789"
                ));

                studentService.saveStudent(new Student(
                        "AI2026-003",
                        "Rohan Verma",
                        "rohan.verma@college.edu",
                        "Artificial Intelligence & DS",
                        "Second Year (Sem 4)",
                        "9988776655"
                ));
            }
        };
    }
}
