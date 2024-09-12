package rs.ac.bg.etf.osrpavicevic.domain;

import lombok.Builder;
import rs.ac.bg.etf.osrpavicevic.constants.Role;

@Builder
public record SchoolUser(
        Integer id,
        String firstname,
        String lastname,
        String username,
        Role role
) {
}
