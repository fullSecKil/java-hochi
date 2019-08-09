import sun.misc.ThreadGroupUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * CompletableFutureTask
 *
 * @author dusk
 * @since 2019/8/7
 */
public class CompletableFutureTask {

    static List<CompletableFutureTask> shops = Arrays.asList(new CompletableFutureTask("BestPrice"),
            new CompletableFutureTask("LetsSaveBig"),
            new CompletableFutureTask("MyFavoriteShop"),
            new CompletableFutureTask("BuyItAll"),
            new CompletableFutureTask("ShopEasy"),
            new CompletableFutureTask("ShopEasy2"),
            new CompletableFutureTask("ShopEasy3"),
            new CompletableFutureTask("ShopEasy4"),
            new CompletableFutureTask("ShopEasy5"),
            new CompletableFutureTask("ShopEasy6"),
            new CompletableFutureTask("ShopEasy7"),
            new CompletableFutureTask("ShopEasy8"),
            new CompletableFutureTask("ShopEasy9"));

    /**
     * 为“最优价格查询器”应用定制的执行器
     */
    private static Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            // 使用守护线程
            t.setDaemon(true);
            return t;
        }
    });

    static Random random = new Random(36);

    private String name;

    public String getName() {
        return name;
    }

    public CompletableFutureTask(String name) {
        this.name = name;
    }

    public static void main(String[] args) {

        long start = System.nanoTime();
        Future<Double> futurePrice = getPriceAsync("my favorite product");
        long invocationTIme = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTIme + " msecs");
        try {
            double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        long retrievalTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Price returned after " + retrievalTime + " msecs");

        //
        // 顺序流
        //
        long start0 = System.nanoTime();
        List<String> streamPrices = findPricesForStream("my favorite product");
        long retrievalTime0 = (System.nanoTime() - start0) / 1_000_000;
        System.out.println(streamPrices);
        System.out.println(retrievalTime0);
        //
        // 并行流操作
        //
        long start1 = System.nanoTime();
        List<String> streamParallelPrices = findPricesForParallelStream("my favorite product");
        long retrievalTime1 = (System.nanoTime() - start1) / 1_000_000;
        System.out.println(streamParallelPrices);
        System.out.println(retrievalTime1);
        //
        //list CompletableFuture test
        //
        //CompletableFuture版本的程序与并行流版本不相伯仲
        //究其原因都一样：它们内部
        //采用的是同样的通用线程池，默认都使用固定数目的线程
        //CompletableFuture具有一定的
        //优势，因为它允许你对执行器（Executor）进行配置，尤其是线程池的大小，让它以更适合应用需求的方式进行配置，满足程序的要求，而这是并行流API无法提供的
        long start2 = System.nanoTime();
        List<String> fPrices = findPrices("my favorite product");
        long retrievalTime2 = (System.nanoTime() - start2) / 1_000_000;
        System.out.println(fPrices);
        System.out.println(retrievalTime2);
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    /**
     * 使用顺序流并行地从不同的商店获取价格
     *
     * @param product
     * @return
     */
    public static List<String> findPricesForStream(String product) {
        return shops.stream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))).collect(Collectors.toList());
    }

    /**
     * 使用并行流并行地从不同的商店获取价格
     *
     * @param product
     * @return
     */
    public static List<String> findPricesForParallelStream(String product) {
        return shops.parallelStream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))).collect(Collectors.toList());
    }

    /**
     * “最佳价格查询器”应用, 查询的所有商店
     *
     * @param product
     * @return
     */
    public static List<String> findPrices(String product) {
        // 使用CompletableFuture 以异步方式计算每一种商品的价格
        List<CompletableFuture<String>> priceFutures = shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)), executor)).collect(Collectors.toList());

        // 等待所有异步操作结束
        return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    /**
     * 获取产品价格
     *
     * @param product
     * @return
     */
    public static Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                future.complete(price);
            } catch (Exception e) {
                // 使用CompletableFuture的completeExceptionally方法将导致CompletableFuture内发生问题的异常抛出
                future.completeExceptionally(e);
            }
        }).start();
        return future;
    }

    /**
     * 睡眠
     */
    public static void delay() {
        try {
            // TimeUnit.SECONDS.sleep(1);
            // 随机生成时间
            Thread.sleep(random.nextInt(2700));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据产品名随机产生价格
     *
     * @param product
     * @return
     */
    public static double calculatePrice(String product) {
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /**
     * 使用工厂方法supplyAsync创建CompletableFuture
     *
     * @param product
     * @return
     */
    public Future<Double> getPriceAsync2(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }
}
