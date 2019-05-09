import manager.Parameters;
import manager.elements.Function;
import manager.elements.Service;
import manager.elements.TrafficFlow;
import org.junit.Test;
import utils.ConfigFiles;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;
import static utils.Definitions.*;

public class ParametersTest {

   @Test
   public void parameters() throws URISyntaxException {
      String path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
      Parameters pm = ConfigFiles.readParameters(path, "/edge.yml");
      pm.initialize(path);
      assertNotNull(pm.getScenario());
      assertNotNull(pm.getServers());
      assertNotNull(pm.getFunctionTypes());
      assertNotNull(pm.getServices().get(0));
      Service service = pm.getServices().get(0);
      assertNotNull(service.getAttribute(SERVICE_MIN_PATHS));
      assertNotNull(service.getAttribute(SERVICE_MAX_PATHS));
      assertNotNull(service.getAttribute(SERVICE_MAX_DELAY));
      assertNotNull(service.getFunctions().get(0));
      Function function = service.getFunctions().get(0);
      assertNotNull(function.getAttribute(FUNCTION_REPLICABLE));
      assertNotNull(function.getAttribute(FUNCTION_LOAD_RATIO));
      assertNotNull(function.getAttribute(FUNCTION_OVERHEAD));
      assertNotNull(function.getAttribute(FUNCTION_SYNC_LOAD_RATIO));
      assertNotNull(function.getAttribute(FUNCTION_PROCESS_DELAY));
      assertNotNull(function.getAttribute(FUNCTION_MIGRATION_DELAY));
      assertNotNull(function.getAttribute(FUNCTION_MAX_LOAD));
      assertNotNull(service.getTrafficFlow());
      TrafficFlow trafficFlow = service.getTrafficFlow();
      assertNotNull(trafficFlow.getDemands());
      assertNotNull(trafficFlow.getPaths());
   }
}
