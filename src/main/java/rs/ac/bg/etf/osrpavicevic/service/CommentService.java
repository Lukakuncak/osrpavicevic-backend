package rs.ac.bg.etf.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.api.request.CommentRequest;
import rs.ac.bg.etf.osrpavicevic.domain.Comments;
import rs.ac.bg.etf.osrpavicevic.domain.News;
import rs.ac.bg.etf.osrpavicevic.entity.CommentEntity;
import rs.ac.bg.etf.osrpavicevic.entity.NewsEntity;
import rs.ac.bg.etf.osrpavicevic.entity.SchoolUserEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.CommentMapper;
import rs.ac.bg.etf.osrpavicevic.mapper.NewsMapper;
import rs.ac.bg.etf.osrpavicevic.respository.CommentRepository;
import rs.ac.bg.etf.osrpavicevic.respository.NewsRepository;
import rs.ac.bg.etf.osrpavicevic.respository.SchoolUserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final NewsRepository newsRepository;
    private final SchoolUserRepository schoolUserRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final NewsMapper newsMapper;

    public Comments createComment(CommentRequest request) {
        CommentEntity commentEntity = commentMapper.fromCreateRequest(request);
        commentEntity.setCommentCreatedDate(LocalDateTime.now());

        SchoolUserEntity schoolUserEntity = schoolUserRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User with id " + request.userId() + " not found!"));
        commentEntity.setUser(schoolUserEntity);
        NewsEntity newsEntity = newsRepository.findById(request.newsId())
                .orElseThrow(() -> new RuntimeException("News with id " + request.newsId() + " not found!"));
        commentEntity.setNews(newsEntity);

        return commentMapper.toDomain(commentRepository.save(commentEntity));
    }

    public List<Comments> getAllUnapproved() {
        List<CommentEntity> commentEntities = commentRepository.findAllByApprovedFalse();
        return commentEntities.stream().map(commentEntity -> {
            Comments commentsElement = commentMapper.toDomain(commentEntity);
            News mappedNews = newsMapper.toDomain(commentEntity.getNews());
            commentsElement.setNews(mappedNews);
            return commentsElement;
        }).toList();
    }

    public void approveComment(Long id) {
        CommentEntity commentEntity = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No comment with id " + id + " found"));
        commentEntity.setApproved(true);
        commentRepository.save(commentEntity);
    }

    public void deleteComment(Long id) {
        CommentEntity commentEntity = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No comment with id " + id + " found"));
        commentRepository.delete(commentEntity);
    }

}
