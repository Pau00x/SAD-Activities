package xat;

import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final int PORT=4444;
    public static ConcurrentHashMap<String, Handler> clients = new ConcurrentHashMap<String, Handler>();
    public static void main(String[] args) throws Exception {
        System.out.println("The server is running...");
        var pool = Executors.newFixedThreadPool(500);
        try (MyServerSocket listener = new MyServerSocket(PORT)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }
    
    public static class Handler implements Runnable {
        private String name;
        private final MySocket socket;
        final int time = 50;
        private String lastMsg; 
        public BufferedReader in = null;
        public PrintWriter out = null;
        public Handler(MySocket sc){
            this.socket = sc;
            this.in = new BufferedReader(new InputStreamReader(this.socket.MyGetInputStream()));
            this.out = new PrintWriter(socket.MyGetOutputStream(), true);
        }
        @Override
        public void run(){  
            try (this.socket) {
                while(true){                  
                    this.out.print("Enter username: \n");
                    this.out.flush();
                    try{
                        this.name = this.in.readLine();
                    }catch(IOException e){
                        System.out.println(e);
                    }
                    if(!clients.containsKey(this.name)){
                        clients.put(this.name, this);
                        Server.clients.values().stream().map((ms) -> {
                            ms.out.print("-> "+this.name+" has joined the chat\n");
                            return ms;
                        }).forEachOrdered((ms) -> {
                            ms.out.flush();
                        });                     
                        System.out.println("NEW USER: "+this.name);
                        break;
                    }else{
                        this.out.println("NAME ALREADY TAKEN\n");
                        this.out.flush();
                    }                
                }
                while(true){
                    try {
                        if (this.in.ready()) {
                            this.lastMsg = this.in.readLine();
                            System.out.println("receive message: '" + this.lastMsg + "'");
                            if(this.lastMsg.equals("logout") || this.lastMsg.equals("Logout")){
                                clients.remove(this.name);
                                Server.clients.values().stream().map((ms) -> {
                                    ms.out.print("-> "+this.name+" has left the chat\n");
                                    return ms;
                                }).forEachOrdered((ms) -> {
                                    ms.out.flush();
                                });
                                break;
                            }
                            
                        }
                    } catch (IOException e) {                        
                        System.out.println(e);
                    }
                    if(!"{{reseted}}".equals(this.lastMsg)){
                        Server.clients.values().stream().map((ms) -> {
                            ms.out.print("-> "+this.name+": "+this.lastMsg+"\n");
                            return ms;
                        }).forEachOrdered((ms) -> {
                            ms.out.flush();
                        });
                    }
                    this.lastMsg = "{{reseted}}";
                }
            }
            try {
                this.in.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.out.close();
        }
    }
}
