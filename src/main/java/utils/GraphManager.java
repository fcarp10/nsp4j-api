package utils;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphManager {

   private static final Logger log = LoggerFactory.getLogger(GraphManager.class);

   public static Graph importTopology(String path, String filename) {
      Graph graph = new SingleGraph("graph");
      if (!path.endsWith("/"))
         path += "/";
      try {
         graph.read(path + filename);
      } catch (IOException e) {
         log.error(e.toString());
      } catch (GraphParseException e) {
         log.error("error reading topology file: " + e.toString());
      }
      return graph;
   }

   public static List<Path> importPaths(Graph graph, String stringPathFile, String filename) {
      List<Path> paths = new ArrayList<>();
      FileInputStream stream = null;
      if (!stringPathFile.endsWith("/"))
         stringPathFile += "/";
      try {
         stream = new FileInputStream(stringPathFile + filename);
      } catch (FileNotFoundException e) {
         log.error("error reading the .txt path file: " + e.getMessage());
      }
      try {
         Scanner input = new Scanner(stream);
         while (input.hasNext()) {
            String sPath = input.nextLine();
            String[] pNodes = sPath.replaceAll("\\s+", "").replace("[", "").replace("]", "").split(",");
            Path path = new Path();
            for (int i = 0; i < pNodes.length - 1; i++) {
               Node node = graph.getNode(pNodes[i]);
               for (Edge edge : node.getEachEdge())
                  if (edge.getTargetNode().getId().equals(pNodes[i + 1])) {
                     path.push(node, edge);
                     break;
                  }
            }
            paths.add(path);
         }
      } catch (Exception e) {
         log.error("format error .txt path file: " + e.toString());
      }
      return paths;
   }
}
