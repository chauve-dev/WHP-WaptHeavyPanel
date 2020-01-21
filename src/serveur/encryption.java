package serveur;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class encryption {



    public static Map<String,Object> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Map<String, Object> keys = new HashMap<String,Object>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
    }



    public static String decryptMessage(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }



    public static String encryptMessage(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }


    public static String publicKeyToString(PublicKey p) {

        byte[] publicKeyBytes = p.getEncoded();
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(publicKeyBytes);

    }

    public static PublicKey stringToPublicKey(String s) {

        BASE64Decoder decoder = new BASE64Decoder();

        byte[] c = null;
        KeyFactory keyFact = null;
        PublicKey returnKey = null;

        try {
            c = decoder.decodeBuffer(s);
            keyFact = KeyFactory.getInstance("RSA");
        } catch (Exception e) {
            System.out.println("Erreur dans la génération de clé");
            e.printStackTrace();
        }


        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(c);
        try {
            returnKey = keyFact.generatePublic(x509KeySpec);
        } catch (Exception e) {

            System.out.println("Erreur dans la génération de clé");
            e.printStackTrace();

        }

        return returnKey;

    }


}
