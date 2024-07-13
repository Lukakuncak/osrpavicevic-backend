package rs.ac.bg.etf.osrpavicevic.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.request.LoginRequest;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.request.RefreshTokenRequest;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.request.RegistrationRequest;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.LoginResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.RegistrationResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.dto.Role;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.service.UserManagementService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserManagementService userManagementService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        if (request.getRole() == null || request.getRole().isEmpty()) {
            request.setRole(Role.STANDARD.name());
        }
        return ResponseEntity.ok(userManagementService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userManagementService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(userManagementService.refreshToken(request));
    }
}
