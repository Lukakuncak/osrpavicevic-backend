package rs.ac.bg.etf.osrpavicevic.api.request;

import lombok.Builder;

@Builder
public record NewsContentRequest(String content) {
}
