import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.rmi.ConnectException;
import java.util.Scanner;
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


public class Client{
	static int PRIMARY_PORT=7000;
	static String SERVER_IP="127.0.0.1";
	String username;
	Notification_thread thread= new Notification_thread(); //instanciate thread
   	String logged=" ";
   	private boolean thread_run=true;
    private Scanner input = new Scanner(System.in);

    public void display(){
    	System.out.println("-- Menu --");
        System.out.println(
                "Selecione uma opcao: \n" +
                        "  1) Login\n" +
                        "  2) Registar\n" +
                        "  3) Pesquisar\n" +
                        "  4) Sair\n"
        );
        System.out.print("Insira a sua opcao:");

        String inputstring=input.nextLine();
        
        try{
        	int selection=Integer.parseInt(inputstring);
        	switch (selection) {
            case 1:
                this.login();
                break;
            case 2:
            	this.register();
                break;
            case 3:
            this.pesquisa(this.username);
            	break;
            case 4:
            this.exit();
                break;
            default:
            	System.out.println("\n");
                System.out.println("Selecao invalida.");
                System.out.println("\n");
                break;
        }}
        catch(NumberFormatException ne){
        	System.out.println("\n");
        	System.out.println("Introduza um numero!");
        	System.out.println("\n");
        }


        
    }

    public void display_user() {
        System.out.println("-- Menu --");
        System.out.println(
                "Selecione uma opcao: \n" +
                        "  1) Pesquisar \n" +
                        "  2) Historico de pesquisa\n" +
                        "  3) Sair\n "
        );
        System.out.print("Insira a sua opcao:");
        String inputstring=input.nextLine();
        
        try{
        	int selection=Integer.parseInt(inputstring);
        	switch (selection) {
            case 1:
            	this.pesquisa(this.username);
                break;
            case 2:
            this.historico(this.username);
            	break;
            case 3:
            this.exit();
                break;
            default:
            	System.out.println("\n");
                System.out.println("Selecao invalida.");
                System.out.println("\n");
                break;
        }}
        catch(NumberFormatException ne){
        	System.out.println("\n");
        	System.out.println("Introduza um numero!");
        	System.out.println("\n");
        }}

    public void display_admin() {
        System.out.println("-- Menu --");
        System.out.println(
                "Selecione uma opcao: \n" +
                        "  1) Tornar admin\n" +
                        "  2) Historico de pesquisa\n" +
                        "  3) Pesquisar \n" +
                        "  4) Indexar URL\n" +
                        "  5) Informações do sistema\n " +
                        "  6) Sair\n"
        );
        System.out.print("Insira a sua opcao:");
        String inputstring=input.nextLine();
        
        try{
        	int selection=Integer.parseInt(inputstring);
        	switch (selection) {
            case 1:
            this.tornar_admin();
                break;
            case 2:
            this.historico(this.username);
                break;
            case 3:
            this.pesquisa(this.username);
                break;
            case 4:
            this.indexar();
                break;
            case 5:
            this.informacao();
                break;
            case 6:
                this.exit();
            default:
                System.out.println("Selecao invalida.");
                break;
        }}
        catch(NumberFormatException ne){
        	System.out.println("\n");
        	System.out.println("Introduza um numero!");
        	System.out.println("\n");
        }}


