package utility;

import javax.crypto.Cipher;
import java.security.*;

final public class RSA {
    static private final int keySize = 2048;

    public static KeyPair generateRSA_keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encryptRSA(PrivateKey privateKey, byte [] plainBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(plainBytes);
    }

    public static byte[] decryptRSA(PublicKey publicKey, byte [] encryptedBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(encryptedBytes);
    }

}
