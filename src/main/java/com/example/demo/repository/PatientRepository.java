package com.example.demo.repository;

import com.example.demo.dto.BloodGroupCountResponseEntity;
import com.example.demo.entity.Patient;
import com.example.demo.entity.type.BloodGroupType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByName(String name);
    List<Patient> findByBirthDateOrEmail(LocalDate birthdate, String email);

    List<Patient> findByBirthDateBetween (LocalDate startDate, LocalDate endDate);
    List<Patient> findByNameContainingOrderByIdDesc(String name);

    @Query("select p from Patient p where p.bloodGroup =  ?1") // this way is used to avoid sql injection attack
    List<Patient>findByBloodGroup(@Param("bloodGroup") BloodGroupType bloodGroup);

    @Query("select p from Patient p where p.birthDate >  :birthDate") // if we want to go simply with variable name then we can use this way
    List<Patient>findByBornAfterDate(@Param("birthDate") LocalDate birthDate);

//    @Query("Select p.bloodGroup, Count(p) from Patient p group by p.bloodGroup")
//    List<Object[]> countEachBloodGroupType();
    @Query("select new com.example.demo.dto.BloodGroupCountResponseEntity(p.bloodGroup," +
            " Count(p)) from Patient p group by p.bloodGroup")  // projection is only possible with the jpql query not with the native query
    List<BloodGroupCountResponseEntity> countEachBloodGroupType();

    @Query(value = "Select * from patient", nativeQuery = true)
//    List<Patient> findAllPatients();
    Page<Patient> findAllPatients(Pageable pageable);


    @Transactional  //Transactional is required in update query
    @Modifying
    @Query("Update Patient p set p.name = :name where p.id = :id")
    int updateNameWithId(@Param("name") String name, @Param("id") Long id);

//    @Query("Select p from Patient p left join Fetch p.appointments a left join Fetch a.doctor")
@Query("Select p from Patient p left join Fetch p.appointments")
List<Patient> findAllPatientWithAppointment();
}
