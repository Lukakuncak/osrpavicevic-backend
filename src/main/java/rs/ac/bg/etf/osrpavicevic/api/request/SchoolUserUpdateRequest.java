package rs.ac.bg.etf.osrpavicevic.api.request;


import lombok.*;


@Builder
public record SchoolUserUpdateRequest(
        Integer id,
        String firstname,
        String lastname,
        String role
) {

}
