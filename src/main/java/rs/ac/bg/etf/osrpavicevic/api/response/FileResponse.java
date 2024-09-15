package rs.ac.bg.etf.osrpavicevic.api.response;

import lombok.Builder;
import org.springframework.core.io.Resource;

@Builder
public record FileResponse(int statusCode,
                           String error, Resource file) {
}
