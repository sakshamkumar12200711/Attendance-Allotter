package com.attendance.attendance.faculty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "faculty")
public class Faculty {

    @Id
    @Column(name = "faculty_id")
    private String facultyId;

    @Column(name = "password")
    private String password;

    public Faculty() {}

    public Faculty(String facultyId, String password) {
        this.facultyId = facultyId;
        this.password = password;
    }

    public String getFacultyId() { return facultyId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
