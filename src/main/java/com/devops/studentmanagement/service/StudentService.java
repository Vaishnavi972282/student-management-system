package com.devops.studentmanagement.service;

import com.devops.studentmanagement.entity.Student;
import com.devops.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student saveStudent(Student student) {
        // Check duplicate studentCode
        studentRepository.findByStudentCode(student.getStudentCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(student.getId())) {
                        throw new IllegalArgumentException("Student ID/Roll Number '" + student.getStudentCode() + "' is already in use.");
                    }
                });

        // Check duplicate email
        studentRepository.findByEmail(student.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(student.getId())) {
                        throw new IllegalArgumentException("Email address '" + student.getEmail() + "' is already registered to another student.");
                    }
                });

        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));

        // Validate unique student code if changed
        studentRepository.findByStudentCode(studentDetails.getStudentCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new IllegalArgumentException("Student ID/Roll Number '" + studentDetails.getStudentCode() + "' is already in use.");
                    }
                });

        // Validate unique email if changed
        studentRepository.findByEmail(studentDetails.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new IllegalArgumentException("Email address '" + studentDetails.getEmail() + "' is already registered to another student.");
                    }
                });

        student.setStudentCode(studentDetails.getStudentCode());
        student.setFullName(studentDetails.getFullName());
        student.setEmail(studentDetails.getEmail());
        student.setDepartment(studentDetails.getDepartment());
        student.setAcademicYear(studentDetails.getAcademicYear());
        student.setContactNumber(studentDetails.getContactNumber());

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete: Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    public long countStudents() {
        return studentRepository.count();
    }
}
