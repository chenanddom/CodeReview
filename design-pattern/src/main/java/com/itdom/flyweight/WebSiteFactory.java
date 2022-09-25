package com.itdom.flyweight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  FlyweightFactory: 享元工厂类
 */
public class WebSiteFactory {
    // 集合，充当池的作用
    private Map<String, ConcreteWebSite> pool = new ConcurrentHashMap<>();

    // 根据网站的类型，返回一个网站，如果没有就创建一个网站，并放入池中，并返回
    public WebSite getWebSiteCategory(String type) {
        if (!pool.containsKey( type )) {
            // 就创建一个网站，并放入池中
            pool.put( type, new ConcreteWebSite( type ) );
        }
        return (WebSite) pool.get( type );
    }

    // 获取网站分类的总数(池中有多少个网站类型)
    public int getWebSiteCount() {
        return pool.size();
    }
}
