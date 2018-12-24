import java.io.BufferedReader;
import java.io.IOException;

/**
 * @file: BufferedReaderProcessor.class
 * @author: Dusk
 * @since: 2018/12/23 14:59
 * @desc:
 */

@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throws IOException;
}
