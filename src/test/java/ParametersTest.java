import manager.Parameters;
import manager.elements.Function;
import manager.elements.Service;
import org.junit.Test;
import utils.ConfigFiles;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Definitions.*;

public class ParametersTest {

   @Test
   public void parameters() throws URISyntaxException {
      String path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
            .getParent();
      Parameters pm = ConfigFiles.readParameters(path, "/7nodes_only_edge.yml");
      pm.initialize(path);
      assertNotNull(pm.getScenario());
      assertNotNull(pm.getServers());
      for (Service s : pm.getServices()) {
         assertNotNull(s.getAttribute(SERVICE_MIN_PATHS));
         assertNotNull(s.getAttribute(SERVICE_MAX_PATHS));
         assertTrue(s.getMaxPropagationDelay() > 0);
         for (Function f : s.getFunctions()) {
            assertNotNull(f.getAttribute(FUNCTION_REPLICABLE));
            assertNotNull(f.getAttribute(FUNCTION_LOAD_RATIO));
            assertNotNull(f.getAttribute(FUNCTION_OVERHEAD));
            assertNotNull(f.getAttribute(FUNCTION_SYNC_LOAD_RATIO));
            assertNotNull(f.getAttribute(FUNCTION_PROCESS_TRAFFIC_DELAY));
            assertNotNull(f.getAttribute(FUNCTION_MAX_CAP_SERVER));
            assertNotNull(f.getAttribute(FUNCTION_MAX_DELAY));
            assertNotNull(f.getAttribute(FUNCTION_MIN_PROCESS_DELAY));
            assertNotNull(f.getAttribute(FUNCTION_PROCESS_DELAY));
         }
         assertTrue(s.getTrafficFlow().getDemands().size() > 0);
         assertTrue(s.getTrafficFlow().getPaths().size() > 0);
      }
   }
}
