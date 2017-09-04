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
        String signature = "IrJO4pIA8mhRKAHM7731FmZNDfBIcdce/4H9N22mldZZEWgfLcolc6xEIaEtHvCcZ1HVdL1KKeXp10FdwAq/3NVHY1RkoBMstUtdQQ9UgKnFovIkGhYm3u7N5G9YM7iivX3AOMiBSCBgTAVsan731e57LTc3Q1klWlSJwyqdwncYYvXv41Aov0UpnzE0UWgmJjmffbezHZOxiWP/dXZV5sCkYSvK5oe0WbLGsJzR8yFuByNCfrv6NDN6V45YvnicXJ1+CPnuR6cEngCxmQEYE0K668IaD3B2zblLiTb89b23ft/E8LaUsM2iNEgI7f1LuL7FzNW09KrPYgDFPFurHA==";
        String merchantId = "5981e8a4c9716c3dca30679d";
        String timestamp = "2017-08-30T13:57:48.726Z";
        String body = "{\"_id\":\"59a6c431f9285003bd46fe56\",\"status\":\"SUCCEEDED\"}";
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