package rs.ac.bg.etf.osrpavicevic.api.request;

import lombok.*;

@Builder
public record LoginRequest(
        String username,
        String password
) {

}
