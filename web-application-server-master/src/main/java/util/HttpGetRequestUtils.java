package util;

import java.util.Arrays;

public class HttpGetRequestUtils {

    /**
     * URL 주소 가져오기
     */
    public static String[] getRequestUrl(String line) {
        String[] tokens = line.split(" ");
        return tokens;
    }

}
