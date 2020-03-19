package manager.elements;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.graphstream.graph.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TrafficFlow {

   private String src;
   private String dst;
   private int[] services;
   @JsonProperty("service_length_mult")
   private int[] serviceLengthMult;
   private List<Integer> demands;
   private List<Boolean> aux;
   private List<Path> paths;
   @JsonProperty("min_dem")
   private int minDem;
   @JsonProperty("max_dem")
   private int maxDem;
   @JsonProperty("min_bw")
   private int minBw;
   @JsonProperty("max_bw")
   private int maxBw;


   public TrafficFlow() {
      demands = new ArrayList<>();
      paths = new ArrayList<>();
      aux = new ArrayList<>();
   }

   public TrafficFlow(String src, String dst, int[] services, int[] serviceLengthMult) {
      demands = new ArrayList<>();
      paths = new ArrayList<>();
      aux = new ArrayList<>();
      this.src = src;
      this.dst = dst;
      this.services = services;
      this.serviceLengthMult = serviceLengthMult;
   }

   public void generateTrafficDemands(Random rnd, int minDem, int maxDem, int minBw, int maxBw) {
      int numDemands = rnd.nextInt(maxDem + 1 - minDem) + minDem;
      for (int td = 0; td < numDemands; td++)
         demands.add(rnd.nextInt(maxBw + 1 - minBw) + minBw);
   }

   public void generateTrafficDemands(Random rnd) {
      int numDemands = rnd.nextInt(maxDem + 1 - minDem) + minDem;
      for (int td = 0; td < numDemands; td++)
         demands.add(rnd.nextInt(maxBw + 1 - minBw) + minBw);
   }

   public String getSrc() {
      return src;
   }

   public int[] getServices() {
      return services;
   }

   public int[] getServiceLengthMult() {
      return serviceLengthMult;
   }

   public void setSrc(String src) {
      this.src = src;
   }

   public String getDst() {
      return dst;
   }

   public void setDst(String dst) {
      this.dst = dst;
   }

   public List<Integer> getDemands() {
      return demands;
   }

   public List<Path> getPaths() {
      return paths;
   }

   public void setPaths(List<Path> allPaths) {
      for (Path p : allPaths)
         if (p.getNodePath().get(0).getId().equals(src) && p.getNodePath().get(p.size() - 1).getId().equals(dst))
            paths.add(p);
   }

   public void setAdmissiblePath(Path admissiblePath) {
      this.paths.add(admissiblePath);
   }

   public int getMinDem() {
      return minDem;
   }

   public int getMaxDem() {
      return maxDem;
   }

   public int getMinBw() {
      return minBw;
   }

   public int getMaxBw() {
      return maxBw;
   }

   public List<Boolean> getAux() {
      return aux;
   }

}
