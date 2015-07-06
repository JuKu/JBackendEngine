package com.jukusoft.jbackendengine.backendengine.encryption;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by Justin on 26.01.2015.
 */
public class HashUtils {

    public static final int NO_OPTIONS = 0;

    private static String convertToHex(byte[] data) throws IOException
    {
        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex= Base64.getEncoder().encodeToString(data);
        sb.append(hex);

        return sb.toString();
    }

    public static String computeSHAHash(String password)
    {
        MessageDigest mdSha1 = null;
        String SHAHash = "";

        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        try {
            SHAHash = convertToHex(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return SHAHash;
    }


    public static String computeMD5Hash(String password)
    {
        StringBuffer MD5Hash = new StringBuffer();

        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return MD5Hash.toString();
    }

}
