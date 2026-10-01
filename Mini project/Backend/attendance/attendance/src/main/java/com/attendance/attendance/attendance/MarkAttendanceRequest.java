package com.attendance.attendance.attendance;

import java.util.List;

// e.g. {"records": ["S101_2026-10-01_Maths", "S102_2026-10-01_Maths"]}
public class MarkAttendanceRequest {
    private List<String> records;

    public List<String> getRecords() { return records; }
    public void setRecords(List<String> records) { this.records = records; }
}
