package kr.co.pinpick.common.extension;

import kr.co.pinpick.common.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;

import static kr.co.pinpick.common.error.ErrorCode.INVALID_TYPE_VALUE;

@Slf4j
public class FileExtension {
    public static Pair<Integer, Integer> getImageSize(MultipartFile file) throws IOException {
        var bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            throw new BusinessException(
                    INVALID_TYPE_VALUE,
                    String.format(
                            "지원하지 않는 파일 형식 입니다. name:%s type:%s",
                            file.getOriginalFilename(),
                            file.getContentType()
                    ));
        }

        return Pair.of(
                bufferedImage.getWidth(),
                bufferedImage.getHeight()
        );
    }
}
