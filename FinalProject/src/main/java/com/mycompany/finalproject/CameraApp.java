
package com.mycompany.camerapi4jexamplecode;

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
                .outputPath("/home/markisawesome/Pictures/")
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

        System.out.println("Taking a video for 3 seconds");
        
        //Configure the video setup
        var vidconfig = Camera.VidConfig.Builder.newInstance()
                .outputPath("/home/markisawesome/Videos/")
                .disablePreview(true)
		.recordTime(3000)
		.useDate(true)
		.build();
        
        //Take the video
        camera.takeVid(vidconfig); 
    }

    
    public static void main(String[] args) {

        //Initialize the Pi4J Runtime Context
        var pi4j = Pi4J.newAutoContext();

        CameraApp runApp = new CameraApp();
        runApp.execute(pi4j);
        
        // Shutdown Pi4J
        pi4j.shutdown();

    }
    
}
