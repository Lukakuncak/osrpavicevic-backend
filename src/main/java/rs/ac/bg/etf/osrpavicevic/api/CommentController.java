package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.etf.osrpavicevic.api.request.CommentRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.CommentResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.CommentsResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.service.CommentService;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN') ||  hasAuthority('STANDARD')")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request) {
        try {
            return ResponseEntity.ok(CommentResponse.builder().comment(commentService.createComment(request))
                    .statusCode(200)
                    .message("Comment successfully posted")
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError()
                    .body(CommentResponse.builder()
                            .error(exception.getMessage())
                            .statusCode(500).build());
        }
    }

    @GetMapping("/get-all-unapproved")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CommentsResponse> getAllUnapproved() {
        try {
            return ResponseEntity.ok(CommentsResponse.builder().statusCode(200).message("All unapproved comments fetched successfully")
                    .comments(commentService.getAllUnapproved()).build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError()
                    .body(CommentsResponse.builder()
                            .error(exception.getMessage())
                            .statusCode(500).build());
        }
    }

    @GetMapping("/get-comment/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CommentResponse> getSingleComment(@PathVariable("id")Long id){
        try {
            return ResponseEntity.ok(CommentResponse.builder().comment(commentService.getSingleComment(id))
                    .statusCode(200)
                    .message("Comment successfully fetched")
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError()
                    .body(CommentResponse.builder()
                            .error(exception.getMessage())
                            .statusCode(500).build());
        }
    }

    @PutMapping("/approve-comment/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> approveComment(@PathVariable("id") Long id) {
        try {
            commentService.approveComment(id);
            return ResponseEntity.ok(DefaultResponse.builder().message("Successfully approved comment").statusCode(200).build());
        } catch (Exception e) {
            DefaultResponse exceptionResponse = DefaultResponse.builder()
                    .statusCode(500)
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @DeleteMapping("/delete-comment/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> deleteComment(@PathVariable("id") Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok(DefaultResponse.builder().message("Successfully deleted comment").statusCode(200).build());
        } catch (Exception e) {
            DefaultResponse exceptionResponse = DefaultResponse.builder()
                    .statusCode(500)
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @PostMapping("/reply-to-comment/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> replyToComment(@PathVariable("id") Long id, @RequestBody String reply) {
        try {
            commentService.replyToComment(id, reply);
            return ResponseEntity.ok(DefaultResponse.builder().message("Successfully replied to comment").statusCode(200).build());
        } catch (Exception e) {
            DefaultResponse exceptionResponse = DefaultResponse.builder()
                    .statusCode(500)
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }
}
