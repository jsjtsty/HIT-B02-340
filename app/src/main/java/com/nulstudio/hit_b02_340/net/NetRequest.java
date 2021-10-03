package com.nulstudio.hit_b02_340.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class NetRequest {
    private final URL url;

    public NetRequest(URL url) {
        this.url = url;
    }

    public String post(String data, int timeout) throws IOException {
        StringBuilder bs = new StringBuilder();
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
        OutputStreamWriter out = new OutputStreamWriter(
                connection.getOutputStream(), StandardCharsets.UTF_8);
        out.write(data);
        out.flush();
        out.close();
        connection.connect();
        InputStream is = connection.getInputStream();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String l = null;
        while ((l = buffer.readLine()) != null) {
            bs.append(l);
        }
        return bs.toString();
    }

    public String post(String data) throws IOException {
        return post(data, 15000);
    }

    public String get(String data, int timeout) throws IOException {
        StringBuilder bs = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
        OutputStreamWriter out = new OutputStreamWriter(
                connection.getOutputStream(), StandardCharsets.UTF_8);
        out.write(data);
        out.flush();
        out.close();
        connection.connect();
        InputStream is = connection.getInputStream();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String l = null;
        while ((l = buffer.readLine()) != null) {
            bs.append(l);
        }
        return bs.toString();
    }

    public String get(String data) throws IOException {
        return get(data, 15000);
    }
}