package reminder.reminderbe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GeminiRestTemplateConfig {

    @Bean
    public RestTemplate geminiRestTemplate() {
        return new RestTemplate();
    }
}
