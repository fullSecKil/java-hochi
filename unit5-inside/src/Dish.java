import lombok.Data;

/**
 * @file: Dish.class
 * @author: Dusk
 * @since: 2019/1/9 20:36
 * @desc:
 */

@Data
public class Dish {

    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public enum Type { MEAT, FISH, OTHER }
}
