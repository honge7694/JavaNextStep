package vo;

public class ResponseHeaderVo {
    private final boolean redirect;
    private final String apiUrl;
    private final String redirectUrl;
    private final boolean login;

    public ResponseHeaderVo(boolean redirect, String apiUrl, String redirectUrl) {
        this(redirect, apiUrl, redirectUrl, false);
    }

    public ResponseHeaderVo(boolean redirect, String apiUrl, String redirectUrl, boolean login) {
        this.redirect = redirect;
        this.apiUrl = apiUrl;
        this.redirectUrl = redirectUrl;
        this.login = login;
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

    public boolean getLogin() {
        return login;
    }
}
