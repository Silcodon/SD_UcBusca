package Meta2.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

interface RMIInterface extends Remote{  
	public String envia_login(String Username, String Password) throws RemoteException;
	public String register(String Username, String Password) throws RemoteException;
	public ArrayList<String> pesquisa(String pesquisa, String username) throws RemoteException;
	public String indexar(String website) throws RemoteException;
    public ArrayList<String> historico(String username) throws RemoteException;
    public String tornar_admin(String Username) throws RemoteException;
    public String recebe_notificacao(String username) throws RemoteException;
    public ArrayList<String> info() throws  RemoteException;
    ArrayList<String> pesquisa_url(String termopesquisa, String username);
}