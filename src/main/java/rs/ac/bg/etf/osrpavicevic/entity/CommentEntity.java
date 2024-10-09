package rs.ac.bg.etf.osrpavicevic.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private SchoolUserEntity user;

    @ManyToOne
    @JoinColumn(nullable = false,name = "news_id")
    private NewsEntity news;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime commentCreatedDate;

    private String reply;

    private LocalDateTime replyCreatedDate;

    @Column(nullable = false)
    private boolean approved;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationEntity> notifications;
}
