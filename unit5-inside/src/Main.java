import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @file: Main.class
 * @author: Dusk
 * @since: 2019/1/9 20:40
 * @desc: 第五章”使用流“
 */
public class Main {

    // 此节继续用dish的学习

    static List<Dish> menu = Arrays.asList(new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH));

    public static void main(String[] args) {

        /**
         *  用谓词筛选，Stream Api 支持filter方法，接收一个谓词作为参数，并返回一个包括所有复合为此的元素流
         */

        List<Dish> vegetarianMenu = menu.stream().filter(Dish::isVegetarian).collect(Collectors.toList());

        /**
         *  Stream Api 支持一个叫distinct方法， 返回元素各异， 根据流所生成元素的hashCode和equals方法实现
         */

        List<Integer> numbers = Arrays.asList(1, 2, 3, 1, 3, 2, 4);
        numbers.stream().filter(i -> i %2 == 0).distinct().forEach(System.out::println);        // 筛选偶数没有重复

        /**
         *  流支持linit(n)方法， 该方法会返回一个不超过长度的流
         */

        List<Dish> dishes = menu.stream().filter(d->d.getCalories() > 300).limit(3).collect(Collectors.toList());
        // 选出热量超过300卡的头三道菜
        System.out.println(dishes);

        /**
         * skip(n)方法， 返回一个人掉了前n个元素的流，不足n个返回一个空流
         */

        List<Dish> dishes1 = menu.stream().filter(d->d.getCalories() > 300).skip(2).collect(Collectors.toList());

        System.out.println(dishes1);

        /**
         *  映射， 从对象中选择信息， map、flatMap
         *  流中每一个元素应用函数， 将其映射成一个新的元素
         */

        List<String> dishNames = menu.stream().map(Dish::getName).collect(Collectors.toList());

        // 返回菜单
        System.out.println(dishNames);

        List<Integer> dishNameLengths = menu.stream().map(Dish::getName).map(String::length).collect(Collectors.toList());

        // 名称长度
        System.out.println(dishNameLengths);

        /**
         * 流的扁平化， 返回一张列表中不同的字符
         */

        String[] words = new String[]{"Hello", "World"};
        List<String> w = Arrays.stream(words).map(word->word.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());

        // Arrays.stream方法可以接受一个数组并产生一个流, map(Arrays::stream)返回Stram<String>把每个单词转换成一个字母数组，然后把每个数组变成了一个独立的流
        // flatMap将各个生成流扁平化为单个流, 所有使用map(Arrays::stream)时生成的单个流都被合并起来，即扁平化为一个流
        System.out.println(w);

        /**
         *  测验5.2
         */
        List<Integer> pfList = Arrays.asList(1, 2, 3, 4, 5).stream().map(s->s*s).collect(Collectors.toList());
        System.out.println(pfList);

        /**
         *  返回对数
         */

        List<Integer> i = Arrays.asList(3, 4);

        List<Integer[]> list = Arrays.asList(1, 2, 3).stream().flatMap(a-> i.stream().map(b->new Integer[]{a, b})).collect(Collectors.toList());

        System.out.println(list);
    }
}
