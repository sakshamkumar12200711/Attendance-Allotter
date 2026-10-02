package com.attendance.attendance.attendance;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // POST /api/v1/attendance/mark   {"records":["351_2026-10-02_Maths"],"status":"ABSENT"}
    @PostMapping("/mark")
    public MarkResult mark(@RequestBody MarkAttendanceRequest req) {
        return attendanceService.mark(req.getRecords(), req.getStatus());
    }

    // DELETE /api/v1/attendance?record=351_2026-10-02_Maths
    @DeleteMapping
    public Map<String, Boolean> unmark(@RequestParam String record) {
        return Map.of("deleted", attendanceService.unmark(record));
    }

    // GET /api/v1/attendance?studentId=351
    // GET /api/v1/attendance?studentId=351&subject=JAVA&status=PRESENT
    // GET /api/v1/attendance?date=2026-10-02&subject=JAVA
    // GET /api/v1/attendance?studentId=351&from=2026-09-01&to=2026-09-30
    @GetMapping
    public List<AttendanceResponse> search(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return attendanceService.search(studentId, subject, status, date, from, to);
    }
}
