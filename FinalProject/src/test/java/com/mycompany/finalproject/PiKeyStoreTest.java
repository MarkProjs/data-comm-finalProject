package com.mycompany.finalproject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jeremy Tang
 */

/* Test class for PiKeyStore class */
public class PiKeyStoreTest {
    
    // fields if needed
    String ksPath;
    
    //Constructor for the test class
    public PiKeyStoreTest() {
        // change to own absolute path (required)
        ksPath = "C:\\Users\\Jeremy\\OneDrive - Dawson College\\2022_fall_5\\data comm\\keystore\\ECcertif.ks";
    } 
    
    @Test
    public void testHelloWorld() throws Exception {
        String expResult = "Hello World";
        System.out.println(expResult);
        assertNotNull(expResult);
        
        //This assertion will only be executed if the previous assertion is valid
        assertTrue(expResult.contains("Hello World"));
    }
    
}
