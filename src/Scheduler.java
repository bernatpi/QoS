/**
 *
 * @authors Fernando, Bernat & Dario
 */

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class Scheduler implements Runnable{
    
    Thread th;
    private boolean exponential;
    private double serviceRate;
    private DataOutputStream wr;
    private Socket socket;
    private String packet;
    private Queue queue;
    //Es possible que es mdifiqui per diferents threads
    private volatile boolean shouldStop;
    
    public Scheduler(Queue queue, char serviceTime, double serviceRate, int port){
        if (serviceTime == 'e')
            this.exponential = true;
        else 
            this.exponential = false;
        //Creem el thread
        this.th = new Thread(this);
        this.queue = queue;
        this.serviceRate = serviceRate;
        this.shouldStop = false;
        //Socket y buffer de escriptura
        try {
            this.socket = new Socket(InetAddress.getByName("127.0.0.1"), port);
            this.wr = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
        }
    }
    
    //Iniciar el thread
    public void start(){
        this.th.start();
    }
    
    //Parar el thread
    public void stop(){
        this.shouldStop = true;
    }
    
    //Proces principal del thread
    @Override
    public void run(){
        while(!shouldStop){
            if(this.exponential){//Fem un sleep exponencial
                //Creem un generador de nombres aleatoris
                Random ran = new Random();
                //generem un nombre aleatori entre 0 i 1
                double uniform = ran.nextDouble();
                //convertim a exponencial
                double sleeptime = Math.log(1-uniform)/(-this.serviceRate);
                try{
                    this.th.sleep(Math.round(sleeptime*1000));
                }catch(InterruptedException e){
                }
            }
            else{//Fem un sleep deterministic
                //Creem un generador de nombres aleatoris
                Random ran = new Random();
                //generem un nombre aleatori entre 0 i serviceRate
                double sleeptime = this.serviceRate*ran.nextDouble();
                try{
                    this.th.sleep(Math.round(sleeptime*1000));
                }catch(InterruptedException e){
                }                
            }
            //Enviem el paquet corresponent sino esta buida la cua
            if ((this.packet = this.queue.nextPacket()) != null){
                try {
                    this.wr.writeBytes(this.packet + '\n');
                } catch (IOException e) {
                }
                System.out.println("\nScheduler: Paquet " + packet + " enviat\n");
            }
        } 
    }  
}
