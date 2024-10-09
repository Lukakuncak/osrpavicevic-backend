package rs.ac.bg.etf.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.api.request.NewsCreateRequest;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;
import rs.ac.bg.etf.osrpavicevic.domain.Comment;
import rs.ac.bg.etf.osrpavicevic.domain.News;
import rs.ac.bg.etf.osrpavicevic.entity.NewsEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.CommentMapper;
import rs.ac.bg.etf.osrpavicevic.mapper.NewsMapper;
import rs.ac.bg.etf.osrpavicevic.respository.NewsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsMapper newsMapper;
    private final CommentMapper commentMapper;
    private final NewsRepository newsRepository;

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

    public String updateClickCount(Long id) {
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        Long clicks = newsEntity.getClicks();
        newsEntity.setClicks(++clicks);
        newsRepository.save(newsEntity);
        return "Successfully updated click count.";
    }

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

    public String deleteNews(Long id) {
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        newsEntity.setDeleted(true);
        newsRepository.save(newsEntity);
        return "Successfully deleted news";
    }
}
