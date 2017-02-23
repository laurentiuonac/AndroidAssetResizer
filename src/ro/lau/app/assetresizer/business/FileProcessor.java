package ro.lau.app.assetresizer.business;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * Created by lau on 15.02.2017.
 */
public class FileProcessor {
    private static final String EXTENSION_PNG = ".png";
    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_9_PNG = ".9.png";

    private static final String PATH_RES = "res";
    private static final String PATH_DRAWABLE_PREFIX = "drawable-";
    private static final String PATH_MIPMAP_PREFIX = "mipmap-";

    boolean isDrawable = false;

    private ImageProcessor imageProcessor;
    private Density originalDensity;

    public FileProcessor() {
        imageProcessor = new ImageProcessor();
    }

    public boolean validateFiles(List<File> fileList) {
        if (!areAllFilesSupported(fileList)) {
            showFilesNotSupportedDialog();
            return false;
        }

        if (!validatePath(fileList)) {
            showWrongResourceLocationDialog();
            return false;
        }

        return true;
    }

    public void processFiles(List<File> fileList) {
        Density[] densities = Density.values();

        for (File originalFile : fileList) {
            BufferedImage image = imageProcessor.readImageFromFile(originalFile);
            if (image == null) continue;

            for (Density density : densities) {
                // Avoid creating images for original densities or bigger ones
                if (density == originalDensity || density.getMultiplier() > originalDensity.getMultiplier()) continue;

                BufferedImage resizedImage = imageProcessor.resizeImage(image,
                        density.getMultiplier() / originalDensity.getMultiplier());
                writeImageForDensity(originalFile, resizedImage, density);
            }
        }
    }

    private void writeImageForDensity(File originalFile, BufferedImage image, Density density) {
        File drawableFolder = originalFile.getParentFile();
        File resFolder = drawableFolder.getParentFile();

        String resizedDrawableFolder = (isDrawable ? PATH_DRAWABLE_PREFIX : PATH_MIPMAP_PREFIX) + density.getName();
        File newFile = new File(resFolder.getAbsolutePath()
                + File.separatorChar + resizedDrawableFolder
                + File.separatorChar + originalFile.getName());

        if (newFile.getParentFile().exists() || newFile.getParentFile().mkdirs()) {
            imageProcessor.writeImageToFile(image, newFile);
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean validatePath(List<File> fileList) {
        if (fileList == null || fileList.size() == 0)
            return false;

        File file = fileList.get(0);
        File drawableFolder = file.getParentFile();
        if (drawableFolder == null)
            return false;

        File resFolder = drawableFolder.getParentFile();
        if (resFolder == null || !resFolder.getName().equals(PATH_RES))
            return false;

        for (Density density : Density.values()) {
            if (drawableFolder.getName().equals(PATH_DRAWABLE_PREFIX + density.getName())
                    || drawableFolder.getName().equals(PATH_MIPMAP_PREFIX + density.getName())) {
                isDrawable = drawableFolder.getName().contains(PATH_DRAWABLE_PREFIX);
                originalDensity = density;

                return true;
            }
        }

        return false;
    }

    private boolean areAllFilesSupported(List<File> fileList) {
        if (fileList == null)
            return false;

        for (File file : fileList) {
            if (!isFileSupported(file))
                return false;
        }

        return true;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean isFileSupported(File file) {
        if (file.getName().endsWith(EXTENSION_9_PNG))
            return false;

        if (file.getName().endsWith(EXTENSION_PNG))
            return true;

        if (file.getName().endsWith(EXTENSION_JPG))
            return true;

        return false;
    }

    private void showFilesNotSupportedDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Not supported");
        alert.setContentText("Currently only .png and .jpg files are supported. " +
                "Support for 9-patch files (.9.png) will come in a later release.");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    private void showWrongResourceLocationDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Wrong resource location");
        alert.setContentText("Copy the resources inside any of the drawable-x folders " +
                "and drag & drop from there. The density will be detected automatically and the " +
                "resized resources will be places in the according folders.");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}
