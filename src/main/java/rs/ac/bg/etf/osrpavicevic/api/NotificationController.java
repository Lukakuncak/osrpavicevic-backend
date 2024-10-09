package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.etf.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.NotificationResponse;
import rs.ac.bg.etf.osrpavicevic.service.NotificationService;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/with-comments")
    @PreAuthorize("hasAuthority('ADMIN') ||  hasAuthority('STANDARD')")
    public ResponseEntity<NotificationResponse> getNotificationsWithComments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            NotificationResponse myUser = NotificationResponse.builder()
                    .notifications(notificationService.getNotificationWithComments(username))
                    .statusCode(200)
                    .message("Notifications successfully fetched.").build();
            return ResponseEntity.ok(myUser);
        } catch (Exception exception) {
            NotificationResponse exceptionResponse = NotificationResponse.builder().notifications(null)
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @PutMapping("/view-single/{id}")
    @PreAuthorize("hasAuthority('ADMIN') ||  hasAuthority('STANDARD')")
    public ResponseEntity<DefaultResponse> viewSingleNotification(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            notificationService.viewNotification(username, id);
            return ResponseEntity.ok(DefaultResponse.builder()
                    .statusCode(200)
                    .message("Successfully viewed notification.")
                    .build());
        } catch (Exception exception) {
            DefaultResponse exceptionResponse = DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @PutMapping("/view-all")
    @PreAuthorize("hasAuthority('ADMIN') ||  hasAuthority('STANDARD')")
    public ResponseEntity<DefaultResponse> viewAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            notificationService.viewAllNotifications(username);
            return ResponseEntity.ok(DefaultResponse.builder()
                    .statusCode(200)
                    .message("Successfully viewed all notifications.")
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
