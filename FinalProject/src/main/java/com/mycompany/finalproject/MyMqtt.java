/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

/**
 *
 * @author Mark Agluba
 */
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import eu.hansolo.tilesfx.Tile;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class MyMqtt {

    private String userName;
    private String passWord;
    private String messageText;
    final String host = "d851ff43cb294d18a69a6d253457ddce.s1.eu.hivemq.cloud";
    private PiKeyStore keystore;

    // create an MQTT client
    final Mqtt5BlockingClient client = MqttClient.builder()
            .useMqttVersion5()
            .serverHost(this.host)
            .serverPort(8883)
            .sslWithDefaultConfig()
            .buildBlocking();

    public MyMqtt(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public void setKeyStore(PiKeyStore keystore) {
        this.keystore = keystore;
    }

    public Mqtt5BlockingClient getClient() {
        return client;
    }

    public static MyMqtt connectMqtt() {
        Scanner reader = new Scanner(System.in);
        boolean isTrue = true;
        String userName = "";
        String passWord = "";
        while (isTrue) {
            System.out.println("Enter your mqtt username: ");
            userName = reader.nextLine();
            System.out.println("Enter your mqtt password: ");
            passWord = reader.nextLine();
            try {
                if (userName.length() > 30) {
                    throw new IllegalArgumentException();
                }
                if (passWord.length() < 8 || passWord.length() > 30) {
                    throw new IllegalArgumentException();
                }

                isTrue = false;
            } catch (Exception e) {
                System.out.println("There was an error when trying log in. Try again");
                isTrue = true;
            }
        }

        MyMqtt mqtt = new MyMqtt(userName, passWord);
        return mqtt;
    }

    public void connectClient() {
        // connect to HiveMQ Cloud with TLS and username/pw
        client.connectWith()
                .simpleAuth()
                .username(this.userName)
                .password(UTF_8.encode(this.passWord))
                .applySimpleAuth()
                .send();

        System.out.println("Connected successfully");

    }

    public void subscribe(String topic) {
        // subscribe to the topic "my/test/topic"
        client.subscribeWith()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .send();
    }

    public void publish(String topic, String message) {
        // publish a message to the topic "my/test/topic"
        client.publishWith()
                .topic(topic)
                .payload(UTF_8.encode(message))
                .qos(MqttQos.EXACTLY_ONCE)
                .send();
    }

    public void publishRetain(String topic, String message) {
        // publish a message to the topic "my/test/topic"
        client.publishWith()
                .topic(topic)
                .payload(UTF_8.encode(message))
                .retain(true)
                .qos(MqttQos.EXACTLY_ONCE)
                .send();
    }

    public void getData(String topic, TextArea doorbellTxtA, TextArea sensorTxtA, Tile humidTile, Tile tempTile,
            Tile imageTile) {
        // set a callback that is called when a message is received (using the async API
        // style)
        client.toAsync().publishes(ALL, publish -> {
            if (topic.equals(publish.getTopic().toString())) {
                messageText = UTF_8.decode(publish.getPayload().get()).toString();
                try {
                    if (messageText.lastIndexOf('|') == -1) {
                        JSONObject json = new JSONObject(messageText);
                        if (json.has("key")) {
                            var key = json.getString("key");
                            var alias = json.getString("alias");
                            if (!this.keystore.getKeyStore().containsAlias(alias)) {
                                this.keystore.savePublicKey(alias, key);
                                System.out.println("saved key");
                                String keyToSend = keystore.getPublicKeyAsString(keystore.getAliases().nextElement());
                                String ownAlias = keystore.getAliases().nextElement();
                                var firstPub = new JSONObject();
                                firstPub.put("alias", ownAlias).put("key", keyToSend);
                                this.publish(topic, firstPub.toString());
                                System.out.println("sent back own key");
                            } else {
                                System.out.println("already have this key");
                            }
                        }
                    } else {
                        System.out.println("data obj and not key");
                        String message = messageText.substring(0, messageText.indexOf('|'));
                        String alias = messageText.substring(messageText.indexOf('|') + 1,
                                messageText.lastIndexOf('|'));
                        String signature = messageText.substring(messageText.lastIndexOf('|') + 1,
                                messageText.length());
                        byte[] sig = Base64.getDecoder().decode(signature);
                        if (this.keystore.verifySignature(sig, alias, message)) {
                            JSONObject json = new JSONObject(message);
                            // get the image
                            if (json.has("image") && !json.isNull("image")) {
                                byte[] decodeArray = Base64.getDecoder().decode(json.getString("image"));
                                ByteArrayInputStream bis = new ByteArrayInputStream(decodeArray);
                                BufferedImage bImage = ImageIO.read(bis);
                                ImageIO.write(bImage, "png",
                                        new File(
                                                "C:\\Users\\Jeremy\\OneDrive - Dawson College\\2022_fall_5\\data comm\\data-comm-final-project\\FinalProject\\src\\main\\resources\\defaultImage\\newImage.png"));
                            }
                            // getting the values
                            if (json.has("doorbell")) {
                                doorbellTxtA.setText(json.getString("doorbell"));
                            }
                            if (json.has("sensor")) {
                                sensorTxtA.setText(json.getString("sensor"));
                            }
                            if (json.has("humidity")) {
                                humidTile.setValue(json.getDouble("humidity"));
                            }
                            if (json.has("temperature")) {
                                tempTile.setValue(json.getDouble("temperature"));
                            }
                            if (json.has("image") && !json.isNull("image")) {
                                imageTile.setImage(
                                        new Image(getClass().getResourceAsStream("/defaultImage/newImage.png")));
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Something is wrong in the ImageIO.read method");
                } catch (Exception e) {
                    System.out.println("Something else other than ImageIO is wrong");
                    e.printStackTrace();
                }
            }
        });
    }

    public String getMessageText() {
        return messageText;
    }

    public void disconnect() {
        // disconnect the client after a message was received
        client.disconnect();
    }

}
