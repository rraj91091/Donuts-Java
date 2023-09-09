package com.project.donuts.pgp;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PgpUtilsTest {

    @Disabled("takes a long time to create keys, therefore disabled by default")
    @Test
    public void generatePgpKeys() throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        String userId = "helloWorld";
        String passphrase = "passphrase";
        PGPSecretKeyRing secretKeyRing = PgpUtils.generateKeys(userId, passphrase);
        PGPPublicKeyRing publicKeyRing = PgpUtils.getPublicKeyRing(secretKeyRing);

        String plaintext = "PLAINTEXT";
        ByteArrayOutputStream encrypted = PgpUtils.encrypt(new ByteArrayInputStream(plaintext.getBytes()), publicKeyRing);
        ByteArrayInputStream toDecrypt = new ByteArrayInputStream(encrypted.toByteArray());
        ByteArrayOutputStream decrypted = PgpUtils.decrypt(toDecrypt, secretKeyRing, passphrase);

        System.out.println("encrypted:\n $encrypted");
        System.out.println("decrypted: $decrypted");

        assertEquals(plaintext, decrypted.toString());
    }
}