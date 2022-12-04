
package com.mycompany.finalproject;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

/**
 *
 * @author Carlton Davis
 * Adapted code from The Pi4J Project:
 * https://pi4j.com
 * 
 */
public class CameraApp implements Application {
    
    //Pi4J code to control camera

    /**
     *
     * @param pi4j
     */
   
    @Override
    public void execute(Context pi4j) {
        System.out.println("\nInitializing the camera");
        Camera camera = new Camera();

        System.out.println("Setting up the config to take a picture.");
        
        //Configure the camera setup
        var config = Camera.PicConfig.Builder.newInstance()
                .outputPath("/home/markisawesome/NetBeansProject/data-comm-final-project/FinalProject/src/main/resources/images/")
		.delay(3000)
		.disablePreview(true)
		.encoding(Camera.PicEncoding.PNG)
		.useDate(true)
		.quality(93)
		.width(1280)
		.height(800)
		.build();
        //Take the picture
        camera.takeStill(config);
        System.out.println("waiting for camera to take pic");
        delay(4000);
    }
}
