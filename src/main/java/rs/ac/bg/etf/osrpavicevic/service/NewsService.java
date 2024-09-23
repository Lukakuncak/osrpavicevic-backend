package rs.ac.bg.etf.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.api.request.NewsCreateRequest;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;
import rs.ac.bg.etf.osrpavicevic.domain.Comments;
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
            return newsRepository.findAll(pageable).map(newsMapper::toDomain);
        } else {
            return newsRepository.findAllByTitleContainingIgnoreCase(search, pageable).map(newsMapper::toDomain);
        }
    }

    public Page<News> getAllNewsByType(TypeOfNews typeOfNews, String search, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            return newsRepository.findAllByType(typeOfNews, pageable).map(newsMapper::toDomain);
        } else {
            return newsRepository.findAllByTypeAndTitleContainingIgnoreCase(typeOfNews, search, pageable).map(newsMapper::toDomain);
        }
    }

    public News getNewsWithComments(Long id) {
        NewsEntity newsEntity = newsRepository.findByIdWithComments(id).orElseThrow(() -> new RuntimeException("Missing news with id: " + id));
        List<Comments> comments = commentMapper.toDomain(newsEntity.getComments());
        News news = newsMapper.toDomain(newsEntity);
        news.setComments(comments);
        return news;
    }

    public String updateClickCount(Long id) {
        NewsEntity newsEntity = newsRepository.findById(id).orElseThrow(()-> new RuntimeException("Missing news with id: "+id));
        Long clicks = newsEntity.getClicks();
        newsEntity.setClicks(++clicks);
        newsRepository.save(newsEntity);
        return "Successfully updated click count.";
    }
}
