package com.example.demo.security;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.SignUpRequestDto;
import com.example.demo.dto.SignupResponseDto;
import com.example.demo.entity.Patient;
import com.example.demo.entity.User;
import com.example.demo.entity.type.AuthProviderType;
import com.example.demo.entity.type.RoleType;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    // login is managed by authenticationManager

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                // we used usernamepasswordAuthenticationToken here since we are login based on username and password
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
                // it will do authentication by selecting the appropriate authentication provider which further uses DaoAuthentication->userdetails..
        );

        // if the authentication is valid
        User user = (User) authentication.getPrincipal();

        // to convert the user into token we add 3 dependencies lik jjwt

        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(token, user.getId());
    }

    public User signUpInternal(SignUpRequestDto signupRequestDto, AuthProviderType authProviderType, String providerId)
    {
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if (user !=null) throw new IllegalArgumentException("User already exists");

        user = User.builder()
                .username(signupRequestDto.getUsername())
                .providerId(providerId)
                .providerType(authProviderType)
//                .roles(Set.of(RoleType.PATIENT))  // At first default while sign up every user will be patient then later on admin will convert them to doctor selectively
                .roles(signupRequestDto.getRoles())
                .build();

        if (authProviderType == AuthProviderType.EMAIL){ // in case of login using email id , providerId will b null
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }

        user = userRepository.save(user);

        Patient patient = Patient.builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getUsername())
                .user(user) // this will be same for patient too
                .build();
        patientRepository.save(patient);

        return user;
    }

    // login controller
    public SignupResponseDto signup(SignUpRequestDto signupRequestDto) {
        User user = signUpInternal(signupRequestDto, AuthProviderType.EMAIL, null);
        return new SignupResponseDto(user.getId(), user.getUsername());
    }

    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        //fetch providerType and providerId
        // save the providerType and providerId info with user
        // if the user has an account: directly login

        // otherwise, first signup and then login


        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        // now we need to see in database whether the user with this providerType and providerId exist or not.
        User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);

        // sometime it can be case like user may login using email id and password and oAuth too. so in this case we get email from Oauth and normal login
        // and if both email is same then we can know same user login but we need tho think that whether we should give them access or not. better we should not allow them, we can allow them to login through only auth provider
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User emailUser = userRepository.findByUsername(email).orElse(null);

        if (user==null && emailUser == null){
            //signup flow:

            // sometime for some provider we may not get the email, so to the unique things as username in this case
            String username = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
            // while signup through email id we are asking for role but through oauth we are default providing patient role for user
            user = signUpInternal(new SignUpRequestDto(username, null, name, Set.of(RoleType.PATIENT)), providerType, providerId);  // here above user is getting overridden
        }else if(user !=null){
            // suppose currently twitter is not providing email and in future if it provides means we need to store
            if (email !=null && !email.isBlank() && !email.equals(user.getUsername())){
                user.setUsername(email);
                userRepository.save(user);
            }
        }else{
            // if user is already logged in using email Id and now trying to login with oAuthProvider then we should not allow them

            throw new BadCredentialsException("This email is already registered with provider "+emailUser.getProviderType());
        }

        // for oAuth to login we are using only loginResponseDto not the loginRequestDto because we are not login using username,password(loginRequestDto) but we are using email.
//        so after login it sends jwt, userId which is in loginResponseDto
        LoginResponseDto loginResponseDto = new LoginResponseDto(authUtil.generateAccessToken(user), user.getId());
        return ResponseEntity.ok(loginResponseDto);
    }

}

