/**
 *
 * @authors Fernando, Bernat & Dario
 */


public class Dropper {
    
    private int packetloss;
    private Queue queue; 
    
    public Dropper(Scheduler sch){
        packetloss = 0;
        queue = new Queue(sch);
    }
    
    public Dropper(long s, Scheduler sch){
        packetloss = 0;
        queue = new Queue(s, sch);
    }
    
    public void insertPacket(String packet) {
        if (this.queue.getNumel() < this.queue.getSize())
            this.queue.addPacket(packet);
        else 
            this.dropPacket();
    }
    
    public void dropPacket(){
        packetloss++;
    }
}
