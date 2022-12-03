package com.mycompany.finalproject;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PiKeyStore {
	final String hashingAlgo = "PBKDF2WithHmacSHA1";
	KeyStore ks; // KeyStore.getInstance("JKS");
	char[] password;

	public PiKeyStore(char[] password, String path) {
		this.password = password.clone();
		// might need null check and default value for path
		try {
			this.ks = KeyStore.getInstance(KeyStore.getDefaultType());
			loadKeyStore(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadKeyStore(String path) throws IOException {
		java.io.FileInputStream keyStoreData = null;
		try {
			keyStoreData = new java.io.FileInputStream(path);
			ks.load(keyStoreData, this.password);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (keyStoreData != null) {
				keyStoreData.close();
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
		PrivateKey myPrivateKey = pkEntry.getPrivateKey();
		return myPrivateKey;
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

	public void storeKeyStore(String path) throws IOException {
		java.io.FileOutputStream fos = null;
		try {
			fos = new java.io.FileOutputStream(path); // newKeyStoreName
			ks.store(fos, password);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}
}
