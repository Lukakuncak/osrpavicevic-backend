package rs.ac.bg.etf.osrpavicevic.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.RegistrationResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.service.UserManagementService;

@RestController
@RequestMapping("/standard/user-management")
@RequiredArgsConstructor
public class StandardUserManagementController {
    private final UserManagementService userManagementService;

    @GetMapping("/get-my-info")
    public ResponseEntity<RegistrationResponse> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(userManagementService.getMyInfo(username));
    }

}
