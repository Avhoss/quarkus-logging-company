package org.ifg.backend.services;

import org.ifg.backend.dtos.CompanyRecord;

import java.util.List;

public interface CompanyService {

    List<CompanyRecord> listAll();

    CompanyRecord findById(String companyName);

    void upsert(CompanyRecord companyRecord);
}
