import filemanager.ConfigFiles;
import filemanager.InputParameters;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class InputParametersTest {

    @Test
    public void inputParameters() {
        InputParameters inputParameters = ConfigFiles.readInputParameters("config.yml");
        inputParameters.initializeParameters();
        assertNotNull(inputParameters.getNetworkFile());
        assertNotNull(inputParameters.getTrafficFlows().get(0).getAdmissiblePaths());
        assertNotNull(inputParameters.getTrafficFlows().get(0).getTrafficDemands());
        assertNotNull(inputParameters.getServers());
    }
}
