
/**
 *
 * @authors Fernando, Bernat & Dario
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class Generator {
    
    protected int packet_Id;
    protected long timeStamp;
    protected int[] Traffic_class;
    protected double rate;
    private int simTime;
    
    public Generator(String a, int p, double lambda){
        try {
	    InetAddress addr = InetAddress.getByName(a);
	    int port = p;
            packet_Id = 0;
            rate = lambda;
            simTime = 200;
            Traffic_class = new int[5];
            Traffic_class[0] = 0;
            Traffic_class[1] = 1;
            Traffic_class[2] = 2;
            Traffic_class[3] = 3;
            Traffic_class[4] = 4;
	    // Aquest constructor es bloqueja fins a que la connexio tingui exit
	    Socket socket = new Socket(addr, port);
	    //Creem un buffer de lectura
	    BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    //Creem un "buffer" de escriptura
	    DataOutputStream wr = new DataOutputStream(socket.getOutputStream());
	    
	    try {
		//Creem un String per a guardar el que s'escriu 
		String datagram = "";
                int i = 0;
		//Creem un bucle per a generar paquets
		while (datagram != null && i < simTime) {
                    //Creem un generador de nombres aleatoris
                    Random ran = new Random();
                    //generem un nombre aleatori entre 0 i 1
                    double uniform = ran.nextDouble();
                    //convertim a exponencial
                    double sleeptime = Math.log(1-uniform)/(-rate);
                    try{
                        Thread.sleep(Math.round(sleeptime*1000));
                    }catch(InterruptedException e){
                    }
                    //el primer packet_Id es 1
                    packet_Id++;
                    timeStamp = System.currentTimeMillis();
                    //generem el paquet
                    datagram = Integer.toString(packet_Id) + " " + Long.toString(timeStamp) + " " + Integer.toString(Traffic_class[ran.nextInt(5)]);
                    //Escrivim al socket el que acabem de generar
                    wr.writeBytes(datagram + '\n');
                    System.out.println("\nPacket " + packet_Id + " sent\n");
                    i++;
		}
	    } catch (IOException e) {
	    }
	    
	} catch (UnknownHostException e) {
	} catch (IOException e) {
        }
    }
    
       //Creem la funcio principals d'on obtindrem les dades
    public static void main(String[] args){
        Generator gen = new Generator(args[0], Integer.parseInt(args[1]), Double.parseDouble(args[2]));
    }
}
