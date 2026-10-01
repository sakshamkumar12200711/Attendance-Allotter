package com.attendance.attendance.attendance;

import com.attendance.attendance.student.StudentRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public MarkResult mark(List<String> records) {
        int saved = 0;
        List<String> duplicates = new ArrayList<>();
        List<String> invalid = new ArrayList<>();

        if (records == null) return new MarkResult(0, duplicates, invalid);

        for (String rec : records) {
            Attendance a = parse(rec);
            if (a == null || !studentRepository.existsById(a.getStudentId())) {
                invalid.add(rec);
                continue;
            }
            if (attendanceRepository.existsByStudentIdAndAttendanceDateAndSubject(
                    a.getStudentId(), a.getAttendanceDate(), a.getSubject())) {
                duplicates.add(rec);
                continue;
            }
            attendanceRepository.save(a);
            saved++;
        }
        return new MarkResult(saved, duplicates, invalid);
    }

    // "studentid_date_subject" -> Attendance, or null if malformed
    private Attendance parse(String rec) {
        if (rec == null) return null;
        String[] p = rec.trim().split("_", 3);   // limit 3: subject may contain '_'
        if (p.length != 3 || p[0].isBlank() || p[2].isBlank()) return null;
        try {
            return new Attendance(p[0].trim(), LocalDate.parse(p[1].trim()), p[2].trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // every filter is optional; pass null to ignore it
    public List<AttendanceResponse> search(String studentId, String subject,
                                           LocalDate date, LocalDate from, LocalDate to) {
        Specification<Attendance> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            if (studentId != null) preds.add(cb.equal(root.get("studentId"), studentId));
            if (subject != null)   preds.add(cb.equal(root.get("subject"), subject));
            if (date != null)      preds.add(cb.equal(root.get("attendanceDate"), date));
            if (from != null)      preds.add(cb.greaterThanOrEqualTo(root.get("attendanceDate"), from));
            if (to != null)        preds.add(cb.lessThanOrEqualTo(root.get("attendanceDate"), to));
            return cb.and(preds.toArray(new Predicate[0]));
        };
        return attendanceRepository
                .findAll(spec, Sort.by(Sort.Direction.DESC, "attendanceDate"))
                .stream().map(AttendanceResponse::from).toList();
    }
}
