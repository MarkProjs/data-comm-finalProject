/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import eu.hansolo.tilesfx.Tile;

import java.util.Base64;
import java.util.Date;
import java.util.Random;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import org.json.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.image.*;

/**
 *
 * @author Mark Agluba
 */
public class Threads {
    private final ProcessCodes process = new ProcessCodes();
    
    //Flag to monitor the threads
    private boolean running = true;
    private Random rand = new Random();
    private Date timeStamp;
    
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
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    System.err.println("DHT thread got interrupted. ");
                }
                if (count % 10 == 0) {
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
            }
        });    
        dhtThread.start();
    }
    
    public void startDoorBellThread(TextArea doorBellTile) {       
        Thread doorBellThread = new Thread(()-> {
            int count = 1;
            while(running) {
                try {
                    //Delay thread for 2 seconds
                    Thread.sleep(1000);
                    
                } catch(InterruptedException e) {
                    System.err.println("DoorBell thread got interrupted. ");
                }
                String text = "";
                if(count % 25 == 0) {
                    timeStamp = new Date();
                    text = "buzzer turned on at " + timeStamp.toString();            
                }
                else {
                    text = "buzzer is off";
                }
                doorBellTile.setText(text);
                count++;
            }
        
        });
        
        doorBellThread.start();
    
    }
    
    public void startSenseLEDThread(TextArea sensorTile, Tile imageTile) {
        Thread senseLEDThread = new Thread(()-> {
            while(running) {
                try{
                    //Delay thread for 2 seconds
                    Thread.sleep(1000);
            
                }catch(InterruptedException e) {
                    System.err.println("SenseLED thread got interrupted. ");
                }
                int randValue = rand.nextInt(100);
                String text = "";
                if(randValue % 5 == 0) {
                    timeStamp = new Date();
                    text = "led turned on at " + timeStamp.toString();
                    imageTile.setImage(new Image(this.getClass().getResourceAsStream("/defaultImage/sunny-clip-art.png")));
                }else {
                    text = "led is off";
                }
                sensorTile.setText(text);
            }
        });        
        senseLEDThread.start();
    }
    public void startPublishThread(MyMqtt mqtt, String topic, TextArea doorbellTxtA, TextArea sensorTxtA, Tile humidTile, Tile tempTile, Tile imageTile) {
        Thread publishThread = new Thread(()->{
            while(running) {
                try{
                    //Delay thread for 2 seconds
                    Thread.sleep(10000);
            
                }catch(InterruptedException e) {
                    System.err.println("SenseLED thread got interrupted. ");
                }

                try {
                    //get the image to convert to string
                    
                    File fi = new File("./FinalProject/src/main/resources/defaultImage/sunny-clip-art.png");
                    try (FileInputStream fileInputReader = new FileInputStream(fi)) {
											byte[] fileContent = new byte[(int)fi.length()];
											fileInputReader.read(fileContent);
											String imageString = Base64.getEncoder().encodeToString(fileContent);
											//do the json stringified
											String message = new JSONObject()
											            .put("doorbell", doorbellTxtA.getText())
											            .put("sensor", sensorTxtA.getText())
											            .put("humidity", humidTile.getValue())
											            .put("temperature", tempTile.getValue())
											            .put("image", imageString)
											            .toString();
											mqtt.publish(topic, message);
										} catch (JSONException e) {
											e.printStackTrace();
										}
                } catch(IOException e) {
                    System.out.println("The path to the image does not exist");
                }
                
                
            }
        });
        publishThread.start();

    }
    
    public void endThreads() {
        this.running = false;
    }
    
    
}
