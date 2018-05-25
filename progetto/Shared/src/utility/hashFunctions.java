package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hashFunctions {
    protected hashFunctions(){
    }

    /**Cripta la string passata
     * @param plainText il testo in chiaro
     * @return l'hash della stringa passata
     * @throws NullPointerException se plainText è null
     */
     static byte[] stringHash(String plainText) throws NullPointerException,NoSuchAlgorithmException
    {
        MessageDigest md;
        if(plainText==null)
        {
            throw new NullPointerException("plainText == null");
        }
        md = MessageDigest.getInstance("SHA-256");
        md.update(plainText.getBytes());
        return md.digest();
    }
     static boolean compareHashandString(byte[] hash,String string) throws NullPointerException,NoSuchAlgorithmException
    {
        byte[] hash2=stringHash(string);
        for(int i=0;i<hash.length;i++)
        {
            if(hash[i]!=hash2[i])
            {
                return false;
            }
        }
        return true;
    }


}
