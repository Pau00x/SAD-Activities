/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//<script>
package finalxat;

/**
 *
 * @author enric.carrera.aguiar
 */    

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author enric.carrera.aguiar
 */
public class MyServerSocket extends ServerSocket {
    ServerSocket ss;
    MySocket sc;
    public MyServerSocket(int port) throws IOException{
       
        this.ss = new ServerSocket(port);
    }
    
    @Override
    public MySocket accept(){
        try {
            this.sc = new MySocket(ss.accept());
            return sc;
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return  null;
    }
    @Override
    public void close(){
        try {
            this.ss.close();
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}


