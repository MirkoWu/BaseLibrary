package com.softgarden.baselibrary.utils;

/**
 * Created by Administrator on 2016/7/1 0001.
 */

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    /**
     * 算法/模式/填充
     **/
    private static final String CipherMode = "AES/CBC/PKCS5Padding";
    public static final String IV = "1234567887654321";

    /**
     * 公钥加密
     *
     * @param contentStr
     * @param key  得到的公钥
     * @param iv
     * @return 已被Base64加密过的字符串
     */
    public static String encrypt(String contentStr, String key, String iv) {
        key = getPassword(key);//截取key 16位
        try {
            byte[] strByte = contentStr.getBytes("UTF-8");
            byte[] keyByte = key.getBytes("UTF-8");
            byte[] ivByte = iv.getBytes("UTF-8");
            byte[] result = encrypt(strByte, keyByte, ivByte);//AES加密

            return Base64.encodeToString(result, Base64.NO_PADDING);//要进行base64再一次加密
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param base64Str
     * @param key
     * @param iv
     * @return
     */

    public static String decryptWithBase64(String base64Str, String key, String iv) {
        key = getPassword(key);//截取key 16位
        try {
            byte[] strByte = Base64.decode(base64Str, Base64.NO_PADDING);//要先base64解密

            byte[] keyByte = key.getBytes("UTF-8");
            byte[] ivByte = iv.getBytes("UTF-8");
            byte[] result = decrypt(strByte, keyByte, ivByte);//AES解密
            return new String(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取16位的秘钥（128 bits）
     * 长度大于16则截取前16位，否则不变（这是错的，因为 128要求必须有16位密码）
     *
     * @param key
     * @return
     */
    private static String getPassword(String key) {
        if (TextUtils.isEmpty(key)) return "0000000000000000";//这是错的，因为 128要求必须有16位密码
        if (key.length() >= 16) {
            return key.substring(0, 16);//长度大于16则截取前16位
        } else {
            return key;//这是错的，因为 128要求必须有16位密码
        }
    }

    public static byte[] encrypt(byte[] content, byte[] keyBytes, byte[] iv) {
        try {
//            byte[] raw = getRawKey(keyBytes);
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] content, byte[] keyBytes, byte[] iv) {
        try {
//            byte[] raw = getRawKey(keyBytes);
//            SecretKeySpec key = new SecretKeySpec(raw, "AES");

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 对密钥进行处理
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //for android
        SecureRandom sr = null;
        int sdk_version = android.os.Build.VERSION.SDK_INT;
        if (sdk_version > 23) {  // Android  6.0 以上
            sr = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
        } else if (sdk_version >= 17) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }

        // for Java
        // secureRandom = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(seed);
        kgen.init(128, sr); //256 bits or 128 bits,192bits
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


//    public static void main(String[] args) throws UnsupportedEncodingException {
//        String IV = "1750654920105581";
//        String API_ENCRYPT_KEY = "1467f4699214cec412f7c2a1d513fe08";
//        String data = "b+W8eZv9suf1DgHEzeny5DtAPrd4L5dBHHL+0YF85ey2W\\/oFX9sBVD3azJkOP\\/M0BXUJu7f0eiQLi2oIbz56LOC1Pto1qVx3dgg\\/agidWTU=";
//        System.out.println(data);
//        System.out.println(data.getBytes("UTF-8"));
//        try {
//
//            String key = MD5Util.ToMD5NOKey("1522220376" + API_ENCRYPT_KEY + "e879ada9061d176a498812c62770b9c3");
//            // byte[] en = encrypt(data.getBytes("UTF-8"), API_ENCRYPT_KEY.getBytes("UTF-8"), IV.getBytes("UTF-8"));
//            System.out.println("key==" + key);
//            byte[] bytes = java.util.Base64.getDecoder().decode(data);
//            System.out.println("解密后数据==" + new String(decrypt(bytes, key.getBytes("UTF-8"), IV.getBytes("UTF-8")), "UTF-8"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public static class CryptoProvider extends Provider {
        /**
         * Creates a Provider and puts parameters
         */
        public CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG",
                    "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }


    private static String toHex(byte[] buf) {
        final String HEX = "0123456789ABCDEF";
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            result.append(HEX.charAt((buf[i] >> 4) & 0x0f)).append(
                    HEX.charAt(buf[i] & 0x0f));
        }
        return result.toString();
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }
}

