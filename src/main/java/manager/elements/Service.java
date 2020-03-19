package manager.elements;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {

   private int id;
   private int[] chain;
   @JsonProperty("max_delay")
   private double maxDelay;
   private transient List<Function> functions;
   private TrafficFlow trafficFlow;
   private Map<String, Object> attributes;

   public Service() {
      attributes = new HashMap<>();
   }

   public Service(int id, double maxDelay, List<Function> functions, TrafficFlow trafficFlow, Map<String, Object> attributes) {
      this.id = id;
      this.maxDelay = maxDelay;
      this.functions = functions;
      this.trafficFlow = trafficFlow;
      this.attributes = attributes;
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

   public double getMaxDelay() {
      return maxDelay;
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
