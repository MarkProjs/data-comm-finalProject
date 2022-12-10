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
    String relPath = System.getProperty("user.dir") + "\\src\\test\\java\\com\\mycompany\\finalproject\\";

    // Constructor for the test class
    public PiKeyStoreTest() {
        // change to own absolute path (required)
        ksPath = relPath + "ECcertif.ks";
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
    // PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
    // Key publicKey = keyStore.getPublicKey(ksAlias);

    // assertNotNull(publicKey);
    // assertTrue(publicKey.getAlgorithm().equals("EC"));
    // }

    @Test
    public void testGetPublicKeyAsString() throws Exception {
        PiKeyStore keyStore = new PiKeyStore(ksPw, relPath + "ECcertif.ks");
        String keyAsString = keyStore.getPublicKeyAsString(ksAlias);
        System.out.println(keyAsString);

        assertNotNull(keyAsString);
    }

    // @Test
    // public void testGetPrivateKey() throws Exception {
    // PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
    // Key privateKey = keyStore.getPrivateKey(ksAlias);

    // assertNotNull(privateKey);
    // assertTrue(privateKey.getAlgorithm().equals("EC"));
    // }

    @Test
    public void testSavePublicKey() throws Exception {
        PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
        PiKeyStore newKeyStore = new PiKeyStore("test1234".toCharArray(), relPath + "Testcertif.ks");
        String publicKeyAsString = newKeyStore.getPublicKeyAsString("TEST");

        keyStore.savePublicKey("TEST", publicKeyAsString);
        String publicKeyAsString2 = keyStore.getPublicKeyAsString("TEST");

        assertEquals(publicKeyAsString, publicKeyAsString2);
        var e = keyStore.getAliases();

        // display the result
        System.out.println("List of all the alias present");
        while (e.hasMoreElements()) {
            System.out.print(e.nextElement() + "");
            System.out.println();
        }
    }

    @Test
    public void testDigitalSignature() throws Exception {
        PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
        PiKeyStore newKeyStore = new PiKeyStore("test1234".toCharArray(), relPath + "Testcertif.ks");
        String x = keyStore.getPublicKeyAsString(ksAlias);
        newKeyStore.savePublicKey(ksAlias, x);

        String message = "hello this is a message";
        var sig = keyStore.generateSignature(message);
        var isValid = newKeyStore.verifySignature(sig, ksAlias, message);

        assertTrue(isValid);
    }

    @Test
    public void testStoreKeyStore() throws Exception {
        PiKeyStore keyStore = new PiKeyStore(ksPw, ksPath);
        String newKsPath = relPath + "newKeyStore.ks";
        keyStore.storeKeyStore(newKsPath);
        PiKeyStore newKeyStore = new PiKeyStore(ksPw, newKsPath);

        assertEquals(keyStore.getPublicKeyAsString(ksAlias), newKeyStore.getPublicKeyAsString(ksAlias));
    }

}
