package reminder.domain.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reminder.domain.card.domain.Card;
import reminder.domain.card.domain.repository.CardRepository;
import reminder.domain.card.controller.dto.CardCreateRequest;
import reminder.domain.user.facade.UserFacade;
import reminder.infra.S3Facade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import reminder.domain.user.entity.User;
import reminder.domain.user.entity.repository.UserRepository;
import reminder.domain.user.exception.UserNotFoundException;


@Service
@RequiredArgsConstructor
public class CreateCardService {

    private final CardRepository cardRepository;
    private final S3Facade s3Facade;
    private final UserRepository userRepository;

    @Qualifier("geminiRestTemplate")
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public Card createCard(CardCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        // 1. Call Gemini API to generate image
        String imageUrl = "";
        try {
            String prompt = "Generate an image related to: " + request.getTitle();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}], \"role\":\"user\"}], \"generationConfig\":{\"responseMimeType\":\"image/jpeg\"}}";

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey,
                    HttpMethod.POST,
                    entity,
                    byte[].class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                byte[] imageBytes = response.getBody();
                // Convert byte[] to MultipartFile and upload to S3
                MultipartFile multipartFile = new ByteArrayMultipartFile(imageBytes, "image.jpeg", "image/jpeg");
                imageUrl = s3Facade.uploadImage(multipartFile);
            } else {
                // Handle error or use a default image
                imageUrl = "default_image_url_if_gemini_fails";
            }
        } catch (Exception e) {
            imageUrl = "default_image_url_if_gemini_fails"; // Fallback
        }

        Card card = Card.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(imageUrl)
                .category(request.getCategory())
                .user(user)
                .build();

        return cardRepository.save(card);
    }

    // Helper class to convert byte[] to MultipartFile
    private static class ByteArrayMultipartFile implements MultipartFile {
        private final byte[] bytes;
        private final String name;
        private final String originalFilename;
        private final String contentType;

        public ByteArrayMultipartFile(byte[] bytes, String originalFilename, String contentType) {
            this.bytes = bytes;
            this.name = "file"; // Default name
            this.originalFilename = originalFilename;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return bytes == null || bytes.length == 0;
        }

        @Override
        public long getSize() {
            return bytes.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return bytes;
        }

        @Override
        public java.io.InputStream getInputStream() throws IOException {
            return new java.io.ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}
