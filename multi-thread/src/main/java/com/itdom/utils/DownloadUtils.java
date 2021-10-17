package com.itdom.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class DownloadUtils {

public static String download() throws IOException {
    StringBuilder sb = new StringBuilder();
    URL url = new URL("http://www.baidu.com");
    HttpURLConnection conn =(HttpURLConnection)url.openConnection();
    InputStream inputStream = conn.getInputStream();
    InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
    BufferedReader reader = new BufferedReader(isr);
    String line = null;
    while ((line=reader.readLine())!=null){
        sb.append(line+"\r\n");
    }
    return sb.toString();
}
}
