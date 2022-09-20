package com.itdom.elasticsearch;

import com.itdom.elasticsearch.dao.ProductDao;
import com.itdom.elasticsearch.entity.Product;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataESProductDaoTest {

    @Resource
    private ProductDao productDao;

    /**
     *
     */
    @Test
    public void saveTest(){
        Product product = new Product();
        product.setId(System.currentTimeMillis());
        product.setCategory("手机");
        product.setImages("http://localhost:5601/app/kibana#/dev_tools/console");
        product.setPrice(4399.00);
        product.setTitle("太合金飞力手机");
        Product save = productDao.save(product);
        Assert.assertTrue(save==null);
    }

    @Test
    public void updateTest(){
        Product product = new Product();
        product.setId(1663423733291L);
        product.setCategory("手机");
        product.setImages("http://localhost:5601/app/kibana#/dev_tools/console");
        product.setPrice(5000.00);
        product.setTitle("太合金飞力手机");
        Product save = productDao.save(product);
        Assert.assertFalse(save==null);
    }
    @Test
    public void findByIdTest(){
        Optional<Product> optional = productDao.findById(1663423733291L);
        Product product = optional.get();
        System.out.println(product.toString());
        Assert.assertFalse(product==null);
    }
    @Test
    public void findAllTest(){
        Iterable<Product> products = productDao.findAll();
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
    }

    @Test
    public void deleteTest(){
        productDao.deleteById(1663423733291L);
    }
    @Test
    public void saveAllTest() throws InterruptedException {
        ArrayList<Product> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Product product = new Product();
            product.setId((System.currentTimeMillis()+1));
            product.setCategory("手机");
            product.setImages("http://localhost:5601/app/kibana#/dev_tools/console");
            product.setPrice((5000.00+Math.random()*100));
            product.setTitle((i%2==0?"太合金飞力手机":"苞米鸡"));
            Thread.sleep(1);
            list.add(product);
        }
        productDao.saveAll(list);
    }

    @Test
    public void findPageTest(){
        PageRequest pageRequest = PageRequest.of(10, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> page = productDao.findAll(pageRequest);
        List<Product> content = page.getContent();
        for (Product product : content) {
            System.out.println(product.toString());
        }
    }
    @Test
    public void termQueryTest(){
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "太合金飞力手机");
        Iterable<Product> products = productDao.search(matchQueryBuilder);
        for (Product product : products) {
            System.out.println(product.toString());
        }

    }
}
