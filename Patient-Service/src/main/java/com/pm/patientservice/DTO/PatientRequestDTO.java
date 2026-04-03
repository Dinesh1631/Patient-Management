package com.pm.patientservice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PatientRequestDTO
{
    @NotBlank(message="User name should not be empty")
    @Size(min=1,max=50,message = "User name is too long")
    private String name;

    @NotBlank(message="Email should not be empty")
    @Email(message="Email should be valid")
    private String email;

    @NotBlank(message="Address should not be empty")
    private String address;

    @NotBlank(message="Date of Birth is required")
    private String dateOfBirth;

    @NotBlank(groups=CreatePatientValidationGroup.class ,message="Registered Date is required")
    private String registeredDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }
}
