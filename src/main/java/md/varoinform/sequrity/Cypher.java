package md.varoinform.sequrity;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Cypher {
    String cipherType = "AES";

    public Key createKey(byte[]... keyParts) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            for (byte[] key : keyParts) {
                digest.update(key);
            }
            byte[] keyData = Arrays.copyOfRange(digest.digest(), 0, 16);

            return new SecretKeySpec(keyData, cipherType);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(byte[] encryptedData, Key key) throws CryptographyException {
        byte[] bytes = crypt(encryptedData, key, Cipher.DECRYPT_MODE);
        if (bytes == null) return null;
        return new String(bytes);
    }

    public byte[] encrypt(String data, Key key) throws CryptographyException {
        byte[] bytes = StringUtils.getBytesFromString(data);
        if (bytes == null) return null;
        return crypt(bytes, key, Cipher.ENCRYPT_MODE);
    }

    private byte[] crypt(byte[] data, Key key, int mode) throws CryptographyException {
        try {
            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(mode, key);
            return crypt(data, cipher);

        } catch (IOException | GeneralSecurityException e) {
            throw new CryptographyException(e);
        }
    }

    private byte[] crypt(byte[] in, Cipher cipher) throws IOException, GeneralSecurityException {
        int blockSize = cipher.getBlockSize();

        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBlock = new byte[blockSize];
        byte[] outBlock = new byte[outputSize];
        InputStream inputStream = new ByteArrayInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        boolean more = true;
        int inLength = 0;
        while (more) {
            inLength = inputStream.read(inBlock);
            if (inLength == blockSize) {
                int outLength = cipher.update(inBlock, 0, blockSize, outBlock);
                out.write(outBlock, 0, outLength);
            } else more = false;
        }
        if (inLength > 0) {
            outBlock = cipher.doFinal(inBlock, 0, inLength);
        } else {
            outBlock = cipher.doFinal();
        }
        out.write(outBlock);
        byte[] result = out.toByteArray();
        inputStream.close();
        out.close();
        return result;
    }

}