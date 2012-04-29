/**
 *
 * @authors Fernando, Bernat & Dario
 */


public class Dropper {
    
    private int packetloss;
    private Queue queue; 
    
    public Dropper(Queue queue){
        packetloss = 0;
        this.queue = queue;
    }

    //Arriba un paquet y si hi ha espai a la cua el posem, sino el descartem
    public void insertPacket(String packet) {
        if (this.queue.getNumel() < this.queue.getSize()){
            this.queue.addPacket(packet);
            System.out.println("\nDropper: Paquet " + packet + " posat en cua");
        }
        else{
            this.dropPacket();
            System.out.println("\nDropper: Paquet " + packet + " descartat");
        } 
    }
    //Si descartem un pacquet augmentem el contador
    public void dropPacket(){
        packetloss++;
    }
    
    public int getPacketLoss(){
        return this.packetloss;
    }
}
