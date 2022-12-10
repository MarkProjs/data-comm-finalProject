package com.mycompany.finalproject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Enumeration;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PiKeyStore {
	private final String hashingAlgo = "SHA256withECDSA";
	private KeyStore ks;
	private char[] password;
	private Key publicKey;
	private Key privateKey;

	public PiKeyStore(char[] password, String path) {
		if (path.equals("")) {
			throw new IllegalArgumentException("Path must not be empty");
		}
		this.password = password.clone();
		try {
			this.ks = KeyStore.getInstance("PKCS12");
			loadKeyStore(path);
			this.publicKey = this.getPublicKey(this.ks.aliases().nextElement());
			this.privateKey = this.getPrivateKey(this.ks.aliases().nextElement());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadKeyStore(String path) throws IOException {
		InputStream is = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				System.out.println("keystore file exists");
				is = new FileInputStream(file);
			}
			ks.load(is, this.password);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	private Key getPrivateKey(String privateKeyAlias) {
		KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(this.password);
		KeyStore.PrivateKeyEntry pkEntry = null;
		try {
			pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(privateKeyAlias, entryPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pkEntry.getPrivateKey();
	}
	
	private Key getPublicKey(String publicKeyAlias) throws KeyStoreException {
		return this.ks.getCertificate(publicKeyAlias).getPublicKey();
	}

	public String getPublicKeyAsString(String publicKeyAlias) throws KeyStoreException, CertificateEncodingException{
		// Key publicKey = this.getPublicKey(publicKeyAlias);
		String keyAsString = Base64.getEncoder().encodeToString(this.ks.getCertificate(publicKeyAlias).getEncoded());
		return keyAsString;
	}

	public void saveSecretKey(String secretKeyAlias) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] salt = new byte[16];
		var random = new SecureRandom();
		random.nextBytes(salt);
		var hash = computeHash(this.password, salt);

		SecretKey mySecretKey = new SecretKeySpec(hash, hashingAlgo);
		KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(mySecretKey);
		try {
			ks.setEntry(secretKeyAlias, skEntry,
					new KeyStore.PasswordProtection(this.password));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void savePublicKey(String publicKeyAlias, String keyAsString) {
		try {
			byte[] keyBytes = Base64.getDecoder().decode(keyAsString);
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			InputStream in = new ByteArrayInputStream(keyBytes);
			X509Certificate cert = (X509Certificate)certFactory.generateCertificate(in);

			this.ks.setCertificateEntry(publicKeyAlias, cert);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] computeHash(char[] pwd, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var spec = new PBEKeySpec(pwd, salt, 65536, 128);
		var skf = SecretKeyFactory.getInstance(hashingAlgo);
		var secret = skf.generateSecret(spec);
		var hash = secret.getEncoded();
		return hash;
	}

	/**
     * Method for generating digital signature.
     */
    byte[] generateSignature (String message) 
            throws NoSuchAlgorithmException, NoSuchProviderException, 
            InvalidKeyException, UnsupportedEncodingException, SignatureException {
        
        //Create an instance of the signature scheme for the given signature algorithm
        Signature sig = Signature.getInstance(hashingAlgo, "SunEC");
        
        //Initialize the signature scheme
        sig.initSign((PrivateKey) this.privateKey);
        
        //Compute the signature
        sig.update(message.getBytes("UTF-8"));
        byte[] signature = sig.sign();
        
        return signature;
    }
    
    
    /**
     * Method for verifying digital signature.
     * @throws KeyStoreException
     */
    boolean verifySignature(byte[] signature, String publicKeyAlias, String message) 
            throws NoSuchAlgorithmException, NoSuchProviderException, 
            InvalidKeyException, UnsupportedEncodingException, SignatureException, KeyStoreException {
        
        //Create an instance of the signature scheme for the given signature algorithm
        Signature sig = Signature.getInstance(hashingAlgo, "SunEC");
        
        //Initialize the signature verification scheme.
        sig.initVerify((PublicKey) this.getPublicKey(publicKeyAlias));
        
        //Compute the signature.
        sig.update(message.getBytes("UTF-8"));
        
        //Verify the signature.
        boolean validSignature = sig.verify(signature);
        
        if(validSignature) {
            System.out.println("\nSignature is valid");
        } else {
            System.out.println("\nSignature is NOT valid!!!");
        }
        
        return validSignature;
    }


	public void storeKeyStore(String path) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			ks.store(fos, this.password);
		}
	}

	public Enumeration<String> getAliases() throws KeyStoreException {
		return this.ks.aliases();
	}

	public KeyStore getKeyStore() throws KeyStoreException{
		return this.ks;
	}
}
