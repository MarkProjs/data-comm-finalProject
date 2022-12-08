//package com.mycompany.finalproject;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.security.Key;
//
///**
// *
// * @author Jeremy Tang
// */
//
///* Test class for PiKeyStore class */
//public class PiKeyStoreTest {
//    
//    // fields if needed
//    String ksPath;
//    
//    //Constructor for the test class
//    public PiKeyStoreTest() {
//        // change to own absolute path (required)
//        ksPath = "C:\\Users\\Jeremy\\OneDrive - Dawson College\\2022_fall_5\\data comm\\keystore\\ECcertif.ks";
//    } 
//    
//    @Test
//    public void testConstructor() throws Exception {
//        char[] pw = "jeremy".toCharArray();
//        PiKeyStore keyStore = new PiKeyStore(pw, ksPath);
//        assertNotNull(keyStore);
//        assertArrayEquals(pw, keyStore.password);
//        assertTrue(keyStore.getClass() == PiKeyStore.class);
//    }
//    
//    @Test
//    public void testGetPublicKey() throws Exception {
//        PiKeyStore keyStore = new PiKeyStore("jeremy".toCharArray(), ksPath);
////        Key publicKey = keyStore.getPublicKey("jrmy");
//        assertNotNull(publicKey);
//        assertTrue(publicKey.getAlgorithm().equals("EC"));
//    }
//
//    @Test
//    public void testGetPrivateKey() throws Exception {
//        PiKeyStore keyStore = new PiKeyStore("jeremy".toCharArray(), ksPath);
//        Key privateKey = keyStore.getPrivateKey("jrmy");
//        assertNotNull(privateKey);
//        assertTrue(privateKey.getAlgorithm().equals("EC"));
//    }
//    
//}
