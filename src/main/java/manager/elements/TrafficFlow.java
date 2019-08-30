package manager.elements;

import org.graphstream.graph.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TrafficFlow {

   private String src;
   private String dst;
   private int serviceId;
   private List<Integer> demands;
   private List<Boolean> aux;
   private List<Path> paths;
   private int minDem;
   private int maxDem;
   private int minBw;
   private int maxBw;

   public TrafficFlow() {
      demands = new ArrayList<>();
      paths = new ArrayList<>();
   }

   public TrafficFlow(String src, String dst, int serviceId) {
      demands = new ArrayList<>();
      paths = new ArrayList<>();
      this.src = src;
      this.dst = dst;
      this.serviceId = serviceId;
   }

   public void setPaths(List<Path> allPaths) {
      for (Path p : allPaths)
         if (p.getNodePath().get(0).getId().equals(src) && p.getNodePath().get(p.size() - 1).getId().equals(dst))
            paths.add(p);
   }

   public String getSrc() {
      return src;
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

   public int getServiceId() {
      return serviceId;
   }

   public void setServiceId(int serviceId) {
      this.serviceId = serviceId;
   }

   public List<Integer> getDemands() {
      return demands;
   }

   public List<Path> getPaths() {
      return paths;
   }

   public void setAdmissiblePath(Path admissiblePath) {
      this.paths.add(admissiblePath);
   }

   public void setTrafficDemand(Integer trafficDemand) {
      demands.add(trafficDemand);
   }

   public int getMinDem() {
      return minDem;
   }

   public void setMinDem(int minDem) {
      this.minDem = minDem;
   }

   public int getMaxDem() {
      return maxDem;
   }

   public void setMaxDem(int maxDem) {
      this.maxDem = maxDem;
   }

   public int getMinBw() {
      return minBw;
   }

   public void setMinBw(int minBw) {
      this.minBw = minBw;
   }

   public int getMaxBw() {
      return maxBw;
   }

   public void setMaxBw(int maxBw) {
      this.maxBw = maxBw;
   }

   public List<Boolean> getAux() {
      return aux;
   }
}
