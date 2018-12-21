/**
 * @file: AppleImple.class
 * @author: Dusk
 * @since: 2018/12/21 22:22
 * @desc:
 */
public class AppleImple {
}

class AppleHeavyWeightPredicate implements ApplePredicate {

    @Override
    public boolean test(Apple apple) {
        return apple.getWeight() >150;
    }
}

class AppleGreenColorPredicate implements ApplePredicate {

    @Override
    public boolean test(Apple apple) {
        return "red".equals(apple.getColor());
    }
}