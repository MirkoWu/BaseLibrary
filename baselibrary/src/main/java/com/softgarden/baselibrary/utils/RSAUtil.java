package com.softgarden.baselibrary.utils;

import android.util.Base64;
import android.util.Log;

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

import javax.crypto.Cipher;

/**
 * RSA 加密算法工具
 */
public class RSAUtil {

    private static final String RSA = "RSA";
    private static final String TRANSFORMATION = "RSA/None/PKCS1Padding";
    private KeyPair keyPair;
    private int keyLength = 1024;

    public RSAUtil() {
        try {
            keyPair = generateRSAKeyPair(keyLength);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPublic() {
        //获取公钥
        byte[] publicKey = getPublicKey(keyPair);
        //公钥用base64编码
        String encodePublic = Base64.encodeToString(publicKey, Base64.DEFAULT);
        Log.d("TAG", "base64编码的公钥：" + encodePublic);
        return encodePublic;
    }

    public String getPrivate() {
        //获取私钥
        byte[] privateKey = getPrivateKey(keyPair);
        //私钥用base64编码
        String encodePrivate = Base64.encodeToString(privateKey, Base64.DEFAULT);
        Log.d("TAG", "base64编码的私钥：" + encodePrivate);
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
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) throws Exception {
        // 得到公钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, pubKey);
        return cp.doFinal(data);
    }

    /**
     * 使用私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] encrypted, byte[] privateKey) throws Exception {
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

}
