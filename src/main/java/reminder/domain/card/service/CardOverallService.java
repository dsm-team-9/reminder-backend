package reminder.domain.card.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CardOverallService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public int calculateOverall(String cardContent) {
        String prompt = """
        다음은 사용자가 작성한 학습용 카드의 설명 내용입니다. 이 내용을 기준으로 카드의 **품질과 완성도**를 1부터 100 사이의 숫자로 평가해 주세요.
        점수 기준은 다음과 같습니다:
        - 90~100점: 매우 명확하고 구체적이며, 학습에 유익한 수준입니다.
        - 70~89점: 대체로 유익하지만 약간의 세부 정보나 예시가 부족할 수 있습니다.
        - 50~69점: 핵심은 전달되지만 너무 짧거나 모호하여 보완이 필요합니다.
        - 30~49점: 핵심 정보가 부족하거나 설명이 매우 부족합니다.
        - 0~29점: 내용이 혼란스럽거나 학습 자료로서 부적절합니다.

        **숫자만 하나 반환해 주세요. 다른 설명은 필요하지 않습니다.**

        설명 내용:
        %s
        """.formatted(cardContent);

        Map<String, Object> payload = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt)),
                        "role", "user"
                )),
                "generationConfig", Map.of("responseMimeType", "text/plain")
        );

        try {
            String requestBody = objectMapper.writeValueAsString(payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode textNode = root
                        .path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text");

                if (!textNode.isMissingNode()) {
                    String rawText = textNode.asText().trim(); // 예: "35\n" 또는 "35"
                    return Integer.parseInt(rawText.replaceAll("[^0-9]", ""));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 50; // 기본 점수
    }

    public String provideFeedbackForCard(String title, String cardContent, int overall) {
        String prompt = """
        다음은 사용자가 작성한 카드의 제목과 설명 내용입니다:

        제목: "%s"
        설명: "%s"

        이 제목과 설명에 대해 점수가 %d점일 때 개선할 점을 한국어로 6줄 이내로 작성해 주세요.
        설명이 너무 짧거나 구체성이 떨어진다면 어떻게 보완하면 좋을지 말해주세요.
        결과는 일반 문장 형태로 출력하고, 번호나 줄바꿈, 마크다운, 영어 번역, 특수기호 없이 작성하세요.
        """.formatted(title, cardContent, overall);

        Map<String, Object> payload = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt)),
                        "role", "user"
                )),
                "generationConfig", Map.of("responseMimeType", "text/plain")
        );

        try {
            String requestBody = objectMapper.writeValueAsString(payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return extractFeedbackText(response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "피드백을 제공할 수 없습니다.";
    }

    private String extractFeedbackText(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson); // ← 오타 수정
            JsonNode textNode = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            if (!textNode.isMissingNode()) {
                return textNode.asText().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "피드백을 추출할 수 없습니다.";
    }
}