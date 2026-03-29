package com.example.demo.entity;

import com.example.demo.entity.type.AuthProviderType;
import com.example.demo.entity.type.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "app_user"
        , indexes = {
        @Index(name = "idx_provider_id_provider_type", columnList = "providerId, providerType")
}
)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(unique = true, nullable = false)
    private String username; // when we use oAuth then we can use here email or sometime providerId since it is unique. eg. google oAuth provide email , facebook doesn't but provide providerId
    private String password;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType;

    @ElementCollection(fetch = FetchType.EAGER)  //elementCOlleciton is for creating separate table, and when we fetch user it will fetch roles of user too
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROle_"+ role.name()))
                .collect(Collectors.toSet());

        // at the end in we consider user authority too while storing userPasswordAuthenticationToken in securityContextHolder
    }
}
