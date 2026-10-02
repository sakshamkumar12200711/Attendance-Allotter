package com.attendance.attendance.faculty;

import com.attendance.attendance.common.LoginRequest;
import com.attendance.attendance.common.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        if (facultyService.login(req.getId(), req.getPassword())) {
            return ResponseEntity.ok(new LoginResponse(true, "Login successful"));
        }
        return ResponseEntity.status(401).body(new LoginResponse(false, "Invalid ID or password"));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody LoginRequest req) {
        try {
            facultyService.register(req.getId(), req.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new LoginResponse(true, "Account created"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new LoginResponse(false, e.getReason()));
        }
    }
}
