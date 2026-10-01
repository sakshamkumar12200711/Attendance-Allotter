package com.attendance.attendance.attendance;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "attendance_date", "subject"}))
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

    public Attendance() {}

    public Attendance(String studentId, LocalDate attendanceDate, String subject) {
        this.studentId = studentId;
        this.attendanceDate = attendanceDate;
        this.subject = subject;
    }

    public Long getId() { return id; }
    public String getStudentId() { return studentId; }
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public String getSubject() { return subject; }
}
