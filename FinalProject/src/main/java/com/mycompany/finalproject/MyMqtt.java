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

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.CharBuffer;
import java.util.Scanner;


public class MyMqtt {
    
    private String userName;
    private String passWord;
    private String messageText;
    final String host = "d851ff43cb294d18a69a6d253457ddce.s1.eu.hivemq.cloud";
    // create an MQTT client
    final Mqtt5BlockingClient client = MqttClient.builder()
                                        .useMqttVersion5()
                                        .serverHost(this.host)
                                        .serverPort(8883)
                                        .sslWithDefaultConfig()
                                        .buildBlocking();
    
    public MyMqtt(String userName,String passWord){
        this.userName = userName;
        this.passWord = passWord;
    }
    
    public Mqtt5BlockingClient getClient() {
        return client;
    }
    
    public static MyMqtt connectMqtt(){
       Scanner reader = new Scanner(System.in);
       boolean isTrue = true;
       String userName = "";
       String passWord = "";
       while(isTrue) {
           System.out.println("Enter your mqtt username: ");
           userName = reader.nextLine();
           System.out.println("Enter your mqtt password: ");
           passWord = reader.nextLine();
           try {
               if(userName.length() > 30) {
                   throw new IllegalArgumentException();
               }
               if(passWord.length() < 8 || passWord.length() > 30) {
                   throw new IllegalArgumentException();
               }
               
               isTrue = false;
            }
           catch(Exception e) {
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
    
    public void getData(String topic) {
        // set a callback that is called when a message is received (using the async API style)
        client.toAsync().publishes(ALL, publish -> { 
            if (topic.equals(publish.getTopic().toString())) {
                messageText = UTF_8.decode(publish.getPayload().get()).toString();
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
