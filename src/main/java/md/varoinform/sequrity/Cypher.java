package md.varoinform.sequrity;

import md.varoinform.sequrity.exception.CryptographyException;
import md.varoinform.util.StringConverter;
import org.apache.commons.lang.ArrayUtils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Cypher {
    private static final int MAX_LENGTH = 16;


    public byte[] createKey(String keyString){
        return getKeyDigest(keyString);
    }

    private byte[] getKeyDigest(String keyString)  {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try {
            messageDigest.update(keyString.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return messageDigest.digest();
    }


    public byte[] encrypt(String data, byte[] key) throws CryptographyException {
        try {
            byte[] bytes = StringConverter.getBytesFromString(data);
            if (bytes == null) return null;

            bytes = ArrayUtils.addAll(bytes, getPadding(MAX_LENGTH - bytes.length));

            return xor(key, bytes);
        } catch (Exception e){
            throw new CryptographyException(e);
        }
    }

    private byte[] getPadding(int length) {
        SecureRandom random = new SecureRandom();
        byte[] padding = new byte[length];
        random.nextBytes(padding);

        padding[padding.length - 1] = (byte) length;
        return padding;
    }


    public String decrypt(byte[] encryptedData, byte[] key)  throws CryptographyException {
        try {
            byte[] bytes = xor(key, encryptedData);

            int paddingLength = bytes[bytes.length - 1];
            byte[] result = Arrays.copyOfRange(bytes, 0, bytes.length - paddingLength);

            return new String(result);
        } catch (Exception e){
            throw new CryptographyException(e);
        }
    }


    private byte[] xor(byte[] key, byte[] bytes) {
        byte[] result = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            int i1 = bytes[i] & 0xff;
            int i2 = key[i % key.length] & 0xff;
            result[i] = (byte) (i1 ^ i2);
        }
        return result;
    }
}