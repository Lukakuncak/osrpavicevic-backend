package rs.ac.bg.etf.osrpavicevic.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Image {
    private Integer id;
    private String name;
    private String imageUrl;
    private String imageId;
}
