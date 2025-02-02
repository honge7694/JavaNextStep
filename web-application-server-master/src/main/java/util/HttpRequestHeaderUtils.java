package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vo.HttpRequestVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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

    public static Map<String, String> getRequestBody(BufferedReader line) throws IOException {
        String currentLine;
        int contentLength = 0;
        boolean isBody = false;
        String requestBody = "";
        while ((currentLine = line.readLine()) != null) {
            if (currentLine.isEmpty()) {
                isBody = true;
                continue;
            }
            if (currentLine.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt((currentLine.split(": ")[1]));
            } else if (isBody) {
                requestBody = currentLine;
                break;
            }
            log.debug("currentLine : {}", currentLine);
        }
        BufferedReader br = new BufferedReader(new StringReader(requestBody));
        return HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength).trim());
    }

    public static Map<String, String> getRequestBody2(BufferedReader line) throws IOException {
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
