package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringHashed {

    private final String algorithm;
    private final byte[] stringHashed;

    public StringHashed(String plainText) throws NoSuchAlgorithmException {

        algorithm = "SHA-256";
        stringHashed = hashFunctions.stringHash(plainText);
    }

    public StringHashed(String plainText, String hash_algorithm) throws NoSuchAlgorithmException {
        algorithm = hash_algorithm;
        stringHashed = hashFunctions.stringHash(plainText, algorithm);
    }

    public byte[] getStringHashed() {
        return stringHashed;
    }

    public boolean isEqual(String plainText)
    {
        byte[] toCompere = null;
        try {
            toCompere = hashFunctions.stringHash(plainText, algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return MessageDigest.isEqual(stringHashed, toCompere);
    }
}
