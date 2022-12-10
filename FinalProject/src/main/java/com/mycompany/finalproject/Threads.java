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
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.awt.image.*;

/**
 *
 * @author Mark Agluba
 */
public class Threads {
    private final ProcessCodes process = new ProcessCodes();

    // Flag to monitor the threads
    private boolean running = true;
    private Random rand = new Random();
    private Date timeStamp;
    private JSONObject data = new JSONObject();
    private MyMqtt mqtt;
    private String topic;
    private PiKeyStore keystore;

    public void setMqtt(MyMqtt mqtt) {
        this.mqtt = mqtt;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setKeyStore(PiKeyStore keystore) {
        this.keystore = keystore;
    }

    public void startDHTThread(Tile humidTile, Tile tempTile) {
        humidTile.setValue(0.0);
        tempTile.setValue(0.0);

        Thread dhtThread = new Thread(() -> {

            int count = 0;
            double humidity = 30.2;
            double temperature = 24.5;
            while (running) {
                try {
                    // Delay thread for 2 seconds
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    System.err.println("DHT thread got interrupted. ");
                }
                if (count % 5 == 0) {
                    int value = rand.nextInt(2);
                    if (value == 0) {
                        humidity += 0.2;
                        temperature += 0.1;
                    } else {
                        humidity -= 0.2;
                        temperature -= 0.1;
                    }
                    data.put("humidity", humidity)
                            .put("temperature", temperature);
                    try {
                        this.mqtt.publish(this.topic, addSignature(this.data.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("sent temp and hum");
                    humidTile.setValue(humidity);
                    tempTile.setValue(temperature);
                }
                count++;
            }
        });
        dhtThread.start();
    }

    public void startDoorBellThread(TextArea doorBellTile) {
        Thread doorBellThread = new Thread(() -> {
            int count = 1;
            while (running) {
                try {
                    // Delay thread for 3 seconds
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    System.err.println("DoorBell thread got interrupted. ");
                }
                String text = "";
                if (count % 10 == 0) {
                    timeStamp = new Date();
                    text = "buzzer turned on at " + timeStamp.toString();
                } else {
                    text = "buzzer is off";
                }
                if (data.has("doorbell")) {
                    // only send if data changed
                    if (!data.get("doorbell").equals(text)) {
                        data.put("doorbell", text);
                        try {
                            this.mqtt.publish(this.topic, addSignature(this.data.toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("sent doorbell");
                    }
                } else {
                    data.put("doorbell", text);
                    try {
                        this.mqtt.publish(this.topic, addSignature(this.data.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("sent doorbell");
                }
                doorBellTile.setText(text);
                count++;
            }

        });

        doorBellThread.start();

    }

    public void startSenseLEDThread(TextArea sensorTile, Tile imageTile) {
        Thread senseLEDThread = new Thread(() -> {
            while (running) {
                try {
                    // Delay thread for 5 seconds
                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    System.err.println("SenseLED thread got interrupted. ");
                }
                int randValue = rand.nextInt(100);
                String text = "";
                if (randValue % 5 == 0) {
                    timeStamp = new Date();
                    text = "led turned on at " + timeStamp.toString();
                    imageTile.setImage(
                            new Image(this.getClass().getResourceAsStream("/defaultImage/sunny-clip-art.png")));
                } else {
                    text = "led is off";
                }
                if (data.has("sensor")) {
                    // only send if data changed
                    if (!data.get("sensor").equals(text)) {
                        data.put("sensor", text);
                        try {
                            this.mqtt.publish(this.topic, addSignature(this.data.toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("sent sensor");
                    }
                } else {
                    data.put("sensor", text);
                    try {
                        this.mqtt.publish(this.topic, addSignature(this.data.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("sent sensor");
                }
                sensorTile.setText(text);
            }
        });
        senseLEDThread.start();
    }

    public void startPublishThread(TextArea doorbellTxtA, TextArea sensorTxtA, Tile humidTile, Tile tempTile,
            Tile imageTile) {
        try {
            // first publication, send public key
            String keyToSend = this.keystore.getPublicKeyAsString(this.keystore.getAliases().nextElement());
            String alias = this.keystore.getAliases().nextElement();
            var firstPub = new JSONObject();
            firstPub.put("alias", alias).put("key", keyToSend);
            this.mqtt.publishRetain(this.topic, firstPub.toString());
            System.out.println("key sent");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread publishThread = new Thread(() -> {
            while (running) {
                try {
                    // Delay thread for 10 seconds
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    System.err.println("publish thread got interrupted. ");
                }

                try {
                    // get the image to convert to string
                    File fi = new File(
                            "C:\\Users\\Jeremy\\OneDrive - Dawson College\\2022_fall_5\\data comm\\data-comm-final-project\\FinalProject\\src\\main\\resources\\defaultImage\\sunny-clip-art.png");
                    try (FileInputStream fileInputReader = new FileInputStream(fi)) {
                        byte[] fileContent = new byte[(int) fi.length()];
                        fileInputReader.read(fileContent);
                        String imageString = Base64.getEncoder().encodeToString(fileContent);
                        try {
                            String message = data
                                    .put("image", imageString)
                                    .toString();
                            message = addSignature(message);
                            this.mqtt.publish(this.topic, message);
                            System.out.println("sent image");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.out.println("The path to the image does not exist");
                }
            }
        });
        publishThread.start();

    }

    // adds signature and alias to message
    private String addSignature(String message) throws KeyStoreException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException, UnsupportedEncodingException, SignatureException {
        String alias = this.keystore.getAliases().nextElement();
        byte[] signature = this.keystore.generateSignature(message);
        String signatureString = Base64.getEncoder().encodeToString(signature);
        message += "|" + alias + "|" + signatureString;
        return message;
    }

    public void endThreads() {
        this.running = false;
    }

}
