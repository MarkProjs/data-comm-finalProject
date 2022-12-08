/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import eu.hansolo.tilesfx.Tile;
import javafx.application.Platform;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Mark Agluba
 */
public class Threads {
    private final ProcessCodes process = new ProcessCodes();
    
    //Flag to monitor the threads
    private static boolean running = true;
    
    public void startDHTThread(Tile humidTile, Tile tempTile) {
        humidTile.setValue(0.0);
        tempTile.setValue(0.0);
        
        Thread dhtThread = new Thread(()-> {
            
            int count = 0;
            double humidity = 30.2;
            double temperature = 24.5;
            while (running) {
               
                try {
                    //Delay thread for 2 seconds
                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    System.err.println("DHT thread got interrupted. ");
                }
                if (count % 30 == 0) {
                    Random rand = new Random();
                    int value = rand.nextInt(2);
                    if (value == 0) {
                        humidity += 0.2;
                        temperature += 0.1;
                    }
                    else {
                        humidity -= 0.2;
                        temperature -= 0.1;
                    }
                    humidTile.setValue(humidity);
                    tempTile.setValue(temperature);
                }    
                count++;
//                //update the active node
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            
//                            process.runDht(humidTile, tempTile);
//                        } catch (IOException e) {
//                            System.err.println("Some is wrong in the DHT Thread");
//                        }
//                    }
//                });
            }
        });    
        dhtThread.start();
    }
    
    public void startDoorBellThread(Tile doorBellTile) {
        Thread doorBellThread = new Thread(()-> {
            while(running) {
                try {
                    //Delay thread for 2 seconds
                    Thread.sleep(1000);
                    
                } catch(InterruptedException e) {
                    System.err.println("DoorBell thread got interrupted. ");
                }
                
//                //update the active node
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            process.runDoorBell(doorBellTile);
//                            
//                        }
//                        catch(IOException e) {
//                            System.err.println("Some is wrong in the Doorbell Thread");
//                        }
//                    }            
//                });
            }
        
        });
        
        doorBellThread.start();
    
    }
    
    public void startSenseLEDThread(Tile sensorTile, Tile imageTile) {
        Thread senseLEDThread = new Thread(()-> {
            while(running) {
                try{
                    //Delay thread for 2 seconds
                    Thread.sleep(1000);
            
                }catch(InterruptedException e) {
                    System.err.println("SenseLED thread got interrupted. ");
                }
                
                //update the active node
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            process.runSensor(sensorTile, imageTile);
                          
                        }
                        catch(IOException e){
                            System.out.println("Something is wrong in the SenseLED Thread. There is an IOException");
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
