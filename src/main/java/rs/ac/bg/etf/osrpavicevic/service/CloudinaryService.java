package rs.ac.bg.etf.osrpavicevic.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.etf.osrpavicevic.domain.Image;
import rs.ac.bg.etf.osrpavicevic.entity.ImageEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.ImageMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @Transactional
    public Image uploadImage(MultipartFile mpartFile) throws IOException {
        BufferedImage bi = ImageIO.read(mpartFile.getInputStream());
        if (bi == null) {
            throw new RuntimeException("Image not valide");
        }
        File file = convert(mpartFile);
        //upload to cloud
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Failed to delete temporary file: " + file.getAbsoluteFile());
        }
        //save entity so it can be accessed
        ImageEntity image = ImageEntity.builder().name((String) result.get("original_filename"))
                .imageUrl((String) result.get("url"))
                .imageId((String) result.get("public_id")).build();
        return imageMapper.toDomain(imageService.save(image));
    }

    @Transactional
    public void delete(Integer id) throws IOException {
        Optional<ImageEntity> imageEntityOptional = imageService.getOne(id);
        if(imageEntityOptional.isEmpty()){
            throw new RuntimeException("No image found with id "+id);
        }
        ImageEntity image = imageEntityOptional.get();
        String cloudinaryId = image.getImageId();
        cloudinary.uploader().destroy(cloudinaryId, ObjectUtils.emptyMap());
        imageService.delete(id);
    }

    @Transactional
    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}
