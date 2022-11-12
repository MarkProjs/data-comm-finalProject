/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import java.io.IOException;
import javafx.application.Platform;

/**
 *
 * @author Mark Agluba
 */
public class Threads {
    private static boolean running = true;
    
    public void startDHTThread() {
        Thread dhtThread = new Thread(()-> {
            while (running) {
                try {
                    //Delay thread for 2 seconds
                    Thread.sleep(2000);
                    
                } catch(InterruptedException e) {
                    System.err.println("DHT thread got interrupted. ");
                }
                
                //update the active node
                Platform.runLater(new Runnable() {
                @Override
                public void run() {
                
                }
                });
            
            }
        
        });
        
        dhtThread.start();
    }
    
    public void startDoorBellThread() {
        Thread doorBellThread = new Thread(()-> {
            
        
        });
        
        doorBellThread.start();
    
    }
    
    public void startSenseLEDThread() {
        Thread senseLEDThread = new Thread(()-> {
        
        
        });
        
        senseLEDThread.start();
    }
    
    public void endThreads() {
        this.running = false;
    }
    
    
}
