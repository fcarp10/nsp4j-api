package filemanager;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphManager {

    private static final Logger log = LoggerFactory.getLogger(GraphManager.class);
    private static Graph graph;
    private static String path;

    public GraphManager() {
        try {
            path = new File(GraphManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        path = path.replaceAll("%20", " ");
    }

    public static void importTopology(String filename) {
        graph = new SingleGraph("graph");
        try {
            graph.read(path + "/" + filename);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (GraphParseException e) {
            e.printStackTrace();
        }
    }

    public static List<Path> importPaths(String filename) {
        List<Path> paths = new ArrayList<>();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(path + "/" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner input = new Scanner(stream);
        while (input.hasNext()) {
            String stringPath = input.nextLine();
            String[] pathNodes = stringPath.replaceAll("\\s+", "").replace("[", "").replace("]", "").split(",");
            Path path = new Path();
            for (int i = 0; i < pathNodes.length - 1; i++) {
                Node node = graph.getNode(pathNodes[i]);
                for (Edge edge : node.getEachEdge())
                    if (edge.getTargetNode().getId().equals(pathNodes[i + 1])) {
                        path.push(node, edge);
                        break;
                    }
            }
            paths.add(path);
        }
        return paths;
    }

    public static Graph getGraph() {
        return graph;
    }
}
