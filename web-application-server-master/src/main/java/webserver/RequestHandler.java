package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpGetRequestUtils;
import util.HttpRequestUtils;
import vo.HttpRequestVo;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader line = new BufferedReader(new InputStreamReader(in));
            String firstLine = line.readLine();
            HttpRequestVo header = null;
            String apiMethod = "";
            String apiUrl = "";
            Map<String, String> apiParams = new HashMap<>();
            if (firstLine != null && !firstLine.isEmpty()) {
                header = HttpGetRequestUtils.getRequestUrl(firstLine);
                apiMethod = header.getMethod();
                apiUrl = header.getUrl();
            }

            // GET 요청
            if (apiMethod.equals("GET")) {
                if (apiUrl.equals("/user/create") && !header.getParams().isEmpty()) {
                    apiParams = new HashMap<>(HttpRequestUtils.parseQueryString(header.getParams()));
                    User user = new User(apiParams.get("userId"), apiParams.get("password"), apiParams.get("name"), apiParams.get("email"));
                    log.info("user : {}", user);
                    apiUrl = "/index.html";
                }
            }

            // TODO: POST 요청
            if (apiMethod.equals("POST")) {

            }

            byte[] body = Files.readAllBytes(new File("./webapp" + apiUrl).toPath());
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
