package filemanager;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GraphManager {

    private static final Logger log = LoggerFactory.getLogger(GraphManager.class);
    private static Graph graph;

    public static void importTopology(String filename) {
        String path = ConfigFiles.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.replaceAll("%20", " ");
        graph = new SingleGraph("graph");
        try {
            graph.read(path + filename);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (GraphParseException e) {
            e.printStackTrace();
        }
    }

    public static Graph getGraph() {
        return graph;
    }
}
