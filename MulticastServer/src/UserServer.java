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
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.*;




public class UserServer extends UnicastRemoteObject implements RMIInterface {
	String MULTICASTGROUP="224.0.224.0";
	String RMIADRESS="224.0.224.1";
	String MULTICASTGROUP2 = "225.0.224.0";
	String RMIADRESS2 = "225.0.224.1";
	int PORT=80;
	int PORT2=81;
	int TIMEOUT=30000;
	private static final long serialVersionUID=1;
    
	public UserServer() throws RemoteException{
		super();
	}

	//--------------------------------------------METODOS------------------------------------------------------------------------------

    //Enviar login para servidor
    public String envia_login(String Username, String Password){
    	String received=null;
    	try{
            //Create the socket but we don't bind it as we are only going to send data
            MulticastSocket s= new MulticastSocket();
            // We don't have to join the multicast group if we are only sending data

            //Enviar informação
            String msg="login "+Username+" "+Password;
            System.out.println(msg);
            byte[] buf= msg.getBytes();
            //Create Datagram Packet
            DatagramPacket pack_sender=new DatagramPacket(buf,buf.length,InetAddress.getByName(MULTICASTGROUP),PORT);
            //Do send
            int ttl=2;
            s.send(pack_sender);
            s.setTimeToLive(ttl);
            s.close();
        } catch(IOException e){
    	    System.out.println("Exception in UserServer.envia_login: "+e);
    	}
    	try{
            MulticastSocket r=new MulticastSocket(PORT);
            //Receber resposta do servidor
            r.joinGroup(InetAddress.getByName(RMIADRESS));
            byte[] buff=new byte[1024];
            DatagramPacket pack_receive=new DatagramPacket(buff,buff.length);
            r.setSoTimeout(TIMEOUT);
            r.receive(pack_receive);
            //Mostrar resposta
            received = new String(
              pack_receive.getData(), 0, pack_receive.getLength());
            //System.out.write(pack_receive.getData(),0,pack_receive.getLength());
            //Fechar conexao
            r.leaveGroup(InetAddress.getByName(RMIADRESS));
            r.close();
        }catch(SocketTimeoutException e2){
            received="Servidor em baixo ou a demorar a responder";
            return received;
        }catch(IOException e){
    	    System.out.println("Exception in UserServer.receber_login: "+e);
        }
        return received;
	}



    //Enviar registo para o servidor
    public String register(String Username, String Password){
    	String received=null;
    	try{
            //Create the socket but we don't bind it as we are only going to send data
            MulticastSocket s= new MulticastSocket();
            // We don't have to join the multicast group if we are only sending data

            //Enviar informação
            String msg="register "+Username+" "+Password;
            System.out.println(msg);
            byte[] buf= msg.getBytes();
            //Create Datagram Packet
            DatagramPacket pack_sender=new DatagramPacket(buf,buf.length,InetAddress.getByName(MULTICASTGROUP),PORT);
            //Do send
            int ttl=s.getTimeToLive();
            s.send(pack_sender);
            s.setTimeToLive(ttl);

            //Fechar conexao
            s.close();
        } catch(IOException e){
    	    System.out.println("Exception in UserServer.register: "+e);
        }
        try{
            MulticastSocket r=new MulticastSocket(PORT);
            //Receber resposta do servidor
            r.joinGroup(InetAddress.getByName(RMIADRESS));
            byte[] buff=new byte[1024];
            DatagramPacket pack_receive=new DatagramPacket(buff,buff.length);
            r.setSoTimeout(TIMEOUT);
            r.receive(pack_receive);
            //Mostrar resposta
            received = new String(
              pack_receive.getData(), 0, pack_receive.getLength());
            //System.out.write(pack_receive.getData(),0,pack_receive.getLength());
            //Fechar conexao
            r.leaveGroup(InetAddress.getByName(RMIADRESS));
            r.close();
        }catch(SocketTimeoutException e2){
            received="Servidor em baixo ou a demorar a responder";
            return received;
        }catch(IOException e){
    	    System.out.println("Exception in UserServer.receber_register: "+e);
        }
        return received;
	}





