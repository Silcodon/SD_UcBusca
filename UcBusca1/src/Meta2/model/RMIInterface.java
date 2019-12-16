package Meta2.model;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;

interface RMIInterface extends Remote{  
	public String envia_login(String Username, String Password) throws RemoteException;
	public String register(String Username, String Password) throws RemoteException;
	public ArrayList<String> pesquisa(String pesquisa, String username) throws RemoteException;
	public String indexar(String website) throws RemoteException;
    public ArrayList<String> historico(String username) throws RemoteException;
    public String tornar_admin(String Username) throws RemoteException;
    public String recebe_notificacao(String username) throws RemoteException;
    public ArrayList<String> info() throws  RemoteException;
}  