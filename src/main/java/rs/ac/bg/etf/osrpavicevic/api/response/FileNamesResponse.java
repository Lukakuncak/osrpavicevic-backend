package rs.ac.bg.etf.osrpavicevic.api.response;

import lombok.Builder;

@Builder
public record FileNamesResponse(
        int statusCode,
        String error,
        String[] filenames
) {

}
