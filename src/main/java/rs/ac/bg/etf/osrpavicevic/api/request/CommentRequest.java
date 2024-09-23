package rs.ac.bg.etf.osrpavicevic.api.request;

public record CommentRequest(
        Integer userId,
        Long newsId,
        String content

) {
}
