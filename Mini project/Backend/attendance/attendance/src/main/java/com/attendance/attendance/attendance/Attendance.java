package com.attendance.attendance.attendance;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "status", nullable = false)
    private String status;          // "PRESENT" or "ABSENT"

    public Attendance() {}

    public Attendance(String studentId, LocalDate attendanceDate, String subject, String status) {
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.subject = subject;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getStudentId() { return studentId; }
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public String getSubject() { return subject; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
