package filemanager;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphManager {

    private static final Logger log = LoggerFactory.getLogger(GraphManager.class);
    private static Graph graph;

    public static void importTopology(String path, String filename) {
        graph = new SingleGraph("graph");
        if (!path.endsWith("/"))
            path += "/";
        try {
            graph.read(path + filename + ".dgs");
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (GraphParseException e) {
            e.printStackTrace();
        }
    }

    public static List<Path> importPaths(String stringPathFile, String filename) {
        List<Path> paths = new ArrayList<>();
        FileInputStream stream = null;
        if (!stringPathFile.endsWith("/"))
            stringPathFile += "/";
        try {
            stream = new FileInputStream(stringPathFile + filename);
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
