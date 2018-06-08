package server;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public  class AES{
    private Cipher cipherEnc;
    private Cipher cipherDec;
    private static SecretKey secretKey;
    private  Base64.Decoder  base64Decoder;
    private  Base64.Encoder base64Encoder;

    public AES(String initVector16bytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        this.cipherEnc=Cipher.getInstance("AES/CBC/PKCS5Padding");
        this.cipherDec=Cipher.getInstance("AES/CBC/PKCS5Padding");
        KeyGenerator keyGen=KeyGenerator.getInstance("AES");
        keyGen.init(256);
        secretKey=keyGen.generateKey();
        cipherDec.init(Cipher.DECRYPT_MODE,secretKey, new IvParameterSpec(initVector16bytes.getBytes("UTF-8")));
        cipherEnc.init(Cipher.ENCRYPT_MODE,secretKey, new IvParameterSpec(initVector16bytes.getBytes("UTF-8")));

        base64Decoder= Base64.getDecoder();
        base64Encoder=Base64.getEncoder();
    }


    public String encrypt(String plainText) throws BadPaddingException, IllegalBlockSizeException{
        return base64Encoder.encodeToString(cipherEnc.doFinal(plainText.getBytes()));
    }
    public String decrypt(String encryptedText) throws BadPaddingException, IllegalBlockSizeException {
        byte[] plainText=cipherDec.doFinal(base64Decoder.decode(encryptedText));
        return  new String(plainText);
    }


}