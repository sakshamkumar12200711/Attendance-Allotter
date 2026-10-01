package com.attendance.attendance.faculty;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    public FacultyService(FacultyRepository facultyRepository, PasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(String facultyId, String rawPassword) {
        if (facultyId == null || rawPassword == null) return false;
        return facultyRepository.findById(facultyId)
                .map(f -> f.getPassword() != null
                        && passwordEncoder.matches(rawPassword, f.getPassword()))
                .orElse(false);
    }
}
