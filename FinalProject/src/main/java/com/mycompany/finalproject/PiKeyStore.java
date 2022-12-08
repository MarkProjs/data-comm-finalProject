package com.mycompany.finalproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PiKeyStore {
	final String hashingAlgo = "SHA256withECDSA";
	KeyStore ks;
	char[] password;

	public PiKeyStore(char[] password, String path) {
		if (path.equals("")) {
			throw new IllegalArgumentException("Path must not be empty");
		}
		this.password = password.clone();
		try {
			this.ks = KeyStore.getInstance("PKCS12");
			loadKeyStore(path);
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

	public Key getPrivateKey(String privateKeyAlias) {
		KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(this.password);
		KeyStore.PrivateKeyEntry pkEntry = null;
		try {
			pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(privateKeyAlias, entryPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pkEntry.getPrivateKey();
	}
	
	public Key getPublicKey(String publicKeyAlias) throws KeyStoreException {
		return this.ks.getCertificate(publicKeyAlias).getPublicKey();
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

	private byte[] computeHash(char[] pwd, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		var spec = new PBEKeySpec(pwd, salt, 65536, 128);
		var skf = SecretKeyFactory.getInstance(hashingAlgo);
		var secret = skf.generateSecret(spec);
		var hash = secret.getEncoded();
		return hash;
	}

	public void storeKeyStore(String path) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			ks.store(fos, this.password);
		}
	}
}
