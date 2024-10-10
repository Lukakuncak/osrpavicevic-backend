package rs.ac.bg.etf.osrpavicevic.config;

import com.cloudinary.Cloudinary;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "cloudinary")
@Configuration
public class CloudinaryConfig {

    private String cloudname;
    private String apikey;
    private String secret;
    private String url;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", cloudname);
        valuesMap.put("api_key", apikey);
        valuesMap.put("api_secret", secret);
        return new Cloudinary(valuesMap);
    }

}
