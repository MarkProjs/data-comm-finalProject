/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import eu.hansolo.tilesfx.Tile;
import javafx.application.Platform;
import java.io.IOException;

/**
 *
 * @author Mark Agluba
 */
public class Threads {
    private ProcessCodes process = new ProcessCodes();
    
    //Flag to monitor the threads
    private static boolean running = true;
    
    public void startDHTThread(Tile humidTile, Tile tempTile) {
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
                        try {
                            process.runDht(humidTile, tempTile);
                        }
                        catch(IOException e) {
                            System.err.println("Some is wrong in the DHT Thread");
                        }
                    }
                });
            }
        
        });
        
        dhtThread.start();
    }
    
    public void startDoorBellThread(Tile doorBellTile) {
        Thread doorBellThread = new Thread(()-> {
            while(running) {
                try {
                    //Delay thread for 2 seconds
                    Thread.sleep(2000);
                    
                } catch(InterruptedException e) {
                    System.err.println("DoorBell thread got interrupted. ");
                }
                
                //update the active node
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            process.runDoorBell(doorBellTile);
                        }
                        catch(IOException e) {
                            System.err.println("Some is wrong in the Doorbell Thread");
                        }
                    }               
                });
            }
        
        });
        
        doorBellThread.start();
    
    }
    
    public void startSenseLEDThread(Tile sensorTile) {
        Thread senseLEDThread = new Thread(()-> {
            while(running) {
                try{
                    //Delay thread for 2 seconds
                    Thread.sleep(2000);
            
                }catch(InterruptedException e) {
                    System.err.println("SenseLED thread got interrupted. ");
                }
                
                //update the active node
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            process.runSensor(sensorTile);
                        }
                        catch(IOException e){
                            System.err.println("Some is wrong in the SenseLED Thread");
                        }
                    }
                });
            }
        });
        
        senseLEDThread.start();
    }
    
    public void endThreads() {
        this.running = false;
    }
    
    
}
