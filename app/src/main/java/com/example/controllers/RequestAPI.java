package com.example.andy.nessus_fenrir_callirrhoe;

/**
 * Created by Andy on 24/01/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Andy on 17/11/2017.
 */

public class RequestAPI extends BroadcastReceiver {

    public RequestAPI(){


    }
    public void sendData(String email) {

        try {
            String curremail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String jsonResponse;
            String message = "Your friend "+curremail+" has tapped in!";
            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic NDhjZGY1NTEtOGUxZC00ZmY0LTlhZjktYjlkMzYxMjBhNjli");
            con.setRequestMethod("POST");

            /*String strJsonBody = "{"
                    +   "\"app_id\": \"47cc7bc4-8d85-4368-b00c-02d4341db977\","
                    +   "\"include_player_ids\": [\"a6843498-cd7b-433c-a740-af3c15af6e3f\",\"9\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"en\": \"Your friend has tapped in!" +
                    "\"}"
                    + "}";*/

            String strJsonBody = "{"
                    +   "\"app_id\": \"47cc7bc4-8d85-4368-b00c-02d4341db977\","
                    +   "\"filters\": [{\"field\": \"tag\", \"key\": \"UserID\", \"relation\": \"=\", \"value\": \""+email+"\"}],"
                    +   "\"data\": {\"foo\": \""+email+"\"},"
                    +   "\"contents\": {\"en\": \""+message+"\"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }


    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
