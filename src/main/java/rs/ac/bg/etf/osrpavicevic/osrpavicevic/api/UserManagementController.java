package rs.ac.bg.etf.osrpavicevic.osrpavicevic.api;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/user-management")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserManagementService userManagementService;

    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AllUserResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username")); // Or any other sorting you prefer
        Page<SchoolUserEntity> userPage = userManagementService.getAllUsers(pageable, search);

        AllUserResponse response = new AllUserResponse();
        response.setUsers(userPage.getContent());
        response.setTotalPages(userPage.getTotalPages());
        response.setTotalElements(userPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RegistrationResponse> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(userManagementService.getUserById(userId));
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RegistrationResponse> updateUser(@PathVariable Integer userId,
                                                           @RequestBody SchoolUserUpdateRequest request) {
        SchoolUserEntity userEntity = SchoolUserEntity.builder().firstname(request.getFirstname())
                .lastname(request.getLastname()).role(request.getRole().toUpperCase()).build();
        return ResponseEntity.ok(userManagementService.updateUser(userId, userEntity));
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> deleteUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userManagementService.deleteUser(userId));
    }

    @GetMapping("/get-my-info")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STANDARD')")
    public ResponseEntity<RegistrationResponse> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(userManagementService.getMyInfo(username));
    }

}
