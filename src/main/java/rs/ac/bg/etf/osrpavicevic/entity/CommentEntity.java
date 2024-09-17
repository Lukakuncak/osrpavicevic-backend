package rs.ac.bg.etf.osrpavicevic.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @JoinColumn(nullable = false)
    private SchoolUserEntity user;
    @ManyToOne
    @JoinColumn(nullable = false)
    private NewsEntity news;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Column(nullable = false)
    private boolean approved = false;
    @Column(nullable = false)
    private boolean deleted = false;
}
