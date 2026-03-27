package com.example.demo;

import com.example.demo.dto.BloodGroupCountResponseEntity;
import com.example.demo.entity.Patient;
import com.example.demo.entity.type.BloodGroupType;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class PatientTests {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Test
    public void testPatientRepository(){
//        List<Patient> patientList = patientRepository.findAll(); // it tries to find all patient and its corresponding appointments. so it hits the database multiple times
        List<Patient> patientList = patientRepository.findAllPatientWithAppointment();
        System.out.println(patientList);
    }

    @Test
    public void testTransactionMethod(){
        Patient patient = patientService.getPatientById(2L);
        System.out.println(patient);

//          Patient patient = patientRepository.findByName("Diya Patel");
//          System.out.println(patient);

//          List<Patient> patientList = patientRepository.findByBirthDateOrEmail(LocalDate.of(1988, 3,15), "diya.patel@gmail.com");
//        List<Patient> patientList = patientRepository.findByNameContainingOrderByIdDesc("Di");
//          for (Patient patient : patientList){
//              System.out.println(patient);
//          }

//        List<Patient> patientList = patientRepository.findByBloodGroup(BloodGroupType.A_POSITIVE);
//        List<Patient> patientList = patientRepository.findByBornAfterDate(LocalDate.of(1993, 3, 14));
//        for (Patient patient : patientList){
//            System.out.println(patient);
//        }

//        List<Object[]> bloodGroupList = patientRepository.countEachBloodGroupType();
//        for (Object[] objects : bloodGroupList){
//            System.out.println(objects[0]+" "+objects[1]);
//        }

//        List<BloodGroupCountResponseEntity> bloodGroupList = patientRepository.countEachBloodGroupType();
//        for (BloodGroupCountResponseEntity bloodGroupCountResponse : bloodGroupList){
//            System.out.println(bloodGroupCountResponse);
//        }

//        List<Patient> patientList1 = patientRepository.findAllPatients();
//        for (Patient patient : patientList1){
//            System.out.println(patient);
//        }

//        Page<Patient> patientList1 = patientRepository.findAllPatients(PageRequest.of(0, 2, Sort.by("name")));
//        for (Patient patient : patientList1){
//            System.out.println(patient);
//        }
//
//        int rowsUpdated = patientRepository.updateNameWithId("Arav Sharma",1L);
//        System.out.println(rowsUpdated);


    }
}
