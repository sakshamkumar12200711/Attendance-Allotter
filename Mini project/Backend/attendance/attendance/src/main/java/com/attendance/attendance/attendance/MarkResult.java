package com.attendance.attendance.attendance;

import java.util.List;

public record MarkResult(int saved, List<String> duplicates, List<String> invalid) {}
