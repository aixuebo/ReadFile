package com.maming.common.util.encode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESFileEncryptor {

    // 生成随机密钥并保存到文件
    public static void generateAndSaveKey(String filePath, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        // 使用PBKDF2从密码派生密钥
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        
        // 将密钥和盐转换为Base64字符串
        String keyBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        
        // 保存到文件
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("KEY: " + keyBase64);//KEY: RHzYXx97q2v6cwqp+wrIEjKjUS3vXLIPGzvvj+y+2Fc=
            writer.println("SALT: " + saltBase64);//SALT: tn3VO7X4S3cHcp4WXLTuGQ==
        }
    }

    // 从文件加载密钥
    public static SecretKey loadKeyFromFile(String filePath, String password) throws Exception {
        String keyLine = null;
        String saltLine = null;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("KEY: ")) keyLine = line.substring(5);
                if (line.startsWith("SALT: ")) saltLine = line.substring(6);
            }
        }
        
        if (keyLine == null || saltLine == null) {
            throw new IOException("Invalid key file format");
        }
        
        // 解码Base64字符串
        byte[] keyBytes = Base64.getDecoder().decode(keyLine);
        byte[] salt = Base64.getDecoder().decode(saltLine);
        
        // 重新派生密钥（验证密码）
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    // 加密方法
    public static String encrypt(String plaintext, SecretKey secretKey,String outFilePath) throws Exception {
        // 生成随机初始化向量 (IV)
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 初始化加密器
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        // 执行加密
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // 组合 IV + 密文
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        String result = Base64.getEncoder().encodeToString(combined);

        // 保存到文件
        try (PrintWriter writer = new PrintWriter(outFilePath)) {
            writer.print(result);
            writer.flush();
        }

        return result;


    }

    public static String readFileToString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filePath),
                        StandardCharsets.UTF_8))) {

            char[] buffer = new char[8192]; // 8KB缓冲区
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, charsRead);
            }
        }
        return content.toString();
    }


    // 解密方法
    public static String decrypt(String ciphertext, SecretKey secretKey) throws Exception {
        byte[] combined = Base64.getDecoder().decode(ciphertext);

        // 分离 IV 和密文
        byte[] iv = new byte[16];
        byte[] encryptedBytes = new byte[combined.length - 16];
        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encryptedBytes, 0, encryptedBytes.length);

        // 初始化解密器
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // 执行解密
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    //根据password + 随机数加盐的方式，生成加密文件，后续编码都基于这个加密文件进行的
    public static void saveKeyFile(String keyFilePath,String password){
        try{
            generateAndSaveKey(keyFilePath, password);
            System.out.println("密钥已保存到: " + keyFilePath);

            // 查看密钥文件内容
            System.out.println("\n密钥文件内容:");
            try (BufferedReader reader = new BufferedReader(new FileReader(keyFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }


    public static void main(String[] args) {
        try {
            // 1. 生成并保存密钥到文件
            String keyFilePath = "/Users/Downloads/myscript.txt";
            String password = "MyStrongPassword123!"; // 用于保护密钥的密码

            String outFilePath = "/Users/Downloads/document1.txt";

            //测试密钥信息输出到文件里
            //saveKeyFile(keyFilePath,password);//有且只能在初始化的时候执行一次

            // 2. 从文件加载密钥
            SecretKey secretKey = loadKeyFromFile(keyFilePath, password);
            System.out.println("\n密钥加载成功!");


            // 3. 加密解密演示
            String originalText = "";

            // 加密
            String encrypted = encrypt(originalText, secretKey,outFilePath);
            System.out.println("加密结果: " + encrypted);

            String decrypted = decrypt(readFileToString(outFilePath), secretKey);
            System.out.println("解密结果: " + decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}