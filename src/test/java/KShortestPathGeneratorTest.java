import filemanager.ConfigFiles;
import filemanager.GraphManager;
import org.junit.Test;
import utils.KShortestPathGenerator;

import java.io.File;
import java.net.URISyntaxException;

public class KShortestPathGeneratorTest {

    @Test
    public void inputParameters() throws URISyntaxException {
        final String TOPOLOGY = "test_scenario1";
        String path = new File(ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        new GraphManager();
        GraphManager.importTopology(path, TOPOLOGY);
        KShortestPathGenerator kShortestPathGenerator = new KShortestPathGenerator(10, 5, path, TOPOLOGY);
        kShortestPathGenerator.run();
    }
}
