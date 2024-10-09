package rs.ac.bg.etf.osrpavicevic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.bg.etf.osrpavicevic.api.request.CommentRequest;
import rs.ac.bg.etf.osrpavicevic.domain.Comment;
import rs.ac.bg.etf.osrpavicevic.domain.News;
import rs.ac.bg.etf.osrpavicevic.entity.CommentEntity;
import rs.ac.bg.etf.osrpavicevic.entity.NewsEntity;
import rs.ac.bg.etf.osrpavicevic.entity.NotificationEntity;
import rs.ac.bg.etf.osrpavicevic.entity.SchoolUserEntity;
import rs.ac.bg.etf.osrpavicevic.mapper.CommentMapper;
import rs.ac.bg.etf.osrpavicevic.mapper.NewsMapper;
import rs.ac.bg.etf.osrpavicevic.respository.CommentRepository;
import rs.ac.bg.etf.osrpavicevic.respository.NewsRepository;
import rs.ac.bg.etf.osrpavicevic.respository.NotificationRepository;
import rs.ac.bg.etf.osrpavicevic.respository.SchoolUserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final NewsRepository newsRepository;
    private final SchoolUserRepository schoolUserRepository;
    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final NewsMapper newsMapper;
    private static final String USER_NOT_FOUND = "User not found!";

    public Comment createComment(CommentRequest request) {
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

    public List<Comment> getAllUnapproved() {
        List<CommentEntity> commentEntities = commentRepository.findAllByApprovedFalse();
        return commentEntities.stream().map(commentEntity -> {
            Comment commentsElement = commentMapper.toDomain(commentEntity);
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

    public void replyToComment(Long id, String reply) {
        CommentEntity commentEntity = commentRepository.findByIdWithUser(id)
                .orElseThrow(() -> new RuntimeException("No comment with id " + id + " found"));

        String oldReply = commentEntity.getReply();

        commentEntity.setReply(reply);
        commentEntity.setReplyCreatedDate(LocalDateTime.now());
        SchoolUserEntity schoolUserEntity = commentEntity.getUser();

        if(oldReply == null){
            //New reply, not editing reply
            NotificationEntity notification = NotificationEntity.builder().comment(commentEntity).user(schoolUserEntity).viewed(false).build();
            notificationRepository.save(notification);
        }
        commentRepository.save(commentEntity);
    }

    public Comment getSingleComment(Long id) {
        CommentEntity commentEntity = commentRepository.findByIdWithUser(id)
                .orElseThrow(() -> new RuntimeException("No comment with id " + id + " found"));
        News mappedNews = newsMapper.toDomain(commentEntity.getNews());
        Comment comment = commentMapper.toDomain(commentEntity);
        comment.setNews(mappedNews);
        return comment;
    }

    public List<Comment> getAllUneplied() {
        List<CommentEntity> commentEntities = commentRepository.findAllByReplyIsNull();
        return commentEntities.stream().map(commentEntity -> {
            Comment commentsElement = commentMapper.toDomain(commentEntity);
            News mappedNews = newsMapper.toDomain(commentEntity.getNews());
            commentsElement.setNews(mappedNews);
            return commentsElement;
        }).toList();
    }
}
