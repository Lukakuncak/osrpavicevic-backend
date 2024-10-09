package rs.ac.bg.etf.osrpavicevic.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Notification {
    private Long id;
    private Comment comment;
    private SchoolUser schoolUser;
}
