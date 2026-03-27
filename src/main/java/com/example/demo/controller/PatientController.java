package com.example.demo.controller;

import com.example.demo.dto.AppointmentResponseDto;
import com.example.demo.dto.CreateAppointmentRequestDto;
import com.example.demo.entity.Patient;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseDto> createNewAppointments(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createNewAppointment(createAppointmentRequestDto));
    }
}
