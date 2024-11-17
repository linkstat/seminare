package ar.com.hmu.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public class ImageUtils {

    /**
     * Convierte un arreglo de bytes en un objeto Image de JavaFX.
     *
     * @param imageBytes El arreglo de bytes que representa la imagen.
     * @return Un objeto Image, o null si el arreglo es nulo o está vacío.
     */
    public static Image byteArrayToImage(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
                return new Image(bis);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Convierte un objeto Image de JavaFX en un arreglo de bytes.
     *
     * @param image La imagen a convertir.
     * @return Un arreglo de bytes que representa la imagen, o null si la imagen es nula.
     */
    public static byte[] imageToByteArray(Image image) {
        if (image != null) {
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(bImage, "png", baos);
                baos.flush();
                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
