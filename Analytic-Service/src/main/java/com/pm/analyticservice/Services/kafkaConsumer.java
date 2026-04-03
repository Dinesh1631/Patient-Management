package com.pm.analyticservice.Services;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.patient.events.PatientEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class kafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(kafkaConsumer.class);

    @KafkaListener(topics = "Patient" ,groupId = "analytic-service")
    public void consume(byte[] event) {
        /*Converting the byte array to string*/
        PatientEvent patientEvent = null;
        try {
            patientEvent = PatientEvent.parseFrom(event);
        } catch (InvalidProtocolBufferException e) {
            log.error("Error in deserializing the event {} ", e.getMessage());
        }
        log.info("received event {}", patientEvent);
    }
}
