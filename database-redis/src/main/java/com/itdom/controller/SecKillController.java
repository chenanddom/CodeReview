package com.itdom.controller;

import com.itdom.entity.SecKillProduct;
import com.itdom.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order/seckill")
public class SecKillController {
    @Autowired
    private SeckillService seckillService;

    @PostMapping
    public Boolean secSkillProduct(@RequestBody SecKillProduct product){
//        return seckillService.secSkillProduct(product);
        return seckillService.secKillProductForLeaveBehind(product);

    }

    @GetMapping
    public String getInfo(){
        return "hello world";
    }



}
