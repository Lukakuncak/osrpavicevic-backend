package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.etf.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.FileNamesResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.FileResponse;
import rs.ac.bg.etf.osrpavicevic.service.PdfService;

@RestController
@RequiredArgsConstructor
public class PdfController {
    private final PdfService pdfService;

    @PostMapping("/pdf/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message(pdfService.uploadFile(file)).build());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build());
        }
    }

    @GetMapping("/public/pdf/list-all")
    public ResponseEntity<FileNamesResponse> listPdfFiles() {
        try {
            return ResponseEntity.ok(FileNamesResponse.builder().filenames(pdfService.listPdfFiles()).statusCode(200).build());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FileNamesResponse.builder().error(exception.getMessage()).statusCode(500).build());
        }
    }

    @GetMapping("/public/pdf/download/{filename}")
    public ResponseEntity<FileResponse> getPdfFile(@PathVariable String filename){
        try {
            return ResponseEntity.ok(FileResponse.builder().file(pdfService.getPdfFile(filename)).statusCode(200).build());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FileResponse.builder().error(exception.getMessage()).statusCode(500).build());
        }
    }
}
