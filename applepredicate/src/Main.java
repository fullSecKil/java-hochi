import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

        List<Apple> appleList = Arrays.asList(new Apple("green", 190), new Apple("green", 150), new Apple("red", 180));
        Main m = new Main();
        /**
         * 方法引用谓词过滤
         */
//        System.out.println(m.filterApples(appleList, a -> "green".equals(a.getColor())));
        // m.filterApples(appleList, Apple::isHeavyApple);

        m.filterApples(appleList, new AppleGreenColorPredicate());

        // lambda 表达式
        List<Apple>  redApples = m.filterApples(appleList, a -> a.getWeight() > 150 && "red".equals(a.getColor()));
        System.out.println(redApples);

        // 匿名类
        List<Apple> redApples2 = m.filterApples(appleList, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return "red".equals(apple.getColor());
            }
        });
        System.out.println(redApples2);

        // 更加灵活，简洁
        List<Apple> redApples3 = m.filter(appleList, a->a.getWeight()<151);
        System.out.println(redApples3);

        List<Integer> eventNumbers = m.filter(Arrays.asList(1, 2, 3, 4), i-> i%2 == 0);
        System.out.println(eventNumbers);

        // comparator(比较器) 排序
        // 在Java 8中，List自带了一个sort(分类)方法。sort的行为可以用java.util.Comparator对象来参数化

        // 匿名类, 重量排序
        appleList.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });
        System.out.println(appleList);

        // 颜色排序
        appleList.sort((a1, a2)->a1.getColor().compareTo(a2.getColor()));
        System.out.println(appleList);

        // Runable 线程代码块
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world");
            }
        });

        t.start();

        Thread t2 = new Thread(()-> System.out.println("Hello world"));

        t2.start();

        new Thread(()-> System.out.println("Hello world")).start();

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

    // 将List类型抽象化, 泛型T使用任何类型对象

    <T> List<T> filter(List<T> list, Predicate<T> p){
        List<T> result = new ArrayList<>();
        for (T e: list){
            if (p.test(e)){
                result.add(e);
            }
        }
        return result;
    }
}
