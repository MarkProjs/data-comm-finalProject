/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import eu.hansolo.tilesfx.Tile;
import java.io.IOException;

public class ProcessCodes {
    public void runDht(Tile humidTile, Tile tempTile) throws IOException {
        String dhtCode = "src/main/Python/DHT11.py";
        var processBuilder = new ProcessBuilderEx(dhtCode);
        
        String output = processBuilder.startProcess();
        String[] splitOutput = output.split(",");
        
        humidTile.setValue(Double.parseDouble(splitOutput[0]));
        tempTile.setValue(Double.parseDouble(splitOutput[1]));
    }
    
    public void runDoorBell(Tile doorBellTile) {
    
    }
}
