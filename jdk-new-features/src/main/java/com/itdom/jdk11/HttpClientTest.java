package com.itdom.jdk11;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class HttpClientTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.baidu.com"))
                .GET()
                .build();
        var client = HttpClient.newHttpClient();
        //同步请求
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        //异步请求
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println);
        Scanner input = new Scanner(System.in);
        String val = null;       // 记录输入度的字符串
        do{
            System.out.print("请输入：");
            val = input.next();       // 等待输入值
            System.out.println("您输入的是："+val);
        }while(!val.equals("#"));   // 如果输入的值不版是#就继续输入
        System.out.println("你输入了\"#\"，程序已经退出！");
        input.close(); // 关闭资源
    }
}
