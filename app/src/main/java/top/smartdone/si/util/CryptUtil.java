package top.smartdone.si.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import top.smartdone.si.core.Config;

/**
 * Created by smartdone on 2017/12/5.
 */

public class CryptUtil {
    private static byte[] iv = new byte[]{1, 3, 2, 5, 4, 6, 7, 8, 0x0a, 0x0c, 0x0b, 0x0d, 0x0e, 0x0f, 0x00, 0x09};

    public static String encryptPsw(String msg){
        try{
            SecretKeySpec keyspec = new SecretKeySpec(Config.KEY.getBytes(), "AES");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
