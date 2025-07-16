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
import reminder.domain.card.controller.dto.CardChatResponse;
import reminder.domain.card.domain.Card;
import reminder.domain.card.domain.repository.CardRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardChatService {

    private final CardRepository cardRepository;

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public CardChatResponse chatWithCard(Long cardId, String userMessage) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found."));

        String cardContent = "Title: " + card.getTitle() + ". Content: " + card.getContent();
        String prompt = "Based on the following card content: \"" + cardContent + "\". " +
                        "Answer the user's question in Korean: \"" + userMessage + "\"";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}], \"role\":\"user\"}], \"generationConfig\":{\"responseMimeType\":\"text/plain\"}}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey,
                HttpMethod.POST,
                entity,
                String.class
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return new CardChatResponse(response.getBody());
        } else {
            return new CardChatResponse("Sorry, I couldn't process your request at the moment.");
        }
    }
}
