package com.attendance.attendance.student;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // returns only IDs, never passwords
    public List<String> getStudentIds() {
        return studentRepository.findAll().stream().map(Student::getStudentId).toList();
    }

    public boolean login(String studentId, String rawPassword) {
        if (studentId == null || rawPassword == null) return false;
        return studentRepository.findById(studentId)
                .map(s -> s.getPassword() != null
                        && passwordEncoder.matches(rawPassword, s.getPassword()))
                .orElse(false);
    }

    public void register(String studentId, String rawPassword) {
        if (studentId == null || studentId.isBlank()
                || rawPassword == null || rawPassword.length() < 6 || rawPassword.length() > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID is required and password must be 6 to 72 characters");
        }
        String id = studentId.trim();
        // '_' is the separator in the attendance string studentid_date_subject
        if (id.contains("_") || id.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID cannot contain '_' or exceed 100 characters");
        }
        if (studentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ID already exists");
        }
        studentRepository.save(new Student(id, passwordEncoder.encode(rawPassword)));
    }
}
