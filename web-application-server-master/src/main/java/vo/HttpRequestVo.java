package vo;

public class HttpRequestVo {
    private final String method;
    private final String url;
    private final String params;
    private final String httpVersion;

    public HttpRequestVo(String method, String url, String params, String httpVersion) {
        this.method = method;
        this.url = url;
        this.params = params;
        this.httpVersion = httpVersion;
    }

    @Override
    public String toString() {
        return "HttpRequestVo{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", params='" + params + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getParams() {
        return params;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
