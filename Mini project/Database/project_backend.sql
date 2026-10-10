use attnedance_allotter;

SET SQL_SAFE_UPDATES = 0;

UPDATE faculty SET password = '$2b$10$wicuuNj5x6/hD.VFguL5VenJtaDmXUQkUw6YeGZ2OBG330mn4coHu';
UPDATE student SET password = '$2b$10$wIwArxd68WG3DkyTG0zEzuJlQC5xxxzPEJ8Izml4mORZ/VSQJDQ4.';

SET SQL_SAFE_UPDATES = 1;

DROP TABLE IF EXISTS attendance;

CREATE TABLE attendance (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id VARCHAR(255) NOT NULL,
    attendance_date DATE NOT NULL,
    subject VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_attendance_student_date_subject
        UNIQUE (student_id, attendance_date, subject)
);
describe attendance;
ALTER TABLE attendance
ADD COLUMN status VARCHAR(10) NOT NULL DEFAULT 'PRESENT';

INSERT INTO attendance (student_id, attendance_date, subject, status) VALUES
('351','2026-09-01','ADSA','PRESENT'),
('351','2026-09-02','JAVA','PRESENT'),
('351','2026-09-03','JAVA','ABSENT'),
('353','2026-09-01','JAVA','PRESENT'),
('358','2026-09-02','ADSA','ABSENT'),
('359','2026-09-01','DBMS','PRESENT');
