package org.ifg.backend.services;

import org.ifg.backend.dtos.ChangeLogRecord;

import java.util.List;

public interface ChangeLogService {

    List<ChangeLogRecord> listAll();

    void insert(ChangeLogRecord changeLogRecord);
}
