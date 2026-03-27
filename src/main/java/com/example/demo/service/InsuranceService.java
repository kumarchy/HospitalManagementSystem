package com.example.demo.service;

import com.example.demo.entity.Insurance;
import com.example.demo.entity.Patient;
import com.example.demo.repository.InsuranceRepository;
import com.example.demo.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;

    @Transactional  // since we want to assign to this perticular patient. in transactional context, dirty checking is done
    public Patient assignInsuranceToPatient(Insurance insurance, Long patientId){
        Patient patient = patientRepository.findById(patientId)  // this is in persistent context memory
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        patient.setInsurance(insurance);  //dirty checking
        insurance.setPatient(patient); //bidirectional consistency maintainence

        return patient;
    }

    @Transactional
    public Patient disassociateInsuranceFromPatient(Long patientId){
        Patient patient = patientRepository.findById(patientId)  // this is in persistent context memory
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        patient.setInsurance(null);
        return patient;
    }

    // task : create 3 appointments and assign it to 1 patient and then delete that patient

}
