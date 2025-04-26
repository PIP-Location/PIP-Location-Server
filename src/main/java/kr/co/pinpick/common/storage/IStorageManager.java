package kr.co.pinpick.common.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IStorageManager {
    String upload(MultipartFile multipartFile, String dirName) throws IOException;
    void delete(String filePath);
}
