package com.attendance.attendance.student;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
}
