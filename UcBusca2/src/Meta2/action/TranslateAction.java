package Meta2.action;

import Meta2.model.BuscaBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import uc.sd.apis.TranslateAPI;
import java.util.Map;

public class TranslateAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 5590830L;
    private Map<String, Object> session;

    @Override
    public String execute() throws Exception {
        // you could execute some business logic here, but for now
        // the result is "success" and struts.xml knows what to do
        return SUCCESS;
    }

    public BuscaBean getBuscaBean() {
        if (!session.containsKey("buscaBean"))
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


    }
