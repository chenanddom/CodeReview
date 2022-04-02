package com.itdom.controller;

import com.itdom.event.UserLoginEvent;
import com.itdom.utils.JasperReportUtil;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller("controller")
@RequestMapping("/jasper")
public class JasperReportController {
    @GetMapping("/getReport")
    public void getReport(@RequestParam("type") String reportType, HttpServletResponse response) throws Exception {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", " test head ");

        List<HashMap> list = new ArrayList<HashMap>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("username",  "测试-" + i);
            int age = random.nextInt(100);
            item.put("age", String.valueOf(age));
            list.add(item);
        }
        String jasperPath = JasperReportUtil.getJasperFileDir("jasper_1");
        if (reportType.equals("pdf")) {
            JasperReportUtil.exportToPdf(jasperPath, parameters, list, response);
        } else if (reportType.equals("html")) {
            JasperReportUtil.exportToHtml(jasperPath, parameters, list, response);
        }
    }
    @GetMapping("/getReport2")
    public void getReport2(@RequestParam("type") String reportType, HttpServletResponse response) throws Exception {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("headName", "表头啦");

        List<HashMap> list = new ArrayList<HashMap>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("order_name",  "订单名称-" + i);
            int age = random.nextInt(100);
            item.put("order_code", UUID.randomUUID().toString()+String.valueOf(age));
            list.add(item);
        }
        String jasperPath = JasperReportUtil.getJasperFileDir("train_1");
        if (reportType.equals("pdf")) {
            JasperReportUtil.exportToPdf(jasperPath, parameters, list, response);
        } else if (reportType.equals("html")) {
            JasperReportUtil.exportToHtml(jasperPath, parameters, list, response);
        }
    }

    @EventListener
    public void listenerUserLogin(UserLoginEvent source){
        System.out.println(source.getUsername());
    }
}
