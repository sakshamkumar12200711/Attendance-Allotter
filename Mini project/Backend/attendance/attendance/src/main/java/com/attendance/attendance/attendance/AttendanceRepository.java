package com.attendance.attendance.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDate;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

    boolean existsByStudentIdAndAttendanceDateAndSubject(String studentId, LocalDate date, String subject);
}
