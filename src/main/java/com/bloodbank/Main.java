package com.bloodbank;

import com.bloodbank.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

            SceneManager.initialize(primaryStage);
            primaryStage.setTitle("CRIMSON CONNECT");
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
           SceneManager.clearCache();
           SceneManager.switchScene("welcome");


            primaryStage.show();


    }
//username: admin
    //password: password

    public static void main(String[] args) {


            launch(args);
    }

}