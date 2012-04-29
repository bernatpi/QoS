/**
 *
 * @authors Fernando, Bernat & Dario
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Buffer {
    
    private int inPort, outPort;
    private long queueSize;
    private char serviceTime;
    private double serviceRate;
    private ServerSocket server;
    private Scheduler scheduler;
    private Dropper dropper;
    private Queue queue;
    
    
    public Buffer(int inPort, int outPort, long queueSize, char serviceTime, double serviceRate) {
        try {
            //Inicialitzem els atributs
            this.inPort = inPort;
            this.outPort = outPort;
            this.queueSize = queueSize;
            this.serviceTime = serviceTime;
            this.serviceRate = serviceRate;
            //Creem una Cua, un Scheduler y un Dropper
            this.queue = new Queue(this.queueSize);
            this.scheduler = new Scheduler(this.queue, this.serviceTime, this.serviceRate, this.outPort);
            this.dropper = new Dropper(this.queue);
            // Connexió amb el generador
            this.server = new ServerSocket (inPort);
            Socket socketIN = server.accept();
            BufferedReader rd = new BufferedReader(new InputStreamReader(socketIN.getInputStream()));
            String datagram;
            //Iniciem el scheduler
            this.scheduler.start();
            //Connexió amb el receptor
            while((datagram = rd.readLine()) != null){
                //Pasem el paquet al dropper per a que el posi a la cua si hi ha espai
                this.dropper.insertPacket(datagram);
            }
        } catch (IOException e) {
        } 
        //Esperem a que el Scheduler acabi de enviar els paquets
        while (true){
            //Si no hi ha elements a la cua parem el scheduler
            if (this.queue.getNumel() == 0){
                this.scheduler.stop();
                break;
            }
            //Sino encara hi ha elements a la cua esperem 1 segon i comprovem una altra vegada
            else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        }
        System.out.println("\nEl nombre de paquets descartats ha sigut: " + this.dropper.getPacketLoss());
    }
    
    public static void main(String[] args){
        /*Arguments: port entrada, port sortida, mida cua, tipus service time, service rate*/
        //Les opcions per al service Time son e (exponencial) o d (deterministic)
        // Si es posa una diferent, per defecte forcem a que sigui exponencial
        char serviceTime = args[3].charAt(0);
        if (serviceTime != 'e' && serviceTime != 'd')
            serviceTime = 'e';
        Buffer buf = new Buffer(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                Long.parseLong(args[2]), serviceTime, Double.parseDouble(args[4]));
    }
}
