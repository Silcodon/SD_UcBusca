
import com.sun.javafx.applet.ExperimentalExtensions;
import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.crypto.Data;
import java.net.*;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.lang.*;
import java.io.File;
import java.util.stream.Collectors;

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS_RECEIVE = "224.0.224.8";
    private String MULTICAST_ADDRESS_SEND = "224.0.224.9";

    private int PORT = 80;
    private int PORT2 = 4322;
    private long SLEEP_TIME = 5000;
    private static final int MAX_DEPTH = 2;
    private HashMap<String, HashSet<String>> lista = new HashMap<>();
    private HashMap<String, HashSet<String>> x = new HashMap<>();
    private HashMap<String, HashSet<String>> urlspesquisa = new HashMap<>();
    Map<String, Integer> countPesquisas = new TreeMap<>();
    Map<String, Integer> countLinks = new TreeMap<>();

    int num = 0;


    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }


    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }
    public boolean verifica_username(String username, File file) {
        try{
            String current;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((current = br.readLine()) != null) {

                String[] dados = current.split(" ");
                if(dados[0].equalsIgnoreCase(username)){
                    return true;
                }
            }
           
        }catch(IOException e){
            System.out.println("IOException");
        }
        return false;
        

    }

    public boolean verifica_password(String password, File file) {
        try{
            String current;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((current = br.readLine()) != null) {
                String[] dados = current.split(" ");
                if(dados[1].equals(password)){
                    return true;
                }
            }
           
        }catch(IOException e){
            System.out.println("IOException");
        }
        return false;
        

    }


    public void modifyFile(String current, File file,int linenumber){
    	try{

            String oldtext = current ;
           
            
             //Troca linha no ficheiro
             String newtext = oldtext.replaceAll("0", "1");
            
            modifyLine(linenumber,newtext,file);
         }
         catch (Exception ioe)
             {
             ioe.printStackTrace();
         }
    }

    public static void modifyLine(int lineNumber, String newtext, File file){
    	try{
	    	Path path = Paths.get("DB.txt");
	    	List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	    	lines.set(lineNumber - 1, newtext);
	    	Files.write(path, lines, StandardCharsets.UTF_8);
	    }catch(Exception e){
	    	System.out.println("Error writing to file");
	    }
    }

 

    public void tornar_admin(String username, File file) {
        try{
        	int contador=1;
            String current;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((current = br.readLine()) != null) {
                String[] dados = current.split(" ");
                if(dados[0].equals(username)){

                	modifyFile(current, file,contador);
                    
                }
                 contador +=1;
            }
            File i = new File(username + "Noti.txt");
            if (i.createNewFile()) {
                    System.out.println("File is created!");
                    FileWriter f = new FileWriter(i.getAbsoluteFile(), true);
                    BufferedWriter bw = new BufferedWriter(f);
                    bw.write("Foi tornado admin ");
                    bw.close();

            } else {
                System.out.println("File already exists.");
            }
        return;
        

    } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }}


        public boolean verifica_admin(String username, File file) {
        try{
            String current;
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((current = br.readLine()) != null) {
                String[] dados = current.split(" ");
               
                if(dados[0].equals(username) && dados[2].equals("1")){
                    return true;
                }
            }
           
        }catch(IOException e){
            System.out.println("IOException");
        }
        return false;
        

    }

    public String getDesTitle(String url) {

            String ws = url;
            if (!ws.startsWith("http://") && !ws.startsWith("https://"))
                ws = "http://".concat(ws);

            // Attempt to connect and get the document
        Document doc = null;  // Documentation: https://jsoup.org/
        try {
            doc = Jsoup.connect(ws).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Title
            String title = doc.title();
            String des = "";
            String u = null;
            Elements paragraf = doc.select("p");
            for (Element p : paragraf) {
                //System.out.println(ws + " | " + p.text());
                des = p.text();
                break;
            }
            if (des.length() == 0){
                int sum=0;
                String paragraphs = doc.body().text();
                paragraphs.concat(".");
                String[] ateponto = paragraphs.split("\\.");
                String[] ateponto1 = ateponto[0].split(" ");
                try {
                    if (ateponto1.length > 100) {
                        des = paragraphs.substring(0, 100);
                    }else{
                        des = ateponto[0].concat(".");
                    }

                }catch(Exception e){
                    des = "Sem descrição";
                }
                u = title + "#" + des;

                //System.out.println(u);

            }else {
                u = title + "#" + des;
            }
            return u;

    }



    public void urlToLink(String url, int depth){



            if(depth <= MAX_DEPTH) {
                    depth++;
                    num++;


                    System.out.println("Url nmr: "+ num);


                    //System.out.print("Website: ");
                    try {
                        String ws = url;
                        if (!ws.startsWith("http://") && !ws.startsWith("https://"))

                            ws = "http://".concat(ws);

                        // Attempt to connect and get the document
                        Document doc = Jsoup.connect(ws).get();  // Documentation: https://jsoup.org/

                        // Title
                        String title = doc.title();


                        // Get all links
                        Elements links = doc.select("a[href]");
                        for (Element link : links) {
                            // Ignore bookmarks within the page
                            if (link.attr("href").startsWith("#")) {
                                continue;
                            }

                            // Shall we ignore local links? Otherwise we have to rebuild them for future parsing
                            if (!link.attr("href").startsWith("http")) {
                                continue;
                            }
                            if(!urlspesquisa.containsKey(link.attr("href").toString())){
                                HashSet<String> temp = new HashSet<String>();
                                temp.add(ws);
                                urlspesquisa.put(link.attr("href").toString(), temp);
                            }
                            if(urlspesquisa.containsKey(link.attr("href").toString())){
                                urlspesquisa.get(link.attr("href").toString()).add(ws);
                            }
                            if(!countLinks.containsKey(link.attr("href"))){
                                countLinks.put(link.attr("href"), 1);
                            }
                            else{
                                //System.out.println("entrou aqui");
                                countLinks.put(link.attr("href"), countLinks.get(link.attr("href"))+1);
                                //System.out.println(countLinks.get(link.attr("href")));
                            }

                            //Iterator para ver se link já está indexado

                            urlToLink(link.attr("href"), depth);
                            //System.out.println("Text: " + link.text() + "\n");
                        }

                        // Get website text and count words
                        String text = doc.text(); // We can use doc.body().text() if we only want to get text from <body></body>
                        countWords(text, ws);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }





    }

    private void countWords(String text, String ws) {
        //Map<String, Integer> countMap = new TreeMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
        String line;

        // Get words and respective count
        while (true) {
            try {
                if ((line = reader.readLine()) == null)
                    break;
                String[] words = line.split("[ ,;:.?!“”(){}\\[\\]<>']+");
                for (String word : words) {
                    word = word.toLowerCase();
                    if ("".equals(word)) {
                        continue;
                    }
                    if (lista.containsKey(word)){
                        lista.get(word).add(ws);
                    }else{
                        HashSet<String> temp = new HashSet<>();
                        temp.add(ws);
                        lista.put(word, temp);
                    }

                    /*if (!countMap.containsKey(word)) {
                        countMap.put(word, 1);
                    }
                    else {
                        countMap.put(word, countMap.get(word) + 1);
                    }*/
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Close reader
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Display words and counts
        /*for (String word : countMap.keySet()) {
            if (word.length() >= 3) { // Shall we ignore small words?
                //System.out.println(word + "\t" + countMap.get(word));
            }
        }*/
    }



    public void getHist(String username){
        try {

            String current;
            BufferedReader br = new BufferedReader(new FileReader(username + "Hist.txt"));
            while ((current = br.readLine()) != null) {
                MulticastSocket a = new MulticastSocket();

                byte[] buf;
                buf = current.getBytes();
                DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                a.send(msgout);
                a.close();
            }

            MulticastSocket s = new MulticastSocket();
            byte[] buf;
            buf = "FIM".getBytes();
            DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
            s.send(msgout);
            s.close();
        }catch (UnknownHostException ex) {
        ex.printStackTrace();
        } catch (FileNotFoundException ex) {
        ex.printStackTrace();
        } catch (IOException ex) {
        ex.printStackTrace();
    }




    }

    public void run() {

        File i = new File("urls.txt");
        File abc = new File("urls_urls.txt");
        File pes = new File("pesquisas.txt");
        File pop = new File("linksPop.txt");
        try {
            if (i.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                BufferedReader br = new BufferedReader(new FileReader("urls.txt"));
                if (br.readLine() == null) {
                    System.out.println("File is created!");

                }else {

                    System.out.println("File already exists.");
                    FileInputStream fis = null;
                    fis = new FileInputStream(i);

                    ObjectInputStream ois = null;
                    ois = new ObjectInputStream(fis);

                    lista = (HashMap<String, HashSet<String>>) ois.readObject();
                    System.out.println(lista);
                    System.out.println(lista.size());
                }
            }

            if (abc.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                BufferedReader bra = new BufferedReader(new FileReader("urls_urls.txt"));
                if (bra.readLine() == null) {
                    System.out.println("File is created!");

                }else {

                    System.out.println("File already exists.");
                    FileInputStream fiss = null;
                    fiss = new FileInputStream(abc);

                    ObjectInputStream oiss = null;
                    oiss = new ObjectInputStream(fiss);

                    urlspesquisa = (HashMap<String, HashSet<String>>) oiss.readObject();
                    System.out.println(urlspesquisa);
                    System.out.println(urlspesquisa.size());
                }
            }

            if (pes.createNewFile())
            {
                System.out.println("File is created!");
            } else {
                BufferedReader br = new BufferedReader(new FileReader("pesquisas.txt"));
                if (br.readLine() == null) {
                    System.out.println("File is created!");
                    System.out.println(countPesquisas);

                }else {

                    System.out.println("File already exists.");
                    FileInputStream fis = null;
                    fis = new FileInputStream(pes);

                    ObjectInputStream ois = null;
                    ois = new ObjectInputStream(fis);

                    countPesquisas = (Map<String, Integer>) ois.readObject();
                    System.out.println(countPesquisas);
                }
            }

            if (pop.createNewFile())
            {
                System.out.println("File is created!");

            } else {
                BufferedReader br = new BufferedReader(new FileReader("linksPop.txt"));
                if (br.readLine() == null) {
                    System.out.println("File is created!");

                }else {

                    System.out.println("File already exists.");
                    FileInputStream fis = null;
                    fis = new FileInputStream(pop);

                    ObjectInputStream ois = null;
                    ois = new ObjectInputStream(fis);

                    countLinks = (Map<String, Integer>) ois.readObject();
                    System.out.println(countLinks);
                    System.out.println(countLinks.size());
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        MulticastSocket socket = null;

        long counter = 0;
        System.out.println(this.getName() + " running...");
        try {
            socket = new MulticastSocket(PORT);  // create socket binding it to PORT
            socket.setTimeToLive(2);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS_RECEIVE);
            //Join Multicast Group
            socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS_RECEIVE));


            
            File file = new File("DB.txt");
            file.createNewFile();
            while (true) {
                // Now the socket is set up and we are ready to receive packets
                byte buf1[] = new byte[1024];
                DatagramPacket pack = new DatagramPacket(buf1, buf1.length);
                socket.receive(pack);

                String msg = new String(pack.getData(), 0, pack.getLength());
                String[] parts = msg.split(" ");
                String type = parts[0];
                System.out.println(type);


                boolean checkname;
                boolean checkpass;
                boolean checkadmin;

                // Do something useful with the data we just received,
                switch (type) {

                    case "login":
                        String username1 = parts[1];
                        String password1 = parts[2];
                        String status;
                        checkname = verifica_username(username1, file);
                        checkpass = verifica_password(password1, file);
                        checkadmin = verifica_admin(username1, file);
                        if (checkname) {
                            if (checkpass) {
                                if (checkadmin) {
                                    System.out.println("aaaaaaa");
                                    status = "on";
                                    MulticastSocket s = new MulticastSocket();
                                    byte buf2[];
                                    buf2 = "Login como Admin com sucesso!".getBytes();
                                    DatagramPacket msgout = new DatagramPacket(buf2, buf2.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                    s.send(msgout);
                                    s.close();

                                } else {
                                    status = "on";
                                    MulticastSocket s = new MulticastSocket();
                                    byte buf2[];
                                    buf2 = "Login com sucesso!".getBytes();
                                    DatagramPacket msgout = new DatagramPacket(buf2, buf2.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                    s.send(msgout);
                                    s.close();
                                }

                            } else {
                                byte buf3[];
                                MulticastSocket s = new MulticastSocket();

                                buf3 = "Password incorreta!".getBytes();
                                DatagramPacket msgout = new DatagramPacket(buf3, buf3.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                s.send(msgout);
                                s.close();

                            }
                        } else {
                            byte buf4[];
                            MulticastSocket s = new MulticastSocket();
                            buf4 = "User não registado!".getBytes();
                            DatagramPacket msgout = new DatagramPacket(buf4, buf4.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                            s.send(msgout);
                            s.close();

                        }

                        break;
                    case "register":

                        String username2 = parts[1];
                        String password2 = parts[2];
                        checkname = verifica_username(username2, file);
                        if (checkname) {
                            byte buf5[];
                            MulticastSocket s = new MulticastSocket();
                            buf5 = "User já registado!".getBytes();
                            DatagramPacket msgout = new DatagramPacket(buf5, buf5.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                            s.send(msgout);
                            s.close();

                        } else {

                            FileWriter f = new FileWriter(file.getAbsoluteFile(), true);
                            BufferedWriter bw = new BufferedWriter(f);
                            if (file.length() == 0) {
                                bw.write(username2 + " " + password2 + " 1");
                            } else {
                                bw.write(username2 + " " + password2 + " 0");
                            }
                            bw.newLine();
                            bw.close();
                            byte buf6[];
                            MulticastSocket s = new MulticastSocket();
                            buf6 = "Registou-se com sucesso!".getBytes();
                            DatagramPacket msgout = new DatagramPacket(buf6, buf6.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                            s.send(msgout);
                            s.close();


                        }


                        break;

                    case "notifica":
                        try {
                            MulticastSocket s = new MulticastSocket();
                            String username6 = parts[1];
                            String filename = username6 + "Noti.txt";
                            File f2 = new File(filename);
                            //delete file to only send 1 notification
                            if (f2.exists()) {
                                BufferedReader br = new BufferedReader(new FileReader(f2));
                                String st;
                                st =br.readLine();
                                br.close();
                                if(f2.delete())
                                {
                                    System.out.println("File deleted successfully");
                                }
                                if (!st.equals(null)) {
                                    st=username6 +" "+st;
                                    byte[] buf = st.getBytes();
                                    DatagramPacket pack_sender = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                    int ttl = s.getTimeToLive();
                                    s.send(pack_sender);
                                    s.setTimeToLive(ttl);
                                    s.close();

                                }
                            }else{
                                byte[] buf = (username6+" Não existem notificações").getBytes();
                                DatagramPacket pack_sender = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                int ttl = s.getTimeToLive();
                                s.send(pack_sender);
                                s.setTimeToLive(ttl);
                                s.close();

                            }


                        } catch (IOException e) {
                            System.out.println("Error in notification thread: " + e);
                        }

                    case "pesquisar":
                        FileOutputStream foos = new FileOutputStream(pes);

                        try {
                            //Escrever para o ficheiro de historico de pesquisa
                            System.out.println(parts.length);
                            String username5 = parts[1];
                            List<String> pesquisa = new ArrayList<>();
                            for (int j = 2; j < parts.length; j++) {
                                pesquisa.add(parts[j]);
                            }
                            System.out.println(pesquisa);
                            if (!username5.equals("null")) {

                                String filename = username5 + "Hist.txt";

                                File f = new File(filename);
                                //Create the file
                                if (f.exists() && f.isFile()) {
                                    for (String pal : pesquisa) {
                                        Files.write(Paths.get(filename), (pal + " ").getBytes(), StandardOpenOption.APPEND);

                                    }

                                    Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);


                                } else {
                                    f.createNewFile();
                                    for (String pal : pesquisa) {
                                        Files.write(Paths.get(filename), (pal + " ").getBytes(), StandardOpenOption.APPEND);

                                    }
                                    Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);
                                }
                            }
                            boolean e = false;
                            if (pesquisa.size() > 1) {
                                for (int k = 0; k < pesquisa.size(); k++) {
                                    if (pesquisa.get(k).length() <= 3) {
                                        pesquisa.remove(k);
                                    }
                                }
                            }
                            String key = "";
                            for (String a : pesquisa) {
                                key += a + " ";

                            }
                            if (!countPesquisas.containsKey(key)) {
                                countPesquisas.put(key, 1);

                            } else {
                                countPesquisas.put(key, countPesquisas.get(key) + 1);
                            }
                            ObjectOutputStream ooos = new ObjectOutputStream(foos);
                            ooos.writeObject(countPesquisas);
                            ooos.close();


                            //System.out.println(pesquisa);
                            List<String> resultados = new ArrayList<String>();
                            int sum = 0;
                            for (String nome : lista.keySet()) {
                                for (String p : pesquisa) {
                                    if (p.equalsIgnoreCase(nome)) {
                                        e = true;

                                        HashSet<String> msg2 = lista.get(nome);
                                        //System.out.println(msg2.toString());

                                        for (String st : msg2) {
                                            for (String s : resultados) {
                                                if (s.equals(st)) {
                                                    sum += 1;
                                                    break;
                                                }
                                            }
                                            if (sum == 0) {
                                                resultados.add(st);
                                                String u = getDesTitle(st);
                                                //System.out.println(u+"aaa");
                                                String[] d = u.split("#");


                                                    String title = d[0];
                                                    String des = d[1];
                                                    MulticastSocket c = new MulticastSocket();
                                                    byte[] buf;
                                                    buf = (title + "|XXX|" + st + "|XXX|" + des + "|XXX|").getBytes();
                                                    DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                                    c.send(msgout);
                                                    c.close();

                                                    //System.out.println(title + "\n" + st + "\n" + des + "\n");


                                            }

                                        }

                                    }


                                }


                            }
                            if (!e) {
                                MulticastSocket c = new MulticastSocket();
                                byte[] buf;
                                buf = "Nenhum resultado encontrado".getBytes();
                                DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                c.send(msgout);
                                c.close();

                            }
                            byte[] buff;
                            MulticastSocket a = new MulticastSocket();
                            buff = "FIM".getBytes();
                            DatagramPacket msgout1 = new DatagramPacket(buff, buff.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                            a.send(msgout1);
                            a.close();

                        } catch (UnknownHostException e) {
                            System.out.println(e.getStackTrace());
                        }
                        break;

                    case "link":
                        System.out.println("entrou no case");
                        FileOutputStream faos = new FileOutputStream(pes);

                        try {
                            //Escrever para o ficheiro de historico de pesquisa
                            System.out.println(parts.length);
                            String username5 = parts[1];
                            List<String> pesquisa = new ArrayList<>();
                            pesquisa.add(parts[2]);
                            System.out.println(pesquisa);
                            if (!username5.equals("null")) {

                                String filename = username5 + "Hist.txt";

                                File f = new File(filename);
                                //Create the file
                                if (f.exists() && f.isFile()) {
                                    for (String pal : pesquisa) {
                                        Files.write(Paths.get(filename), (pal + " ").getBytes(), StandardOpenOption.APPEND);

                                    }

                                    Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);


                                } else {
                                    f.createNewFile();
                                    for (String pal : pesquisa) {
                                        Files.write(Paths.get(filename), (pal + " ").getBytes(), StandardOpenOption.APPEND);

                                    }
                                    Files.write(Paths.get(filename), "\n".getBytes(), StandardOpenOption.APPEND);
                                }
                            }
                            boolean e = false;
                            if (pesquisa.size() > 1) {
                                for (int k = 0; k < pesquisa.size(); k++) {
                                    if (pesquisa.get(k).length() <= 3) {
                                        pesquisa.remove(k);
                                    }
                                }
                            }
                            String key = "";
                            for (String a : pesquisa) {
                                key += a + " ";

                            }
                            if (!countPesquisas.containsKey(key)) {
                                countPesquisas.put(key, 1);

                            } else {
                                countPesquisas.put(key, countPesquisas.get(key) + 1);
                            }
                            ObjectOutputStream ooos = new ObjectOutputStream(faos);
                            ooos.writeObject(countPesquisas);
                            ooos.close();


                            //System.out.println(pesquisa);
                            List<String> resultados = new ArrayList<String>();
                            int sum = 0;
                            for (String nome : urlspesquisa.keySet()) {
                                for (String p : pesquisa) {
                                    if (p.equalsIgnoreCase(nome)) {
                                        e = true;

                                        HashSet<String> msg2 = urlspesquisa.get(nome);
                                        //System.out.println(msg2.toString());

                                        for (String st : msg2) {
                                            for (String s : resultados) {
                                                if (s.equals(st)) {
                                                    sum += 1;
                                                    break;
                                                }
                                            }
                                            if (sum == 0) {
                                                resultados.add(st);
                                                //System.out.println(u+"aaa");
                                                MulticastSocket c = new MulticastSocket();
                                                byte[] buf;
                                                buf = (st).getBytes();
                                                DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                                c.send(msgout);
                                                c.close();

                                                //System.out.println(title + "\n" + st + "\n" + des + "\n");


                                            }

                                        }

                                    }


                                }


                            }
                            if (!e) {
                                MulticastSocket c = new MulticastSocket();
                                byte[] buf;
                                buf = "Nenhum resultado encontrado".getBytes();
                                DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                c.send(msgout);
                                c.close();

                            }
                            byte[] buff;
                            MulticastSocket a = new MulticastSocket();
                            buff = "FIM".getBytes();
                            DatagramPacket msgout1 = new DatagramPacket(buff, buff.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                            a.send(msgout1);
                            a.close();

                        } catch (UnknownHostException e) {
                            System.out.println(e.getStackTrace());
                        }
                        break;
                    case "admin":
                        String username3 = parts[1];
                        checkname = verifica_username(username3, file);
                        System.out.println(checkname);
                        if (checkname) {
                            checkadmin = verifica_admin(username3, file);
                            if (checkadmin) {
                                byte[] buf;
                                MulticastSocket s = new MulticastSocket();
                                buf = ("O user " + username3 + " já é admin!").getBytes();
                                DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                s.send(msgout);
                                s.close();


                            } else {
                                tornar_admin(username3, file);
                                byte[] buf;
                                MulticastSocket s = new MulticastSocket();
                                buf = ("O user " + username3 + " passou a ser admin!").getBytes();
                                DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                s.send(msgout);
                                s.close();

                            }
                        } else {
                            System.out.println(username3);
                            byte[] buf;
                            MulticastSocket s = new MulticastSocket();
                            buf = ("O user " + username3 + " não existe !").getBytes();
                            DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                            s.send(msgout);
                            s.close();
                        }
                        break;
                    case "historico":
                        String username4 = parts[1];
                        System.out.println(username4);
                        File ficheiro = new File(username4 + "Hist.txt");
                        if (ficheiro.exists() && ficheiro.isFile() == true) {
                            getHist(username4);
                        } else {

                            byte[] buff;
                            MulticastSocket a = new MulticastSocket();
                            buff = "User nao tem historico".getBytes();
                            DatagramPacket msgout = new DatagramPacket(buff, buff.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                            a.send(msgout);

                            buff = "FIM".getBytes();
                            DatagramPacket msgout1 = new DatagramPacket(buff, buff.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                            a.send(msgout1);
                            a.close();
                        }
                        break;
                    case "indexar":
                            int count = 0;
                            FileOutputStream fooos = new FileOutputStream(pop);
                            FileOutputStream fos = new FileOutputStream(i);
                            String url = parts[1];

                            System.out.println("here");
                            //Iterator para ver se url já não foi indexado
                            Iterator it = lista.entrySet().iterator();
                            List<String> where = new ArrayList<String>();
                            while (it.hasNext()) {
                                HashMap.Entry key = (HashMap.Entry)it.next();
                                where.add(key.getValue().toString());
                                it.remove(); // avoids a ConcurrentModificationException
                            }
                            List<String> where1 = new ArrayList<String>();
                            List<String> where2 = new ArrayList<String>();
                            for(String xx : where){
                                xx = xx.replace("[", "");
                                xx=xx.replace("]", "");
                                String[] temp1 = xx.split(", ");
                                if (temp1.length >0) {
                                    for (String ss : temp1) {
                                        where1.add(ss);
                                    }
                                }else{
                                    where1.add(xx);
                                }
                            }
                            for(String ooo : where1){
                                System.out.println("url a indexar +++++++"+url);
                                System.out.println(ooo);
                                if (ooo.equals("http://" + url)){
                                    count++;
                                    break;
                                }
                            }

                            if (count > 0){
                                byte[] buf;
                                MulticastSocket s = new MulticastSocket();
                                buf = ("O url " + url + " já se encontra indexado").getBytes();
                                DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                s.send(msgout);
                                s.close();
                            }else {
                                urlToLink(url, 0);
                                ObjectOutputStream ooos = new ObjectOutputStream(fooos);
                                ooos.writeObject(countLinks);
                                ooos.close();
                                System.out.println(countLinks);

                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(lista);
                                oos.close();
                                byte[] buf;
                                MulticastSocket s = new MulticastSocket();
                                buf = ("O url " + url + " foi indexado com sucesso").getBytes();
                                DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                s.send(msgout);
                                s.close();
                            }





                        break;
                    case "info":

                        List<String> commonsearches = new ArrayList<String>();
                        List<String> ncommonsearches = new ArrayList<String>();

                        List<String> popLinks = new ArrayList<String>();
                        List<String> npopLinks = new ArrayList<String>();
                        int tam = countPesquisas.size();

                        String higher = "Ainda não existem pesquisas";
                        if (countPesquisas.isEmpty()) {
                            commonsearches.add(higher);
                        } else {
                            int k = 0;
                            if (tam < 10) {
                                while (k < tam) {
                                    int maior = 0;
                                    for (Map.Entry<String, Integer> entry : countPesquisas.entrySet()) {
                                        if (entry.getValue() >= maior && !commonsearches.contains(entry.getKey())) {
                                            higher = entry.getKey();
                                            maior = entry.getValue();
                                        }
                                    }
                                    commonsearches.add(higher);
                                    ncommonsearches.add(Integer.toString(maior));
                                    k++;
                                }
                            } else {
                                while (k < 10) {
                                    int maior = 0;
                                    for (Map.Entry<String, Integer> entry : countPesquisas.entrySet()) {
                                        if (entry.getValue() >= maior && !commonsearches.contains(entry.getKey())) {
                                            higher = entry.getKey();
                                            maior = entry.getValue();
                                        }
                                    }
                                    commonsearches.add(higher);
                                    ncommonsearches.add(Integer.toString(maior));
                                    k++;
                                }
                            }
                        }

                        int tama = countLinks.size();

                        String higherpop = "Ainda não foram indexados url's";
                        if (countLinks.isEmpty()) {
                            commonsearches.add(higherpop);
                        } else {
                            int k = 0;
                            if (tama < 10) {
                                while (k < tama) {
                                    int maior = 0;
                                    for (Map.Entry<String, Integer> entry : countLinks.entrySet()) {
                                        if (entry.getValue() != null && entry.getValue() >= maior && !popLinks.contains(entry.getKey())) {
                                            higherpop = entry.getKey();
                                            maior = entry.getValue();
                                        }
                                    }
                                    popLinks.add(higherpop);
                                    npopLinks.add(Integer.toString(maior));
                                    k++;
                                }
                            } else {
                                while (k < 10) {
                                    int maior = 0;
                                    for (Map.Entry<String, Integer> entry : countLinks.entrySet()) {
                                        if (entry.getValue() != null && entry.getValue() >= maior && !popLinks.contains(entry.getKey())) {
                                            higherpop = entry.getKey();
                                            maior = entry.getValue();
                                        }
                                    }
                                    popLinks.add(higherpop);
                                    npopLinks.add(Integer.toString(maior));
                                    k++;
                                }
                            }
                        }

                        System.out.println(popLinks);
                        System.out.println(npopLinks);

                        System.out.println(commonsearches);
                        System.out.println(ncommonsearches);
                        String resposta = "\nSites mais importantes:\n\n";

                        int conta = 0;
                        for (String v : popLinks){
                            //System.out.println(v);
                            resposta = resposta.concat(npopLinks.get(conta)+ " links vão dar ao site " + v  +"\n");
                            conta++;
                        }
                        resposta = resposta.concat("\nPesquisas mais comuns:\n\n");
                        int cont = 0;
                        for (String v : commonsearches){
                            System.out.println(v);
                            resposta = resposta.concat(v+ " foi pesquisado "  + ncommonsearches.get(cont)+ " vezes!"+"\n");
                            cont++;
                        }



                        byte[] buff;
                        MulticastSocket ss = new MulticastSocket();
                        buff = resposta.getBytes();
                        DatagramPacket msgoutt = new DatagramPacket(buff, buff.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                        ss.send(msgoutt);
                        ss.close();
                        byte[] buff1;
                        MulticastSocket sss = new MulticastSocket();
                        buff1 = "FIM".getBytes();
                        DatagramPacket msgoutt1 = new DatagramPacket(buff1, buff1.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                        sss.send(msgoutt1);
                        sss.close();
                        //System.out.println(resposta);
                        break;
                }





                try { sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
            }
        } catch (IOException  e) {
            e.printStackTrace();
        } finally {
            try{
            socket.leaveGroup(InetAddress.getByName(MULTICAST_ADDRESS_RECEIVE));}
            catch(Exception e){
                System.out.println("Erro");
            }
            socket.close();

        }
    }
}
