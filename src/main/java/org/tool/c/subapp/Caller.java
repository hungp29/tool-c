package org.tool.c.subapp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Caller {

    public static void doWithRequest() throws IOException {
        String u1 = "https://api.checkin.bap.jp/api/rpc";
        String u2 = "https://api.identity.bap.jp/api/rpc";
        URL url = new URL(u2);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        Map<String, Object> data = new HashMap<>();
        data.put("action", "get_oauth_token");
        data.put("grantType", "password");
        data.put("grantData", Arrays.asList("hungp@bap.jp", "112qwaszx!"));
        String input = "{\"action\": \"get_oauth_token\", \"grantType\": \"password\", \"grantData\": [\"hungp@bap.jp\", \"112qwaszx!\"]}";

        JSONObject object = new JSONObject(data);
        System.out.println(object.toString());
        OutputStream os = conn.getOutputStream();
        os.write(object.toString().getBytes());
        os.flush();

//            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + conn.getResponseCode());
//            }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        conn.disconnect();
    }
}
