import utility.ECDH;
import utility.RSA;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class ECDH_RSA__test {
    public static void main(String[] args) throws Exception {
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
}
