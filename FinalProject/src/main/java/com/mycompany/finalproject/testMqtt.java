/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

/**
 *
 * @author Mark Agluba
 */
public class testMqtt {
    
    public static void main(String[] args) {
        MyMqtt hiveMq = MyMqtt.connectMqtt();
        hiveMq.connectClient();
        hiveMq.subscribe("testSub/test/topic");
        hiveMq.getData("testSub/test/topic");
        hiveMq.publish("testSub/test/topic", "Test publish and receive message");
        System.out.println(hiveMq.getMessageText());
          
    }
}
