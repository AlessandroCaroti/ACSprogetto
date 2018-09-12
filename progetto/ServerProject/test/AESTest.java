import server.utility.AES;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESTest {

    public static void main(String[] args) {
        String ciao="10";
        AES aes;
        try{
            aes=new AES("RandomInitVectol");
            String enc=aes.encrypt(ciao);
            String dec=aes.decrypt(enc);

            if(ciao.equals(dec)){
                System.out.println("OK:"+ ciao+";"+enc+";"+dec);
            }else{
                System.out.println("NOTOK:"+ciao+";"+enc+";"+dec);
            }





        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }




}
