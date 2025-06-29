package org.ifg.backend.services;

import org.ifg.backend.dtos.EmployeeRecord;

import java.util.List;

public interface EmployeeService {
    List<EmployeeRecord> listAll();
    EmployeeRecord findById(Integer id);
    void upsert(EmployeeRecord employeeRecord);
}
