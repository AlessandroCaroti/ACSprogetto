import utility.AES;
import utility.ECDH;
import utility.RSA;

import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class ECDH_RSA__test2 {

    public static void main(String[] args) throws Exception {
        //CAMPI SOLO DEL SERVER
        KeyPair serverRSA = RSA.generateKeyPair();
        KeyPair serverECDH = ECDH.generateKeyPair("prime192v1");

        //CAMPI SOLO DEL CLIENT
        KeyPair clientECDH = ECDH.generateKeyPair("prime192v1");

        //CAMPI CONDIVISI tra CLIENT e SERVER
        byte[] serverECDHpubKey_encrypted = RSA.encrypt(serverRSA.getPrivate(), serverECDH.getPublic().getEncoded());
        PublicKey serverRSApubKey = serverRSA.getPublic();
        PublicKey clientECDHpubKey = clientECDH.getPublic();

        //Server side operation
        byte[] sheredSecret_server = ECDH.sheredSecretKey(serverECDH.getPrivate(), clientECDHpubKey);
        System.out.println("Server secret key: " + Arrays.toString(sheredSecret_server));
        SecretKeySpec secretAesKey_server = new SecretKeySpec(sheredSecret_server, "AES");


        //Client side operation
        byte[] serverECDHpubKey_decrypted = RSA.decrypt(serverRSApubKey, serverECDHpubKey_encrypted);
        PublicKey serverECDHpubKey = KeyFactory.getInstance("ECDH", "BC").generatePublic(new X509EncodedKeySpec(serverECDHpubKey_decrypted));
        byte[] sheredSecret_client = ECDH.sheredSecretKey(clientECDH.getPrivate(), serverECDHpubKey);
        System.out.println("Client secret key: " + Arrays.toString(sheredSecret_client));
        SecretKeySpec secretAesKey_client = new SecretKeySpec(sheredSecret_client, "AES");


        //Test criptografia AES
        String plainText = "Hello world!";
        byte[] encriptedText = AES.encrypt(plainText.getBytes(), secretAesKey_server);
        byte[] decriptedText = AES.decrypt(encriptedText, secretAesKey_client);
        String decriptedString = new String(decriptedText,"UTF-8");
        System.out.println("\n\nTesto originale:\t\t\t\t\t" + plainText);
        System.out.println("Testo criptato e decriptato:\t\t" + decriptedString);
    }
}
