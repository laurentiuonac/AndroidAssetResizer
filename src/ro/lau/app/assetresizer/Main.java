package ro.lau.app.assetresizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.lau.app.assetresizer.business.FileProcessor;
import ro.lau.app.assetresizer.ui.MainScene;

import java.io.File;
import java.util.List;

public class Main extends Application implements MainScene.FileProcessor {
    private FileProcessor fileProcessor;

    public static void main(String[] args) {
        launch(args);
    }

    public Main() {
        fileProcessor = new FileProcessor();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Android Asset Resizer");
        primaryStage.setScene(createScene());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Scene createScene() throws Exception {
        MainScene scene = MainScene.obtainScene();
        scene.setFileProcessor(this);

        return scene;
    }

    @Override
    public boolean onFilesDropped(List<File> fileList) {
        if (!fileProcessor.validateFiles(fileList)) {
            return false;
        }

        fileProcessor.processFiles(fileList);

        return true;
    }
}
