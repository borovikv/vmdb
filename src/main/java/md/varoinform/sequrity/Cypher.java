package md.varoinform.sequrity;

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
        byte[] bytes = StringUtils.getBytesFromString(data);
        if (bytes == null) return null;

        bytes = ArrayUtils.addAll(bytes, getPadding(MAX_LENGTH - bytes.length));

        return xor(key, bytes);
    }

    private byte[] getPadding(int length) {
        SecureRandom random = new SecureRandom();
        byte[] padding = new byte[length];
        random.nextBytes(padding);

        padding[padding.length - 1] = (byte) length;
        return padding;
    }


    public String decrypt(byte[] encryptedData, byte[] key)  {
        byte[] bytes = xor(key, encryptedData);

        int padding_length = bytes[bytes.length-1];
        byte[] result = Arrays.copyOfRange(bytes, 0, bytes.length - padding_length);

        return new String(result);
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