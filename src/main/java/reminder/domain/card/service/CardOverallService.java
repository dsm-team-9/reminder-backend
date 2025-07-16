package reminder.domain.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CardOverallService {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public int calculateOverall(String cardContent) {
        String prompt = "Rate the quality and comprehensiveness of the following text content on a scale of 1 to 100. Provide only the numerical rating. Text: " + cardContent;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}], \"role\":\"user\"}], \"generationConfig\":{\"responseMimeType\":\"text/plain\"}}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                try {
                    return Integer.parseInt(response.getBody().trim());
                } catch (NumberFormatException e) {
                    // Fallback if Gemini doesn't return a perfect number
                    return 50; // Default overall
                }
            } else {
                return 50; // Default overall on API error
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 50; // Default overall on exception
        }
    }

    public String provideFeedbackForCard(String cardContent, int overall) {
        String prompt = "Based on the following card content: \"" + cardContent + "\" and its overall score of " + overall + ". Provide constructive feedback in Korean on how to improve its quality and comprehensiveness. Suggest specific improvements.";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}], \"role\":\"user\"}], \"generationConfig\":{\"responseMimeType\":\"text/plain\"}}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                return "피드백을 제공할 수 없습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "피드백을 제공하는 중 오류가 발생했습니다.";
        }
    }
}
