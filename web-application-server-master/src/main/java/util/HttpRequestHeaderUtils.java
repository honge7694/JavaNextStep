package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vo.HttpRequestVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeaderUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestHeaderUtils.class);

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

    /**
     * HTTP 요청의 메서드에 따라 파라미터 파싱 후 반환
     * @param method
     * @param header: header 첫 라인
     * @param line: requestHeader의 모든 라인
     * @return
     * @throws IOException
     */
    public static Map<String, String> getParamsByMethod(String method, HttpRequestVo header, BufferedReader line) throws IOException {
        if ("GET".equalsIgnoreCase(method)) {
            return HttpRequestUtils.parseQueryString(header.getParams());
        } else if ("POST".equalsIgnoreCase(method)) {
            return getRequestBody(line);
        }
        return new HashMap<>();
    }

    /**
     * HTTP POST 요청의 requestBody 데이터를 반환
     * @param line: requestHeader의 모든 라인
     * @return
     * @throws IOException
     */
    public static Map<String, String> getRequestBody(BufferedReader line) throws IOException {
        String currentLine;
        int contentLength = 0;
        boolean isBody = false;
        StringBuilder requestBody = new StringBuilder();
        while ((currentLine = line.readLine()) != null) {
            if (currentLine.isEmpty()) {
                isBody = true;
                break;
            }
            if (currentLine.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt((currentLine.split(": ")[1]));
            }
            log.debug("currentLine : {}", currentLine);
        }

        if (isBody && contentLength > 0) {
            char[] body = IOUtils.readBodyData(line, contentLength);
            requestBody.append(body);
        }

        log.debug("requestBody : {}", requestBody);
        return HttpRequestUtils.parseQueryString(requestBody.toString().trim());
    }

}
