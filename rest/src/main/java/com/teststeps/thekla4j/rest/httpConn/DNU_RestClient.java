package com.teststeps.thekla4j.rest.httpConn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DNU_RestClient {

    public static void send() throws IOException {
        URL url = new URL("https://czcholstc001230.prg-dc.dhl.com:8444/lpsAuth/auth/registerEmployee?requestId=1583853261199");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("app-version", "1.0.0-SNAPSHOT");
        con.setRequestProperty("Device-Key", "Test-Device");
        con.setRequestProperty("Authorization", "Basic MjkxNjkzMDowMDAw");

        con.setDoOutput(true);

        String jsonInputString = "[\"DMAC - COURIER\"]";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
}