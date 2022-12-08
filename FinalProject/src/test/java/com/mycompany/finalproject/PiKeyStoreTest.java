package com.mycompany.finalproject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.security.Key;

/**
 *
 * @author Jeremy Tang
 */

/* Test class for PiKeyStore class */
public class PiKeyStoreTest {
    
    // fields if needed
    String ksPath;
    char[] ksPw;
    String ksAlias;
    
    //Constructor for the test class
    public PiKeyStoreTest() {
        // change to own absolute path (required)
        ksPath = "C:\\Users\\Jeremy\\OneDrive - Dawson College\\2022_fall_5\\data comm\\keystore\\ECcertif.ks";
        ksPw = "jeremy".toCharArray();
        ksAlias = "jrmy";
    } 

    @Test
    public void testConstructor() throws Exception {
        PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);

        assertNotNull(keyStore);
        assertArrayEquals(ksPw, keyStore.password);
        assertTrue(keyStore.getClass() == PiKeyStore.class);
    }

    @Test
    public void testGetPublicKey() throws Exception {
        PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
        Key publicKey = keyStore.getPublicKey(ksAlias);

        assertNotNull(publicKey);
        assertTrue(publicKey.getAlgorithm().equals("EC"));
    }

    @Test
    public void testGetPrivateKey() throws Exception {
        PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
        Key privateKey = keyStore.getPrivateKey(ksAlias);

        assertNotNull(privateKey);
        assertTrue(privateKey.getAlgorithm().equals("EC"));
    }

    @Test
    public void testStoreKeyStore() throws Exception{
        PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
        String newKsPath = "C:\\Users\\Jeremy\\OneDrive - Dawson College\\2022_fall_5\\data comm\\keystore\\newKeyStoreFileName.ks";
        keyStore.storeKeyStore(newKsPath);
        PiKeyStore newKeyStore = new PiKeyStore(ksPw, newKsPath);
        
        assertEquals(keyStore.getPublicKey(ksAlias), newKeyStore.getPublicKey(ksAlias));
        assertEquals(keyStore.getPrivateKey(ksAlias), newKeyStore.getPrivateKey(ksAlias));

    }
    
}
