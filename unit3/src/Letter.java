/**
 * @file: Letter.class
 * @author: Dusk
 * @since: 2018/12/27 20:42
 * @desc:
 */
public class Letter {
    public static String addHeader(String text){
        return "From Raoul, Mario and Alan: " + text;
    }

    public static String addFooter(String text){
        return text + " Kind regards";
    }

    public static String checkSpelling(String text){
        return text.replaceAll("labda", "labda");
    }
}
