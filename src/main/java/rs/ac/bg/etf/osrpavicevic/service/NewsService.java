package rs.ac.bg.etf.osrpavicevic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.etf.osrpavicevic.api.request.NewsCreateRequest;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;
import rs.ac.bg.etf.osrpavicevic.domain.Comment;
import rs.ac.bg.etf.osrpavicevic.domain.Image;
import rs.ac.bg.etf.osrpavicevic.domain.News;
import rs.ac.bg.etf.osrpavicevic.entity.ImageEntity;
import rs.ac.bg.etf.osrpavicevic.entity.NewsEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.CommentMapper;
import rs.ac.bg.etf.osrpavicevic.mapper.NewsMapper;
import rs.ac.bg.etf.osrpavicevic.respository.NewsRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsMapper newsMapper;
    private final CommentMapper commentMapper;
    private final NewsRepository newsRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public News createNews(NewsCreateRequest newsRequest) {
        NewsEntity newsEntity = newsMapper.fromCreateToEntity(newsRequest);
        newsEntity.setDateTime(LocalDateTime.now());
        return newsMapper.toDomain(newsRepository.save(newsEntity));

    }

    public Page<News> getAllNews(Pageable pageable, String search) {
        if (search == null || search.isEmpty()) {
            return newsRepository.findAllByPinnedFalseAndDeletedFalse(pageable).map(newsMapper::toDomain);
        } else {
            return newsRepository.findAllByTitleContainingIgnoreCaseAndPinnedFalseAndDeletedFalse(search, pageable).map(newsMapper::toDomain);
        }
    }

    public Page<News> getAllNewsByType(TypeOfNews typeOfNews, String search, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            return newsRepository.findAllByTypeAndPinnedFalseAndDeletedFalse(typeOfNews, pageable).map(newsMapper::toDomain);
        } else {
            return newsRepository.findAllByTypeAndTitleContainingIgnoreCaseAndPinnedFalseAndDeletedFalse(typeOfNews, search, pageable).map(newsMapper::toDomain);
        }
    }

    public News getNewsWithComments(Long id) {
        NewsEntity newsEntity = newsRepository.findByIdWithComments(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        List<Comment> comments = commentMapper.toDomain(newsEntity.getComments());
        News news = newsMapper.toDomain(newsEntity);
        news.setComments(comments);
        return news;
    }

    @Transactional
    public String updateClickCount(Long id) {
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        Long clicks = newsEntity.getClicks();
        newsEntity.setClicks(++clicks);
        newsRepository.save(newsEntity);
        return "Successfully updated click count.";
    }

    @Transactional
    public String pinUnpinNews(Long id) {
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        boolean pinned = newsEntity.isPinned();
        newsEntity.setPinned(!pinned);
        newsRepository.save(newsEntity);
        return "Successfully pinned/unpinned news";
    }

    public List<News> getAllNewsPinned() {
        return newsRepository.findAllByPinnedTrue().stream().map(newsMapper::toDomain).toList();
    }

    @Transactional
    public String deleteNews(Long id) throws IOException {
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        if(newsEntity.getImage() != null){
            cloudinaryService.delete(newsEntity.getImage().getId());
            newsEntity.setImage(null);
        }
        newsEntity.setDeleted(true);
        newsRepository.save(newsEntity);
        return "Successfully deleted news";
    }

    public News updateContent(Long id, String content) {
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        newsEntity.setContent(content);
        return newsMapper.toDomain(newsRepository.save(newsEntity));
    }


    public News addImageToNews(Long id, MultipartFile multipartFile) throws IOException {
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        if(newsEntity.getImage()!=null){
            cloudinaryService.delete(newsEntity.getImage().getId());
        }
        Image image = cloudinaryService.uploadImage(multipartFile);
        newsEntity.setImage(ImageEntity.builder().id(image.getId())
                .imageId(image.getImageId())
                .imageUrl(image.getImageUrl())
                .name(image.getName()).build());
        return newsMapper.toDomain(newsRepository.save(newsEntity));
    }

    public News removePictureForId(Long id) throws IOException {
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        ImageEntity imageEntity = newsEntity.getImage();
        if (imageEntity == null) throw new RuntimeException("You cant delete image if it does not exists.");
        cloudinaryService.delete(imageEntity.getId());
        newsEntity.setImage(null);
        return newsMapper.toDomain(newsRepository.save(newsEntity));
    }
}
