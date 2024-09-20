package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.etf.osrpavicevic.api.request.NewsCreateRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.PageNewsResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.NewsResponse;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;
import rs.ac.bg.etf.osrpavicevic.service.NewsService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/news/create")
    public ResponseEntity<NewsResponse> createNews(@RequestBody NewsCreateRequest newsRequest) {
        try {
            return ResponseEntity.ok(NewsResponse.builder()
                    .news(newsService.createNews(newsRequest))
                    .statusCode(200)
                    .message("News successfully created!")
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(NewsResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @GetMapping("public/news/get-all-types")
    public ResponseEntity<List<String>> getAllTypes() {
        return ResponseEntity.ok(Arrays.stream(TypeOfNews.values()).map(Enum::name).toList());
    }

    @GetMapping("public/news/get-all")
    public ResponseEntity<PageNewsResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "dateTime") String sortBy,  // Default sorting by dateTime
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDir);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return ResponseEntity.ok(PageNewsResponse.builder()
                    .newsPage(newsService.getAllNews(pageable,search))
                    .statusCode(200)
                    .message("Fetched all news successfully.")
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(PageNewsResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @GetMapping("public/news/get-all-by-type")
    public ResponseEntity<PageNewsResponse> getAllByType(
            @RequestParam String type,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDir);
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return ResponseEntity.ok(PageNewsResponse.builder()
                    .newsPage(newsService.getAllNewsByType(TypeOfNews.valueOf(type), search, pageable))
                    .statusCode(200)
                    .message("Fetched all news successfully.")
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(PageNewsResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }
}
