package rs.ac.bg.etf.osrpavicevic.api;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.etf.osrpavicevic.api.request.SchoolUserUpdateRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.SchoolUserResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.AllUserResponse;
import rs.ac.bg.etf.osrpavicevic.service.UserManagementService;

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
        Pageable pageable = PageRequest.of(page, size, Sort.by("username"));
        try {
            AllUserResponse successfullyFetchedUsers = AllUserResponse.builder()
                    .usersPage(userManagementService.getAllUsers(pageable, search))
                    .statusCode(200)
                    .message("Successfully fetched users")
                    .build();
            return ResponseEntity.ok(successfullyFetchedUsers);
        } catch (Exception exception) {
            AllUserResponse exceptionResponse = AllUserResponse.builder()
                    .usersPage(null)
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @GetMapping("/get/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SchoolUserResponse> getUserById(@PathVariable Integer userId) {
        try {
            SchoolUserResponse schoolUserResponse = SchoolUserResponse.builder().schoolUser(userManagementService.getUserById(userId))
                    .statusCode(200)
                    .message("User successfully fetched by id.").build();
            return ResponseEntity.ok(schoolUserResponse);
        } catch (Exception exception) {
            SchoolUserResponse exceptionResponse = SchoolUserResponse.builder().schoolUser(null)
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SchoolUserResponse> updateUser(@RequestBody SchoolUserUpdateRequest updateRequest) {
        try {
            SchoolUserResponse updateResponse = SchoolUserResponse.builder()
                    .schoolUser(userManagementService.updateUser(updateRequest))
                    .statusCode(200)
                    .message("Successfully updated user with id: " + updateRequest.id())
                    .build();
            return ResponseEntity.ok(updateResponse);
        } catch (Exception exception) {
            SchoolUserResponse exceptionResponse = SchoolUserResponse.builder().schoolUser(null)
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> deleteUser(@PathVariable Integer userId) {
        try {
            userManagementService.deleteUser(userId);
            return ResponseEntity.ok(DefaultResponse.builder()
                    .statusCode(200)
                    .message("User with id: " + userId + " successfully deleted.")
                    .build());
        } catch (Exception exception) {
            DefaultResponse exceptionResponse = DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @GetMapping("/get-my-info")
    @PreAuthorize("hasAuthority('ADMIN') ||  hasAuthority('STANDARD')")
    public ResponseEntity<SchoolUserResponse> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            SchoolUserResponse myUser = SchoolUserResponse.builder()
                    .schoolUser(userManagementService.getMyInfo(username))
                    .statusCode(200)
                    .message("My info successfully fetched.").build();
            return ResponseEntity.ok(myUser);
        } catch (Exception exception) {
            SchoolUserResponse exceptionResponse = SchoolUserResponse.builder().schoolUser(null)
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @PostMapping("change-password")
    @PreAuthorize("hasAuthority('ADMIN') ||  hasAuthority('STANDARD')")
    public ResponseEntity<DefaultResponse> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            userManagementService.changePassword(username, oldPassword, newPassword);
            return ResponseEntity.ok(DefaultResponse.builder()
                    .statusCode(200)
                    .message("Successfully changed password.")
                    .build());
        } catch (Exception exception) {
            DefaultResponse exceptionResponse = DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }
}
