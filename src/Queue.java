/**
 *
 * @authors Fernando, Bernat & Dario
 */

import java.util.LinkedList;



public class Queue {
    
    //Llista on s'emmagatzemen els paquets
    private LinkedList<String> queue;
    private long size;
    
    //Iniciaitzem amb mida per defecte si no s'especifica tamany de cua
    public Queue(){
        this.queue = new LinkedList<String>();
        this.size = Long.MAX_VALUE;
    }
    //Inicialitem amb la mida que ens diguin
    public Queue(long s){
        this.queue = new LinkedList<String>();
        this.size = s;     
     }
    //Pasa el paquet al scheduler per a que ho envii a la Sink
    public String nextPacket() {
        return this.queue.pollFirst();
    }
    //Retorna el numero de elements en cua
    public long getNumel(){
        return this.queue.size();
    }
          
    //Retorna el tamany total de la cua
    public long getSize(){
        return this.size;
    }
    //Afegeix un paquet a la cua
    public void addPacket(String s){
        this.queue.add(s);
    }
}
