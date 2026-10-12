package com.devops.studentmanagement.controller;

import com.devops.studentmanagement.entity.Student;
import com.devops.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("totalStudents", studentService.countStudents());
        model.addAttribute("currentUser", userDetails != null ? userDetails.getUsername() : "User");
        return "dashboard";
    }

    @GetMapping("/students/new")
    public String showCreateStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("pageTitle", "Add New Student");
        return "student-form";
    }

    @PostMapping("/students")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", student.getId() == null ? "Add New Student" : "Edit Student");
            return "student-form";
        }

        try {
            if (student.getId() == null) {
                studentService.saveStudent(student);
                redirectAttributes.addFlashAttribute("successMessage", "Student created successfully!");
            } else {
                studentService.updateStudent(student.getId(), student);
                redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
            }
            return "redirect:/dashboard";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("pageTitle", student.getId() == null ? "Add New Student" : "Edit Student");
            model.addAttribute("errorMessage", ex.getMessage());
            return "student-form";
        }
    }

    @GetMapping("/students/edit/{id}")
    public String showEditStudentForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        return studentService.getStudentById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("pageTitle", "Edit Student");
                    return "student-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Student not found with ID: " + id);
                    return "redirect:/dashboard";
                });
    }

    @GetMapping("/students/view/{id}")
    public String viewStudentDetails(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        return studentService.getStudentById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    return "student-view";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Student not found with ID: " + id);
                    return "redirect:/dashboard";
                });
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student record deleted successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/dashboard";
    }
}
