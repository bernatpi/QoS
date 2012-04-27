
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author U58434
 */
public class Buffer {
    
    private int inPort, outPort;
    private long queueSize;
    private boolean exp;
    private double serviceRate;
    private ServerSocket server;
    
    public Buffer(int inPort, int outPort, long queueSize, char st, double serviceRate) {
        try {
            
            // Connexió amb el generador
            server = new ServerSocket (inPort);
            Socket socketIN = server.accept();
            BufferedReader rd = new BufferedReader(new InputStreamReader(socketIN.getInputStream()));
            
            //Connexió amb el receptor
            
            Socket socketOUT = new Socket (InetAddress.getByName("127.0.0.1"), outPort);	    
	    DataOutputStream wr = new DataOutputStream(socketOUT.getOutputStream());
            
        } catch (IOException e) {
        }
        
        
    }
    
    public static void main(String[] args){
        /*Arguments: port entrada, port sortida, mida cua, tipus service time, service rate*/
        Buffer buf = new Buffer(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                Long.parseLong(args[2]),args[3].charAt(0), Double.parseDouble(args[4]));
    }
}
