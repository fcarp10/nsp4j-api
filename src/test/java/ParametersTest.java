import manager.Parameters;
import manager.elements.Function;
import manager.elements.Service;
import manager.elements.TrafficFlow;
import org.junit.Test;
import utils.ConfigFiles;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class ParametersTest {

   @Test
   public void parameters() throws URISyntaxException {
      String path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
      Parameters parameters = ConfigFiles.readParameters(path, "/example.yml");
      parameters.initialize(path);
      assertNotNull(parameters.getScenario());
      assertNotNull(parameters.getServers());
      assertNotNull(parameters.getServices().get(0));
      Service service = parameters.getServices().get(0);
      assertNotNull(service.getAttribute("minPaths"));
      assertNotNull(service.getAttribute("maxPaths"));
      assertNotNull(service.getFunctions().get(0));
      Function function = service.getFunctions().get(0);
      assertNotNull(function.getAttribute("replicable"));
      assertNotNull(function.getAttribute("load"));
      assertNotNull(function.getAttribute("overhead"));
      assertNotNull(function.getAttribute("sync_load"));
      assertNotNull(function.getAttribute("delay"));
      assertNotNull(parameters.getTrafficFlows().get(0));
      TrafficFlow trafficFlow = parameters.getTrafficFlows().get(0);
      assertNotNull(trafficFlow.getDemands());
      assertNotNull(trafficFlow.getPaths());
   }
}
