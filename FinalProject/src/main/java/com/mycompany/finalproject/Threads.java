/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

/**
 *
 * @author Mark Agluba
 */
public class Threads {
    private static boolean running = true;
    
    public void startDHTThread() {
        Thread dhtThread = new Thread(()->{
        
        
        });
        
        dhtThread.start();
    }
    
    public void startDoorBellThread() {
        Thread doorBellThread = new Thread(()->{
        
        
        });
        
        doorBellThread.start();
    
    }
    
    public void startSenseLEDThread() {
        Thread senseLEDThread = new Thread(()->{
        
        
        });
        
        senseLEDThread.start();
    }
    
    public void endThreads() {
        this.running = false;
    }
    
    
}
