package manager.elements;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {

   private int id;
   private int[] chain;
   private transient List<Function> functions;
   private TrafficFlow trafficFlow;
   private Map<String, Object> attributes;

   public Service() {
      attributes = new HashMap<>();
   }

   public Service(Service serviceChain, List<Function> functions) {
      this.id = serviceChain.getId();
      this.functions = functions;
      this.trafficFlow = serviceChain.getTrafficFlow();
      this.attributes = serviceChain.getAttributes();
   }

   public Object getAttribute(String key) {
      return attributes.get(key);
   }

   public Map<String, Object> getAttributes() {
      return attributes;
   }

   public int getId() {
      return id;
   }

   public int[] getChain() {
      return chain;
   }

   public List<Function> getFunctions() {
      return functions;
   }

   public TrafficFlow getTrafficFlow() {
      return trafficFlow;
   }
}
