package com.itdom.flyweight;

/**
 *
 */
public class FlyWeightTest {

    public static void main(String[] args) {
        WebSiteFactory webSiteFactory = new WebSiteFactory();
        WebSite news = webSiteFactory.getWebSiteCategory("热榜");
        news.use(new User("zhangsan"));
        WebSite blog = webSiteFactory.getWebSiteCategory("博客");
        blog.use(new User("zhangsan"));
        System.out.println("网站的分类共有:"+webSiteFactory.getWebSiteCount());
    }
}