	//Método para a thread receber as notificações
    public String recebe_notificacao(String username){
    	String received=null;
		try{
			//Create the socket but we don't bind it as we are only going to send data
			MulticastSocket s= new MulticastSocket();
			// We don't have to join the multicast group if we are only sending data

			//Enviar informação
			String msg="notifica "+username;
			System.out.println(msg);
			byte[] buf= msg.getBytes();
			//Create Datagram Packet
			DatagramPacket pack_sender=new DatagramPacket(buf,buf.length,InetAddress.getByName(MULTICASTGROUP2),PORT2);
			//Do send
			int ttl=s.getTimeToLive();
			s.send(pack_sender);
			s.setTimeToLive(ttl);

			//Fechar conexao
			s.close();
		}catch(IOException e){
			System.out.println("Exception in UserServer.envia_notifica: "+e);
		}
    	try{
            MulticastSocket r=new MulticastSocket(PORT2);
            //Receber resposta do servidor
            r.joinGroup(InetAddress.getByName(RMIADRESS2));
            byte[] buff=new byte[1024];
            DatagramPacket pack_receive=new DatagramPacket(buff,buff.length);
            r.setSoTimeout(TIMEOUT);
            r.receive(pack_receive);
            //Mostrar resposta
            received = new String(
              pack_receive.getData(), 0, pack_receive.getLength());
            //System.out.write(pack_receive.getData(),0,pack_receive.getLength());
            //Fechar conexao
            r.leaveGroup(InetAddress.getByName(RMIADRESS2));
            r.close();
		}catch(SocketTimeoutException e2){
            received="Servidor em baixo ou a demorar a responder";
            return received;
        }catch(IOException e) {
			System.out.println("Exception in UserServer.receber_resposta: " + e);
		}
		String[] parts = received.split(" ");
    	//Verifica para qual Client mandar
		if(username.equals(parts[0])) {
		    //Se for o certo manda a notificação
		    String retorna="";
		    for(int j=1;j<= parts.length-1;j++) {
                retorna += parts[j] + " ";
            }
            return retorna;
		}
		else{
		    //Se não for o user certo manda uma string de volta
			String retorna = parts[1];
			return retorna;
		}
    }


    //Metodo para mandar o termo da pesquisa e receber os urls
    public ArrayList<String> pesquisa(String pesquisa,String username){
    	ArrayList<String> received=new ArrayList<String>();;
    	String urls="";
    	try{
            //Create the socket but we don't bind it as we are only going to send data
            MulticastSocket s= new MulticastSocket();
            // We don't have to join the multicast group if we are only sending data

            //Enviar informação
            String msg="pesquisar "+username+" "+pesquisa;
            System.out.println(msg);
            byte[] buf= msg.getBytes();
            //Create Datagram Packet
            DatagramPacket pack_sender=new DatagramPacket(buf,buf.length,InetAddress.getByName(MULTICASTGROUP),PORT);
            //Do send
            int ttl=2;
            s.send(pack_sender);
            s.setTimeToLive(ttl);
            s.close();
            }catch(IOException e){
            System.out.println("Exception in UserServer.pesquisa: "+e);
            }
    	byte[] buff=new byte[1024];
    	try{
            MulticastSocket r=new MulticastSocket(PORT);
            //Receber resposta do servidor
            r.joinGroup(InetAddress.getByName(RMIADRESS));
            //Vai adicionando os titulos,url e descrições ate receber uma string a dizer "FIM"
            while(!urls.equals("FIM")){
                Arrays.fill(buff,(byte)0);
                DatagramPacket pack_receive=new DatagramPacket(buff,buff.length);
                r.setSoTimeout(TIMEOUT);
                r.receive(pack_receive);
                //Mostrar resposta
                urls = new String(
                  pack_receive.getData(), 0, pack_receive.getLength());

                    //System.out.write(pack_receive.getData(),0,pack_receive.getLength());
                if(!urls.equals("FIM"))
                        received.add(urls);
            }
            //Fechar conexao
            r.leaveGroup(InetAddress.getByName(RMIADRESS));
            r.close();
    	}catch(SocketTimeoutException e2){
            received.add("Servidor em baixo ou a demorar a responder");
            return received;
        }catch(IOException e){
    		System.out.println("Exception in UserServer.receber_pesquisa: "+e);
    	}
    	return received; 
	}







