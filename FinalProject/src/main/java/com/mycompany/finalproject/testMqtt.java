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
        String test = "{json: bla bla bla, more: uh oh}|keysuperimportant3451=";
        if (test.lastIndexOf('-') == -1) {
            System.out.println("- does not exists");
        }
        String first = test.substring(0, test.lastIndexOf('|'));
        String second = test.substring(test.lastIndexOf('|')+1, test.length());
        System.out.println(first);
        System.out.println(second);
    }
}
