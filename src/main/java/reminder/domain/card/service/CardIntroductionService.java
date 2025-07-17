package reminder.domain.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reminder.domain.card.controller.dto.CardChatResponse;
import reminder.domain.card.domain.Card;
import reminder.domain.card.domain.repository.CardRepository;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardIntroductionService {

    private final CardRepository cardRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public CardChatResponse getCardIntroduction(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found."));

        String introductionPrompt = "너는 지금부터 '" + card.getTitle() + "'의 역할을 맡는다.\n" +
                                  card.getTitle() + "이 실제로 말하듯, 해당 인물의 말투, 가치관, 시대적 배경을 반영하여 자신을 소개하라. 답변은 최대 두 줄로 제한한다.\n" +
                                  "답변은 한국어로 하되, 품격 있고 진중한 어조를 유지하고, 가벼운 농담은 피한다.\n\n" +
                                  "다음은 카드의 정보다:\n" +
                                  "Title: " + card.getTitle() + ". Content: " + card.getContent() + ". Introduction: " + card.getIntroduction() + "\n" +
                                  "이 내용을 바탕으로 자신을 소개하라.";
        return callGeminiApi(introductionPrompt);
    }

    private CardChatResponse callGeminiApi(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part), "role", "user");
        Map<String, Object> generationConfig = Map.of("responseMimeType", "text/plain");
        Map<String, Object> requestBodyMap = Map.of(
                "contents", List.of(content),
                "generationConfig", generationConfig
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBodyMap, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                try {
                    JsonNode rootNode = objectMapper.readTree(response.getBody());
                    JsonNode textNode = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");
                    if (textNode != null) {
                        return new CardChatResponse(textNode.asText());
                    } else {
                        log.error("Could not extract text from Gemini API response: {}", response.getBody());
                        return new CardChatResponse("Sorry, I couldn't process your request at the moment. (Response parsing error)");
                    }
                } catch (Exception e) {
                    log.error("Error parsing Gemini API response: {}", e.getMessage(), e);
                    return new CardChatResponse("Sorry, I couldn't process your request at the moment. (Response parsing exception)");
                }
            } else {
                log.error("Gemini API call failed with status code: {} and body: {}", response.getStatusCode(), response.getBody());
                return new CardChatResponse("Sorry, I couldn't process your request at the moment due to an API error.");
            }
        } catch (Exception e) {
            log.error("Error calling Gemini API: {}", e.getMessage(), e);
            return new CardChatResponse("Sorry, an unexpected error occurred while processing your request.");
        }
    }
}
