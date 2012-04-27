
/**
 *
 * @authors Fernando, Bernat & Dario
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class Sink {
    
    private ServerSocket server;
    protected int packet_Id;
    protected long timeStamp;
    protected int Traffic_class;
    private int packetloss;
    private long delay;
    private long delay_accumulated;
    private long jitter;
    private long jitter_accumulated;
    
    private long max_delay, min_delay;
    private long max_jitter, min_jitter;
    
  
    
    public Sink(int p){
        try {
	    int port = p;         
	    server = new ServerSocket(port);
	    //Espera fins a la connexio del client
	    Socket socket = server.accept();
	    String datagram;
            packetloss = 0;
            max_delay = 0; min_delay = 0;
            max_jitter = 0; min_jitter = 0;
            
            System.out.println("\nServidor escoltant al port " + p + "...\n");
            System.out.println("Packet ID   Delay   Jitter   Traffic Class");
            System.out.println("-------------------------------------------");
	    try{
		//Creem un buffer de lectura
		BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//Fem un bucle de lectura 
                String [] aux = new String[3]; 
                int i = 0;
                int packetId_previous = 0;
                long delay_previous = 0;
                long timeStamp_current;
		while ((datagram = rd.readLine()) != null) {
                    //Separem els trDes valors del datagrama
                    StringTokenizer st = new StringTokenizer(datagram);
                    while (st.hasMoreTokens()) {
                        aux[i] = st.nextToken();
                        i++;
                    }
                    timeStamp_current = System.currentTimeMillis();
                    i = 0; 
                    //assignem cada valor a la variable corresponent
                    packet_Id = Integer.parseInt(aux[0]);
                    timeStamp = Long.parseLong(aux[1]);
                    Traffic_class = Integer.parseInt(aux[2]);
                    //Comprovem si s'ha perdut algun paquet
                    PacketLoss(packet_Id, packetId_previous);
                    Delay(timeStamp, timeStamp_current);
                    Jitter(timeStamp, timeStamp_current, delay_previous);
                    DelayMinMax();
                    JitterMinMax();
                    
                    packetId_previous = packet_Id;
                    delay_previous = delay;
		}
		rd.close();
	    } catch (IOException e) {
	    }
        } catch (IOException e) {
        }
        System.out.println("El nombre total de paquets perduts es: " + packetloss + '\n');
        System.out.println("El delay mitja es: " + delay_accumulated/(packet_Id-packetloss) + '\n');
        System.out.println("Maxim delay: " + max_delay + '\n' + "Minim delay: " + min_delay + '\n');
        System.out.println("El jitter mitja es: " + jitter_accumulated/(packet_Id-packetloss) + '\n');
        System.out.println("Maxim jitter: " + max_jitter + '\n' + "Minim jitter: " + min_jitter + '\n');
    }
    
    
    private void PacketLoss (int packet_Id, int packetId_previous){
        // si el paquet rebut no es el seguent
         if (packet_Id - packetId_previous > 1)
             //s'han perdut tants paquets com la diferencia de IDs menys 1
             packetloss = packetloss + (packet_Id - packetId_previous) - 1;
    }
    
    private void Delay (long timeStamp, long timeStamp_current){
        delay = timeStamp_current - timeStamp;
        delay_accumulated = delay_accumulated + delay;
    }
    
    private void Jitter (long timeStamp, long timeStamp_current, long delay_previous){
        jitter = Math.abs(delay_previous - delay);
        System.out.println("   " + packet_Id + "          " + delay + "        " + jitter + "          " + Traffic_class + '\n');
        jitter_accumulated = jitter_accumulated + jitter;
    }
    
    /*Compare and update the min/max values of the delay with the current one*/
    private void DelayMinMax() {
        if (delay < min_delay)
                min_delay = delay;
        if (delay > max_delay) {
            max_delay = delay;
        }   
    }
    
    /*Compare and update the min/max values of the jitter with the current one*/
    private void JitterMinMax() {
        if (jitter < min_jitter)
                min_jitter = jitter;
        if (jitter > max_jitter) {
            max_jitter = jitter;
        } 
    }
    
        //Creem la funcio principal d'on obtindrem el port
    public static void main(String[] args){
        Sink sink = new Sink(Integer.parseInt(args[0]));
    }
}