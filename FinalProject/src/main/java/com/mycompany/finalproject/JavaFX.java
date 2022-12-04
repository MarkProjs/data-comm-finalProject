/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import java.io.IOException;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class JavaFX extends HBox {
    //setting the tiles
    private MyMqtt mqtt;
    private Threads threads;
    //mark's tiles
    private Tile markHumidTile;
    private Tile markTempTile;
    private Tile markDoorBellTile;
    private Tile markSensorTile;
    private Tile markImageTile;

    //Anotnio's tiles
    private Tile antHumidTile;
    private Tile antTempTile;
    private Tile antDoorBellTile;
    private Tile antSensorTile;
    private Tile antImageTile;

    //Jeremy's tiles
    private Tile jerHumidTile;
    private Tile jerTempTile;
    private Tile jerDoorBellTile;
    private Tile jerSensorTile;
    private Tile jerImageTile;

    
    public JavaFX() throws IOException {
        threads = new Threads();
        
        //Build the screen
     
        this.buildScreen();

        this.threads.startDHTThread(markHumidTile, markTempTile);
        this.threads.startDoorBellThread(markDoorBellTile);
        this.threads.startSenseLEDThread(markSensorTile, markImageTile);
 
    }
    
    private void buildScreen() throws IOException {
        //create the doorBell Tile
        //Generate a timestamp
        markDoorBellTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Mark's doorbell tile")
                        .description("Output from external program")
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        antDoorBellTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Antonio's doorbell tile")
                        .description("Output from external program")
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        jerDoorBellTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Jeremy's doorbell tile")
                        .description("Output from external program")
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        //create sensor tile
        markSensorTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Mark's sensor tile")
                        .description("Output from external program")
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        antSensorTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Antonio's sensor tile")
                        .description("Output from external program")
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        jerSensorTile = TileBuilder.create()
                        .skinType(Tile.SkinType.TEXT)
                        .prefSize(350, 300)
                        .textSize(Tile.TextSize.BIGGER)
                        .title("Jeremy's Sensor tile")
                        .description("Output from external program")
                        .descriptionAlignment(Pos.CENTER_LEFT)
                        .textVisible(true)
                        .build();
        
        
        
        //create tempTile
        markTempTile = TileBuilder.create()
                              .skinType(Tile.SkinType.GAUGE)
                              .prefSize(350, 300)
                              .title("Mark's Temp Tile")
                              .unit("C")
                              .threshold(75)
                              .build();
        
        antTempTile = TileBuilder.create()
                              .skinType(Tile.SkinType.GAUGE)
                              .prefSize(350, 300)
                              .title("Antonio's Temp Tile")
                              .unit("C")
                              .threshold(75)
                              .build();
        
        jerTempTile = TileBuilder.create()
                              .skinType(Tile.SkinType.GAUGE)
                              .prefSize(350, 300)
                              .title("Jeremy's Temp Tile")
                              .unit("C")
                              .threshold(75)
                              .build();
     
        
        //Create humidTile
        markHumidTile = TileBuilder.create()
                               .skinType(Tile.SkinType.PERCENTAGE)
                               .prefSize(350, 300)
                               .title("Mark's Humid Tile")
                               .unit("g.m-3")
                               .maxValue(60)
                               .build();
        
        antHumidTile = TileBuilder.create()
                               .skinType(Tile.SkinType.PERCENTAGE)
                               .prefSize(350, 300)
                               .title("Antonio's Humid Tile")
                               .unit("g.m-3")
                               .maxValue(60)
                               .build();
        
        jerHumidTile = TileBuilder.create()
                               .skinType(Tile.SkinType.PERCENTAGE)
                               .prefSize(350, 300)
                               .title("Jeremy's Humid Tile")
                               .unit("g.m-3")
                               .maxValue(60)
                               .build();
        
        //setup the image tile
        markImageTile = TileBuilder.create()
                            .skinType(Tile.SkinType.IMAGE)
                            .prefSize(350, 300)
                            .title("Mark's Image Tile")
                            .imageMask(Tile.ImageMask.RECTANGULAR)
                            .build();
        
        antImageTile = TileBuilder.create()
                            .skinType(Tile.SkinType.IMAGE)
                            .prefSize(350, 300)
                            .title("Antonio's Image Tile")
                            .image(new Image(this.getClass().getResourceAsStream("/images/sunny-clip-art.png")))
                            .imageMask(Tile.ImageMask.RECTANGULAR)
                            .build();
        
        jerImageTile = TileBuilder.create()
                            .skinType(Tile.SkinType.IMAGE)
                            .prefSize(350, 300)
                            .title("Jeremy's Image Tile")
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
       
       
       var column1 = new HBox(markHumidTile, markTempTile, markSensorTile, markDoorBellTile, markImageTile);
       var column2 = new HBox(antHumidTile, antTempTile, antSensorTile, antDoorBellTile, antImageTile);
       var column3 = new HBox(jerHumidTile, jerTempTile, jerSensorTile, jerDoorBellTile, jerImageTile);
       var elems = new VBox(column1, column2, column3);
  
       //adding to the main screen
       this.getChildren().add(elems);
       
       this.setSpacing(5);
       
    }
    
    private void endApplication() {
        this.threads.endThreads();
        Platform.exit();
         
    }
}
