package org.ifg.backend.dtos;

import java.time.LocalDateTime;

public record ChangeLogRecord(String changeType,
                              String description,
                              LocalDateTime logDate) {
}
