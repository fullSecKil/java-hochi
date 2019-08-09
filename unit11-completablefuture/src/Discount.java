import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Discount
 *
 * @author dusk
 * @since 2019/8/8
 */
public class Discount {

    /**
     * 为“最优价格查询器”应用定制的执行器
     */
    private static Executor executor = Executors.newFixedThreadPool(Math.min(StreamFutureDemo.shops.size(), 100), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            // 使用守护线程
            t.setDaemon(true);
            return t;
        }
    });

    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " + Discount.apply(quote.getPrice(), quote.getDiscountCode());
    }

    private static String apply(double price, Code discountCode) {
        CompletableFutureTask.delay();
        return String.format("%.2f", price * (100 - discountCode.percentage) / 100);
    }

    public List<String> findPrices(String product) {
        List<CompletableFuture<String>> priceFutures = StreamFutureDemo.shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(futrue -> futrue.thenCompose(quote -> CompletableFuture.supplyAsync(
                        () -> Discount.applyDiscount(quote), executor
                ))).collect(Collectors.toList());
        return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
//        return StreamFutureDemo.shops.parallelStream().map(shop -> shop.getPrice(product))      // 取得每个shop对象 中商品原始价格
//                .map(Quote::parse)                                                                                                 //
//                .map(Discount::applyDiscount).collect(toList());
    }

//    public void findPriceRate(String product) {
//        StreamFutureDemo shop = StreamFutureDemo.shops.get(0);
//        Future<Double> futurePriceInUSD = CompletableFuture.supplyAsync(() -> shop.getPrice(product)).thenCombine(
//                CompletableFuture.supplyAsync(
//                        () -> rate(),
//                        (price, rate) -> price * rate
//                )
//        );
//    }

    public static Stream<CompletableFuture<String>> findPricesStream(String product) {
        return StreamFutureDemo.shops.stream().map(shop -> CompletableFuture.supplyAsync(
                () -> shop.getPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)));
    }

    public static double rate() {
        return 0.8;
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
//        Discount discount = new Discount();
//        System.out.println(discount.findPrices("asvc"));

        CompletableFuture[] futures = findPricesStream("myPhone").map(f -> f.thenAccept(s -> System.out.printf("%s (done in %d msecs)\r\n", s, (System.nanoTime() - start) / 1_000_000))).toArray(CompletableFuture[]::new);
        // randomDelay 模拟远程方法调用，产生一个介于0.5秒到2.5秒的随机延迟
        // 执行代码不同价格不再像之前那样总是在一个时刻
        //返回，而是随着商店折扣价格返回的顺序逐一地打印输出
        CompletableFuture.allOf(futures).join();
        
        // CompletableFuture对象数组中有任何一个执行完毕就不再等待
        CompletableFuture.anyOf(futures).join();

        System.out.println((System.nanoTime() - start) / 1_000_000);
    }
}
