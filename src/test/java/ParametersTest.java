import manager.Parameters;
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
      assertNotNull(parameters.getTrafficFlows().get(0).getPaths());
      assertNotNull(parameters.getTrafficFlows().get(0).getDemands());
      assertNotNull(parameters.getServers());
   }
}
