
package dataManipulations;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.util.Base64;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
 


public class encryptor {

public static String encrypt(String unencrypted, Cipher ecipher, SecretKey key)
{
    try
    {
      byte[] utf8 = unencrypted.getBytes("UTF8");
      byte[] enc = ecipher.doFinal(utf8);
      enc = BASE64EncoderStream.encode(enc);
      return new String(enc);
    }
    catch (Exception e)
    {
       e.printStackTrace();
    }
    return null;
 }

public static String decrypt (String encrypted, Cipher dcipher, SecretKey key)
{
    try
    {
        byte[] dec = BASE64DecoderStream.decode(encrypted.getBytes());
        byte[] utf8 = dcipher.doFinal(dec);
        return new String(utf8, "UTF8");
    }
    catch (Exception e)
    {
            e.printStackTrace();
    }
    return "null";
}
   
    
}
