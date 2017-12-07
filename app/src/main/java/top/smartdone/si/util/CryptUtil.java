package top.smartdone.si.util;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import top.smartdone.si.core.Config;

/**
 * Created by smartdone on 2017/12/5.
 */

public class CryptUtil {
    private static byte[] iv = new byte[]{1, 3, 2, 5, 4, 6, 7, 8, 0x0a, 0x0c, 0x0b, 0x0d, 0x0e, 0x0f, 0x00, 0x09};

    private static final String Pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGoTGsKtUlZwnE0efliAMuEVNbDS6rX3s3+U1pX/FfaKcdDAqwcRI8dzLHRdX4Nt2oXAeDSkTLVvSRrQxbHZNN3U/FshjO1QICPiuBJ9R+lEsZwuR9CjPiwQeu7tsHUEIJ4N0nbLd6ZUFApP5jNI2MilIGxVxn9dORvcEW7vOecwIDAQAB";

    public static String encryptPsw(String msg) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(Config.KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(msg.getBytes());
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //公钥解密
    public static byte[] decryptWithPublicKey(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    //公钥加密
    public static String encryptWithPublicKey(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//java默认"RSA"="RSA/ECB/PKCS1Padding"
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeToString(cipher.doFinal(content), Base64.NO_WRAP);
    }

    public static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(Pub, Base64.NO_WRAP));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
}
