package com.attendance.attendance.faculty;

import com.attendance.attendance.common.LoginRequest;
import com.attendance.attendance.common.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
