package reminder.domain.museum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reminder.domain.museum.domain.Museum;
import reminder.domain.museum.domain.repository.MuseumRepository;
import reminder.infra.S3Facade;

@Service
@RequiredArgsConstructor
public class BannerUploadService {
    private final S3Facade s3Facade;
    private final MuseumRepository museumRepository;

    @Transactional
    public String execute(MultipartFile file, Long id) {
        Museum museum = museumRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("adf"));
        String bannerImageUrl = s3Facade.uploadImage(file);
        museum.updateBanner(bannerImageUrl);
        return bannerImageUrl;

    }
}
