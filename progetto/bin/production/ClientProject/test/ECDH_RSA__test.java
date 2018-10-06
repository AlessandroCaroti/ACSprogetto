import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import utility.cryptography.AES;
import utility.cryptography.ECDH;
import utility.cryptography.RSA;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class ECDH_RSA__test {

    public static void main(String[] args) throws Exception {
//        test_base();
//        test_soloRSA();
        test_simulazioneScambioChiaveServerClient();
    }


    /********************************************************************************************************************************/

    static private void test_base() throws Exception {
        KeyPair rsa = RSA.generateKeyPair();
        KeyPair dh = ECDH.generateKeyPair("prime192v1");
        PublicKey publicDH_key = dh.getPublic();
        System.out.println(publicDH_key);
        byte[] publicDH_key_encripted = RSA.encrypt(rsa.getPrivate(), dh.getPublic().getEncoded());
        byte[] publicDH_key_dencripted = RSA.decrypt(rsa.getPublic(), publicDH_key_encripted);
        PublicKey publicDH_key_decripted = KeyFactory.getInstance("ECDH", "BC").generatePublic(new X509EncodedKeySpec(publicDH_key_dencripted));
        System.out.println(publicDH_key_decripted);
        System.out.println(publicDH_key.equals(publicDH_key_decripted));
    }


    /********************************************************************************************************************************/

    static private void test_soloRSA() throws Exception {
        KeyPair rsa = RSA.generateKeyPair();
        PublicKey p = rsa.getPublic();
        System.out.println(p);
        String s_p = publicKeyToString(p);
        PublicKey pp = stringToPublicKey(s_p);
        System.out.println(pp);
    }

    private static String publicKeyToString(PublicKey chiavePubblica) {
        return new String(Base64.encode(chiavePubblica.getEncoded()).getBytes());
    }

    private static PublicKey stringToPublicKey(String chiavePubblica) {
        byte[] pubBytes64Decode = Base64.decode(chiavePubblica);
        java.security.interfaces.RSAPublicKey chiavePubblicaRicostruitra = null;
        try {
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
            java.security.PublicKey pubKey = keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(pubBytes64Decode));
            chiavePubblicaRicostruitra = (java.security.interfaces.RSAPublicKey) pubKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return chiavePubblicaRicostruitra;
    }


    /********************************************************************************************************************************/

    static private void test_simulazioneScambioChiaveServerClient() throws Exception {
        //CAMPI SOLO DEL SERVER
        KeyPair serverRSA = RSA.generateKeyPair();
        KeyPair serverECDH = ECDH.generateKeyPair("prime192v1");

        //CAMPI SOLO DEL CLIENT
        String userName = "username";
        String password = "password";
        String email    = "email";
        KeyPair clientECDH = ECDH.generateKeyPair("prime192v1");

        //CAMPI CONDIVISI tra CLIENT e SERVER
        byte[] serverECDHpubKey_encrypted = RSA.encrypt(serverRSA.getPrivate(), serverECDH.getPublic().getEncoded());
        PublicKey serverRSApubKey = serverRSA.getPublic();
        PublicKey clientECDHpubKey = clientECDH.getPublic();

        //Server side operation
        byte[] sheredSecret_server = ECDH.sharedSecretKey(serverECDH.getPrivate(), clientECDHpubKey);
        System.out.println("Server secret key: " + Arrays.toString(sheredSecret_server));
        SecretKeySpec secretAesKey_server = new SecretKeySpec(sheredSecret_server, "AES");


        //Client side operation
        byte[] serverECDHpubKey_decrypted = RSA.decrypt(serverRSApubKey, serverECDHpubKey_encrypted);
        PublicKey serverECDHpubKey = KeyFactory.getInstance("ECDH", "BC").generatePublic(new X509EncodedKeySpec(serverECDHpubKey_decrypted));
        byte[] sheredSecret_client = ECDH.sharedSecretKey(clientECDH.getPrivate(), serverECDHpubKey);
        System.out.println("Client secret key: " + Arrays.toString(sheredSecret_client));
        SecretKeySpec secretAesKey_client = new SecretKeySpec(sheredSecret_client, "AES");


        //Test criptografia AES
        String plainText = "Hello world!";
        byte[] encriptedText = AES.encrypt(plainText.getBytes(), secretAesKey_server);
        byte[] decriptedText = AES.decrypt(encriptedText, secretAesKey_client);
        String decriptedString = new String(decriptedText, StandardCharsets.UTF_8);
        System.out.println("\n\nTesto originale:\t\t\t\t\t" + plainText);
        System.out.println("Testo criptato e decriptato:\t\t" + decriptedString);

        //SIMULAZIONE PASSAGGIO DELLE INFORMAZIONI DELL'ACCOUNT DEL CLIENT AL SERVER

        //Lato client
        byte[][] encryptedAccountInfo = new byte[3][];
        encryptedAccountInfo[0] = AES.encrypt(email.getBytes(),    secretAesKey_client);
        encryptedAccountInfo[1] = AES.encrypt(userName.getBytes(), secretAesKey_client);
        encryptedAccountInfo[2] = AES.encrypt(password.getBytes(), secretAesKey_client);
        //parte non necessaria messa per assicurarsi che le informazioni siano veramente criptate
        String[] encriptedInfo = new String[3];
        encriptedInfo[0] = new String(encryptedAccountInfo[0], StandardCharsets.UTF_8);
        encriptedInfo[1] = new String(encryptedAccountInfo[1], StandardCharsets.UTF_8);
        encriptedInfo[2] = new String(encryptedAccountInfo[2], StandardCharsets.UTF_8);
        System.out.println("\n\n\n" + Arrays.toString(encriptedInfo));
        //invio di 'encryptedAccountInfo' al server


        //Lato server
        String emailClient    = new String(AES.decrypt(encryptedAccountInfo[0], secretAesKey_server), StandardCharsets.UTF_8);
        String usernameClient = new String(AES.decrypt(encryptedAccountInfo[1], secretAesKey_server), StandardCharsets.UTF_8);
        String passwordClient = new String(AES.decrypt(encryptedAccountInfo[2], secretAesKey_server), StandardCharsets.UTF_8);
        System.out.println("\n"+emailClient+" - "+usernameClient+" - "+passwordClient);
    }
}
