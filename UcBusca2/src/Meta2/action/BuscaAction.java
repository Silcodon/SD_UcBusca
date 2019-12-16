package Meta2.action;

import Meta2.model.BuscaBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class BuscaAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 5590830L;
    private String username,userpass,usernameadmin,url;
    private Map<String, Object> session;

    @Override
    public String execute() throws Exception {
        // you could execute some business logic here, but for now
        // the result is "success" and struts.xml knows what to do
        return SUCCESS;
    }

    public BuscaBean getBuscaBean() {
        if(!session.containsKey("buscaBean"))
            this.setBuscaBean(new BuscaBean());
        return (BuscaBean) session.get("buscaBean");
    }

    public void setBuscaBean(BuscaBean BuscaBean) {
        this.session.put("buscaBean", BuscaBean);
    }


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }


    public String login(){
        if(BuscaBean.validate(username, userpass).equals("admin")){
            session.put("Admin", true); //setting session attribute
            return "successadmin";
        }
        else if(BuscaBean.validate(username, userpass).equals("user")){
            session.put("User",true);
            return "successuser";
        }
        else{
            return "error";
        }
    }


    public String logout(){
        BuscaBean.logout();
        session.put("Admin",false);
        session.put("User",false);
        session.put("Facebook",false);
        return "success";
    }


    public String register(){
        String verifica=BuscaBean.save(username,userpass);
        if(verifica.equals("Registou-se com sucesso!")){
            return "success";
        }
        return "error";
    }

    public String historico(){
        return SUCCESS;
    }

    public String tornaradmin(){
        if(BuscaBean.tornar_admin(usernameadmin)==1){
            return SUCCESS;
        }
        else{
            return ERROR;
        }
    }


    public void setUsernameadmin(String usernameadmin) { this.usernameadmin=usernameadmin;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public String indexarurl(){
        BuscaBean.indexaurl(url);
        return SUCCESS;

    }

}