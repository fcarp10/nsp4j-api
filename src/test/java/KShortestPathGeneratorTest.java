import filemanager.ConfigFiles;
import filemanager.GraphManager;
import filemanager.InputParameters;
import org.junit.Test;
import utils.KShortestPathGenerator;

public class KShortestPathGeneratorTest {

    @Test
    public void inputParameters() {
        InputParameters inputParameters = ConfigFiles.readInputParameters("config.yml");
        inputParameters.initializeParameters();
        KShortestPathGenerator kShortestPathGenerator = new KShortestPathGenerator(10, 3);
        kShortestPathGenerator.run(GraphManager.getGraph().getNode("n1"),GraphManager.getGraph().getNode("n9"));
    }
}
