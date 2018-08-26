import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import utility.cryptography.RSA;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class RSA_test {
    public static void main(String[] args) throws Exception{
        KeyPair rsa = RSA.generateKeyPair();
        PublicKey p = rsa.getPublic();
        System.out.println(p);
        String s_p = publicKeyToString(p);
        PublicKey pp = stringToPublicKey(s_p);
        System.out.println(pp);
    }

    private static String publicKeyToString(PublicKey chiavePubblica){
        return new String(Base64.encode(chiavePubblica.getEncoded()).getBytes());
    }

    private static PublicKey stringToPublicKey(String chiavePubblica){
        byte[] pubBytes64Decode = Base64.decode(chiavePubblica);
        java.security.interfaces.RSAPublicKey chiavePubblicaRicostruitra=null;
        try {
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
            java.security.PublicKey pubKey = keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(pubBytes64Decode));
            chiavePubblicaRicostruitra = (java.security.interfaces.RSAPublicKey) pubKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) { e.printStackTrace();
        }
        return chiavePubblicaRicostruitra;
    }
}
