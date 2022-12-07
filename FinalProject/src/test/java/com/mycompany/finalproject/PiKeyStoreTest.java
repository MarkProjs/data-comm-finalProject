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
    
    //Constructor for the test class
    public PiKeyStoreTest() {
        // init fields if needed
    } 
    
    @Test
    public void testHelloWorld() throws Exception {
              
        String expResult = "Hello World";
        assertNotNull(expResult);
        
        //This assertion will only be executed if the previous assertion is valid
        assertTrue(expResult.contains("Hello World"));
    }
    
}
