package org.ifg.backend.kafka.deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.ifg.backend.dtos.ChangeLogRecord;

public class ChangeLogDeserializer extends ObjectMapperDeserializer<ChangeLogRecord> {
    public ChangeLogDeserializer() {
        super(ChangeLogRecord.class);
    }
}
