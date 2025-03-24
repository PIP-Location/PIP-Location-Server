package kr.co.pinpick.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class MockMultipartFileFixture {
    public static FileInputStream mockImageFile() throws IOException {
        File tempFile = File.createTempFile("test", ".jpg");
        tempFile.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write("test file content".getBytes());
        }

        return new FileInputStream(tempFile);
    }

    public static InputStream mockImageFile(int width, int height, String imageFormat) throws IOException {
        var buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        var stream = new ByteArrayOutputStream();
        ImageIO.write(buffer, imageFormat, stream);
        return new ByteArrayInputStream(stream.toByteArray());
    }
}
