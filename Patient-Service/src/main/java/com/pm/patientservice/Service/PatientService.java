package com.pm.patientservice.Service;

import com.pm.patientservice.DTO.PatientRequestDTO;
import com.pm.patientservice.DTO.PatientResponceDTO;
import com.pm.patientservice.Exception.EmailAlredayExistsException;
import com.pm.patientservice.Exception.PatientNotFoundException;
import com.pm.patientservice.GRPC.BillingServiceGrpcClinet;
import com.pm.patientservice.Kafka.KafkaProducer;
import com.pm.patientservice.Mapper.PatientMapper;
import com.pm.patientservice.Modal.Patient;
import com.pm.patientservice.Repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {
    private final Logger log = LoggerFactory.getLogger(PatientService.class);
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClinet billingServiceGrpcClinet;
    private final KafkaProducer kafkaProducer;
    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClinet billingServiceGrpcClinet, KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClinet = billingServiceGrpcClinet;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponceDTO> findAllPatients() {
       List<Patient> patients = patientRepository.findAll();
       List<PatientResponceDTO> listDTO = patients.stream()
               .map(PatientMapper::EntityTODtoMapper).toList();
       return listDTO;
    }

    public PatientResponceDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail()))
        {
            throw new EmailAlredayExistsException("User with this email already exists :"+patientRequestDTO.getEmail());
        }

        Patient patient = patientRepository.save(PatientMapper.DtoTOEntityMapper(patientRequestDTO));
        log.info("Created Patient : {}", patient);
        try {
            billingServiceGrpcClinet.createBillingAccount(patient.getId() + "", patient.getName(), patient.getEmail());
            log.info("gRPC call succeeded");
        } catch (Exception e) {
            log.error("Error calling billing service", e);
        }
        log.info("Sending Kafka event");
        try {
            kafkaProducer.sendEvent(patient);
            log.info("Kafka call succeeded");
        } catch (Exception e) {
            log.error("Error sending Kafka event", e);
        }
        return PatientMapper.EntityTODtoMapper(patient);
    }

    public PatientResponceDTO updatePatinetDetails(PatientRequestDTO patientRequestDTO,int id) {
        Patient patient = patientRepository.findById(id).
                orElseThrow(() -> new PatientNotFoundException("Patient with given Id doesnot exist :" + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)) {
            throw new EmailAlredayExistsException("User with this email already exists :" + patientRequestDTO.getEmail());
        }
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setName(patientRequestDTO.getName());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient UpadatedPatient = patientRepository.save(patient);
        return PatientMapper.EntityTODtoMapper(UpadatedPatient);
    }


    public boolean deletePatient(int id) {
        Patient patient = patientRepository.findById(id).
                orElseThrow(() -> new PatientNotFoundException("Patient with given Id doesnot exist :" + id));
        if(patient!=null) {
            patientRepository.delete(patient);
            return true;
        }
        return false;
    }

}
