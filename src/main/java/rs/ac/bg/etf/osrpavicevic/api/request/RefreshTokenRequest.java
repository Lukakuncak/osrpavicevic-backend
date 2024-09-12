package rs.ac.bg.etf.osrpavicevic.api.request;

import lombok.*;

@Builder
public record RefreshTokenRequest(
        String refreshToken
) {
}
