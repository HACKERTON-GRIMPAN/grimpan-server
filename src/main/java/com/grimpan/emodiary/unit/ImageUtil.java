package com.grimpan.emodiary.unit;

import com.grimpan.emodiary.exception.CommonException;
import com.grimpan.emodiary.exception.ErrorCode;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageUtil {
    public void decoder(String base64, String imagePath) {
        String data = base64.split(",")[0];

        byte[] imageBytes = DatatypeConverter.parseBase64Binary(data);

        try {
            BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imageBytes));

            ImageIO.write(bufImg, "jpg", new File(imagePath));
        } catch (IOException e) {
            throw new CommonException(ErrorCode.FILE_IO_ERROR, null);
        }
    }

    public String encoder(String imagePath) {
        String base64 = null;

        try {
            BufferedImage bufImg = ImageIO.read(new File(imagePath));

            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();

            ImageIO.write(bufImg, "jpg", os);

            base64 = DatatypeConverter.printBase64Binary(os.toByteArray());
        } catch (IOException e) {
            throw new CommonException(ErrorCode.FILE_IO_ERROR, null);
        }

        return base64;
    }
}
