package org.ifg.backend.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.ifg.backend.dtos.ChangeLogRecord;
import org.ifg.backend.dtos.CompanyRecord;
import org.ifg.backend.dtos.EmployeeRecord;
import org.ifg.backend.services.ChangeLogService;
import org.ifg.backend.services.CompanyService;
import org.ifg.backend.services.EmployeeService;

import java.util.List;

@Path("/company-access")
public class CompanyAccessResource {

    @Inject
    ChangeLogService changeLogService;

    @Inject
    CompanyService companyService;

    @Inject
    EmployeeService employeeService;

    @Path("/change-log")
    @GET
    public Response getAllChangeLog() {
        List<ChangeLogRecord> changeLogRecordList = changeLogService.listAll();
        return changeLogRecordList == null ? Response.noContent().build() : Response.ok(changeLogRecordList).build();
    }

    @Path("/company")
    @GET
    public Response getCompany() {
        List<CompanyRecord> companyRecordList = companyService.listAll();
        return companyRecordList == null ? Response.noContent().build() : Response.ok(companyRecordList).build();
    }

    @Path("/company/{companyName}")
    @GET
    public Response getCompany(@PathParam("companyName") String companyName) {
        CompanyRecord companyRecord = companyService.findById(companyName);
        return companyRecord == null ? Response.noContent().build() : Response.ok(companyRecord).build();
    }

    @Path("/company")
    @POST
    public Response upsertCompany(CompanyRecord companyRecord) {
        companyService.upsert(companyRecord);
        return Response.ok().build();
    }

    @Path("/employee")
    @GET
    public Response getEmployee() {
        List<EmployeeRecord> employeeRecordList = employeeService.listAll();
        return employeeRecordList == null ? Response.noContent().build() : Response.ok(employeeRecordList).build();
    }

    @Path("/employee/{id}")
    @GET
    public Response getEmployee(@PathParam("id") Integer employeeId) {
        EmployeeRecord employeeRecord = employeeService.findById(employeeId);
        return employeeRecord == null ? Response.noContent().build() : Response.ok(employeeRecord).build();
    }

    @Path("/employee")
    @POST
    public Response upsertEmployee(EmployeeRecord employeeRecord) {
        employeeService.upsert(employeeRecord);
        return Response.ok().build();
    }
}
