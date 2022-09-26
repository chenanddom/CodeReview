package com.itdom.service;

import com.itdom.entity.SecKillProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SeckillService {
    private static final Logger logger = LoggerFactory.getLogger(SeckillService.class);
    static String scriptStr = "local userid=KEYS[1];\n" +
            "local prodid=KEYS[2];\n" +
            "local qtkey=\"sku:\"..prodid..\":product\";\n" +
            "local userskey=\"userId:\"..prodid..\":user\";\n" +
            "local userExists=redis.call(\"sismember\",userskey,userid);" +
            "if tonumber(userExists)==1 then\n" +
            "  return 2;\n" +
            "end\n" +
            "local num=redis.call(\"get\",qtkey);\n" +
            "if tonumber(num)<=0 then\n" +
            "  return 0;\n" +
            "else\n" +
            "  redis.call(\"decr\",qtkey);\n" +
            "  redis.call(\"sadd\",userskey,userid);\n" +
            "  end\n" +
            "  return 1";

    @Autowired
    private JedisPool pool;

    /**
     * 使用lua脚本实现遗留问题
     * @param product
     * @return
     */
    public Boolean secKillProductForLeaveBehind(SecKillProduct product) {
        product.setProductName("飞科剃须刀");
        product.setProductNumber("1001");
        product.setUserId(UUID.randomUUID().toString());

        String userid = product.getUserId();
        String prodid = product.getProductNumber();

        Jedis jedis = pool.getResource();
        String sha1 = jedis.scriptLoad(scriptStr);
        Object result = jedis.evalsha(sha1, 2, userid, prodid);
        String reString = String.valueOf(result);
        if ("0".equals(reString)) {
            logger.debug("已抢空！");
            jedis.close();
            return false;
        } else if ("1".equals(reString)) {
            logger.debug("抢购成功");
            jedis.close();
            return true;
        } else if ("2".equals(reString)) {
            logger.debug("该用户已抢过！");
            jedis.close();
            return false;
        } else {
            logger.debug("抢购异常");
            jedis.close();
            return false;
        }
    }

    public Boolean secSkillProduct(SecKillProduct product) {
        product.setProductName("飞科剃须刀");
        product.setProductNumber("1001");
        product.setUserId(UUID.randomUUID().toString());
        Jedis jedis = pool.getResource();
        String kcKey = "sku:" + product.getProductNumber() + ":product";
        String userKey = "userId:" + product.getProductNumber() + ":user";
        try {
            //建时库存‘
            jedis.watch(kcKey);

            String num = jedis.get(kcKey);
            Integer number = Integer.valueOf(num);
            if (number == null || number <= 0) {
                logger.debug("秒杀结束了");
                jedis.close();
                return false;
            }
            boolean isExist = jedis.sismember(userKey, product.getUserId());
            if (isExist) {
                logger.debug("你已经秒杀过了，不可以再次参与秒杀!!!");
                jedis.close();
                return false;
            }
            Transaction multi = jedis.multi();

            multi.decr(kcKey);
            multi.sadd(userKey, product.getUserId());
            List<Object> result = multi.exec();
            if (CollectionUtils.isEmpty(result)) {
                logger.debug("秒杀失败");
                jedis.close();
                return false;
            }
            jedis.close();
            return true;
        } catch (Exception e) {
            jedis.close();
            return false;
        }
    }

    public static void main(String[] args) {
//        System.out.printf(scriptStr);
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.0.199",6379));
        nodes.add(new HostAndPort("192.168.0.199",6380));
        nodes.add(new HostAndPort("192.168.0.199",6381));
        nodes.add(new HostAndPort("192.168.0.199",6389));
        nodes.add(new HostAndPort("192.168.0.199",6390));
        nodes.add(new HostAndPort("192.168.0.199",6391));
        DefaultJedisClientConfig.Builder builder = DefaultJedisClientConfig.builder();
        builder.password("root@123");
        JedisCluster jedisCluster = new JedisCluster(nodes,builder.build());
        for (int i = 2; i <=1000 ; i++) {
        jedisCluster.set("key-00"+i,"v-00"+i);

        }
        System.out.println(jedisCluster.get("key-001"));

    }

}
