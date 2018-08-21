/**
    This file is part of ACSprogetto.

    ACSprogetto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ACSprogetto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ACSprogetto.  If not, see <http://www.gnu.org/licenses/>.

**/
package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hashFunctions {

    static private MessageDigest md_MD5;
    static private MessageDigest md_s1;
    static private MessageDigest md_s256;
    static {
        try {
            md_MD5 = MessageDigest.getInstance("MD5");
            md_s1 = MessageDigest.getInstance("SHA-1");
            md_s256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
    }

    /**Cripta la string passata
     * @param plainText il testo in chiaro
     * @return l'hash della stringa passata
     * @throws NullPointerException se plainText è null
     */
     static byte[] stringHash(String plainText) throws NullPointerException {
        if(plainText==null)
            throw new NullPointerException("plainText == null");
        md_s256.update(plainText.getBytes());
        return md_s256.digest();
    }

    static byte[] stringHash(String plainText, String hashAlgorithm) throws NullPointerException,NoSuchAlgorithmException
    {
        MessageDigest md;
        if(plainText==null)
            throw new NullPointerException("plainText == null");
        switch (hashAlgorithm) {
            case "MD5":
                md = md_MD5;
                break;
            case "SHA-1":
                md = md_s1;
                break;
            case "SHA-256":
                md = md_s256;
                break;
            default:
                throw new NoSuchAlgorithmException();
        }
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
