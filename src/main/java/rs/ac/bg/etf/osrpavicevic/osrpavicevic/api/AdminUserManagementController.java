package rs.ac.bg.etf.osrpavicevic.osrpavicevic.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.request.SchoolUserUpdateRequest;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.AllUserResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.api.response.RegistrationResponse;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.entity.SchoolUserEntity;
import rs.ac.bg.etf.osrpavicevic.osrpavicevic.service.UserManagementService;

@RestController
@RequestMapping("/admin/user-management")
@RequiredArgsConstructor
public class AdminUserManagementController {

    private final UserManagementService userManagementService;

    @GetMapping("/get-all")
    public ResponseEntity<AllUserResponse> getAllUsers() {
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<RegistrationResponse> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(userManagementService.getUserById(userId));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<RegistrationResponse> updateUser(@PathVariable Integer userId,
                                                           @RequestBody SchoolUserUpdateRequest request) {
        SchoolUserEntity userEntity = SchoolUserEntity.builder().firstname(request.getFirstname())
                .lastname(request.getLastname()).build();
        return ResponseEntity.ok(userManagementService.updateUser(userId, userEntity));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<DefaultResponse> deleteUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userManagementService.deleteUser(userId));
    }

    @GetMapping("/get-my-info")
    public ResponseEntity<RegistrationResponse> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(userManagementService.getMyInfo(username));
    }

}
