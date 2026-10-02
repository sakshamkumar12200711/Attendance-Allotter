package com.attendance.attendance.faculty;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public void register(String facultyId, String rawPassword) {
        if (facultyId == null || facultyId.isBlank()
                || rawPassword == null || rawPassword.length() < 6 || rawPassword.length() > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID is required and password must be 6 to 72 characters");
        }
        String id = facultyId.trim();
        if (id.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID cannot exceed 100 characters");
        }
        if (facultyRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ID already exists");
        }
        facultyRepository.save(new Faculty(id, passwordEncoder.encode(rawPassword)));
    }
}
