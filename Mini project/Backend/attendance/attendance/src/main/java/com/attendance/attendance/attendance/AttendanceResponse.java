package com.attendance.attendance.attendance;

import java.time.LocalDate;

public record AttendanceResponse(String studentId, LocalDate date, String subject, String record) {

    public static AttendanceResponse from(Attendance a) {
        return new AttendanceResponse(a.getStudentId(), a.getAttendanceDate(), a.getSubject(),
                a.getStudentId() + "_" + a.getAttendanceDate() + "_" + a.getSubject());
    }
}
