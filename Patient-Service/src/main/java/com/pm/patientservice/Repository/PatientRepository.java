package com.pm.patientservice.Repository;

import com.pm.patientservice.Modal.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    public boolean existsByEmail(String email);

    public boolean existsByEmailAndIdNot(String email,int id);
}
