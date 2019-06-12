package utils;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static utils.Definitions.NODE_CLOUD;

public class KShortestPathGenerator {

   private static Logger log = LoggerFactory.getLogger(KShortestPathGenerator.class);
   private WritePlainTextFile writePlainTextFile;
   private int maxLength;
   private int numOfKPaths;
   private Graph graph;

   public KShortestPathGenerator(Graph graph, int maxLength, int numOfKPaths, String path, String fileName) {
      writePlainTextFile = new WritePlainTextFile(path, fileName, ".txt");
      this.maxLength = maxLength;
      this.numOfKPaths = numOfKPaths;
      this.graph = graph;
   }

   public void run() {
      List<Node> nodes = new ArrayList<>();
      List<Node> cloudNodes = new ArrayList<>();
      for (Node node : graph.getNodeSet()) {
         if (node.getAttribute(NODE_CLOUD) == null)
            nodes.add(node);
         else cloudNodes.add(node);
      }
      for (Node src : nodes)
         for (Node dst : nodes)
            if (!src.equals(dst))
               findPaths(src, dst, cloudNodes, null, numOfKPaths);
      for (Node cloudNode : cloudNodes)
         for (Node src : nodes)
            for (Node dst : nodes)
               if (!src.equals(dst))
                  findPaths(src, dst, cloudNodes, cloudNode, 1);
   }

   private void findPaths(Node src, Node dst, List<Node> cloudNodes, Node cloudNode, int numOfKPaths) {
      log.info(src.getId() + " > " + dst.getId());
      PathCollection pathCollection = new PathCollection(cloudNodes, cloudNode);
      pathCollection.generateAllPaths(src.getId(), dst.getId());
      pathCollection.orderPathsBySize();
      int minimumLength = Integer.MAX_VALUE;

      for (Path path : pathCollection.getPaths())
         if (path.size() < minimumLength)
            minimumLength = path.size();

      if (!pathCollection.getPaths().isEmpty())
         for (int k = 0; k < numOfKPaths; k++) {
            if (k < pathCollection.getPaths().size())
               writePlainTextFile.write(pathCollection.getPaths().get(k) + System.getProperty("line.separator"));
         }
   }

   private class PathCollection {

      private List<Path> paths = new ArrayList<>();
      private List<String> onPath = new ArrayList<>();
      private Stack<String> pathNodes = new Stack<>();
      private Node cloudNode;
      private List<Node> cloudNodes;

      PathCollection(List<Node> cloudNodes, Node cloudNode) {
         this.cloudNodes = cloudNodes;
         if (cloudNode != null)
            this.cloudNode = cloudNode;
      }

      void generateAllPaths(String srcNode, String dstNode) {
         pathNodes.push(srcNode);
         onPath.add(srcNode);
         if (!srcNode.equals(dstNode)) {
            for (Iterator<Node> it = graph.getNode(srcNode).getNeighborNodeIterator(); it.hasNext(); ) {
               Node currentNode = it.next();
               String currentNodeString = currentNode.getId();
               if (!onPath.contains(currentNodeString))
                  if (onPath.size() < maxLength)
                     generateAllPaths(currentNodeString, dstNode);
            }
         } else {
            Path path = generatePath(pathNodes);
            boolean containsCloudNode = false;
            for (Node cloudNode : cloudNodes)
               if (path.contains(cloudNode))
                  containsCloudNode = true;
            if (cloudNode == null && !containsCloudNode)
               paths.add(path);
            if (cloudNode != null && path.contains(cloudNode))
               paths.add(path);
         }
         pathNodes.pop();
         onPath.remove(srcNode);
      }

      private Path generatePath(Stack<String> pathNodes) {
         ArrayList<Node> nodes = new ArrayList<>();
         ArrayList<Edge> edges = new ArrayList<>();
         for (int i = 0; i < pathNodes.size() - 1; i++) {
            Node node = graph.getNode(pathNodes.get(i));
            nodes.add(node);
            for (Edge edge : node.getEachEdge()) {
               if (edge.getOpposite(node).getId().equals(pathNodes.get(i + 1))) {
                  edges.add(edge);
                  break;
               }
            }
         }
         Path path = new Path();
         for (int n = 0; n < nodes.size(); n++)
            path.push(nodes.get(n), edges.get(n));
         return path;
      }

      void orderPathsBySize() {
         paths.sort(Comparator.comparingInt(Path::size));
      }

      List<Path> getPaths() {
         return paths;
      }

   }
}