    //Metodo para enviar o url a ser indexado
  	public String indexar(String website){
  		String received=null;
  		try{
            //Create the socket but we don't bind it as we are only going to send data
            MulticastSocket s= new MulticastSocket();
            // We don't have to join the multicast group if we are only sending data

            //Enviar informação
            String msg="indexar "+website;
            System.out.println(msg);
            byte[] buf= msg.getBytes();
            //Create Datagram Packet
            DatagramPacket pack_sender=new DatagramPacket(buf,buf.length,InetAddress.getByName(MULTICASTGROUP),PORT);
            //Do send
            int ttl=2;
            s.send(pack_sender);
            s.setTimeToLive(ttl);
            s.close();
    	}catch(IOException e){
    	    System.out.println("Exception in UserServer.envia_indexar: "+e);
    	}
    	try{
            MulticastSocket r=new MulticastSocket(PORT);
            //Receber resposta do servidor
            r.joinGroup(InetAddress.getByName(RMIADRESS));
            byte[] buff=new byte[1024];
            DatagramPacket pack_receive=new DatagramPacket(buff,buff.length);
            r.receive(pack_receive);
            //Mostrar resposta
            received = new String(
              pack_receive.getData(), 0, pack_receive.getLength());
            //System.out.write(pack_receive.getData(),0,pack_receive.getLength());
            //Fechar conexao
            r.leaveGroup(InetAddress.getByName(RMIADRESS));
            r.close();
        }catch(IOException e){
    	    System.out.println("Exception in UserServer.receber_indexar: "+e);
    	}
        return received;
	}







    //Metodo para pedir e receber o historico do username que o chama
    public ArrayList<String> historico(String username){
		ArrayList<String> received= new ArrayList<String>();
        try{
            //Create the socket but we don't bind it as we are only going to send data
            MulticastSocket s= new MulticastSocket();
            // We don't have to join the multicast group if we are only sending data

            //Enviar informação
            String msg="historico "+username;
            System.out.println(msg);
            byte[] buf= msg.getBytes();
            //Create Datagram Packet
            DatagramPacket pack_sender=new DatagramPacket(buf,buf.length,InetAddress.getByName(MULTICASTGROUP),PORT);
            //Do send
            int ttl=s.getTimeToLive();
            s.send(pack_sender);
            s.setTimeToLive(ttl);
            s.close();
        }
        catch(IOException e){
            System.out.println("Exception in UserServer.historico: "+e);
        }
	    //Receber array do servidor
    	String pesquisado="";
	    byte[] buff=new byte[1024];
    	try{
	    	MulticastSocket r=new MulticastSocket(PORT);
	    	//Receber resposta do servidor
	    	r.joinGroup(InetAddress.getByName(RMIADRESS));
	    	//Vai recebendo os termos de pesquisa ate receber a string "FIM"
	    	while(!pesquisado.equals("FIM")){
		    	Arrays.fill(buff,(byte)0);
		    	DatagramPacket pack_receive=new DatagramPacket(buff,buff.length);
		    	r.setSoTimeout(TIMEOUT);
		    	r.receive(pack_receive);
		    	//Mostrar resposta
		    	pesquisado = new String(
		          pack_receive.getData(), 0, pack_receive.getLength());
		    	//System.out.write(pack_receive.getData(),0,pack_receive.getLength());
		    	
		    	if(!pesquisado.equals("FIM"))
		    	received.add(pesquisado);
	    	}
	    	//Fechar conexão
	    	r.leaveGroup(InetAddress.getByName(RMIADRESS));
	    	r.close();
    	}catch(SocketTimeoutException e2){
            received.add("Servidor em baixo ou a demorar a responder");
            return received;
        }catch(IOException e){
    		System.out.println("Exception in UserServer.receber_historico: "+e);
    	}
        return received;
    }




