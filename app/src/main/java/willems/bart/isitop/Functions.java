package willems.bart.isitop;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by bart on 6/08/16.
 */
public class Functions {

    // Returns either SHA512 or null
    static String getSHA512Password(String passwordToHash, String salt){
        String passwordResult  = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            passwordResult = sb.toString();
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passwordResult;
    }

    static String generateSalt(){
        int length = 15;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPSRSTUVWXYZ0123456789";
        SecureRandom rng = new SecureRandom();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}
