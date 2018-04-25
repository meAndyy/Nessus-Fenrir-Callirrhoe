package com.example.controllers;

/**
 * Created by Andy on 24/01/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.andy.nessus_fenrir_callirrhoe.NFCActivity;
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
    public boolean sendData(String email, String msgbdy) {

        try {
            String curremail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String jsonResponse;
            DatabaseController dbc = new DatabaseController();
            String senderid = dbc.getSenderid();

            String message = "APOM from"+curremail+": "+ msgbdy;
            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic NDhjZGY1NTEtOGUxZC00ZmY0LTlhZjktYjlkMzYxMjBhNjli");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"47cc7bc4-8d85-4368-b00c-02d4341db977\","
                    +   "\"filters\": [{\"field\": \"tag\", \"key\": \"UserID\", \"relation\": \"=\", \"value\": \""+email+"\"}],"
                    +   "\"data\": {\"foo\": \""+senderid+"\"},"
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
        return true;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
