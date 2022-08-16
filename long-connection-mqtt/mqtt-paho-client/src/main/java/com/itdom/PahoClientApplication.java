package com.itdom;

import com.itdom.configuration.MqttConfig;
import com.itdom.enums.QosEnum;
import com.itdom.utils.EmqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class PahoClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(PahoClientApplication.class,args);
    }

    @Autowired
    private EmqttClient emqttClient;

    @Autowired
    private MqttConfig properties;

     @PostConstruct
    public void init(){
        //连接服务端
        emqttClient.connect(properties.getUsername(),properties.getPassword());
        //订阅一个主题
        emqttClient.subscribe("testtopic/#", QosEnum.QoS2);
        //开启一个新的线程 每隔5秒去向 testtopic/123
        new Thread(()->{
            while (true){
                emqttClient.publish("testtopic/123"," publish msg :"+ LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        QosEnum.QoS2,false);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

}
