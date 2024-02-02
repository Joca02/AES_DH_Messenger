package program;

import java.io.Serializable;
import java.security.SecureRandom;

public class ClientKey implements Serializable {
    public String clientName;
    public int key;
    public ClientKey(String name)
    {
        key= new SecureRandom().nextInt(3,100);
        clientName=name;
    }
}