package com.example.demo.controller;

import com.example.demo.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final AppointmentService appointmentService;

@GetMapping("/appointments")
    public ResponseEntity<?> getAllAppointmentsOfDoctor(Authentication authentication){
    User user = (User)authentication.getPrincipal();
    String username = user.getUsername();

    return ResponseEntity.ok(appointmentService.getAllAppointmentsOfDoctor(username));
}
}
