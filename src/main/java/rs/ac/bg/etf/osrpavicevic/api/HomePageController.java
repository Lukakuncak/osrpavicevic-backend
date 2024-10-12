package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.etf.osrpavicevic.service.HomePageService;

@RestController
@RequestMapping("/public/home")
@RequiredArgsConstructor
public class HomePageController {
    private final HomePageService homePageService;
    @GetMapping("/year-of-working")
    public ResponseEntity<Integer> getYearOfWork(){
        return ResponseEntity.ok(homePageService.getHowLongSchoolWorks());
    }

    @GetMapping("/number-of-workers")
    public ResponseEntity<Integer> getNumberOfWorkers(){
        return ResponseEntity.ok(homePageService.getNumberOfWorkers());
    }
}
