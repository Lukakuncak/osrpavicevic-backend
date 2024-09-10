package rs.ac.bg.etf.osrpavicevic.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.request.LoginRequest;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.request.RefreshTokenRequest;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.request.RegistrationRequest;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.AllUserResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.LoginResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.RegistrationResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.entity.SchoolUserEntity;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.respository.SchoolUserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final String USER_NOT_FOUND = "User not found!";
    private final SchoolUserRepository schoolUserRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public RegistrationResponse register(RegistrationRequest request) {
        RegistrationResponse response = RegistrationResponse.builder().build();
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
                response.setSchoolUserEntity(databaseResponse);
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
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));
            SchoolUserEntity user = schoolUserRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
            String token = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(token);
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
            String username = jwtUtils.extractUsername(request.getRefreshToken());
            SchoolUserEntity schoolUserEntity = schoolUserRepository.findByUsername(username).orElseThrow();
            if (jwtUtils.isTokenValid(request.getRefreshToken(), schoolUserEntity)) {
                String token = jwtUtils.generateToken(schoolUserEntity);
                response.setStatusCode(200);
                response.setToken(token);
                response.setRefreshToken(request.getRefreshToken());
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

    public AllUserResponse getAllUsers() {
        AllUserResponse response = AllUserResponse.builder().build();
        try {
            List<SchoolUserEntity> userEntities = schoolUserRepository.findAll();
            if (userEntities.isEmpty()) {
                response.setStatusCode(200);
                response.setMessage("No users found");
            } else {
                response.setUsers(userEntities);
                response.setStatusCode(200);
                response.setMessage("Successfuly fetched all users!");
            }
            return response;
        } catch (Exception exception) {
            response.setStatusCode(500);
            response.setError(exception.getMessage());
        }
        return response;
    }

    public RegistrationResponse getUserById(Integer id) {
        RegistrationResponse response = RegistrationResponse.builder().build();
        try {
            SchoolUserEntity user = schoolUserRepository.findById(id).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
            response.setSchoolUserEntity(user);
            response.setStatusCode(200);
            response.setMessage("User with id: " + id + " found successfully!");
        } catch (Exception exception) {
            response.setStatusCode(500);
            response.setError(exception.getMessage());
        }
        return response;
    }

    public DefaultResponse deleteUser(Integer id) {
        DefaultResponse response = DefaultResponse.builder().build();
        try {
            SchoolUserEntity schoolUserEntity = schoolUserRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
            String username = schoolUserEntity.getUsername();
            schoolUserRepository.delete(schoolUserEntity);
            response.setStatusCode(200);
            response.setMessage("Successfully deleted user with username: " + username + " and id: " + id);
        } catch (Exception exception) {
            response.setStatusCode(500);
            response.setError(exception.getMessage());
        }
        return response;
    }

    public RegistrationResponse updateUser(Integer id, SchoolUserEntity updateUser) {
        RegistrationResponse response = RegistrationResponse.builder().build();
        try {
            SchoolUserEntity user = schoolUserRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
            user.setUsername(updateUser.getUsername() == null ? user.getUsername() : updateUser.getUsername());
            user.setFirstname(updateUser.getFirstname() == null ? user.getFirstname() : updateUser.getFirstname());
            user.setLastname(updateUser.getLastname() == null ? user.getLastname() : updateUser.getLastname());
            user.setRole(updateUser.getRole() == null ? user.getRole() : updateUser.getRole());
            if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            }
            SchoolUserEntity savedUser = schoolUserRepository.save(user);
            response.setSchoolUserEntity(savedUser);
            response.setStatusCode(200);
            response.setMessage("User updated successfully!");
        } catch (Exception exception) {
            response.setStatusCode(500);
            response.setError(exception.getMessage());
        }
        return response;
    }

    public RegistrationResponse getMyInfo(String username) {
        RegistrationResponse response = RegistrationResponse.builder().build();
        try {
            SchoolUserEntity user = schoolUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
            response.setSchoolUserEntity(user);
            response.setStatusCode(200);
            response.setMessage("Successful!");
        } catch (Exception exception) {
            response.setStatusCode(500);
            response.setError(exception.getMessage());
        }
        return response;
    }
}
