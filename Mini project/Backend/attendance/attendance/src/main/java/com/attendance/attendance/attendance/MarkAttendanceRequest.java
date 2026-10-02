package com.attendance.attendance.attendance;

import java.util.List;

// {"records": ["351_2026-10-02_Maths"], "status": "PRESENT"}
public class MarkAttendanceRequest {
    private List<String> records;
    private String status;          // PRESENT or ABSENT (defaults to PRESENT if missing)

    public List<String> getRecords() { return records; }
    public void setRecords(List<String> records) { this.records = records; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
