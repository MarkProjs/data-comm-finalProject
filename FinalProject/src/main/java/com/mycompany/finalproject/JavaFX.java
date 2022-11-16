/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class JavaFX extends HBox {
    //setting the tiles
    private MyMqtt mqtt;
    private Threads threads;
    private Tile humidTile;
    private Tile tempTile;
    private Tile doorBellTile;
    private Tile sensorTile;
    
    public JavaFX() throws IOException {
        threads = new Threads();
        
        //Build the screen
        System.out.println("Before buildscreen");
        this.buildScreen();
        System.out.println("After buildscreen");
        this.threads.startDHTThread(humidTile, tempTile);
//        this.threads.startDoorBellThread(doorBellTile);
//        this.threads.startSenseLEDThread(sensorTile);
        System.out.println(" thread");
    }
    
    private void buildScreen() throws IOException {
        //create the doorBell Tile
        //Generate a timestamp
        var timeStamp2 = new Date();
        doorBellTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Output from external program to Text Tile")
                        .description("Output from external program at" + "\n" + timeStamp2)
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        //create sensor tile
        sensorTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Output from external program to Text Tile")
                        .description("Output from external program at" + "\n" + timeStamp2)
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        
        
        //create tempTile
        tempTile = TileBuilder.create()
                              .skinType(Tile.SkinType.GAUGE)
                              .prefSize(350, 300)
                              .title("Temperature Tile")
                              .unit("C")
                              .threshold(75)
                              .build();
        
        
        //Create humidTile
        humidTile = TileBuilder.create()
                               .skinType(Tile.SkinType.PERCENTAGE)
                               .prefSize(350, 300)
                               .title("Humidity Tile")
                               .unit("g.m-3")
                               .maxValue(60)
                               .build();
        
        //setup the update button
        var updateButton = new Button("Update");
        updateButton.setOnAction(e-> {
            var updateDataObj = new UpdateData();
            try {
                updateDataObj.updateOutput();
            } catch(IOException ex) {
                Logger.getLogger(JavaFX.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Generate a timestamp
        var timeStamp = new Date();
        //tile for the update button
        var updateTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(350, 300)
                .title("Update the output")
                .textSize(Tile.TextSize.BIGGER)
                .text("Last update date and time: " + timeStamp)
                .textColor(Color.MIDNIGHTBLUE)
                .backgroundColor(Color.LIGHTBLUE)
                .titleColor(Color.BLUE)
                .graphic(updateButton)
                .roundedCorners(true)
                .build();
        
        
       //Setup the Exit button
       var exitButton = new Button("Exit");
       //event handler for the exit button
       exitButton.setOnAction(e -> endApplication());
       //Setup the tile
       var exitTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(350, 300)
                .textSize(Tile.TextSize.BIGGER)
                .title("Quit the application")
                .graphic(exitButton)
                .roundedCorners(false)
                .build();
       
       
       var column1 = new VBox(humidTile, tempTile, sensorTile, doorBellTile);
       var column2 = new VBox(humidTile, tempTile, sensorTile, doorBellTile);
       var column3 = new VBox(humidTile, tempTile, sensorTile, doorBellTile);
       var elems = new HBox(column1, column2, column3);
       var footer = new HBox(updateTile, exitTile);
        VBox screen = new VBox(elems, footer);
       //adding to the main screen
       this.getChildren().add(screen);
       
       this.setSpacing(5);
       
    }
    
    private void endApplication() {
        this.threads.endThreads();
        Platform.exit();
         
    }
}
