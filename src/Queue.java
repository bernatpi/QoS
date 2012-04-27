/**
 *
 * @authors Fernando, Bernat & Dario
 */

import java.util.LinkedList;



public class Queue {
    
    private LinkedList<String> queue;
    private long size;
    private Scheduler scheduler;
    
    public Queue(Scheduler sch){
        this.queue = new LinkedList<>();
        this.size = Long.MAX_VALUE;
        this.scheduler = sch;
    }
    
    public Queue(long s, Scheduler sch){
        this.queue = new LinkedList<>();
        this.size = s;
        this.scheduler = sch;
      
     }
    
    public void nextPacket() {
        this.scheduler.sendToSink(queue.pollFirst());
    }
    
    public long getNumel(){
        return this.queue.size();
    }
    
    public long getSize(){
        return this.size;
    }
    
    public void addPacket(String s){
        this.queue.add(s);
    }
}
