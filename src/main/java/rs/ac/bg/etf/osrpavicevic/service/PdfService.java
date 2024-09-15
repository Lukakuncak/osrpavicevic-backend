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
import java.rmi.RemoteException;
import java.util.Arrays;

@Service
public class PdfService {
    @Value("${pdf.upload.dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty.");
        }
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path filePath = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
        file.transferTo(filePath);
        return "Successfully uploaded file!";
    }

    public String[] listPdfFiles() {
        File folder = new File(uploadDir);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".pdf"));
        if (files != null) {
            return Arrays.stream(files).map(File::getName).toArray(String[]::new);
        }
        return new String[0];
    }

    public Resource getPdfFile(String filename) throws MalformedURLException {
        Path filepath = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(filepath.toUri());
        if(resource.exists() || resource.isReadable()){
            return resource;
        }
        else {
            throw new RuntimeException("File "+filename+" not found.");
        }
    }
}
