package rs.ac.bg.etf.osrpavicevic.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.etf.osrpavicevic.api.response.DefaultResponse;
import rs.ac.bg.etf.osrpavicevic.api.response.FileNamesResponse;
import rs.ac.bg.etf.osrpavicevic.service.PdfService;

@RestController
@RequiredArgsConstructor
public class PdfController {
    private final PdfService pdfService;

    @PostMapping("/pdf/upload/{rootFile}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> uploadPdf(@RequestParam("file") MultipartFile file, @PathVariable String rootFile) {
        try {
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message(pdfService.uploadFile(rootFile, file)).build());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build());
        }
    }


    @DeleteMapping("pdf/delete/{rootFile}/{filename}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DefaultResponse> deleteFile(@PathVariable String rootFile, @PathVariable String filename) {
        try {
            return ResponseEntity.ok(DefaultResponse.builder().statusCode(200).message(pdfService.deleteFile(rootFile, filename)).build());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DefaultResponse.builder()
                    .statusCode(500)
                    .error(exception.getMessage())
                    .build());
        }
    }

    @GetMapping("/public/pdf/list-all/{rootFile}")
    public ResponseEntity<FileNamesResponse> listPdfFiles(@PathVariable String rootFile) {
        try {
            return ResponseEntity.ok(FileNamesResponse.builder().filenames(pdfService.listPdfFiles(rootFile)).statusCode(200).build());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FileNamesResponse.builder().error(exception.getMessage()).statusCode(500).build());
        }
    }

    @GetMapping("/public/pdf/download/{rootFile}/{filename}")
    public ResponseEntity<Resource> getPdfFile(@PathVariable String rootFile, @PathVariable String filename) {
        try {
            return ResponseEntity.ok(pdfService.getPdfFile(rootFile, filename));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
