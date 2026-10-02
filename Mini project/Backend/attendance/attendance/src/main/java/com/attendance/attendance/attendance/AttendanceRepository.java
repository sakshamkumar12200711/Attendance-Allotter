package com.attendance.attendance.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

    Optional<Attendance> findByStudentIdAndAttendanceDateAndSubject(
            String studentId, LocalDate attendanceDate, String subject);
}
