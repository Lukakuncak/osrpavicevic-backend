package rs.ac.bg.etf.osrpavicevic.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import rs.ac.bg.etf.osrpavicevic.domain.News;

@SuperBuilder
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsResponse extends DefaultResponse{
    private News news;
}
