package utils;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class KShortestPathGenerator {

    private static Logger log = LoggerFactory.getLogger(KShortestPathGenerator.class);
    private WritePathsFile writePathsFile;
    private int maxLength;
    private int numOfKPaths;
    private Graph graph;

    public KShortestPathGenerator(Graph graph, int maxLength, int numOfKPaths, String path, String fileName) {
        writePathsFile = new WritePathsFile(path, fileName);
        this.maxLength = maxLength;
        this.numOfKPaths = numOfKPaths;
        this.graph = graph;
    }

    public void run() {
        for (Node src : graph.getNodeSet())
            for (Node dst : graph.getNodeSet())
                if (!src.equals(dst))
                    findPaths(src, dst);
    }

    public void run(Node src, Node dst) {
        if (!src.equals(dst))
            findPaths(src, dst);
    }

    private void findPaths(Node src, Node dst) {
        log.info(src.getId() + " > " + dst.getId());
        PathCollection pathCollection = new PathCollection();
        pathCollection.generateAllPaths(src.getId(), dst.getId(), maxLength);
        pathCollection.orderPathsBySize();
        int minimumLength = Integer.MAX_VALUE;

        for (Path path : pathCollection.getPaths())
            if (path.size() < minimumLength)
                minimumLength = path.size();

        if (!pathCollection.getPaths().isEmpty())
            for (int k = 0; k < numOfKPaths; k++) {
                if (k < pathCollection.getPaths().size())
                    writePathsFile.write(pathCollection.getPaths().get(k) + System.getProperty("line.separator"));
            }
    }

    private class PathCollection {

        private List<Path> paths = new ArrayList<>();
        private List<String> onPath = new ArrayList<>();
        private Stack<String> pathNodes = new Stack<>();

        void generateAllPaths(String srcNode, String dstNode, int maxLength) {

            pathNodes.push(srcNode);
            onPath.add(srcNode);

            if (!srcNode.equals(dstNode)) {
                for (Iterator<Node> it = graph.getNode(srcNode).getNeighborNodeIterator(); it.hasNext(); ) {
                    Node currentNode = it.next();
                    String currentNodeString = currentNode.getId();
                    if (!onPath.contains(currentNodeString))
                        if (onPath.size() < maxLength)
                            generateAllPaths(currentNodeString, dstNode, maxLength);
                }
            } else
                paths.add(generatePath(pathNodes));

            pathNodes.pop();
            onPath.remove(srcNode);
        }

        private Path generatePath(Stack<String> pathNodes) {
            ArrayList<Node> nodes = new ArrayList<>();
            ArrayList<Edge> edges = new ArrayList<>();
            for (int i = 0; i < pathNodes.size() - 1; i++) {
                Node node = graph.getNode(pathNodes.get(i));
                nodes.add(node);
                for (Edge edge : node.getEachEdge()) {
                    if (edge.getOpposite(node).getId().equals(pathNodes.get(i + 1))) {
                        edges.add(edge);
                        break;
                    }
                }
            }
            Path path = new Path();
            for (int n = 0; n < nodes.size(); n++)
                path.push(nodes.get(n), edges.get(n));
            return path;
        }

        void orderPathsBySize() {
            paths.sort(Comparator.comparingInt(Path::size));
        }

        List<Path> getPaths() {
            return paths;
        }

    }
}
