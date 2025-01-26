package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class HttpGetRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpGetRequestUtils.class);

    /**
     * URL 주소 가져오기
     * @param line: header 첫 라인
     * @return tokenList: method, url, params, HTTP/1.1
     */
    public static ArrayList<String> getRequestUrl(String line) {
        String[] tokens = line.split(" ");
        ArrayList<String> tokenList = new ArrayList<>(Arrays.asList(tokens));
        if (tokenList.get(1).contains("?")) {
            tokenList.add(3, tokenList.get(2));
            tokenList.set(2, tokens[1].substring(tokens[1].indexOf("?")+1, tokens[1].length()));
            tokenList.set(1, tokens[1].substring(0, tokens[1].indexOf("?")));
        }
        log.info("tokenList : {}", tokenList);
        return tokenList;
    }

}
