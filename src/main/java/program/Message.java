package program;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Message implements Serializable {
    private BigInteger p;
    private BigInteger q;

    private BigInteger sharedKey;  //ovim kljucem vrsim enc/dec

    public String message;

    private String sender;
    public  Map<String, Integer> clientsKeys;
    public Message(String message,String sender) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.message=message;
        this.sender=sender;
        p=BigInteger.probablePrime(128,new Random());
        q=p;
        while (q.compareTo(p) !=-1)
        {
            q=BigInteger.probablePrime(128,new Random());
        }
    }

    public void sendPublicKeys(Map<String, Integer> keys)
    {
        clientsKeys=keys;
    }
    private BigInteger generateSharedKeyWithDH()
    {
        if(Server.clientsKeys!=null && !Server.clientsKeys.isEmpty())
            clientsKeys=Server.clientsKeys;
        if(sender.equals("Bob"))
        {
            BigInteger a=q.modPow(BigInteger.valueOf(clientsKeys.get("Alice")),p);
            return a.modPow(BigInteger.valueOf(clientsKeys.get(sender)),p);
        }
        else if(sender.equals("Alice"))
        {
            BigInteger b=q.modPow(BigInteger.valueOf(clientsKeys.get("Bob")),p);
            return b.modPow(BigInteger.valueOf(clientsKeys.get(sender)),p);
        }
        return  BigInteger.valueOf(1);
    }


    public String encrypt() {
        sharedKey = generateSharedKeyWithDH();
        byte[] sharedKeyBytes = sharedKey.toByteArray();
        byte[] keyBytes = new byte[16];
        System.arraycopy(sharedKeyBytes, 0, keyBytes, 0, Math.min(sharedKeyBytes.length, keyBytes.length));

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            if(Base64.getEncoder().encodeToString(encryptedBytes).equals(message))
                System.out.println("?????????");
            else System.out.println(Base64.getEncoder().encodeToString(encryptedBytes));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void decrypt() {
        try {
            sharedKey = generateSharedKeyWithDH();
            String ciphertext = message;
            byte[] sharedKeyBytes = sharedKey.toByteArray();
            byte[] keyBytes = new byte[16];
            System.arraycopy(sharedKeyBytes, 0, keyBytes, 0, Math.min(sharedKeyBytes.length, keyBytes.length));

            byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            message = new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "["+sender+"]: "+message;
    }
}


