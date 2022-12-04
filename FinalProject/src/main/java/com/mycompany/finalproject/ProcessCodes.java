/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import com.pi4j.Pi4J;
import eu.hansolo.tilesfx.Tile;
import java.io.IOException;
import java.util.Date;
import javafx.scene.image.Image;

public class ProcessCodes {
    //execute the python code for DHT11
    public void runDht(Tile humidTile, Tile tempTile) throws IOException {
        String dhtCode = "src/main/Python/DHT11.py";
        var processBuilder = new ProcessBuilderEx(dhtCode);
        
        String output = processBuilder.startProcess();
        String[] splitOutput = output.split(",");
        
        humidTile.setValue(Double.parseDouble(splitOutput[0]));
        tempTile.setValue(Double.parseDouble(splitOutput[1]));
    }
    
    //execute the python code for Doorbell
    public void runDoorBell(Tile doorBellTile) throws IOException{
        String doorBellCode = "src/main/Python/Doorbell.py";
        var processBuilder = new ProcessBuilderEx(doorBellCode);
        
        String output = processBuilder.startProcess();
        
        String tileText = "";
        
        if(output.equals("buzzer turned on")) {
           var timeStamp2 = new Date();
           tileText = "buzzer turned on at " + timeStamp2.toString();
        }
        else if (output.equals("buzzer turned off")){
            tileText = "buzzer is off";
            
        }
        doorBellTile.setText(tileText);
        
    }
    
    //execute the python code for the Sensor
    public void runSensor(Tile senseTile, Tile imageTile) throws IOException {
        String sensorCode = "src/main/Python/SenseLED.py";
        var processBuilder = new ProcessBuilderEx(sensorCode);
        
        String output = processBuilder.startProcess();
        
        String tileText = "";
        
        if(output.equals("led turned on")) {
           var timeStamp2 = new Date();
           tileText = "led turned on at " + timeStamp2.toString();
            //Initialize the Pi4J Runtime Context
            var pi4j = Pi4J.newAutoContext();

            CameraApp runApp = new CameraApp();
            runApp.execute(pi4j);
        
            // Shutdown Pi4J
            pi4j.shutdown();
            
            imageTile.setImage(new Image(this.getClass().getResourceAsStream("/images/sunny-clip-art.png")));
        }
        else if (output.equals("led turned off")){
            tileText = "led is off";
            
        }
        senseTile.setText(tileText);
    }
}
