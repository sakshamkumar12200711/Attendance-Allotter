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
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    // parsed form of "studentid_date_subject"
    private record Key(String studentId, LocalDate date, String subject) {}

    // Creates the row, or changes its status if it already exists (Present <-> Absent)
    @Transactional
    public MarkResult mark(List<String> records, String status) {
        int saved = 0;
        int updated = 0;
        List<String> invalid = new ArrayList<>();

        if (records == null) return new MarkResult(0, 0, invalid);

        String st = normalizeStatus(status);
        if (st == null) {                       // bad status value: reject everything
            invalid.addAll(records);
            return new MarkResult(0, 0, invalid);
        }

        for (String rec : records) {
            Key k = parse(rec);
            if (k == null || !studentRepository.existsById(k.studentId())) {
                invalid.add(rec);
                continue;
            }
            Optional<Attendance> existing = attendanceRepository
                    .findByStudentIdAndAttendanceDateAndSubject(k.studentId(), k.date(), k.subject());
            if (existing.isPresent()) {
                existing.get().setStatus(st);
                attendanceRepository.save(existing.get());
                updated++;
            } else {
                attendanceRepository.save(new Attendance(k.studentId(), k.date(), k.subject(), st));
                saved++;
            }
        }
        return new MarkResult(saved, updated, invalid);
    }

    // "Unmark" = remove the row. Returns true if something was deleted.
    @Transactional
    public boolean unmark(String rec) {
        Key k = parse(rec);
        if (k == null) return false;
        Optional<Attendance> existing = attendanceRepository
                .findByStudentIdAndAttendanceDateAndSubject(k.studentId(), k.date(), k.subject());
        if (existing.isEmpty()) return false;
        attendanceRepository.delete(existing.get());
        return true;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) return "PRESENT";
        String s = status.trim().toUpperCase();
        return (s.equals("PRESENT") || s.equals("ABSENT")) ? s : null;
    }

    // "studentid_date_subject" -> Key, or null if malformed
    private Key parse(String rec) {
        if (rec == null) return null;
        String[] p = rec.trim().split("_", 3);   // limit 3: subject may contain '_'
        if (p.length != 3 || p[0].isBlank() || p[2].isBlank()) return null;
        try {
            return new Key(p[0].trim(), LocalDate.parse(p[1].trim()), p[2].trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // every filter is optional; pass null to ignore it
    public List<AttendanceResponse> search(String studentId, String subject, String status,
                                           LocalDate date, LocalDate from, LocalDate to) {
        Specification<Attendance> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            if (studentId != null) preds.add(cb.equal(root.get("studentId"), studentId));
            if (subject != null)   preds.add(cb.equal(root.get("subject"), subject));
            if (status != null)    preds.add(cb.equal(root.get("status"), status.toUpperCase()));
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
