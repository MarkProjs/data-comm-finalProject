/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import java.io.IOException;

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
    public String runDoorBell() throws IOException{
        String doorBellCode = "src/main/Python/Doorbell.py";
        var processBuilder = new ProcessBuilderEx(doorBellCode);
        
        String output = processBuilder.startProcess();
        
        return output;
        
    }
    
    //execute the python code for the Sensor
    public String runSensor() throws IOException {
        String sensorCode = "src/main/Python/SenseLED.py";
        var processBuilder = new ProcessBuilderEx(sensorCode);
        
        String output = processBuilder.startProcess();
        
        return output;
    }
}
