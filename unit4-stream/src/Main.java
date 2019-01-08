
import java.util.*;
import java.util.stream.Collectors;

/**
 * @file: Main.class
 * @author: Dusk
 * @since: 2019/1/3 20:41
 * @desc: stream流，javaapi的新成员， 以声明的方式处理数据集合，透明的并行处理
 */
public class Main {

    // 此节用dish的学习

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
         * java8 前过滤， sort，map都要显示的迭代比较
         */
        // 累加器筛选元素
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for (Dish d:
             menu) {
            if(d.getCalories() < 400){
                lowCaloricDishes.add(d);
            }
        }
        System.out.println(lowCaloricDishes);

        // 匿名类进行排序
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o1.getCalories(), o2.getCalories());
            }
        });

        // 取出名单
        List<String> lowCaloricDishesName = new ArrayList<>();
        for (Dish d:
             lowCaloricDishes) {
            lowCaloricDishesName.add(d.getName());
        }
        // lowCaloricDishes 垃圾变量，一次性的中间容器

        // java8中同时流水线操作 stream, parallelStream(多核处理集合)
        List<String> lowCaloricDishesName2 =
                menu.parallelStream()
                    .filter(d->d.getCalories() < 400)       //选出400卡路里以下的菜肴
                    .sorted(Comparator.comparingInt(Dish::getCalories))         //卡里路排序
                    .map(Dish::getName)        // 映射成名字
                    .collect(Collectors.toList());      //list保存

        System.out.println(lowCaloricDishesName2);

        //  行为参数化  X重复造轮子X

        // 流处理操作模式筛选、切片、查找、匹配、映射和归约

        // Java 8中的集合支持一个新的 stream方法，它会返回一个流

        /**
         *  流的定义“从支持数据处理操作的源生成的元素序列”
         *  流操作有两个重要的特点 流水线 内部迭代
         *  filter、map、limit 除了collect之外，所有这些操作都会返回另一个流，这样它们就可以接成一条流水线
         *  最后，collect操作开始处理流水线 在调用collect之前，没有任
         * 何结果产生，实际上根本就没有从menu里选择元素。
         */

        List<String> threeHighCaloricDishNames = menu.stream().filter(d-> d.getCalories() > 300)
                .map(Dish::getName)
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(threeHighCaloricDishNames);

        // stream方法，由菜单得到一个流


        /**
         *  集合与流之间的差异就在于什么时候进行计算(dvd与在线视频例子)
         */
        // 和迭代器类似，流只能遍历一次。遍历完之后，我们就说这个流已经被消费掉了
        // 无法执行两次终端操作

        // 使用Collection接口需要用户去做迭代（比如用for-each），这称为外部迭代，相反stream库用内部迭代----他帮你把迭代做了，还把得到的流值存在了某个地方，你只要给出一个函数要干什么就可以了

        List<String> names = new ArrayList<>();
        // 集合：用for-each循环外部迭代
        for(Dish d: menu){
            names.add(d.getName());
        }
        // for-each 隐藏了迭代中一些复杂性。for-each结构是一个语法糖，它背后的东西用Iterator对象表达出来更要丑陋得多。

        List<String> names2 = new ArrayList<>();
        Iterator<Dish> iterator = menu.iterator();
        while (iterator.hasNext()){
            Dish d = iterator.next();
            names2.add(d.getName());
        }
        // 集合：用背后的迭代器做外部迭代

        // 流：内部迭代
        List<String> names3 = menu.stream().map(Dish::getName).collect(Collectors.toList());        // 开始操作流水线，没有迭代

        /**
         *  java.util.stream stream接口定义了许多操作分为中间操作 和 终端操作
         */

        List<String> names4 = menu.stream()                                                 // 从菜单获得流
                                                        .filter(d->{
                                                            System.out.println("filtering" + d.getName());
                                                            return d.getCalories() > 300;
                                                        })           // 中间操作
                                                        .map(d->{
                                                            System.out.println("mapping" + d.getName());
                                                            return d.getName();
                                                        })                            // 中间操作
                                                        .limit(3)                                                  // 中间操作
                                                        .collect(Collectors.toList());                  // 将stream转为list

        // filter、map和limit可以连成一条流水线  collect触发流水线执行并关闭它。

        // 可以连接起来的流操作称为中间操作，关闭流的操作称为终端操作。

        // 打印可以看到是同时，尽管filter和map是两个独立的操作，但它们合并到同一次遍历中了（我们把这种技术叫作循环合并）

        /**
         *  终端操作
         */

        menu.stream().forEach(System.out::println);     // forEach是一个返回void的终端操作，它会对源中的每道菜应用一个Lambda

        // 终端操作会从留的流水线生成结果。 其结果是任何不适流的值比如List、Integer，void

        /**
         *  流的使用一般包括三件事
         *   一个数据源（如集合）来执行一个查询
         *   一个中间操作链，形成一条流的流水线
         *   一个终端操作，执行流水线，并能生成结果
         */
    }


}
