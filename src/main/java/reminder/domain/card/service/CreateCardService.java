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
import reminder.domain.user.facade.UserFacade;


import reminder.domain.museum.domain.repository.MuseumRepository;
import reminder.domain.museum.domain.Museum;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateCardService {

    private final CardRepository cardRepository;
    private final S3Facade s3Facade;
    private final UserFacade userFacade;
    private final CardOverallService cardOverallService;
    private final MuseumRepository museumRepository;

    private final RestTemplate restTemplate;

    @Value("${google.search.api-key}")
    private String googleSearchApiKey;

    @Value("${google.search.cx}")
    private String googleSearchCx;

    

    public Card createCard(CardCreateRequest request) {
        User user = userFacade.getCurrentUser();
        Museum museum = museumRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Museum not found for current user."));

        // 1. Use Google Custom Search API to find an image
        String imageUrl = "";
        try {
            String searchQuery = request.getTitle() + " image";
            String googleSearchUrl = String.format("https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&q=%s&searchType=image&num=1",
                    googleSearchApiKey, googleSearchCx, searchQuery);

            log.info("Calling Google Custom Search API: {}", googleSearchUrl);
            ResponseEntity<String> searchResponse = restTemplate.getForEntity(googleSearchUrl, String.class);

            if (searchResponse.getStatusCode().is2xxSuccessful() && searchResponse.getBody() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(searchResponse.getBody());
                JsonNode items = root.path("items");

                if (items.isArray() && items.size() > 0) {
                    String foundImageUrl = items.get(0).path("link").asText();
                    log.info("Found image URL from Google Search: {}", foundImageUrl);

                    // Download the image
                    ResponseEntity<byte[]> imageDownloadResponse = restTemplate.getForEntity(foundImageUrl, byte[].class);

                    if (imageDownloadResponse.getStatusCode().is2xxSuccessful() && imageDownloadResponse.getBody() != null) {
                        byte[] imageBytes = imageDownloadResponse.getBody();
                        log.info("Image downloaded successfully. Size: {} bytes", imageBytes.length);
                        MultipartFile multipartFile = new ByteArrayMultipartFile(imageBytes, "image.jpeg", "image/jpeg");
                        imageUrl = s3Facade.uploadImage(multipartFile);
                        log.info("Image uploaded to S3. URL: {}", imageUrl);
                    } else {
                        log.warn("Failed to download image from {}. Status: {}", foundImageUrl, imageDownloadResponse.getStatusCode());
                        imageUrl = "default_image_url_if_download_fails";
                    }
                } else {
                    log.warn("No image found for query: {}", searchQuery);
                    imageUrl = "default_image_url_if_no_image_found";
                }
            } else {
                log.warn("Google Custom Search API call failed. Status: {}, Body is null: {}", searchResponse.getStatusCode(), searchResponse.getBody() == null);
                imageUrl = "default_image_url_if_search_fails";
            }
        } catch (Exception e) {
            log.error("Error during Google Custom Search API call, image download, or S3 upload: {}", e.getMessage(), e);
            imageUrl = "default_image_url_if_exception"; // Fallback
        }

        Card card = Card.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(imageUrl)
                .category(request.getCategory())
                .user(user)
                .museum(museum)
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
