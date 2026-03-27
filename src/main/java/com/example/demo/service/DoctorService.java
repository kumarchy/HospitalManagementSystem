package com.example.demo.service;

import com.example.demo.dto.DoctorResponseDto;
import com.example.demo.dto.OnboardDoctorRequestDto;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.User;
import com.example.demo.entity.type.RoleType;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public DoctorResponseDto onBoardNewDoctor(OnboardDoctorRequestDto onboardDoctorRequestDto) {
        User user = userRepository.findById(onboardDoctorRequestDto.getUserId()).orElseThrow();

        if (doctorRepository.existsById(onboardDoctorRequestDto.getUserId())){
            throw new IllegalArgumentException("Already a doctor");
        }

        Doctor doctor = Doctor.builder()
                .name(onboardDoctorRequestDto.getName())
                .specialization(onboardDoctorRequestDto.getSpecialization())
                .user(user)
                .build();

        user.getRoles().add(RoleType.DOCTOR);

        return modelMapper.map(doctorRepository.save(doctor), DoctorResponseDto.class);
    }

    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doctor ->
                modelMapper.map(doctor, DoctorResponseDto.class))
                .collect(Collectors.toList());
    }
}
