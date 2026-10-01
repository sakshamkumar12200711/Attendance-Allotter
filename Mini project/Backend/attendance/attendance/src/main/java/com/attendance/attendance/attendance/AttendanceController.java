package com.attendance.attendance.attendance;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // POST /api/v1/attendance/mark
    @PostMapping("/mark")
    public MarkResult mark(@RequestBody MarkAttendanceRequest req) {
        return attendanceService.mark(req.getRecords());
    }

    // GET /api/v1/attendance?studentId=S101&subject=Maths&date=2026-10-01
    // GET /api/v1/attendance?studentId=S101&from=2026-09-01&to=2026-09-30
    @GetMapping
    public List<AttendanceResponse> search(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return attendanceService.search(studentId, subject, date, from, to);
    }
}
