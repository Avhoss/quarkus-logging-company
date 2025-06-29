package org.ifg.backend.services.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ifg.backend.dtos.ChangeLogRecord;
import org.ifg.backend.dtos.EmployeeRecord;
import org.ifg.backend.entities.Company;
import org.ifg.backend.entities.Employee;
import org.ifg.backend.kafka.producers.ChangeLogProducer;
import org.ifg.backend.services.EmployeeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmployeeServiceImpl implements EmployeeService {

    @Inject
    ChangeLogProducer changeLogProducer;

    @Override
    public List<EmployeeRecord> listAll() {
        List<Employee> employeeList = Employee.listAll();
        List<EmployeeRecord> employeeRecordList = new ArrayList<>();
        for (Employee employee : employeeList) {
            employeeRecordList.add(new EmployeeRecord(employee.id, employee.firstName, employee.firstName, employee.workplace.companyName));
        }
        return employeeRecordList.isEmpty() ? null : employeeRecordList;
    }

    @Override
    public EmployeeRecord findById(Integer id) {
        Employee employee = Employee.findById(id);
        return employee == null ? null : new EmployeeRecord(employee.id, employee.firstName, employee.firstName, employee.workplace.companyName);
    }

    @Transactional
    @Override
    public void upsert(EmployeeRecord employeeRecord) {
        Employee employee = employeeRecord.id() == null ? null : Employee.findById(employeeRecord.id());
        Company company = Company.findById(employeeRecord.workplaceName());
        if (company == null) {
            throw new RuntimeException("No such company in the database!");
        }
        if (employee != null) {
            String employeeFirstNameInitial = employee.firstName;
            String employeeLastNameInitial = employee.lastName;
            String employeeWorkplaceInitial = employee.workplace.companyName;
            employee.firstName = employeeRecord.firstName().isBlank() ? employee.firstName : employeeRecord.firstName();
            employee.lastName = employeeRecord.lastName().isBlank() ? employee.lastName : employeeRecord.lastName();
            employee.workplace = employeeRecord.workplaceName() == null ? employee.workplace : company;
            employee.persist();
            changeLogProducer.send(new ChangeLogRecord(
                    "UPDATE_EMPLOYEE",
                    String.format(
                            "Updated employee ID: %d. First Name from [%s] to [%s]. Last Name from [%s] to [%s]. Company from [%s] to [%s]",
                            employee.id,
                            employeeFirstNameInitial, employee.firstName,
                            employeeLastNameInitial, employee.lastName,
                            employeeWorkplaceInitial, employee.workplace.companyName
                    ),
                    LocalDateTime.now()
            ));
        } else {
            Employee employeeToBeInserted = new Employee();
            employeeToBeInserted.firstName = employeeRecord.firstName();
            employeeToBeInserted.lastName = employeeRecord.lastName();
            employeeToBeInserted.workplace = company;
            employeeToBeInserted.persist();
            changeLogProducer.send(new ChangeLogRecord(
                    "INSERT_EMPLOYEE",
                    String.format(
                            "Created employee ID: %d. First Name [%s]. Last Name [%s]. Company [%s]",
                            employeeToBeInserted.id,
                            employeeToBeInserted.firstName,
                            employeeToBeInserted.lastName,
                            employeeToBeInserted.workplace.companyName
                    ),
                    LocalDateTime.now()
            ));
        }
    }
}