    public void informacao(){
        try{
            RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
            ArrayList<String> infogeral=h.info();
            //Palavras e numero de vezes que foram pesquisadas e sites
            System.out.println(infogeral.get(0));
            //FIM
            System.out.println("\n");
            System.out.println("Servidores Multicast ativos:");
            //Percorrer Array de Multicast ativos
            System.out.println(infogeral.get(1));
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
    }



    public void login(){
        System.out.println("-- Login --");
        System.out.print("Insira o seu Username:");
        String username = input.nextLine();
        System.out.print("Insira a sua password:");
        String pass = input.nextLine();
        //Mandar para server RMI
        try{
        	RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
        	//h.envia_login(username,pass);
        	String Resposta=h.envia_login(username,pass);
        	System.out.println("\n");
        	System.out.println(Resposta);
        	System.out.println("\n");
        	if(Resposta.equals("Login com sucesso!")){
        		this.username=username;
                this.thread.start();
                logged="user";
        	}
        	if(Resposta.equals("Login como Admin com sucesso!")){
        		logged="admin";
                this.thread.start();
                this.username=username;
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
    }



    public void register(){
        System.out.println("-- Registar --");
        System.out.print("Insira o seu Username:");
        String username = input.nextLine();
        System.out.print("Insira a sua password:");
        String pass = input.nextLine();
        //Mandar para server RMI
        try{
        	RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
        	String Resposta=h.register(username,pass);
        	System.out.println("\n");
        	System.out.println(Resposta);
        	System.out.println("\n");
        	
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
    }


    public void pesquisa(String username){
    	//Pesquisar URL's
    	System.out.println("-- Pesquisar --");
        System.out.print("Insira o que pretende pesquisar:");
        String pesquisa = input.nextLine();
    	try{
        	RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
        	ArrayList<String> Resposta=h.pesquisa(pesquisa,username);
			if((Resposta.size()==1) && (Resposta.get(0).equals("Nenhum resultado encontrado"))){
				System.out.println("Resultados: 0");
			}
			else{
				System.out.println("Resultados: "+Resposta.size());
			}
			for(int i=0; i<Resposta.size();i++){
        		System.out.println(Resposta.get(i));
        	}
			System.out.println("\n");
        	
        }
        catch (ConnectException e2) {
            System.out.println("ConnectException in Client.pesquisa: " + e2);
        }catch(Exception e2){
        	System.out.println("Exception in pesquisa: "+e2);
        	e2.printStackTrace();

    }
    }

    public void indexar(){
    	//Indexar URL
    	int count=0;
    	int maxTries=3;
    	 System.out.println("-- Indexar --");
        System.out.print("Insira o Website a indexar:");
        String website = input.nextLine();
        //Mandar para server RMI
        while(true){
        try{
        	RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
        	String Resposta=h.indexar(website);
        	System.out.println("\n");
        	System.out.println(Resposta);
        	System.out.println("\n");
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

    public void historico(String username){
    	//Mostrar historico pesquisas
    	System.out.println("-- Historico de pesquisas --");
    	try{
        	RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
        	ArrayList<String> Resposta=h.historico(username);
        	for(int i=0; i<Resposta.size();i++){
        		System.out.println(Resposta.get(i));
        	}
        	System.out.println("\n");
        	
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

    }

    public void tornar_admin(){
    	//Tornar Admin
    	System.out.println("-- Tornar Admin --");
        System.out.print("Insira o Username da pessoa pretendida:");
        String username = input.nextLine();
    	try{
        	RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
        	String Resposta=h.tornar_admin(username);
        	System.out.println("\n");
        	System.out.println(Resposta);
        	System.out.println("\n");
        	
        }
        catch (ConnectException e2) {
            System.out.println("ConnectException in Client.tonar_admin: " + e2);
        }
    	catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public void exit(){
    	
    	this.thread_run=false;
    	thread.interrupt();
    	System.exit(0);

    }



    public class Notification_thread extends Thread{
    @Override
    public void run(){
    	//Receive notification
    	do{
    		try{
    			RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
    			String notified=h.recebe_notificacao(username);
				Notification_thread.sleep(1000);
                if(notified.equals("Foi tornado admin ")) {
                    System.out.print("\n");
                    System.out.println(notified);
                    logged="admin";
                }
    }catch (ConnectException e2) {
                System.out.println("ConnectException in Client.tonar_admin: " + e2);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }  catch (NotBoundException e) {
                e.printStackTrace();
            }
        }while(thread_run);}

    }



//-------------------------------------------------------------------------------
    public static void main(String[] args) {
    	//System.getProperties().put("java.security.policy","policy.all") ;
		//if (System.getSecurityManager() == null) {
        //System.setSecurityManager(new SecurityManager());
    	//}
    	System.getProperties().put("java.security.policy","UserServer.policy");
    	if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
		try{
			RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(SERVER_IP,PRIMARY_PORT).lookup("rmi://localhost/UserServer");
			Client menu = new Client();
        	while (true){
        		if (menu.logged.equals(" ")){
            		menu.display();
        		}
            	if(menu.logged.equals("user")){
            		menu.display_user();
            	}
            	if(menu.logged.equals("admin")){
            		menu.display_admin();
            	}
    		}}

        catch (ConnectException e2) {
            System.out.println("ConnectException in Client.main: " + e2);
        }
		catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }
}

