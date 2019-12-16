package Meta2.model;

import com.github.scribejava.core.model.Token;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;

import uc.sd.apis.TranslateAPI;

import static uc.sd.apis.TranslateAPI.*;

public class BuscaBean {
    private static String logged=" ";
    private static String username;
    ArrayList<String> resultados;
    private String termopesquisa;
    static int PRIMARY_PORT=7000;
    public static String url;
    public static Token accessToken;
    static String SERVER_IP="127.0.0.1";


    //Verifica o login
    public static String validate(String username, String userpass) {
        //Mandar para server RMI
        try{
            RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            //h.envia_login(username,pass);
            String Resposta=h.envia_login(username,userpass);
            System.out.println(Resposta);
            if(Resposta.equals("Login com sucesso!")){
                BuscaBean.username =username;
                logged="user";
                return logged;
            }
            if(Resposta.equals("Login como Admin com sucesso!")){
                logged="admin";
                BuscaBean.username=username;
                return logged;
            }
            else{
                return "error";
            }

        }
        catch (ConnectException e2) {
            System.out.println("ConnectException in Client.login: " + e2);
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return "error";
    }


    //Faz o registo no server
    public static String save(String username, String userpass){
        //Mandar para server RMI
        String Resposta="";
        try{
            RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            Resposta=h.register(username,userpass);
            return Resposta;

        }
        catch (ConnectException e2) {
            System.out.println("ConnectException in Client.register: " + e2);
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        return Resposta;
    }


    //Logout
    public static void logout(){
        username=null;
        logged=" ";
    }

    //Historico do utilizador
    public static ArrayList<String> getHistoricopesquisa(){
        ArrayList<String> historicopesquisa=new ArrayList<String>();
        try{
            RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            historicopesquisa=h.historico(username);

        }
        catch (ConnectException e2) {
            System.out.println("ConnectException in Client.historico: " + e2);
        } catch (AccessException ex) {
            ex.printStackTrace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }
        return historicopesquisa;

    }

    //Tornar admin
    public static int tornar_admin(String username){
        try{
            RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            String Resposta=h.tornar_admin(username);
            if (Resposta.equals("O user " + username + " passou a ser admin!")){
                return 1;
            }
            else{
                return 0;
            }

        }
        catch (ConnectException e2) {
            System.out.println("ConnectException in Client.tornar_admin: " + e2);
        }
        catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setUrl(String url){
        BuscaBean.url = url;
    }
    public String getUrl(){
        return url;
    }

    public void setAccessToken(Token token1){
        accessToken = token1;
    }

    public Token getAccessToken(){
        return accessToken;
    }

    //notificacao offline
    public String getNotificacao(){
        String notified="";
        try{
            RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            notified=h.recebe_notificacao(username);
        }catch (ConnectException e2) {
            System.out.println("ConnectException in Client.tonar_admin: " + e2);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        System.out.println(notified);
        return notified;
    }

    //Indexar URL
    public static void indexaurl(String url){
        //Indexar URL
        int count=0;
        int maxTries=3;
        //Mandar para server RMI
        while(true){
            try{
                RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
                h.indexar(url);
                break;

            }
            catch (ConnectException e2) {
                if(++count==maxTries){
                    System.out.println("Error: "+e2);
                    break;
                }

            } catch (AccessException ex) {
                ex.printStackTrace();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            } catch (NotBoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Informacao Sistema
    public ArrayList<String> getInformacao(){

        ArrayList<String> informacao=new ArrayList<String>();
        try{
            RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            informacao=h.info();
            //Palavras e numero de vezes que foram pesquisadas e sites
            //informacao.get(0)
            //Percorrer Array de Multicast ativos
            //informacao.get(1)
            //FIM
        }  catch (ConnectException e2) {
            System.out.println("ConnectException in Client.informacao: " + e2);
        }catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return informacao;
    }

    public String getTermopesquisa() {
        return this.termopesquisa;
    }

    public void setTermopesquisa(String termopesquisa) {
        this.termopesquisa = termopesquisa;
    }

    public ArrayList<String> getResultados() {
        ArrayList<String> resultados=new ArrayList<String>();
        ArrayList<String> resultadosamostrar=new ArrayList<String>();
        try{
            RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            resultados=h.pesquisa(termopesquisa,username);
            if((resultados.size()==1) && ((resultados.get(0).equals("Nenhum resultado encontrado")) || resultados.get(0).equals("Servidor em baixo ou a demorar a responder"))){
                resultadosamostrar.add("Resultados: 0");
            }
            else{
                for (int i = 0; i < resultados.size(); i++) {


                    //conjunnto[0]=title conjunto[1]=URL conjunto[2]=descricao
                    String[] conjunto=(resultados.get(i)).split("\\|XXX\\|");
                    String URL_com_lingua= conjunto[1] + " (" +getLangs().get(detectLanguage(conjunto[2].replaceAll("\\s", "%20")))+")";
                    String adicionar=conjunto[0]+"|XXX|"+URL_com_lingua+"|XXX|"+conjunto[2];
                    //Traduzir cada resultado
                    resultadosamostrar.add(adicionar);
                }
                resultadosamostrar.add("Resultados: "+resultados.size());
            }


        }
        catch (ConnectException e2) {
            System.out.println("ConnectException in pesquisa: " + e2);
        }catch(Exception e2){
            System.out.println("Exception in pesquisa: "+e2);
            e2.printStackTrace();
        }
        return resultadosamostrar;
    }

    public ArrayList<String> getResultadostraduzidos() {
        ArrayList<String> resultados = new ArrayList<String>();
        ArrayList<String> resultadostraduzidos = new ArrayList<String>();
        Map<String, String> langs = null;
        try {
            langs = getLangs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String portugues = TranslateAPI.getKey(langs, "portuguese");
        try {
            RMIInterface h = (RMIInterface) LocateRegistry.getRegistry(SERVER_IP, PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            resultados = h.pesquisa(termopesquisa, username);
            if ((resultados.size() == 1) && ((resultados.get(0).equals("Nenhum resultado encontrado")) || resultados.get(0).equals("Servidor em baixo ou a demorar a responder"))) {
                resultadostraduzidos.add("Resultados: 0");
            } else {
                for (int i = 0; i < resultados.size(); i++) {
                    // Trim the given string
                    resultados.set(i, resultados.get(i).trim());

                    // Replace All space (unicode is \\s) to %20
                    resultados.set(i,resultados.get(i).replaceAll("\\s", "%20"));


                    //conjunnto[0]=title conjunto[1]=URL conjunto[2]=descricao
                    String[] conjunto=(resultados.get(i)).split("\\|XXX\\|");
                    String titulo_traduzido=translate(conjunto[0],detectLanguage(conjunto[0]),portugues);
                    String descricao_traduzida=translate(conjunto[2],detectLanguage(conjunto[2]),portugues);
                    String traduzido=titulo_traduzido+"|XXX|"+conjunto[1]+"|XXX|"+descricao_traduzida;
                    //Traduzir cada resultado
                    resultadostraduzidos.add(traduzido);
                }
                resultadostraduzidos.add("Resultados: " + resultadostraduzidos.size());
            }


        } catch (ConnectException e2) {
            System.out.println("ConnectException in pesquisa: " + e2);
        } catch (Exception e2) {
            System.out.println("Exception in pesquisa: " + e2);
            e2.printStackTrace();
        }
        return resultadostraduzidos;
    }

    public ArrayList<String> getResultados_url() {
        ArrayList<String> resultados_url=new ArrayList<String>();
        try{
            RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            resultados_url=h.pesquisa_url(termopesquisa,username);
            if((resultados_url.size()==1) && ((resultados_url.get(0).equals("Nenhum resultado encontrado")) || resultados_url.get(0).equals("Servidor em baixo ou a demorar a responder"))){
                resultados_url.add("Resultados: 0");
            }
            else{
                resultados_url.add("Resultados: "+resultados_url.size());
            }


        }
        catch (ConnectException e2) {
            System.out.println("ConnectException in pesquisa: " + e2);
        }catch(Exception e2){
            System.out.println("Exception in pesquisa: "+e2);
            e2.printStackTrace();
        }
        return resultados_url;
    }

}
