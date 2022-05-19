package kr.polymarket.domain.file.service;

import kr.polymarket.domain.product.entity.ProductFile;
import kr.polymarket.domain.product.repository.ProductFileRepository;
import kr.polymarket.domain.user.entity.UserFile;
import kr.polymarket.domain.user.repository.UserFileRepository;
import kr.polymarket.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final S3Uploader s3Uploader;
    private final UserFileRepository userFileRepository;
    private final ProductFileRepository productFileRepository;

    /**
     * input
     *
     * @param multipartFile
     * @return save 결과를 저장내용 fileId
     */
    @Transactional
    public Long uploadUserProfileImage(MultipartFile multipartFile) {
        return userFileRepository.save(
                UserFile.builder()
                        .fileUrl(s3Uploader.uploadImage(multipartFile))
                        .createDate(LocalDateTime.now())
                        .isDelete(false)
                        .build()
        ).getFileId();
    }

    /**
     * @param multipartFile
     * @return fileUrlList
     */
    @Transactional
    public List<String> uploadProductImage(List<MultipartFile> multipartFile) {
        List<String> fileUrlList = s3Uploader.uploadImage(multipartFile);

        fileUrlList.forEach(fileUrl -> {
            productFileRepository.save(
                    ProductFile.builder()
                            .fileUrl(fileUrl)
                            .createDate(LocalDateTime.now())
                            .isDelete(false)
                            .build()
            );
        });
        return fileUrlList;
    }
}




