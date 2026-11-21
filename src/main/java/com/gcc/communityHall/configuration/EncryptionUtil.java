package com.gcc.communityHall.configuration;


import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Component
public class EncryptionUtil {

    private static final String SECRET_KEY = "MySecretKey12345"; // 16 chars
    private static final String ALGORITHM = "AES";


    public String encrypt(String value) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getUrlEncoder().encodeToString(encrypted); // URL safe
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String decrypt(String encryptedValue) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getUrlDecoder().decode(encryptedValue);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}