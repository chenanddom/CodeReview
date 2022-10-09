package com.itdom.config;

/**
 * mq常量类
 *
 * @Date 10/8/2022 18:34:39
 */
public interface MqConstant {
    //消息延时-30min取消订单-回增库存 order（生产）与item（消费）

    //交换机 正常和死信交换机
    String NORMAL_EXCHANGE="normal.exchange";
    String DEAD_EXCHANGE="dead_exchange";

    //队列 正常和死信队列
    String NORMAL_QUEUE="normal.queue";
    String DEAD_QUEUE="dead.queue";

    //route key 正常交换机的bindingKey 死信交换机的bindingKey
    String NORMAL_ROUTE="normal.route";// 这里充当route key
    String DEAD_ROUTE="dead.route";

    // 过期时间设置 15秒
    Long TTL_TIME=15000L;
}