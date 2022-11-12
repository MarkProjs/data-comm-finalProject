/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.finalproject;

import java.util.Scanner;


public class FinalProject {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        MyMqtt mqtt = connectMqtt(reader);
        
        
        
        System.out.println("Hello World!");
    }
    
    private static MyMqtt connectMqtt(Scanner reader){
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
}
