import utility.ECDH;
import utility.RSA;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class ECDH_RSA__test2 {
    public static void main(String[] args) throws Exception {
        //CAMPI DEL SERVER
        KeyPair serverRSA = RSA.generateKeyPair();
        KeyPair serverECDH = ECDH.generateKeyPair("prime192v1");

        //CAMPI DEL CLIENT
        KeyPair clientECDH = ECDH.generateKeyPair("prime192v1");

        //CAMPI CONDIVISI tra CLIENT e SERVER
        byte[] serverECDHpubKey_encripted = RSA.encrypt(serverRSA.getPrivate(), serverECDH.getPublic().getEncoded());
        PublicKey serverRSApubKey = serverRSA.getPublic();
        PublicKey clientECDHpubKey = clientECDH.getPublic();

        //server side operation
        byte[] sheredSecret_server = ECDH.sheredSecretKey(serverECDH.getPrivate(), clientECDHpubKey);
        System.out.println("Server secret key: " + Arrays.toString(sheredSecret_server));

        //client side operation
        byte[] serverECDHpubkey_decripted = RSA.decrypt(serverRSApubKey, serverECDHpubKey_encripted);
        PublicKey serverECDHpubKey = KeyFactory.getInstance("ECDH", "BC").generatePublic(new X509EncodedKeySpec(serverECDHpubkey_decripted));
        byte[] sheredSecret_client = ECDH.sheredSecretKey(clientECDH.getPrivate(), serverECDHpubKey);
        System.out.println("Client secret key: " + Arrays.toString(sheredSecret_client));
        System.out.println(sheredSecret_client.length);

    }
}
