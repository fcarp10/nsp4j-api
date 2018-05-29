package filemanager;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InputParametersTest {

    private static InputParameters inputParameters;

    @Test
    public void _1_inputParameters() {

        inputParameters = ConfigFiles.readInputParameters("/files/config-test.yml");
        assertThat(inputParameters.getNetworkFile(), is("network-test.txt"));
    }

    @Test
    public void _2_readSeeds() {

        inputParameters.readSeeds("files/seeds.txt");
        Long seed = inputParameters.getSeed();
        assertNotNull(seed);
    }

    @Test
    public void _3_importTopology() {

        GraphManager.importTopology("files/" + inputParameters.getNetworkFile(), inputParameters.isBidirectionalLinks());
        assertNotNull(GraphManager.getGraph().getVertexSet());
        assertNotNull(GraphManager.getGraph().getEdgeSet());
        assertNotNull(GraphManager.getPaths());
    }

    @Test
    public void _4_initializeNetwork() {

        inputParameters.setupPathsToEndPoints(GraphManager.getPaths());
        inputParameters.setupTrafficDemands();
        assertNotNull(inputParameters.getEndPoints().get(0).getAdmissiblePaths());
        assertNotNull(inputParameters.getEndPoints().get(0).getTrafficDemands());
    }

}
