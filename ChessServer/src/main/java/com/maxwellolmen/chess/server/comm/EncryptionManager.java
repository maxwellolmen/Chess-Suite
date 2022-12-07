package com.maxwellolmen.chess.server.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionManager {

    private DataInputStream in;
    private DataOutputStream out;

    private Cipher encrypt;

    public EncryptionManager(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    private SecretKeySpec secretKey;

    public boolean handshake() throws Exception {
        int length = in.readInt();

        if (length <= 0) return false;

        byte[] serverKeyEncoded = new byte[length];
        in.readFully(serverKeyEncoded, 0, length);
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(serverKeyEncoded);
        PublicKey publicKey = keyFactory.generatePublic(x509);
        DHParameterSpec dhParameters = ((DHPublicKey) publicKey).getParams();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(dhParameters);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(keyPair.getPrivate());
        byte[] publicKeyEncoded = keyPair.getPublic().getEncoded();
        out.writeInt(publicKeyEncoded.length);
        out.write(publicKeyEncoded);
        keyAgreement.doPhase(publicKey, true);
        int l = in.readInt();
        byte[] sharedSecret = new byte[l];
        keyAgreement.generateSecret(sharedSecret, 0);
        this.secretKey = new SecretKeySpec(sharedSecret, 0, 16, "AES");
        return true;
    }

    public void sendEncryptedData(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, this.secretKey);
        byte[] cipherData = cipher.doFinal(data);
        byte[] iv = cipher.getParameters().getEncoded();
        sendData(cipherData);
        sendData(iv);
    }

    public void sendData(byte[] data) throws IOException {
        out.writeInt(data.length);
        out.write(data);
    }

    public void sendEncryptedString(String string) throws Exception {
        sendEncryptedData(string.getBytes());
    }

    public byte[] interpretEncryptedData(byte[] data, byte[] iv) throws Exception {
        AlgorithmParameters aesParams = AlgorithmParameters.getInstance("AES");
        aesParams.init(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, this.secretKey, aesParams);
        return cipher.doFinal(data);
    }
}


