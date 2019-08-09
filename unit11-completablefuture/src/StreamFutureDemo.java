import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * StreamFutureDemo
 *
 * @author dusk
 * @since 2019/8/7
 */
public class StreamFutureDemo {

    private String name;

    public StreamFutureDemo(String name) {
        this.name = name;
    }

    static List<StreamFutureDemo> shops = Arrays.asList(new StreamFutureDemo("BestPrice"),
            new StreamFutureDemo("LetsSaveBig"),
            new StreamFutureDemo("MyFavoriteShop"),
            new StreamFutureDemo("BuyItAll"),
            new StreamFutureDemo("ShopEasy"),
            new StreamFutureDemo("ShopEasy2"),
            new StreamFutureDemo("ShopEasy3"),
            new StreamFutureDemo("ShopEasy4"),
            new StreamFutureDemo("ShopEasy5"),
            new StreamFutureDemo("ShopEasy6"),
            new StreamFutureDemo("ShopEasy7"),
            new StreamFutureDemo("ShopEasy8"),
            new StreamFutureDemo("ShopEasy9")
    );


    static Random random = new Random(36);

    public String getPrice(String product) {
        double price = CompletableFutureTask.calculatePrice(product);
        Discount.Code code = Discount.Code.values()[
                random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", this.name, price, code);
    }

//    public static void main(String[] args) {
//        System.out.println(getPrice("ab cd ef ge ggg fff"));
//    }
}
