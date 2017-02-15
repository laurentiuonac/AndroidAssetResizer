package ro.lau.app.assetresizer.ui;

import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.util.List;

/**
 * Created by lau on 15.02.2017.
 */
public class MainScene extends Scene {
    private FileProcessor fileProcessor;

    public static MainScene obtainScene() throws Exception {
        Parent root = FXMLLoader.load(MainScene.class.getResource("main.fxml"));
        return new MainScene(root, 300, 140);
    }

    private MainScene(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height) {
        super(root, width, height);
        setUpDragEvent();
    }

    public void setFileProcessor(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    private void setUpDragEvent() {
        this.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();

            if (dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.LINK);
            } else {
                event.consume();
            }
        });

        this.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean dropCompleted = false;

            if (dragboard.hasFiles()) {
                dropCompleted = fileProcessor.onFilesDropped(dragboard.getFiles());
            }

            event.setDropCompleted(dropCompleted);
            event.consume();
        });
    }

    public interface FileProcessor {
        boolean onFilesDropped(List<File> fileList);
    }
}
