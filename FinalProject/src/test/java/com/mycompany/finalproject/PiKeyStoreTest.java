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
    public void testConstructor() throws Exception {
        char[] pw = "jeremy".toCharArray();
        PiKeyStore keyStore = new PiKeyStore(pw, ksPath);
        assertNotNull(keyStore);
        assertArrayEquals(pw, keyStore.password);
        assertTrue(keyStore.getClass() == PiKeyStore.class);
    }
    
}
