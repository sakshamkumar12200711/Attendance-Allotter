package com.attendance.attendance.attendance;

import java.util.List;

// saved = new rows, updated = existing rows whose status changed
public record MarkResult(int saved, int updated, List<String> invalid) {}
