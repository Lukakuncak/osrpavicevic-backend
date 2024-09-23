package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.etf.osrpavicevic.api.request.CommentRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.CommentResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.CommentsResponse;
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
    public ResponseEntity<CommentsResponse> getAllUnapproved(){
        try {
            return ResponseEntity.ok(CommentsResponse.builder().statusCode(200).message("All unapproved comments fetched successfully")
                    .comments(commentService.getAllUnapproved()).build());
        }catch (Exception exception){
            return ResponseEntity.internalServerError()
                    .body(CommentsResponse.builder()
                            .error(exception.getMessage())
                            .statusCode(500).build());
        }
    }
}
