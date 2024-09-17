package rs.ac.bg.etf.osrpavicevic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.bg.etf.osrpavicevic.constants.TypeOfNews;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 10000,nullable = false)
    private String content;
    @Column(nullable = false)
    private TypeOfNews type;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private LocalDateTime dateTime;
    private Long clicks;
    private boolean pinned;
}
