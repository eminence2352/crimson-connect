package com.bloodbank.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SceneManager {





    private static Stage primaryStage;

    private static final Map<String, Scene> sceneCache = new HashMap<>();

    private static final String FXML_PATH = "/fxml/";


    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String sceneName) {
        try {
            Scene scene = sceneCache.get(sceneName);
            if (scene == null) {
                scene = loadScene(sceneName);
                sceneCache.put(sceneName, scene);
            }

            primaryStage.setScene(scene);

        } catch (IOException e) {
            System.err.println("Error switching to scene: " + sceneName);

        }
    }


    private static Scene loadScene(String sceneName) throws IOException {

        String fxmlFile = FXML_PATH + sceneName + ".fxml";
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));

        Parent root = loader.load();

        Scene scene = new Scene(root);


        String cssFile = SceneManager.class.getResource("/styles/style.css").toExternalForm();
        scene.getStylesheets().add(cssFile);

        return scene;
    }


    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void clearCache() {
        sceneCache.clear();
    }
}