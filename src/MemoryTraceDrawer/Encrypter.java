package MemoryTraceDrawer;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class Encrypter
{
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static final byte[] SALT = ",o.uj}j\\aVB.@^[V_C3>=tWSCdTLq5=o{o(s".getBytes();

    /*
        Encrypts the data from the input file and writes to the output file
        @param inputFile: file to be read from
        @param outputFile: file to write to
     */
    public static void encrypt(File inputFile, File outputFile)
    {
        KeySpec spec = new PBEKeySpec("70,+qy{{O-\"0E^~l".toCharArray(), SALT, 65536, 256); // AES-256
        SecretKeyFactory f = null;
        try
        {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = f.generateSecret(spec).getEncoded();
            doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
        }
        catch (InvalidKeySpecException | NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

    /*
        Decrypts the data from the input file and writes to the output file
        @param inputFile: file to be read from
        @param outputFile: file to write to
     */
    public static void decrypt(File inputFile, File outputFile)
    {
        KeySpec spec = new PBEKeySpec("70,+qy{{O-\"0E^~l".toCharArray(), SALT, 65536, 256); // AES-256
        SecretKeyFactory f = null;
        try
        {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = f.generateSecret(spec).getEncoded();
            doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
        }
        catch (InvalidKeySpecException | NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

    /*
        Encrypts or Decrypts the data from inputFile and writes it to outputFile
        @param cipherMode: either ENCRYPT_MODE or DECRYPT_MODE
        @param key: the key used for the encryption
        @param inputFile: the file to read from
        @param outputFile: the file to write to
     */
    private static void doCrypto(int cipherMode, byte[] key, File inputFile, File outputFile)
    {
        try
        {
            Key secretKey = new SecretKeySpec(key, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
