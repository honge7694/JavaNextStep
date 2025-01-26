package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vo.HttpRequestVo;

import java.util.Arrays;

public class HttpGetRequestUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpGetRequestUtils.class);

    /**
     * URL 주소 가져오기
     * @param line: header 첫 라인
     * @return tokenList: method, url, params, HTTP/1.1
     */
    public static HttpRequestVo getRequestUrl(String line) {
        String[] tokens = line.split(" ");
        String method = tokens[0];
        String url = tokens[1];
        String params = "";
        String httpVersion = tokens[2];

        if (tokens[1].contains("?")) {
            url = tokens[1].substring(0, tokens[1].indexOf("?"));
            params = tokens[1].substring(tokens[1].indexOf("?")+1, tokens[1].length());
        }
        HttpRequestVo httpRequestVo = new HttpRequestVo(method, url, params, httpVersion);
        log.info("httpRequestVo : {}", httpRequestVo);
        return httpRequestVo;
    }

}
