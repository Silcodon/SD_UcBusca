import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class MulticastServer2 extends Thread {
    private String MULTICAST_ADDRESS_RECEIVE = "224.0.224.2";
    private String MULTICAST_ADDRESS_SEND = "224.0.224.0";

    private int PORT = 4321;
    private int PORT2 = 4322;
    private long SLEEP_TIME = 5000;
    private static final int MAX_DEPTH = 2;
    private HashMap<String, HashSet<String>> lista = new HashMap<>();
    private HashMap<String, HashSet<String>> x = new HashMap<>();
    Map<String, Integer> countPesquisas = new TreeMap<>();


    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }


    public MulticastServer2() {
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
                    bw.write("Foi tornado admin");
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
                u = title + "#" + "@X";
                //System.out.println(u);

            }else {
                u = title + "#" + des;
            }
            return u;

    }



    public void urlToLink(String url, int depth){
            if(depth < MAX_DEPTH) {
                    depth++;


                    System.out.println("tttttttt");


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
                        break;
                    }else{
                        HashSet<String> x = null;
                        lista.put(word, x = new HashSet<String>(1));
                        lista.get(word).add(ws);
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
        File pes = new File("pesquisas.txt");
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    ServerSocket server=null;
        MulticastSocket socket=null;
        long counter = 0;
        System.out.println(this.getName() + " running...");
        try {
        	//creates ServerSocket
            server = new ServerSocket(1234);  // create socket binding it to PORT
            DataOutputStream os;
            DataInputStream is;
            socket = new MulticastSocket(PORT);  // create socket binding it to PORT
            socket.setTimeToLive(2);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS_RECEIVE);
            //Join Multicast Group
            socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS_RECEIVE));

            while (true) {
                // Now the socket is set up and we are ready to receive packets
                // wait for client request
            	Socket cliente=server.accept();
            	System.out.println("sal");
            	//Create I/O streams
            	is= new DataInputStream(cliente.getInputStream());
            	//Receive data from client

                String msg = new String(is.readUTF());
                String[] parts = msg.split(" ");
                String type = parts[0];
                System.out.println(type);


                boolean checkname;
                boolean checkpass;
                boolean checkadmin;

                // Do something useful with the data we just received,
                switch (type) {

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
                                                String a = d[0];
                                                String b = d[1];
                                                if (b.equals("@X")) {
                                                    String title = d[0];
                                                    MulticastSocket x = new MulticastSocket();
                                                    byte[] buf;
                                                    buf = (title + "\n" + st + "\n").getBytes();
                                                    DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                                    x.send(msgout);
                                                    x.close();
                                                    //System.out.println(title + "\n" + st + "\n");

                                                } else {
                                                    String title = d[0];
                                                    String des = d[1];
                                                    MulticastSocket c = new MulticastSocket();
                                                    byte[] buf;
                                                    buf = (title + "\n" + st + "\n" + des + "\n").getBytes();
                                                    DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                                                    c.send(msgout);
                                                    c.close();

                                                    //System.out.println(title + "\n" + st + "\n" + des + "\n");

                                                }
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

                    case "indexar":
                        FileOutputStream fos = new FileOutputStream(i);
                        String url = parts[1];

                        System.out.println("here");
                        urlToLink(url, 0);

                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(lista);
                        oos.close();
                        byte[] buf;
                        MulticastSocket s = new MulticastSocket();
                        buf = ("O url " + url + " foi indexado com sucesso").getBytes();
                        DatagramPacket msgout = new DatagramPacket(buf, buf.length, InetAddress.getByName(MULTICAST_ADDRESS_SEND), PORT);
                        s.send(msgout);
                        s.close();
                        cliente.close();


                        break;
                    case "info":
                        List<String> commonsearches = new ArrayList<String>();
                        List<String> ncommonsearches = new ArrayList<String>();
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
                        System.out.println(commonsearches);
                        System.out.println(ncommonsearches);
                        String resposta = "Pesquisas mais comuns:\n\n";
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
                        System.out.println(resposta);
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
