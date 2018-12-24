import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

import static java.util.Comparator.comparing;

/**
 * @file: Main.class
 * @author: Dusk
 * @since: 2018/12/23 14:42
 * @desc:
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Main m = new Main();

        System.out.println(m.processFile(b->b.readLine()+b.readLine()));

        List<String> list = Arrays.asList("Acc", "vB", "C%%%%");

        System.out.println("a".isEmpty());

        System.out.println(m.map(list, String::length));

        // 原始类型特化，避免装箱解箱
        IntPredicate evenNumbers = i -> i%2 == 0;       // 无装箱

        Predicate<Integer> evenNumbers2 = (Integer i) -> i%2 == 1;      //装箱
        System.out.println(evenNumbers.test(1000));

        // 类型推断，上下文推断类型

        // 方法引用, 管中窥豹
        List<Apple> appleList = Arrays.asList(new Apple("green", 190), new Apple("green", 150), new Apple("red", 180));
        // 排序
        appleList.sort(comparing(Apple::getWeight));
        System.out.println(appleList);
        // 字符串截取
        System.out.println("xuerui".substring(4));
        BiFunction<String, Integer, String> subString1 = String::substring;
        String name = subString1.apply("xuerui", 4);
        System.out.println(name);

        // 方法引用1类 指向静态方法的方法引用
        System.out.println(Integer.parseInt("12345"));
        Function<String, Integer> parse = String::length;
        parse.apply("564353");

        // 方法引用2类 任意类型实例方法
        Function<String, Integer> rstLen = String::length;
        rstLen.apply("xueruizuishuai");

        // 方法引用3类 现有对象的实例方法引用
        Function<Apple, Integer> appGetWeight = Apple::getWeight;
        appGetWeight.apply(new Apple("red", 156));

    }

    public String processFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return br.readLine();
        }
    }

    // 函数式接口 传递行为

    public String processFile(BufferedReaderProcessor p) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            // 处理bufferedreader对象
            return p.process(br);
        }

    }

    // Function 信息映射
    public <T, R> List<R> map(List<T> list, Function<T, R> f){
        List<R> result = new ArrayList<>();
        for (T s: list){
            result.add(f.apply(s));
        }
        return result;
    }
}
