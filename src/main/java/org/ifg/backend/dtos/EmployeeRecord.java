package org.ifg.backend.dtos;

import org.ifg.backend.entities.Company;

public record EmployeeRecord(Integer id,
                             String firstName,
                             String lastName,
                             String workplaceName) {
}
