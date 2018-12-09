package com.softgarden.baselibrary.utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;

/**
 * @Author: liujinghui
 * @Date: 2018-08-15
 * @Version 1.0
 * @Desc: 描述...
 */
public class RSAUtil {

    private static final String RSA = "RSA";
    private static final String UTF_8 = "UFT-8";
    private static final String TRANSFORMATION = "RSA/None/PKCS1Padding";
    //    private static KeyPair keyPair;
    private static int keyLength = 1024;
    static HashMap<String, KeyPair> hashMap = new HashMap<>();
    static String urlKey;

    private KeyPair keyPair;


    public RSAUtil() {
        try {
            if (keyPair == null) {
                keyPair = generateRSAKeyPair(keyLength);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getPublicByKey() {
        //获取公钥
        byte[] publicKey = getPublicKey(hashMap.get(urlKey));
        //公钥用base64编码
        String encodePublic = Base64.encodeToString(publicKey, Base64.DEFAULT);
//        Log.d("TAG", "base64编码的公钥：" + encodePublic);

        return encodePublic;
    }

    public String getPublicKey() {
        //获取公钥
        byte[] publicKey = getPublicKey(keyPair);
        //公钥用base64编码
        String encodePublic = Base64.encodeToString(publicKey, Base64.DEFAULT);
//        Log.d("TAG", "base64编码的公钥：" + encodePublic);

        return encodePublic;
    }

    public String getPrivateByKey() {
        //获取私钥
        byte[] privateKey = getPrivateKey(hashMap.get(urlKey));
        //私钥用base64编码
        String encodePrivate = Base64.encodeToString(privateKey, Base64.DEFAULT);
//        Log.d("TAG", "base64编码的私钥：" + encodePrivate);
        return encodePrivate;
    }

    public String getPrivateKey() {
        //获取私钥
        byte[] privateKey = getPrivateKey(keyPair);
        //私钥用base64编码
        String encodePrivate = Base64.encodeToString(privateKey, Base64.DEFAULT);
//        Log.d("TAG", "base64编码的私钥：" + encodePrivate);
        return encodePrivate;
    }


    /**
     * 生成密钥对，即公钥和私钥。key长度是512-2048，一般为1024
     */
    private static KeyPair generateRSAKeyPair(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
        kpg.initialize(keyLength);
        return kpg.genKeyPair();
    }

    /**
     * 获取公钥，打印为48-12613448136942-12272-122-913111503-126115048-12...等等一长串用-拼接的数字
     */
    private static byte[] getPublicKey(KeyPair keyPair) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return rsaPublicKey.getEncoded();
    }

    /**
     * 获取私钥，同上
     */
    private static byte[] getPrivateKey(KeyPair keyPair) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return rsaPrivateKey.getEncoded();
    }

    /**
     * 使用公钥加密
     *
     * @param data
     * @param base64PublicKey
     * @return
     * @throws Exception
     */
    public static String encryptWithBase64(String data, String base64PublicKey) {
        try {
            byte[] bytes = data.getBytes("UTF-8");
            byte[] publicKey = Base64.decode(base64PublicKey, Base64.DEFAULT);

            byte[] encrypted = encrypt(bytes, publicKey);
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 使用公钥加密
     */
    public static byte[] encrypt(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, pubKey);
        return cp.doFinal(data);
    }


    /**
     * 使用私钥解密
     *
     * @param base64Str        密文内容
     * @param base64PrivateKey 秘钥内容
     * @return
     * @throws Exception
     */
    public static String decryptWithBase64(String base64Str, String base64PrivateKey)   {
        byte[] encrypted = Base64.decode(base64Str, Base64.DEFAULT);//密文要先base64解密
        byte[] privateKey = Base64.decode(base64PrivateKey, Base64.DEFAULT);//秘钥要先base64解密
        try {
            return new String(decrypt(encrypted, privateKey), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥解密
     *
     * @param encrypted  密文内容
     * @param privateKey 秘钥内容
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encrypted, byte[] privateKey) throws Exception {
        // 得到私钥对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PrivateKey keyPrivate = kf.generatePrivate(keySpec);
        // 解密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.DECRYPT_MODE, keyPrivate);
        byte[] arr = cp.doFinal(encrypted);
        return arr;
    }


    private void test() {
        int keyLength = 1024;
        try {
            //生成密钥对
            KeyPair keyPair = generateRSAKeyPair(keyLength);

            //获取公钥
            byte[] publicKey = getPublicKey(keyPair);
            //公钥用base64编码
            String encodePublic = Base64.encodeToString(publicKey, Base64.DEFAULT);
            Log.d("TAG", "base64编码的公钥：" + encodePublic);

            //获取私钥
            byte[] privateKey = getPrivateKey(keyPair);
            //私钥用base64编码
            String encodePrivate = Base64.encodeToString(privateKey, Base64.DEFAULT);
            Log.d("TAG", "base64编码的私钥：" + encodePrivate);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
