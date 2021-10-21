package com.tobe.fishking.v2.utils;

import com.tobe.fishking.v2.addon.kisaseed.KISA_SEED_CBC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

/*
    암호화 클래스
 */
@Component
public class SeedUtils {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static byte[] pbszUserKey;
    private static byte[] pbszIV;

    public static String encrypt(String rawMessage) {
        Encoder encoder = Base64.getEncoder();

        byte[] message = rawMessage.getBytes(UTF_8);
        System.out.println(pbszUserKey);
        byte[] encryptedMessage = KISA_SEED_CBC.SEED_CBC_Encrypt(pbszUserKey, pbszIV, message, 0, message.length);

        return new String(encoder.encode(encryptedMessage), UTF_8);
    }

    public static String decrypt(String encryptedMessage) {
        Decoder decoder = Base64.getDecoder();

        byte[] message = decoder.decode(encryptedMessage);
        byte[] decryptedMessage = KISA_SEED_CBC.SEED_CBC_Decrypt(pbszUserKey, pbszIV, message, 0, message.length);

        return new String(decryptedMessage, UTF_8);
    }

    @Value("${encrypKey.key}")
    public void setPbszUserKey(String value) {
        pbszUserKey = value.getBytes();
    }

    @Value("${encrypKey.iv}")
    public void setPbszIV(String value) {
        pbszIV = value.getBytes();
    }
}
