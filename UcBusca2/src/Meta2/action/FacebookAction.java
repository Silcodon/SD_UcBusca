package Meta2.action;

import Meta2.model.BuscaBean;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uc.sd.apis.FacebookApi2;

import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.util.Map;

public class FacebookAction extends ActionSupport implements SessionAware {
    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final Token EMPTY_TOKEN = null;
    private static final String API_KEY = "358340648349521";
    private static final String API_SECRET = "85931a3619d471bbe57b72b433cffaf7";
    private static final long serialVersionUID = 5590830L;
    private Map<String, Object> session;

    private String username;
    private Token accessToken;
    private String url;


    public void setUsername(String username){
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(username);
            this.username = (String) json.get("name");
            this.username=this.username.replaceAll("\\s+","");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String execute() throws RemoteException {
        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .callback("http://localhost:8008/UcBusca2/facebooklogin") // Do not change this.
                .scope("public_profile")
                .build();

        HttpServletRequest servletRequest = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String code = servletRequest.getParameter("code");
        System.out.println(code);
        Verifier verifier = new Verifier(code);
        accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);

        service.signRequest(accessToken, request);
        Response response = request.send();

        this.setUsername(response.getBody());
        BuscaBean.save(username, "FacebookPassword");
        String resposta = BuscaBean.validate(username, "FacebookPassword");
        session.put("Facebook",true);
        if(resposta.equals("admin")){
            session.put("Admin", true); //setting session attribute
            return "successadmin";
        }
        else if(resposta.equals("user")){
            session.put("User",true);
            return "successuser";
        }
        else{
            return "error";
        }

    }



    public void setBuscaBean(BuscaBean BuscaBean) {
        this.session.put("buscaBean", BuscaBean);
    }




    public String getUsername() {
        return username;
    }


    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }


    public BuscaBean getBuscaBean() {
        if(!session.containsKey("buscaBean"))
            this.setBuscaBean(new BuscaBean());
        return (BuscaBean) session.get("buscaBean");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}