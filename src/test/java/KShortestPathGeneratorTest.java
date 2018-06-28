import filemanager.ConfigFiles;
import filemanager.GraphManager;
import filemanager.Parameters;
import org.junit.Test;
import utils.KShortestPathGenerator;

public class KShortestPathGeneratorTest {

    @Test
    public void inputParameters() {
        Parameters parameters = ConfigFiles.readParameters("config.yml");
        parameters.initialize();
        KShortestPathGenerator kShortestPathGenerator = new KShortestPathGenerator(10, 3);
        kShortestPathGenerator.run(GraphManager.getGraph().getNode("n1"),GraphManager.getGraph().getNode("n9"));
    }
}
