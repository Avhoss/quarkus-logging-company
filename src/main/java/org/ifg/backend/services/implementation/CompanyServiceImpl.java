package org.ifg.backend.services.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ifg.backend.dtos.ChangeLogRecord;
import org.ifg.backend.dtos.CompanyRecord;
import org.ifg.backend.entities.Company;
import org.ifg.backend.kafka.producers.ChangeLogProducer;
import org.ifg.backend.services.CompanyService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CompanyServiceImpl implements CompanyService {

    @Inject
    ChangeLogProducer changeLogProducer;

    @Override
    public List<CompanyRecord> listAll() {
        List<Company> companyList = Company.listAll();
        List<CompanyRecord> companyRecordList = new ArrayList<>();
        for (Company company : companyList) {
            companyRecordList.add(new CompanyRecord(company.companyName, company.sector, company.location));
        }
        return companyRecordList.isEmpty() ? null : companyRecordList;
    }

    @Override
    public CompanyRecord findById(String companyName) {
        Company company = Company.findById(companyName);
        return company == null ? null : new CompanyRecord(company.companyName, company.sector, company.location);
    }

    @Transactional
    @Override
    public void upsert(CompanyRecord companyRecord) {
        Company company = Company.findById(companyRecord.companyName());
        if (company != null) {
            String companySectorInitial = company.sector;
            String companyLocationInitial = company.location;
            company.sector = companyRecord.sector().isBlank() ? company.sector : companyRecord.sector();
            company.location = companyRecord.location().isBlank() ? company.location : companyRecord.location();
            company.persist();
            changeLogProducer.send(new ChangeLogRecord(
                    "UPDATE_COMPANY",
                    String.format(
                            "Updated company with name: %s. Sector from [%s] to [%s]. Location from [%s] to [%s]",
                            company.companyName,
                            companySectorInitial, company.sector,
                            companyLocationInitial, company.location
                    ),
                    LocalDateTime.now()
            ));
        } else {
            Company companyToBeInserted = new Company();
            companyToBeInserted.companyName = companyRecord.companyName();
            companyToBeInserted.sector = companyRecord.sector();
            companyToBeInserted.location = companyRecord.location();
            companyToBeInserted.persist();
            changeLogProducer.send(new ChangeLogRecord(
                    "INSERT_COMPANY",
                    String.format(
                            "Created company with name: %s. Sector [%s]. Location [%s]",
                            companyToBeInserted.companyName,
                            companyToBeInserted.sector,
                            companyToBeInserted.location
                    ),
                    LocalDateTime.now()
            ));
        }
    }
}
