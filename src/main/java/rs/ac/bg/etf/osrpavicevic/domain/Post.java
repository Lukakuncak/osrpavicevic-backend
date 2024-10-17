package rs.ac.bg.etf.osrpavicevic.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import rs.ac.bg.etf.osrpavicevic.constants.PostType;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Post {
    private Long id;
    private String title;
    private String content;
    private PostType type;
    private LocalDateTime dateTime;
    private boolean deleted;
    private Image image;
    private String file;
}
