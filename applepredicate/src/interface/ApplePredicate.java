/**
 * @file: ApplePredicate.class
 * @author: Dusk
 * @since: 2018/12/21 22:16
 * @desc:
 */

// 此接口设计成函数式接口

@FunctionalInterface
public interface ApplePredicate {
    boolean test(Apple apple);
}
