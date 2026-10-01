package com.attendance.attendance.student;

import com.attendance.attendance.common.LoginRequest;
import com.attendance.attendance.common.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<String> getStudentIds() {
        return studentService.getStudentIds();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        if (studentService.login(req.getId(), req.getPassword())) {
            return ResponseEntity.ok(new LoginResponse(true, "Login successful"));
        }
        return ResponseEntity.status(401).body(new LoginResponse(false, "Invalid ID or password"));
    }
}
