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

      final String TOPOLOGY = "atlanta_from_cloud_to_edge";
      int MAX_LENGTH = 10;
      int NUM_K_PATHS = 1;

      String path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
      Graph graph = GraphManager.importTopology(path, TOPOLOGY);
      KShortestPathGenerator generator = new KShortestPathGenerator(path, TOPOLOGY, graph, MAX_LENGTH);

      generator.run(NUM_K_PATHS); // 1. generate k-shortest paths
//      generator.runTraversingIntermediateNode(); // 2. generate paths traversing intermediate node
//      generator.runFromAndToSpecificNode("n16", 1); // 3. generate paths from and to specific node

   }
}
