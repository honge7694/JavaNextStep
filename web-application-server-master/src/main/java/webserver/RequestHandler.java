package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestHeaderUtils;
import vo.HttpRequestVo;
import vo.ResponseHeaderVo;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

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
            if (firstLine != null && !firstLine.isEmpty()) {
                header = HttpRequestHeaderUtils.getRequestUrl(firstLine);
            }
            if (header == null) {
                log.error("Invalid request");
                return;
            }

            String apiMethod = header.getMethod();
            String apiUrl = header.getUrl();
            Map<String, String> apiParams = HttpRequestHeaderUtils.getParamsByMethod(apiMethod, header, line);
            ResponseHeaderVo responseHeader = processRequest(apiUrl, apiParams);
            log.debug("responseHeader : {}", responseHeader.toString());

            byte[] body = Files.readAllBytes(new File("./webapp" + responseHeader.getRedirectUrl()).toPath());
            DataOutputStream dos = new DataOutputStream(out);

            if (responseHeader.getRedirect()) {
                if (apiUrl.equals("/user/login")) {
                    response302LoginHeader(dos, responseHeader.getRedirectUrl(), responseHeader.getLogin());
                } else {
                    response302Header(dos, responseHeader.getRedirectUrl());
                }
            } else {
                response200Header(dos, body.length);
            }
            responseBody(dos, body);
        } catch (IOException e) {
            log.error("run: {}", e.getMessage());
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

    private void response302Header(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error("response302Header: {}", e.getMessage());
        }
    }

    private void response302LoginHeader(DataOutputStream dos, String redirectUrl, boolean loginCookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Set-Cookie: logined=" + loginCookie + "; Path=/; SameSite=Lax\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error("response302LoginHeader: {}", e.getMessage());
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

    private ResponseHeaderVo processRequest(String apiUrl, Map<String, String> params) {
        log.debug("processRequest: {}", apiUrl);
        if (apiUrl.equals("/user/create")) {
            User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
            DataBase.addUser(user);
            log.debug("user : {}", user);
            return new ResponseHeaderVo(true, apiUrl,"/index.html");
        } else if(apiUrl.equals("/user/login")) {
            User loginUser = DataBase.findUserById(params.get("userId"));
            log.debug("loginUser: {}", loginUser);

            if (loginUser != null && params.get("password").equals(loginUser.getPassword())) {
                return new ResponseHeaderVo(true, apiUrl,"/index.html", true);
            } else {
                return new ResponseHeaderVo(true, apiUrl, "/user/login_failed.html");
            }
        }
        return new ResponseHeaderVo(false, apiUrl, apiUrl);
    }
}
