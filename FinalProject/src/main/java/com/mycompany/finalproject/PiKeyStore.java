public class PiKeyStore{
	KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType()); // KeyStore.getInstance("JKS");
	char[] password;

	public PiKeyStore(char[] password, String path){
		this.password = password; // need deep copy?
		// might need null check and default value for path
		loadKeyStore(path);
	}

	private loadKeyStore(String path){
		java.io.FileInputStream keyStoreData = null;
		try {
			keyStoreData = new java.io.FileInputStream(path);
			ks.load(keyStoreData, this.password);
		} finally {
			if (keyStoreData != null) {
				keyStoreData.close();
			}
		}
	}

	public getPrivateKey(String privateKeyAlias){
		KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
			ks.getEntry(privateKeyAlias, this.password);
		PrivateKey myPrivateKey = pkEntry.getPrivateKey();
	}  

    public saveSecretKey(String secretKeyAlias){
		javax.crypto.SecretKey mySecretKey;
		KeyStore.SecretKeyEntry skEntry =
			new KeyStore.SecretKeyEntry(mySecretKey);
		ks.setEntry(secretKeyAlias, skEntry, 
			new KeyStore.PasswordProtection(this.password));
	}
    
	public storeKeyStore(string path){
		java.io.FileOutputStream fos = null;
		try {
			fos = new java.io.FileOutputStream(path); // newKeyStoreName
			ks.store(fos, password);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}
}
