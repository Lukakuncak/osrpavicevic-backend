package rs.ac.bg.etf.osrpavicevic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private CommentEntity comment;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SchoolUserEntity user;
}
