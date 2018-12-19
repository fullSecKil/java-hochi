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

        List<Apple> appleList = Arrays.asList(new Apple("green", 150));
        Main m = new Main();
        /**
         * 方法引用谓词过滤
         */
        System.out.println(m.filterApples(appleList, Apple::isGreenApple));
        // m.filterApples(appleList, Apple::isHeavyApple);
    }

    List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p){

        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }
}
