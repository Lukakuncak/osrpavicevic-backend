package rs.ac.bg.etf.osrpavicevic.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
public class PdfService {
    @Value("${pdf.upload.dir}")
    private String uploadDir;

    public String uploadFile(String rootFile, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty.");
        }
        Path path = Paths.get(uploadDir+'/'+rootFile);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path filePath = Paths.get(uploadDir+'/'+rootFile + File.separator + file.getOriginalFilename());
        file.transferTo(filePath);
        return "Successfully uploaded file!";
    }

    public String[] listPdfFiles(String rootFile) {
        File folder = new File(uploadDir+'/'+rootFile);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".pdf"));
        if (files != null) {
            return Arrays.stream(files).map(File::getName).toArray(String[]::new);
        }
        return new String[0];
    }

    public Resource getPdfFile(String rootFile, String filename) throws MalformedURLException {
        Path filepath = Paths.get(uploadDir+'/'+rootFile).resolve(filename);
        Resource resource = new UrlResource(filepath.toUri());
        if(resource.exists() || resource.isReadable()){
            return resource;
        }
        else {
            throw new RuntimeException("File "+filename+" not found.");
        }
    }

    public String deleteFile(String rootFile, String filename) throws IOException {
        Path filePath = Paths.get(uploadDir+'/'+rootFile).resolve(filename).normalize();
        Files.deleteIfExists(filePath);
        return "File "+filename+" successfully deleted.";
    }
}
