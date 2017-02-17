package ro.lau.app.assetresizer.business;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by lau on 15.02.2017.
 */
public class ImageProcessor {
    private static final float RATIO_UNCHANGED = 1f;

    public BufferedImage readImageFromFile(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage resizeImage(BufferedImage img, float ratio) {
        if (ratio == RATIO_UNCHANGED) return img;

        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();

        int newWidth = Math.round(originalWidth * ratio);
        int newHeight = Math.round(originalHeight * ratio);

        int destinationImageType = img.getType() != 0 ? img.getType() : BufferedImage.TYPE_4BYTE_ABGR;

        BufferedImage destinationImage = new BufferedImage(newWidth, newHeight, destinationImageType);
        Graphics2D graphics = destinationImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(img, 0, 0, newWidth, newHeight, 0, 0, originalWidth, originalHeight, null);
        graphics.dispose();

        return destinationImage;
    }

    public boolean writeImageToFile(BufferedImage image, File file) {
        try {
            if (file.exists()) {
                if (!file.delete()) {
                    throw new IOException("Cannot delete file: " + file);
                }
            }

            ImageIO.write(image, "png", file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
