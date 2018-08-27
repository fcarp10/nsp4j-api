import filemanager.ConfigFiles;
import filemanager.Parameters;
import org.junit.Test;
import utils.KShortestPathGenerator;

import java.io.File;
import java.net.URISyntaxException;

public class KShortestPathGeneratorTest {

    @Test
    public void inputParameters() throws URISyntaxException {
        String path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        Parameters parameters = ConfigFiles.readParameters(path, "/test_scenario1.yml");
        parameters.initialize(path);
        KShortestPathGenerator kShortestPathGenerator = new KShortestPathGenerator(10, 3);
        kShortestPathGenerator.run();
//        kShortestPathGenerator.run(GraphManager.getGraph().getNode("n1"),GraphManager.getGraph().getNode("n9"));
    }
}
