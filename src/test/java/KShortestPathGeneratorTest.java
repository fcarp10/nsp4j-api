import org.graphstream.graph.Graph;
import org.junit.Test;
import utils.ConfigFiles;
import utils.GraphManager;
import utils.KShortestPathGenerator;

import java.io.File;
import java.net.URISyntaxException;

public class KShortestPathGeneratorTest {

   @Test
   public void inputParameters() throws URISyntaxException {

      final String graphName = "palmetto_cloud";
      final String extensionGraph = ".gml";
      final boolean directedEdges = false;
      int MAX_LENGTH = 10;

      String path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
            .getParent() + "/";

      Graph graph = GraphManager.importTopology(path + graphName + extensionGraph, directedEdges);
      KShortestPathGenerator generator = new KShortestPathGenerator(path, graphName, graph, MAX_LENGTH);

      // 1. generate k-shortest paths
      // generator.run(3); // num k-paths

      // 2. generate paths traversing intermediate node
      // generator.runTraversingIntermediateNode();

      // 3. generate paths from and to specific node
      generator.runFromAndToSpecificNode("45", 1);

   }
}
