package com.itdom;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j(topic = "c.TestBias")
public class TestBias {
    public static void main(String[] args) throws InterruptedException {
//        log.debug("hello {}","TestBias");
        Dog o = new Dog();
        String s = ClassLayout.parseInstance(o).toPrintable(o);
        log.info("class layout:{}",s);
        System.out.println("------------------------------------------------------------------------------------------");
//        Thread.sleep(4000);
        String s2 = ClassLayout.parseInstance(o).toPrintable(o);

        log.info("class layout:{}",s2);

        synchronized (o){
            String s3 = ClassLayout.parseInstance(o).toPrintable(o);

            log.info("class layout:{}",s3);
        }
        String s4 = ClassLayout.parseInstance(o).toPrintable(o);

        log.info("class layout:{}",s4);

    }
}
class  Dog{

}