import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Demo2 {


    public Demo2() {
        System.out.println("Demo2...");
    }

    public static class Demo3 extends Demo2 {
        public Demo3(){
            System.out.println("Demo3");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        new Demo3();
        Callable<Double> callable = new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                System.out.println("call call()...");
                Thread.sleep(10000L);
                return 3.14;
            }
        };
        FutureTask<Double> futureTask = new FutureTask<>(callable);
        new Thread(futureTask,"t1").start();
        System.out.println("等待结果.....");
        System.out.println(futureTask.get());
    }
}
