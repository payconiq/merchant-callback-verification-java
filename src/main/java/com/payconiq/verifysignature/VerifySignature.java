package com.payconiq.verifysignature;


import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.util.zip.CRC32;

public class VerifySignature {
    public static void main(String[] args) {
        
        String signature = "RwdPmsA63xbTIa0MWDK3MOnibYfiObZAuZlJ/skvHenP6jSi5+UZSBG+NewwtoWQTnNV4BNHAtm65l6lX1/X1b7zdvavDipJyFr4fh/WKZmPV1g4RJfdMJALNgTO3E7Jp1uI2sji2fBR0+1FikOH2I32aZhCPqzTFP+jBY+18/pR6ZWq8n2TwdS12XLZPCo1Y6r+4VXmxuY0gzxzQ7ozmFbgES6E10mfXAL54Uk1IJ89vvYeV3PHK2VG0oYVzmoTWj4NLpflB3dJcVjG5k0DeGhwyovCPGIMtzxTCSKco7yIFwEh0c3/IXKPUcyMaJzQi3GwEPWC10uctngWDxt3/Q==";
        String merchantId = "569f8289c25c4103a3597ec8";
        String timestamp = "2019-04-23T12:24:57.451Z";
        String body = "{\"_id\":\"ddfc15cb007c5449082459dd\",\"status\":\"SUCCEEDED\",\"externalRefId\":\"1556021853839\"}";
        String alg = "SHA256WithRSA";

        
        String env = "dev";
        boolean isValid = isValidSignature(signature, merchantId, timestamp, body, alg, env);

        System.out.printf("isValid: %s", isValid);
    }


    private static boolean isValidSignature(String signature, String merchantId, String timestamp, String body, String algorithm, String env) {
        CRC32 crc32 = new CRC32();
        crc32.update(body.getBytes());

        String formattedSignature = String.format("%s|%s|%x", merchantId, timestamp, crc32.getValue());
        System.out.println(formattedSignature);
        try {
            PublicKey publicKey = getCertificateFromFile(env);
            Signature signatureAlgorithm = Signature.getInstance(algorithm);
            signatureAlgorithm.initVerify(publicKey);
            signatureAlgorithm.update(formattedSignature.getBytes());
            return signatureAlgorithm.verify(base64ToBytes(signature));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static byte[] base64ToBytes(String base64) {
        return DatatypeConverter.parseBase64Binary(base64);
    }

    private static PublicKey getCertificateFromFile(String env) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        InputStream certificateContent = VerifySignature.class.getResourceAsStream(String.format("/public_key-%s.txt", env));

        try (InputStream is = new BufferedInputStream(certificateContent)) {
            return CertificateFactory.getInstance("X.509").generateCertificate(is).getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
