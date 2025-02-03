package vo;

public class ResponseHeaderVo {
    private final boolean redirect;
    private final String apiUrl;
    private final String redirectUrl;

    public ResponseHeaderVo(boolean redirect, String apiUrl, String redirectUrl) {
        this.redirect = redirect;
        this.apiUrl = apiUrl;
        this.redirectUrl = redirectUrl;
    }

    public boolean isRedirect() {
        return redirect;
    }

    @Override
    public String toString() {
        return "ResponseHeaderVo{" +
                "redirect=" + redirect +
                ", apiUrl='" + apiUrl + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                '}';
    }

    public boolean getRedirect() {
        return redirect;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
