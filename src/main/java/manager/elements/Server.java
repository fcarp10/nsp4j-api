package manager.elements;

import org.graphstream.graph.Node;

public class Server {

   private Node parent;
   private String id;
   private int capacity;
   private Double reliability;
   private int processDelay;

   public Server(String id, Node parent, int capacity, int processDelay) {
      this.id = id;
      this.parent = parent;
      this.capacity = capacity;
      this.processDelay = processDelay;
   }

   public Node getParent() {
      return parent;
   }

   public String getId() {
      return id;
   }

   public int getCapacity() {
      return capacity;
   }

   public Double getReliability() {
      return reliability;
   }

   public int getProcessDelay() {
      return processDelay;
   }


   public void setCapacity(int capacity) {
      this.capacity = capacity;
   }

   public void setReliability(Double reliability) {
      this.reliability = reliability;
   }

   public void setProcessDelay(int processDelay) {
      this.processDelay = processDelay;
   }
}
