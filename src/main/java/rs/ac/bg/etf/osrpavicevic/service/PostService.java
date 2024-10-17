package rs.ac.bg.etf.osrpavicevic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.etf.osrpavicevic.api.request.PostCreateRequest;
import rs.ac.bg.etf.osrpavicevic.constants.PostType;
import rs.ac.bg.etf.osrpavicevic.domain.Image;
import rs.ac.bg.etf.osrpavicevic.domain.Post;
import rs.ac.bg.etf.osrpavicevic.entity.ImageEntity;
import rs.ac.bg.etf.osrpavicevic.entity.PostEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.PostMapper;
import rs.ac.bg.etf.osrpavicevic.respository.PostRepository;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;
    @Value("${ppk.upload.dir}")
    private String uploadDir;

    public Post createPost(PostCreateRequest postCreateRequest) {
        PostEntity postEntity = postMapper.fromCreateToEntity(postCreateRequest);
        postEntity.setDateTime(LocalDateTime.now());
        return postMapper.toDomain(postRepository.save(postEntity));
    }

    public Page<Post> getAllUceniciPost(Pageable pageable, String search) {
        if (search == null || search.isEmpty()) {
            return postRepository.findAllByTypeAndDeletedFalse(PostType.UCENICI, pageable).map(postMapper::toDomain);
        } else {
            return postRepository.findAllByTitleContainingIgnoreCaseAndTypeAndDeletedFalse(search, PostType.UCENICI, pageable).map(postMapper::toDomain);
        }
    }

    public Page<Post> getAllRoditeljiPost(Pageable pageable, String search) {
        if (search == null || search.isEmpty()) {
            return postRepository.findAllByTypeAndDeletedFalse(PostType.RODITELJI, pageable).map(postMapper::toDomain);
        } else {
            return postRepository.findAllByTitleContainingIgnoreCaseAndTypeAndDeletedFalse(search, PostType.RODITELJI, pageable).map(postMapper::toDomain);
        }
    }

    @Transactional
    public String deletePost(Long id) throws IOException {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing post with id: " + id));
        if (postEntity.getImage() != null) {
            cloudinaryService.delete(postEntity.getImage().getId());
            postEntity.setImage(null);
        }
        if (postEntity.getFile() != null) {
            Path filePath = Paths.get(postEntity.getFile());
            Files.deleteIfExists(filePath);
            postEntity.setFile(null);
        }
        postEntity.setDeleted(true);
        postRepository.save(postEntity);
        return "Successfully deleted news";
    }

    public Post getPost(Long id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing post with id: " + id));
        return postMapper.toDomain(postEntity);
    }

    @Transactional
    public Post updateContent(Long id, String content) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing post with id: " + id));
        postEntity.setContent(content);
        return postMapper.toDomain(postRepository.save(postEntity));
    }

    @Transactional
    public Post addImageToPost(Long id, MultipartFile multipartFile) throws IOException {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing post with id: " + id));
        if (postEntity.getImage() != null) {
            cloudinaryService.delete(postEntity.getImage().getId());
        }
        Image image = cloudinaryService.uploadImage(multipartFile);
        postEntity.setImage(ImageEntity.builder().id(image.getId())
                .imageId(image.getImageId())
                .imageUrl(image.getImageUrl())
                .name(image.getName()).build());
        return postMapper.toDomain(postRepository.save(postEntity));
    }

    @Transactional
    public Post removePictureForId(Long id) throws IOException {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing post with id: " + id));
        ImageEntity imageEntity = postEntity.getImage();
        if (imageEntity == null) throw new RuntimeException("You cant delete image if it does not exists.");
        cloudinaryService.delete(imageEntity.getId());
        postEntity.setImage(null);
        return postMapper.toDomain(postRepository.save(postEntity));
    }

    @Transactional
    public String addFileToPost(Long id, MultipartFile file) throws IOException {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing post with id: " + id));
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty.");
        }
        Path path = Paths.get(uploadDir + '/' + id);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path filePath = Paths.get(uploadDir + '/' + id + File.separator + file.getOriginalFilename());
        file.transferTo(filePath);
        postEntity.setFile(filePath.toString());
        postRepository.save(postEntity);
        return "Successfully uploaded file!";
    }

    @Transactional
    public String deleteFile(Long id) throws IOException {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing post with id: " + id));
        if (postEntity.getFile() != null) {
            Path filePath = Paths.get(postEntity.getFile());
            Files.deleteIfExists(filePath);
            postEntity.setFile(null);
            postRepository.save(postEntity);
        } else {
            throw new RuntimeException("Missing file for post with id: " + id);
        }
        return "File for post with id " + id + " successfully deleted.";
    }

    public Resource getFile(Long id) throws MalformedURLException {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing post with id: " + id));
        if (postEntity.getFile() != null) {
            Path filepath = Paths.get(postEntity.getFile());
            Resource resource = new UrlResource(filepath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File for post with id " + id + " not found.");
            }
        } else {
            throw new RuntimeException("File for post with id " + id + " not found.");
        }
    }
}
