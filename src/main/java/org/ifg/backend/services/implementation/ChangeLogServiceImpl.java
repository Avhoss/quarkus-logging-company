package org.ifg.backend.services.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.ifg.backend.dtos.ChangeLogRecord;
import org.ifg.backend.entities.ChangeLog;
import org.ifg.backend.services.ChangeLogService;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ChangeLogServiceImpl implements ChangeLogService {
    @Override
    public List<ChangeLogRecord> listAll() {
        List<ChangeLog> changeLogList = ChangeLog.listAll();
        List<ChangeLogRecord> changeLogRecordList = new ArrayList<>();
        for (ChangeLog changeLog : changeLogList) {
            changeLogRecordList.add(new ChangeLogRecord(changeLog.changeType, changeLog.description, changeLog.logDate));
        }
        return changeLogRecordList.isEmpty() ? null : changeLogRecordList;
    }

    @Transactional
    @Override
    public void insert(ChangeLogRecord changeLogRecord) {
        ChangeLog changeLog = new ChangeLog();
        changeLog.changeType = changeLogRecord.changeType();
        changeLog.description = changeLogRecord.description();
        changeLog.logDate = changeLogRecord.logDate();
        changeLog.persist();
    }
}
