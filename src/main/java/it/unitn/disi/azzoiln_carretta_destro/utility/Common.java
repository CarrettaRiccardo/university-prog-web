package it.unitn.disi.azzoiln_carretta_destro.utility;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Classe per metodi statici di comune utilità nell' Applicazione
 * 
 * Crypto -> https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
 * @author Steve
 */
public class Common {
    private static final SecureRandom RAND = new SecureRandom();
    private static final int salt_lenght = 10;
    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final String ALGORITHM_SALT = "SHA1PRNG";
    
    
    
    public static String getPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }
     
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance(ALGORITHM_SALT);
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
     
    private static String toHex(byte[] array) throws NoSuchAlgorithmException{
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    
    
    
    
}
