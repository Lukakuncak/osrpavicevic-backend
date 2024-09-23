package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.etf.osrpavicevic.api.request.RefreshTokenRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.LoginResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.SchoolUserResponse;
import rs.ac.bg.etf.osrpavicevic.constants.Role;
import rs.ac.bg.etf.osrpavicevic.api.request.LoginRequest;
import rs.ac.bg.etf.osrpavicevic.api.request.RegistrationRequest;
import rs.ac.bg.etf.osrpavicevic.service.AuthService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SchoolUserResponse> register(@RequestBody RegistrationRequest request) {
        if (request.getRole() == null || request.getRole().isEmpty()) {
            request.setRole(Role.STANDARD.name());
        }else {
            request.setRole(request.getRole().toUpperCase());
        }
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @GetMapping("/get-all-roles")
    public ResponseEntity<List<String>> getAllRoles() {
        return ResponseEntity.ok(Arrays.stream(Role.values()).map(Enum::name).toList());
    }
}
