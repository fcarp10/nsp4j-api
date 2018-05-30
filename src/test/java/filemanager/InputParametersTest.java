package filemanager;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class InputParametersTest {

    private static InputParameters inputParameters;

    @Test
    public void inputParameters() {

        inputParameters = ConfigFiles.readInputParameters("/config-test.yml");
        inputParameters.initializeParameters();
        assertNotNull(inputParameters.getNetworkFile());
        assertNotNull(GraphManager.getGraph().getVertexSet());
        assertNotNull(GraphManager.getGraph().getEdgeSet());
        assertNotNull(GraphManager.getPaths());
        assertNotNull(inputParameters.getTrafficFlows().get(0).getAdmissiblePaths());
        assertNotNull(inputParameters.getTrafficFlows().get(0).getTrafficDemands());
        assertNotNull(inputParameters.getServers());
    }
}
