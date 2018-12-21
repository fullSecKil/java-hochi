import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @file: Main.class
 * @author: Dusk
 * @since: 2018/12/19 23:29
 * @desc:
 */
public class Main {
    public static void main(String[] args) {

        List<Apple> appleList = Arrays.asList(new Apple("green", 150), new Apple("red", 180));
        Main m = new Main();
        /**
         * 方法引用谓词过滤
         */
//        System.out.println(m.filterApples(appleList, a -> "green".equals(a.getColor())));
        // m.filterApples(appleList, Apple::isHeavyApple);

        m.filterApples(appleList, new AppleGreenColorPredicate());
        List<Apple>  redApples = m.filterApples(appleList, a -> a.getWeight() > 150 && "red".equals(a.getColor()));
        System.out.println(redApples);
    }

    /**
     * Predicate java8 提供谓词接口
     * @param inventory
     * @param p
     * @return
     */
/*    List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p){

        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }*/

    // 迭代集合的, 逻辑与要应用到集合中每个元素的行为(策略)区分开了
    List<Apple> filterApples(List<Apple> inventory, ApplePredicate p){

        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }
}
