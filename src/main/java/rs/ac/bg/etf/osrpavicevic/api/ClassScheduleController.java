package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.etf.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.constants.ClassScheduleSchool;
import rs.ac.bg.etf.osrpavicevic.entity.ClassScheduleEntity;
import rs.ac.bg.etf.osrpavicevic.respository.ClassScheduleRepository;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ClassScheduleController {
    private final ClassScheduleRepository classScheduleRepository;

    @PutMapping("/class-schedule/rajak")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> changeUrlRajak(@RequestParam String url) {
        try {
            Optional<ClassScheduleEntity> optionalClassScheduleEntity = classScheduleRepository.findById(ClassScheduleSchool.RAJAK);
            if (optionalClassScheduleEntity.isEmpty()) {
                classScheduleRepository.save(ClassScheduleEntity.builder().nameOfSchool(ClassScheduleSchool.RAJAK).url(url).build());
            } else {
                optionalClassScheduleEntity.get().setUrl(url);
                classScheduleRepository.save(optionalClassScheduleEntity.get());
            }
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message("Successfully updated schedule url.").build());
        } catch (Exception e) {
            DefaultResponse exceptionResponse = DefaultResponse.builder()
                    .statusCode(500)
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @PutMapping("/class-schedule/pilica")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> changeUrlPilica(@RequestParam String url) {
        try {
            Optional<ClassScheduleEntity> optionalClassScheduleEntity = classScheduleRepository.findById(ClassScheduleSchool.PILICA);
            if (optionalClassScheduleEntity.isEmpty()) {
                classScheduleRepository.save(ClassScheduleEntity.builder().nameOfSchool(ClassScheduleSchool.PILICA).url(url).build());
            } else {
                optionalClassScheduleEntity.get().setUrl(url);
                classScheduleRepository.save(optionalClassScheduleEntity.get());
            }
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message("Successfully updated schedule url.").build());
        } catch (Exception e) {
            DefaultResponse exceptionResponse = DefaultResponse.builder()
                    .statusCode(500)
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
        }
    }

    @GetMapping("/public/class-schedule/rajak")
    public ResponseEntity<String> getRajakUrl() {
        Optional<ClassScheduleEntity> optionalClassScheduleEntity = classScheduleRepository.findById(ClassScheduleSchool.RAJAK);
        return optionalClassScheduleEntity.map(classScheduleEntity -> ResponseEntity.ok(classScheduleEntity.getUrl())).orElseGet(() -> ResponseEntity.ok(""));
    }
    @GetMapping("/public/class-schedule/pilica")
    public ResponseEntity<String> getPilicaUrl() {
        Optional<ClassScheduleEntity> optionalClassScheduleEntity = classScheduleRepository.findById(ClassScheduleSchool.PILICA);
        return optionalClassScheduleEntity.map(classScheduleEntity -> ResponseEntity.ok(classScheduleEntity.getUrl())).orElseGet(() -> ResponseEntity.ok(""));
    }
}
