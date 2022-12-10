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
        // MyMqtt hiveMq = MyMqtt.connectMqtt();
        // // hiveMq.getData("testSub/test/topic");
        // hiveMq.connectClient();
        // hiveMq.subscribe("testSub/test/topic");
        // hiveMq.publish("testSub/test/topic", "Test publish and receive message");
        String messageText = "{json: bla bla bla, more: uh oh}|alias|keysuperimportant3451=";
        String message = messageText.substring(0, messageText.indexOf('|'));
        String alias = messageText.substring(messageText.indexOf('|') + 1, messageText.lastIndexOf('|'));
        String signature = messageText.substring(messageText.lastIndexOf('|') + 1, messageText.length());
        System.out.println(message);
        System.out.println(alias);
        System.out.println(signature);
    }
}
