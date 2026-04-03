package com.pm.patientservice.Controller;

import com.pm.patientservice.DTO.CreatePatientValidationGroup;
import com.pm.patientservice.DTO.PatientRequestDTO;
import com.pm.patientservice.DTO.PatientResponceDTO;
import com.pm.patientservice.Modal.Patient;
import com.pm.patientservice.Service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Patients")
public class PatinetController {

    private PatientService patientService;

    public PatinetController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/findAllPatients")
    public ResponseEntity<List<PatientResponceDTO>> getAllThePatients() {
        List<PatientResponceDTO> patientResponceDTO = patientService.findAllPatients();
        return ResponseEntity.status(HttpStatus.OK).body(patientResponceDTO);
    }

    @PostMapping("/createPatient")
    public ResponseEntity<PatientResponceDTO> savePatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/updatePatientDetails")
    public ResponseEntity<PatientResponceDTO> updatePatient(@Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO,
                                                            @RequestParam int id) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.updatePatinetDetails(patientRequestDTO, id));
    }

    @DeleteMapping("/deletePatientById")
    public ResponseEntity<String> deletePatientById(@RequestParam int id) {
        boolean result =patientService.deletePatient(id);
        if(result) {
            return ResponseEntity.status(HttpStatus.OK).body("Delete Patient Successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete Patient Failed");
        }
    }
}