    //Metodo para pedir ao servidor para tornar um utilizador como admin
    public String tornar_admin(String Username){
    	String received=null;
    	try{
            //Create the socket but we don't bind it as we are only going to send data
            MulticastSocket s= new MulticastSocket();
            // We don't have to join the multicast group if we are only sending data

            //Enviar informação
            String msg="admin "+Username;
            System.out.println(msg);
            byte[] buf= msg.getBytes();
            //Create Datagram Packet
            DatagramPacket pack_sender=new DatagramPacket(buf,buf.length,InetAddress.getByName(MULTICASTGROUP),PORT);
            //Do send
            int ttl=2;
            s.send(pack_sender);
            s.setTimeToLive(ttl);
            s.close();
        } catch(IOException e){
    	    System.out.println("Exception in UserServer.envia_admin: "+e);
    	}

    	try{
            MulticastSocket r=new MulticastSocket(PORT);
            //Receber resposta do servidor
            r.joinGroup(InetAddress.getByName(RMIADRESS));
            byte[] buff=new byte[1024];
            DatagramPacket pack_receive=new DatagramPacket(buff,buff.length);
            r.setSoTimeout(TIMEOUT);
            r.receive(pack_receive);
            //Mostrar resposta
            received = new String(
              pack_receive.getData(), 0, pack_receive.getLength());
            //System.out.write(pack_receive.getData(),0,pack_receive.getLength());
            //Fechar conexao
            r.leaveGroup(InetAddress.getByName(RMIADRESS));
            r.close();
        }catch(SocketTimeoutException e2){
            received="Servidor em baixo ou a demorar a responder";
            return received;
        }catch(IOException e){
    	    System.out.println("Exception in UserServer.receber_admin: "+e);
        }
        return received;
    }


    //Metodo para pedir as informaçoes do sistema ao servidor
    public ArrayList <String> info(){
        ArrayList<String> received= new ArrayList<String>();
        String Multicaststring="";
        try{
            //Create the socket but we don't bind it as we are only going to send data
            MulticastSocket s= new MulticastSocket();
            // We don't have to join the multicast group if we are only sending data

            //Enviar informação
            String msg="info";
            System.out.println(msg);
            byte[] buf= msg.getBytes();
            //Create Datagram Packet
            DatagramPacket pack_sender=new DatagramPacket(buf,buf.length,InetAddress.getByName(MULTICASTGROUP),PORT);
            //Do send
            int ttl=s.getTimeToLive();
            s.send(pack_sender);
            s.setTimeToLive(ttl);
            s.close();
        } catch(IOException e){
            System.out.println("Exception in UserServer.enviarinfo: "+e);
        }
        //Receber array do servidor
        String pesquisado="";
        byte[] buff=new byte[16384];
        try{
            MulticastSocket r=new MulticastSocket(PORT);
            //Receber resposta do servidor
            r.joinGroup(InetAddress.getByName(RMIADRESS));
            while(!pesquisado.equals("FIM")){
                Arrays.fill(buff,(byte)0);
                DatagramPacket pack_receive=new DatagramPacket(buff,buff.length);
                r.setSoTimeout(TIMEOUT);
                r.receive(pack_receive);
                //Mostrar resposta
                pesquisado = new String(
                        pack_receive.getData(), 0, pack_receive.getLength());
                //System.out.write(pack_receive.getData(),0,pack_receive.getLength());

                if(!pesquisado.equals("FIM"))
                    received.add(pesquisado);
            }
            //Fechar conexão
            r.leaveGroup(InetAddress.getByName(RMIADRESS));
            r.close();
        }catch(SocketTimeoutException e2){
            received.add("Servidor em baixo ou a demorar a responder");
            return received;
        }catch(IOException e){
            System.out.println("Exception in UserServer.receberinfo: "+e);
        }

        //Adicionar informaçao dos servidores multicast

        Multicaststring="Multicast Servers: "+MULTICASTGROUP+"\n" +
                "Multicast notification threads: "+MULTICASTGROUP2+"\n" +
                "Multicast and RMI server port: "+PORT+"\n" +
                "Multicast and RMI threads port: "+PORT2+"\n";

        received.add(Multicaststring);
        return received;
    }



   


//=========================================================
	public static void main(String args[]) {
		int count=0;
		int maxtries=5;
		int PORT=7000;
		while(true){
			try{
				RMIInterface h=(RMIInterface) LocateRegistry.getRegistry(PORT).lookup("rmi://localhost/UserServer");
			}
			catch(Exception e){

				if (++count==maxtries){
					try {
						System.getProperties().put("java.security.policy","UserServer.policy");

						if (System.getSecurityManager() == null) {
							System.setSecurityManager(new SecurityManager());
						}
						UserServer u = new UserServer();
						Registry registo=LocateRegistry.createRegistry(7000);
						registo.rebind("rmi://localhost/UserServer",u);
						System.out.println("UserServer ready.");
					}
					catch (RemoteException re) {
						System.out.println("Exception in UserServer.main: " + re);
					} } } } } }