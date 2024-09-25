package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.etf.osrpavicevic.api.request.NewsCreateRequest;
import rs.ac.bg.etf.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.ListNewsResponse;
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
                    .newsPage(newsService.getAllNews(pageable, search))
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

    @GetMapping("public/news/get-all-pinned")
    public ResponseEntity<ListNewsResponse> getAllPinnedNews(){
        try{
            return ResponseEntity.ok(ListNewsResponse.builder()
                    .newsList(newsService.getAllNewsPinned())
                    .statusCode(200)
                    .message("Fetched all news successfully.")
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(ListNewsResponse.builder()
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

    @GetMapping("public/news/{id}")
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(NewsResponse.builder().message("Successfully fetched comments").statusCode(200)
                    .news(newsService.getNewsWithComments(id))
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(NewsResponse.builder()
                    .error(exception.getMessage())
                    .statusCode(500)
                    .build());
        }
    }

    @PutMapping("public/news/update-click/{id}")
    public ResponseEntity<DefaultResponse> updateClickCount(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message(newsService.updateClickCount(id)).build());
        } catch (Exception exception) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build());
        }
    }

    @PutMapping("news/pin-unpin-news/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> pinUnpinNews(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message(newsService.pinUnpinNews(id)).build());
        } catch (Exception exception) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build());
        }
    }
}
