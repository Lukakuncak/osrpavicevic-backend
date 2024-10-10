package rs.ac.bg.etf.osrpavicevic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.entity.ImageEntity;
import rs.ac.bg.etf.osrpavicevic.respository.ImageRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public List<ImageEntity> list() {
        return imageRepository.findByOrderById();
    }

    public Optional<ImageEntity> getOne(Integer id) {
        return imageRepository.findById(id);
    }

    @Transactional
    public ImageEntity save(ImageEntity image) {
        return imageRepository.save(image);
    }

    @Transactional
    public void delete(Integer id) {
        imageRepository.deleteById(id);
    }

    @Transactional
    public boolean exists(int id) {
        return imageRepository.existsById(id);
    }

}
