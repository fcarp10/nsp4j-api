import filemanager.ConfigFiles;
import filemanager.Parameters;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class ParametersTest {

    @Test
    public void parameters() throws URISyntaxException {
        String path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        Parameters parameters = ConfigFiles.readParameters(path, "/test_scenario1.yml");
        parameters.initialize(path);
        assertNotNull(parameters.getScenario());
        assertNotNull(parameters.getTrafficFlows().get(0).getAdmissiblePaths());
        assertNotNull(parameters.getTrafficFlows().get(0).getTrafficDemands());
        assertNotNull(parameters.getServers());
    }
}
