
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
    }


}
