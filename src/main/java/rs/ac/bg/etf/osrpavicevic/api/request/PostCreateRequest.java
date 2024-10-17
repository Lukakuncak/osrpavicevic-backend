package rs.ac.bg.etf.osrpavicevic.api.request;

import rs.ac.bg.etf.osrpavicevic.constants.PostType;
public record PostCreateRequest(

        String title,
        String content,
        PostType type
) {
}
