package rs.ac.bg.etf.osrpavicevic.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import rs.ac.bg.etf.osrpavicevic.domain.Post;

@SuperBuilder
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagePostResponse extends DefaultResponse{
    private Page<Post> postPage;
}
