import filemanager.ConfigFiles;
import filemanager.Parameters;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ParametersTest {

    @Test
    public void parameters() {
        Parameters parameters = ConfigFiles.readParameters("config.yml");
        parameters.initialize();
        assertNotNull(parameters.getNetworkFile());
        assertNotNull(parameters.getTrafficFlows().get(0).getAdmissiblePaths());
        assertNotNull(parameters.getTrafficFlows().get(0).getTrafficDemands());
        assertNotNull(parameters.getServers());
    }
}
