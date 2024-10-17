package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.etf.osrpavicevic.api.request.NewsContentRequest;
import rs.ac.bg.etf.osrpavicevic.api.request.PostCreateRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.*;
import rs.ac.bg.etf.osrpavicevic.constants.PostType;
import rs.ac.bg.etf.osrpavicevic.service.PostService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/post/create")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreateRequest postCreateRequest){
        try {
            return ResponseEntity.ok(PostResponse.builder()
                    .post(postService.createPost(postCreateRequest))
                    .statusCode(200)
                    .message("Post successfully created!")
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(PostResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @GetMapping("public/post/get-all-types")
    public ResponseEntity<List<String>> getAllTypes() {
        return ResponseEntity.ok(Arrays.stream(PostType.values()).map(Enum::name).toList());
    }


    @GetMapping("public/post/get-all-ucenici")
    public ResponseEntity<PagePostResponse> getAllUcenici(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "dateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDir);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return ResponseEntity.ok(PagePostResponse.builder()
                    .postPage(postService.getAllUceniciPost(pageable, search))
                    .statusCode(200)
                    .message("Fetched all ucenici post successfully.")
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(PagePostResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @GetMapping("public/post/get-all-roditelji")
    public ResponseEntity<PagePostResponse> getAllRoditelji(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "dateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDir);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return ResponseEntity.ok(PagePostResponse.builder()
                    .postPage(postService.getAllRoditeljiPost(pageable, search))
                    .statusCode(200)
                    .message("Fetched all roditelji post successfully.")
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(PagePostResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @PutMapping("post/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> deletePost(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message(postService.deletePost(id)).build());
        } catch (Exception exception) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build());
        }
    }

    @GetMapping("public/post/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(PostResponse.builder().statusCode(200)
                    .post(postService.getPost(id))
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(PostResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @PutMapping("post/edit-content/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponse> editPostContent(@RequestBody NewsContentRequest content, @PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(PostResponse.builder().message("Successfully updated post content").statusCode(200)
                    .post(postService.updateContent(id, content.content()))
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(PostResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @PutMapping("post/add-picture/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponse> addImageToPost(@RequestParam MultipartFile multipartFile, @PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(PostResponse.builder().statusCode(200)
                    .post(postService.addImageToPost(id, multipartFile))
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(PostResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @PutMapping("post/remove-picture/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponse> removePicture(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(PostResponse.builder().statusCode(200)
                    .post(postService.removePictureForId(id))
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(PostResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @PutMapping("post/add-file/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> addFileToRoditeljiPost(@RequestParam MultipartFile multipartFile, @PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200)
                    .message(postService.addFileToPost(id, multipartFile))
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(DefaultResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @DeleteMapping("post/delete-file/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> deleteFile(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message(postService.deleteFile(id)).build());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build());
        }
    }

    @GetMapping("/public/post/download/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(postService.getFile(id));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
