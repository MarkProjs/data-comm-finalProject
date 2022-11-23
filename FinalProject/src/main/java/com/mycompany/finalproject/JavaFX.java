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
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class JavaFX extends HBox {
    //setting the tiles
    private MyMqtt mqtt;
    private Threads threads;
    private Tile markHumidTile;
    private Tile markTempTile;
    private Tile markDoorBellTile;
    private Tile markSensorTile;
    private Tile markImageTile;
    private Tile antHumidTile;
    private Tile antTempTile;
    private Tile antDoorBellTile;
    private Tile antSensorTile;
    private Tile antImageTile;
    private Tile jerHumidTile;
    private Tile jerTempTile;
    private Tile jerDoorBellTile;
    private Tile jerSensorTile;
    private Tile jerImageTile;

    
    public JavaFX() throws IOException {
        threads = new Threads();
        
        //Build the screen
        System.out.println("Before buildscreen");
        this.buildScreen();
        System.out.println("After buildscreen");
//        this.threads.startDHTThread(markHumidTile, markTempTile);
//        this.threads.startDoorBellThread(doorBellTile);
//        this.threads.startSenseLEDThread(sensorTile);
        System.out.println(" thread");
    }
    
    private void buildScreen() throws IOException {
        //create the doorBell Tile
        //Generate a timestamp
        var timeStamp2 = new Date();
        markDoorBellTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Output from external program to Text Tile")
                        .description("Output from external program at" + "\n" + timeStamp2)
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        //create sensor tile
        markSensorTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Output from external program to Text Tile")
                        .description("Output from external program at" + "\n" + timeStamp2)
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        
        
        //create tempTile
        markTempTile = TileBuilder.create()
                              .skinType(Tile.SkinType.GAUGE)
                              .prefSize(350, 300)
                              .title("Temperature Tile")
                              .unit("C")
                              .threshold(75)
                              .build();
     
        
        //Create humidTile
        markHumidTile = TileBuilder.create()
                               .skinType(Tile.SkinType.PERCENTAGE)
                               .prefSize(350, 300)
                               .title("Humidity Tile")
                               .unit("g.m-3")
                               .maxValue(60)
                               .build();
        
        //setup the image tile
        markImageTile = TileBuilder.create()
                            .skinType(Tile.SkinType.IMAGE)
                            .prefSize(150, 150)
                            .title("Mark's Image Tile")
                            .image(new Image(this.getClass().getResourceAsStream("/images/sunny-clip-art.png")))
                            .imageMask(Tile.ImageMask.RECTANGULAR)
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
       
       
       var column1 = new VBox(markHumidTile, markTempTile, markSensorTile, markDoorBellTile, markImageTile);
       var column2 = new VBox(antHumidTile, antTempTile, antSensorTile, antDoorBellTile, antImageTile);
       var column3 = new VBox(jerHumidTile, jerTempTile, jerSensorTile, jerDoorBellTile, jerImageTile);
       var elems = new HBox(column1, column2, column3);
  
       //adding to the main screen
       this.getChildren().add(elems);
       
       this.setSpacing(5);
       
    }
    
    private void endApplication() {
        this.threads.endThreads();
        Platform.exit();
         
    }
}
