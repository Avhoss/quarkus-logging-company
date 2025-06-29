package org.ifg.backend.kafka.consumers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.ifg.backend.dtos.ChangeLogRecord;
import org.ifg.backend.entities.ChangeLog;

@ApplicationScoped
public class ChangeLogConsumer {

    @Incoming("changelog-in")
    @Transactional
    public void consume(ChangeLogRecord changeLogRecord) {
        ChangeLog changeLog = new ChangeLog();
        changeLog.changeType = changeLogRecord.changeType();
        changeLog.description = changeLogRecord.description();
        changeLog.logDate = changeLogRecord.logDate();
        changeLog.persist();
    }
}
