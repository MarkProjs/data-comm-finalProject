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

    // Constructor for the test class
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
        assertTrue(keyStore.getClass() == PiKeyStore.class);
    }

    // @Test
    // public void testGetPublicKey() throws Exception {
    //     PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
    //     Key publicKey = keyStore.getPublicKey(ksAlias);

    //     assertNotNull(publicKey);
    //     assertTrue(publicKey.getAlgorithm().equals("EC"));
    // }

    @Test
    public void testGetPublicKeyAsString() throws Exception {
        PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
        String keyAsString = keyStore.getPublicKeyAsString(ksAlias);
        System.out.println(keyAsString);

        assertNotNull(keyAsString);
        assertEquals(
                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEshkstfORfPYoqkuEpMHg7PPSk" +
                        "+Njm4uzBDo1kXGkyJWQiYzwJkTNz3XRfWp7EKLRltBzbidqpJ8wDrlw7AFs5A==",
                keyAsString);
    }

    // @Test
    // public void testGetPrivateKey() throws Exception {
    //     PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
    //     Key privateKey = keyStore.getPrivateKey(ksAlias);

    //     assertNotNull(privateKey);
    //     assertTrue(privateKey.getAlgorithm().equals("EC"));
    // }

    @Test
    public void testSavePublicKey() throws Exception {

    }

    @Test
    public void testStoreKeyStore() throws Exception {
        PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
        String newKsPath = "C:\\Users\\Jeremy\\OneDrive - Dawson College\\2022_fall_5\\data comm\\keystore\\newKeyStoreFileName.ks";
        keyStore.storeKeyStore(newKsPath);
        PiKeyStore newKeyStore = new PiKeyStore(ksPw, newKsPath);

        assertEquals(keyStore.getPublicKeyAsString(ksAlias), newKeyStore.getPublicKeyAsString(ksAlias));
    }

}
