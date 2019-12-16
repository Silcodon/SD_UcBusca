package Meta2.action;

import Meta2.model.BuscaBean;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import uc.sd.apis.FacebookApi2;

import java.rmi.RemoteException;
import java.util.Map;

public class FacebookCallback extends ActionSupport implements SessionAware {
    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final Token EMPTY_TOKEN = null;
    private static final String API_KEY = "358340648349521";
    private static final String API_SECRET = "85931a3619d471bbe57b72b433cffaf7";
    private static final long serialVersionUID = 5590830L;
    private Map<String, Object> session;

    private String url;
    private String username;

    public String execute() throws RemoteException {
        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .callback("http://localhost:8008/UcBusca2/facebooklogin") // Do not change this.
                .scope("public_profile")
                .build();


        url = service.getAuthorizationUrl(EMPTY_TOKEN);
        BuscaBean.setUrl(url);

        return SUCCESS;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setBuscaBean(BuscaBean buscaBean) {
        this.session.put("buscaBean", buscaBean);
    }
    public BuscaBean getBuscaBean() {
        if(!session.containsKey("buscaBean"))
            this.setBuscaBean(new BuscaBean());
        return (BuscaBean) session.get("buscaBean");
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

}
