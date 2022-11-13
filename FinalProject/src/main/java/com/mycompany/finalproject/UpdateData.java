/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject;

import java.io.IOException;
import javafx.scene.Scene;

/**
 *
 * @author Mark Agluba
 */

//Heleper class to update the tiles
public class UpdateData {
    public void updateOutput() throws IOException {
        var updatedScene = new Scene(new JavaFX(), 1060, 1200);
        FinalProject.theStage.setScene(updatedScene);
        FinalProject.theStage.show();
    }
}
