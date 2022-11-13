/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.finalproject;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinalProject extends Application{
    
    public static Stage theStage;
    
    @Override
    public void start(Stage stage)throws IOException {
        var scene = new Scene(new JavaFX(), 1060, 1500);
        FinalProject.theStage = stage;
        
        //set the active scene
        theStage.setScene(scene);
        theStage.show();
        
        //make sure that the application quits completely on close
        theStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
