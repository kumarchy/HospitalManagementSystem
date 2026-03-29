package com.example.demo.entity;

import com.example.demo.entity.type.BloodGroupType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Table (
        name = "patient",
        uniqueConstraints = {
//                @UniqueConstraint(name = "unique_patient_email", columnNames = {"email"}),
                @UniqueConstraint(name = "unique_patient_name_birthdate", columnNames = {"name","birthDate"})
                },
        indexes = {
                @Index(name ="idx_patinent_birthdate", columnList = "birthDate")
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    @ToString.Exclude
    private LocalDate birthDate;

    @Column(unique = true, nullable = false)
    private String email;

    private String gender;

    @OneToOne
    @MapsId    // now in this patient table there will be no more id field. there will be user_id in patient table which is equivalent to user table id
    private User user;

    @CreationTimestamp // set date only at first creation or saving of data not at updation
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

//    @Override
//    public String toString() {
//        return "Patient{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", birthDate=" + birthDate +
//                ", email='" + email + '\'' +
//                ", gender='" + gender + '\'' +
//                '}';
//    }

//    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})  // MERGE for update and PERSIST for first time save in parent
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "patient_insurance_id") // it changes the join column name in database
    private Insurance insurance; // owning side

    // in one to one relation fetchType is bydefault Eager since when we fetch patient we will get insurence too
    // but in one to many explicitely we need to mention eager or lazy or else ToString.Exclude

    @OneToMany(mappedBy = "patient", cascade = {CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
//    @ToString.Exclude   // since while fetching patient with its insurence it was lazy fetching with appointments error
    private List<Appointment> appointments = new ArrayList<>();  // this will try to make database call for each patient to find its appointment which cause n+1 problems
    // solution is either we don't want to fetch appointments with patients i.e.fetchType lazy or we have to write custom query to fetch at one time database call
}
