package rs.ac.bg.etf.osrpavicevic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.api.request.LoginRequest;
import rs.ac.bg.etf.osrpavicevic.api.request.RefreshTokenRequest;
import rs.ac.bg.etf.osrpavicevic.api.request.RegistrationRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.LoginResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.SchoolUserResponse;
import rs.ac.bg.etf.osrpavicevic.entity.SchoolUserEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.SchoolUserMapper;
import rs.ac.bg.etf.osrpavicevic.respository.SchoolUserRepository;
import rs.ac.bg.etf.osrpavicevic.utils.JwtUtils;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final SchoolUserRepository schoolUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final SchoolUserMapper schoolUserMapper;

    @Transactional
    public SchoolUserResponse register(RegistrationRequest request) {
        SchoolUserResponse response = SchoolUserResponse.builder().build();
        try {
            Optional<SchoolUserEntity> user = schoolUserRepository.findByUsername(request.getUsername());
            if (user.isPresent()) {
                throw new RuntimeException("User with this username already exists, username must be unique!");
            }
            SchoolUserEntity schoolUserEntity = SchoolUserEntity.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .build();

            SchoolUserEntity databaseResponse = schoolUserRepository.save(schoolUserEntity);
            if (databaseResponse.getId() > 0) {
                response.setStatusCode(200);
                response.setMessage("User successfully registered!");
                response.setSchoolUser(schoolUserMapper.toDomain(databaseResponse));
            }
        } catch (Exception exception) {
            response.setStatusCode(500);
            response.setError(exception.getMessage());
        }
        return response;
    }


    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse response = LoginResponse.builder().build();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(),
                            loginRequest.password()));
            SchoolUserEntity user = schoolUserRepository.findByUsername(loginRequest.username()).orElseThrow();
            String token = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setId(user.getId());
            response.setRefreshToken(refreshToken);
            response.setExpirationDate(JwtUtils.EXPIRATION_TIME_STRING);
            response.setRole(user.getRole());
            response.setMessage("Successfully logged in!");
        } catch (Exception exception) {
            response.setStatusCode(500);
            response.setError(exception.getMessage());
        }
        return response;
    }


    public LoginResponse refreshToken(RefreshTokenRequest request) {
        LoginResponse response = LoginResponse.builder().build();
        try {
            String username = jwtUtils.extractUsername(request.refreshToken());
            SchoolUserEntity schoolUserEntity = schoolUserRepository.findByUsername(username).orElseThrow();
            if (jwtUtils.isTokenValid(request.refreshToken(), schoolUserEntity)) {
                String token = jwtUtils.generateToken(schoolUserEntity);
                response.setId(schoolUserEntity.getId());
                response.setStatusCode(200);
                response.setToken(token);
                response.setRefreshToken(request.refreshToken());
                response.setExpirationDate(JwtUtils.EXPIRATION_TIME_STRING);
                response.setMessage("Successfully refreshed token!");
            }
            response.setStatusCode(200);
            return response;
        } catch (Exception exception) {
            response.setStatusCode(500);
            response.setError(exception.getMessage());
            return response;
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void authenticatePasswordAndUsername(String username, String password){
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username,
                        password));
    }
}
