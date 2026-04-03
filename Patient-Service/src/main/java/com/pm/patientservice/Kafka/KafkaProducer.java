package com.pm.patientservice.Kafka;

import com.pm.patient.events.PatientEvent;
import com.pm.patientservice.Modal.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendEvent(Patient patient){
        PatientEvent patientEvent = PatientEvent.newBuilder().setPatientId(patient.getId()+"")
                .setEmail(patient.getEmail())
                .setName(patient.getName())
                .setEventType("Publish Event : Patient_Created").build();
       try {
           kafkaTemplate.send("Patient", patientEvent.toByteArray());
       }
       catch (Exception e) {
           log.error("Sending Patient Event to Kafka failed {} ",patientEvent);
       }
    }
}
