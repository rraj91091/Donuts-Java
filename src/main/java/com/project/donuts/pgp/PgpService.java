package com.project.donuts.pgp;

import org.bouncycastle.openpgp.PGPException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PgpService {

    public String encrypt(String plaintext, String pgpPublicKey) throws IOException, PGPException {
        ByteArrayOutputStream ciphertext = PgpUtils.encrypt(
                new ByteArrayInputStream(plaintext.getBytes()),
                PgpUtils.getPublicKeyRing(pgpPublicKey)
        );
        return ciphertext.toString();
    }

    public String decrypt(String ciphertext, String pgpPrivateKey, String pgpPrivateKeyPassphrase) throws IOException, PGPException {
        ByteArrayOutputStream plaintext = PgpUtils.decrypt(
                new ByteArrayInputStream(ciphertext.getBytes()),
                PgpUtils.getSecretKeyRing(pgpPrivateKey),
                pgpPrivateKeyPassphrase
        );
        return plaintext.toString();
    }

}
