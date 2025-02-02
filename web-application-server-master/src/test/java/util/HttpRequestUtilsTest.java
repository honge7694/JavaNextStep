package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import model.User;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;
import vo.HttpRequestVo;

public class HttpRequestUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtilsTest.class);
    String fileDir = "./src/test/resources/";

    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is("password2"));
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty(), is(true));
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));
    }

    @Test
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters.get("logined"), is("true"));
        assertThat(parameters.get("JSessionId"), is("1234"));
        assertThat(parameters.get("session"), is(nullValue()));
    }

    @Test
    public void getKeyValue() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair, is(new Pair("userId", "javajigi")));
    }

    @Test
    public void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair, is(nullValue()));
    }

    @Test
    public void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair, is(new Pair("Content-Length", "59")));
    }

    @Test
    public void requestQuestion1() throws Exception {
        InputStream in = new FileInputStream(fileDir + "Http_Get.txt");
        BufferedReader line = new BufferedReader(new InputStreamReader(in));
        //System.out.printf("line : " + line + " readLine : " + line.readLine());

        String firstLine = line.readLine();
        HttpRequestVo header = null;
        if (!firstLine.isEmpty() && firstLine != null) {
            header = HttpRequestHeaderUtils.getRequestUrl(firstLine);
            System.out.println("header = " + header);
        }

        String currentLine;
        while((currentLine = line.readLine()) != null) {
            if (currentLine.isEmpty()) continue;
            System.out.println(currentLine);
        }

        assertEquals("/index.html", header.getUrl());
        assertThat(header.getUrl(), is("/index.html"));
        line.close();
        in.close();
    }

    @Test
    public void requestQuestion2() throws Exception {
        InputStream in = Files.newInputStream(Paths.get(fileDir + "Http_Get_Join.txt"));
        BufferedReader line = new BufferedReader(new InputStreamReader(in));

        String firstLine = line.readLine();
        HttpRequestVo header = null;
        if (!firstLine.isEmpty()) {
            header = HttpRequestHeaderUtils.getRequestUrl(firstLine);
        }
        if (header == null) {
            log.error("Invalid request");
            return;
        }

        String apiMethod = header.getMethod();
        Map<String, String> apiParams = HttpRequestHeaderUtils.getParamsByMethod(apiMethod, header, line);

        assertThat(apiParams.getOrDefault("userId", ""), is("java"));
        assertThat(apiParams.getOrDefault("password", ""), is("test"));
        assertThat(apiParams.getOrDefault("name", ""), is("test"));
        assertThat(apiParams.getOrDefault("email", ""), is("test%40naver.com"));
    }

    @Test
    public void requestQuestion3() throws Exception {
        InputStream in = new FileInputStream(fileDir + "Http_Post_Join.txt");
        BufferedReader line = new BufferedReader(new InputStreamReader(in));

        String firstLine = line.readLine();
        HttpRequestVo header = null;
        if (!firstLine.isEmpty() && firstLine != null) {
            header = HttpRequestHeaderUtils.getRequestUrl(firstLine);
            log.info("header : {}", header);
        }
        if (header == null) {
            log.error("Invalid request");
            return;
        }

        String apiMethod = header.getMethod();
        Map<String, String> apiParams = HttpRequestHeaderUtils.getParamsByMethod(apiMethod, header, line);
        log.debug("getRequestBody : {}", apiParams);

        assertThat(apiParams.getOrDefault("userId", ""), is("javajigi"));
        assertThat(apiParams.getOrDefault("password", ""), is("password"));
        assertThat(apiParams.getOrDefault("name", ""), is("JaeSung"));
//        assertThat(apiParams.getOrDefault("email", ""), is("test%40naver.com"));
    }

    @Test
    public void test1() {
        String test = "abc";
        String test2 = test;
        test = "aaa";
        log.debug("test : {}, test2: {}", test, test2);

    }
}
