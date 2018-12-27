import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

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
        Apple redApple = new Apple("red", 215);
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
        Function<String, Integer> parse = Integer::parseInt;
        parse.apply("564353");

        // 方法引用2类 任意类型实例方法
        Function<String, Integer> rstLen = String::length;
        rstLen.apply("xueruizuishuai");
        Function<Apple, Integer> appGetWeight = Apple::getWeight;
        appGetWeight.apply(new Apple("red", 156));

        // 方法引用3类 现有对象的实例方法引用
        // supplier生产者， consumer消费者
        Supplier<String> color = redApple::getColor;
        color.get();
        System.out.println(color.get());

        List<String> str = Arrays.asList("a", "b", "A", "B");
        // 忽略大小写排序
        str.sort((s1, s2)->s1.compareToIgnoreCase(s2));

        str.sort(String::compareToIgnoreCase);

        // 包含返回谓词
        BiPredicate<List<String>, String> contains = List::contains;
        Boolean c = contains.test(Arrays.asList("wozuishuai", "xuerui"), "xuerui");
        System.out.println(c);

        // 构造函数引用

        // 延时构造
        Supplier<Apple> c1 = Apple::new;
        c1.get();

        Supplier<Apple> c2 = ()-> new Apple("red", 158);

        BiFunction<String, Integer, Apple> apple3 = Apple::new;
        apple3.apply("yellow", 210);

        Map<String, Integer> appleMap = new HashMap<>();
        appleMap.put("red", 180);
        appleMap.put("green", 210);
        appleMap.put("yellow", 150);

        // 传递元素生成APple
        List<Apple> appleList1 = m.map(appleMap, apple3);
        System.out.println(appleList1);

        appleMap.entrySet().parallelStream().map(a->new Apple(a.getKey(), a.getValue())).collect(Collectors.toList());

        // 排序，sort需要Comparator 签名对象来比较
        // 类实现跳过，匿名类开始
        appleList.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });
        // lambda表达式 Comparator函数式接口 (T, T)->int 函数描述
        appleList.sort((Apple a1, Apple a2)->a1.getColor().compareToIgnoreCase(a2.getColor()));
        // java编译器通过上下文推断参数类型
        appleList.sort((a1, a2)->a1.getWeight().compareTo(a2.getWeight()));
        // comparing 提取对象值生成Comparator对象
        appleList.sort(comparing(a->a.getWeight()));
        // 方法引用
        appleList.sort(comparing(Apple::getWeight));
        // 逆序 reversed
        appleList.sort(comparing(Apple::getWeight).reversed());

        // 比较器链，两个一样重再提供比较
        appleList.sort(comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));

        // 谓词复合 Predicate接口
        // negate(否定)
        Predicate<Apple> redApple2 = a-> "red".equals(a.getColor());
        // 现有predicate对象redApple的非
        Predicate<Apple> notRedApple = redApple2.negate();
        // and 组合红色切比较重
        Predicate<Apple> redAndHeavyApple = redApple2.and(a->a.getWeight()>150);

        // 函数复合 Function接口
        // andThen g(f(x)) f函数输出g函数输入
        Function<Integer, Integer> f = x -> x+1;
        Function<Integer, Integer> g = x -> x*2;
        Function<Integer, Integer> h = f.andThen(g);
        int result = h.apply(1);        //结果4

        // compose(构成) 相反f(g(x))
        Function<Integer, Integer> h2 = f.compose(g);

        // 信的文本转换
        Function<String, String> addHeader = Letter::addHeader;
        // 先加上抬头，在进行拼写检查，最后加上落款
        Function<String, String> transformationPipeline = addHeader.andThen(Letter::checkSpelling).andThen(Letter::addFooter);

        // 落款，检查，抬头
        Function<String, String> transformationPipeline2= addHeader.compose(Letter::checkSpelling).compose(Letter::addFooter);
        
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

    // 创建Apple 遍历抽象类
    public <T, U, R> List<R> map(Map<T, U> map, BiFunction<T, U, R> f){
        List<R> result = new ArrayList<>();
        for(Map.Entry<T, U> entry : map.entrySet()){
            result.add(f.apply(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
