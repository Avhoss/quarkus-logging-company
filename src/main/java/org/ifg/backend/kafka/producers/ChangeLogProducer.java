package org.ifg.backend.kafka.producers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.ifg.backend.dtos.ChangeLogRecord;

@ApplicationScoped
public class ChangeLogProducer {

    @Inject
    @Channel("changelog-out")
    Emitter<ChangeLogRecord> emitter;

    public void send(ChangeLogRecord changeLogRecord) {
        emitter.send(changeLogRecord);
    }
}
