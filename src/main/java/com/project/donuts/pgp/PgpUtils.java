package com.project.donuts.pgp;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.util.io.Streams;
import org.pgpainless.PGPainless;
import org.pgpainless.algorithm.CompressionAlgorithm;
import org.pgpainless.algorithm.KeyFlag;
import org.pgpainless.decryption_verification.ConsumerOptions;
import org.pgpainless.decryption_verification.DecryptionStream;
import org.pgpainless.encryption_signing.EncryptionOptions;
import org.pgpainless.encryption_signing.EncryptionStream;
import org.pgpainless.encryption_signing.ProducerOptions;
import org.pgpainless.encryption_signing.SigningOptions;
import org.pgpainless.key.generation.KeySpec;
import org.pgpainless.key.generation.type.ecc.EllipticCurve;
import org.pgpainless.key.generation.type.ecc.ecdh.ECDH;
import org.pgpainless.key.generation.type.ecc.ecdsa.ECDSA;
import org.pgpainless.key.generation.type.rsa.RSA;
import org.pgpainless.key.generation.type.rsa.RsaLength;
import org.pgpainless.key.protection.SecretKeyRingProtector;
import org.pgpainless.util.Passphrase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public class PgpUtils {

    public static PGPSecretKeyRing getSecretKeyRing(String asciiArmoredSecretKey) throws IOException {
        return PGPainless.readKeyRing().secretKeyRing(asciiArmoredSecretKey);
    }

    public static PGPPublicKeyRing getPublicKeyRing(String asciiArmoredPublicKey) throws IOException {
        return PGPainless.readKeyRing().publicKeyRing(asciiArmoredPublicKey);
    }

    public static PGPPublicKeyRing getPublicKeyRing(PGPSecretKeyRing secretKeyRing) {
        return PGPainless.extractCertificate(secretKeyRing);
    }

    public static PGPSecretKeyRing generateKeys(String userId, String secretKeyPassphrase) throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        return PGPainless.buildKeyRing()
                .setPrimaryKey(KeySpec.getBuilder(
                        RSA.withLength(RsaLength._8192),
                        KeyFlag.SIGN_DATA, KeyFlag.CERTIFY_OTHER))
                .addSubkey(
                        KeySpec.getBuilder(ECDSA.fromCurve(EllipticCurve._P256), KeyFlag.SIGN_DATA)
                                .overridePreferredCompressionAlgorithms(CompressionAlgorithm.ZLIB)
                ).addSubkey(
                        KeySpec.getBuilder(
                                ECDH.fromCurve(EllipticCurve._P256),
                                KeyFlag.ENCRYPT_COMMS, KeyFlag.ENCRYPT_STORAGE)
                )
                .addUserId(userId)
                .setPassphrase(Passphrase.fromPassword(secretKeyPassphrase))
                .build();
    }

    public static ByteArrayOutputStream encrypt(
            InputStream plaintextInputStream,
            PGPPublicKeyRing publicKey
    ) throws IOException, PGPException {
        ByteArrayOutputStream cipherTextOutputStream = new ByteArrayOutputStream();
        EncryptionStream encryptionStream = PGPainless.encryptAndOrSign()
                .onOutputStream(cipherTextOutputStream)
                .withOptions(
                        ProducerOptions.signAndEncrypt(
                                new EncryptionOptions()
                                        .addRecipient(publicKey),
                                new SigningOptions()
                        ).setAsciiArmor(true)
                );

        Streams.pipeAll(plaintextInputStream, encryptionStream);
        encryptionStream.close();
        return cipherTextOutputStream;
    }

    public static ByteArrayOutputStream decrypt(
            InputStream encryptedInputStream,
            PGPSecretKeyRing secretKey,
            String secretKeyPassphrase
    ) throws PGPException, IOException {
        ByteArrayOutputStream plaintextOutputStream = new ByteArrayOutputStream();
        DecryptionStream decryptionStream = PGPainless.decryptAndOrVerify()
                .onInputStream(encryptedInputStream)
                .withOptions(
                        new ConsumerOptions()
                                .addDecryptionKey(
                                        secretKey,
                                        SecretKeyRingProtector.unlockAnyKeyWith(Passphrase.fromPassword(secretKeyPassphrase))
                                )
                );

        Streams.pipeAll(decryptionStream, plaintextOutputStream);
        decryptionStream.close();

        return plaintextOutputStream;
    }
}
