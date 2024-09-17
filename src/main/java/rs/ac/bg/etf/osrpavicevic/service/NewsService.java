package rs.ac.bg.etf.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.api.request.NewsCreateRequest;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;
import rs.ac.bg.etf.osrpavicevic.domain.News;
import rs.ac.bg.etf.osrpavicevic.mapper.NewsMapper;
import rs.ac.bg.etf.osrpavicevic.respository.NewsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsMapper newsMapper;
    private final NewsRepository newsRepository;

    public News createNews(NewsCreateRequest newsRequest, String username) {
        return newsMapper.toDomain(newsRepository.save(newsMapper.fromCreateToEntity(newsRequest, username)));

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
}
