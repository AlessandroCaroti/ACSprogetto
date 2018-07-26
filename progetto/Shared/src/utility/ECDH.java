package utility;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import javax.crypto.KeyAgreement;
import java.security.*;

final public class ECDH {

    public static KeyPair generateKeyPair(String curveName) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(curveName);
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDH", "BC");
        keyGen.initialize(ecSpec, new SecureRandom());

        return keyGen.generateKeyPair();
    }

    public static byte[] sheredSecretKey(PrivateKey myPrivateKey, PublicKey otherPublicKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement keyAgree = KeyAgreement.getInstance("ECDH", "BC");
        keyAgree.init(myPrivateKey);
        keyAgree.doPhase(otherPublicKey, true);

        return keyAgree.generateSecret();
    }

}