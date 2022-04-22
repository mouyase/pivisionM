package com.reiya.pixiv.util;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PixivOAuth {
    private static PixivOAuth mPixivOAuth = new PixivOAuth();
    private String mCodeVerifier, mChallenge;

    private PixivOAuth() {
        mCodeVerifier = "-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI";
        mChallenge = "usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0";
        create();
    }

    public static PixivOAuth getInstance() {
        return mPixivOAuth;
    }

    private void create() {
        mCodeVerifier = generateCodeVerifier();
        try {
            mChallenge = generateCodeChallange(mCodeVerifier);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            mCodeVerifier = "-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI";
            mChallenge = "usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0";
        }
    }

    public String getCodeVerifier() {
        String oldCodeVerifier = mCodeVerifier;
        create();
        return oldCodeVerifier;
    }

    public String getChallenge() {
        return mChallenge;
    }

    private static String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.encodeToString(codeVerifier, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
    }

    private static String generateCodeChallange(String codeVerifier) throws NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.encodeToString(digest, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
    }
}
