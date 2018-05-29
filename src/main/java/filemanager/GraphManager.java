package filemanager;

import graph.elements.edge.EdgeElement;
import graph.elements.edge.params.EdgeParams;
import graph.elements.edge.params.impl.BasicEdgeParams;
import graph.elements.vertex.VertexElement;
import graph.graphcontroller.Gcontroller;
import graph.graphcontroller.impl.GcontrollerImpl;
import graph.path.PathElement;
import graph.path.pathelementimpl.PathElementImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphManager {

    private static final Logger log = LoggerFactory.getLogger(GraphManager.class);
    private static Gcontroller graph;
    private static List<PathElement> paths;

    public static void importTopology(String filename, boolean bidirectionalLinks) {

        graph = new GcontrollerImpl();
        paths = new ArrayList<>();

        Scanner scanner = ConfigFiles.scanPlainTextFile(filename);
        String temp;
        VertexElement vertex1, vertex2;

        while ((scanner.nextLine()).trim().compareTo("NODES (") != 0) {
        }

        log.debug("Reading nodes... ");
        while ((temp = scanner.nextLine()) != null) {
            temp = temp.trim();
            if (temp.trim().compareTo(")") == 0) {
                break;
            }

            Pattern p;
            Matcher m;

            String sourceID = "";
            p = Pattern.compile("[a-zA-Z0-9\\.-]+");
            m = p.matcher(temp);
            if (m.find()) {
                sourceID = m.group(0);
            }

            double[] temp1 = new double[2];
            int count = 0;
            while (m.find()) {
                temp1[count] = Double.parseDouble(m.group(0));
                count++;
                if (count == 2)
                    break;
            }

            vertex1 = new VertexElement(sourceID, graph, temp1[0], temp1[1]);
            log.debug(vertex1.getVertexID());
            graph.addVertex(vertex1);
        }

        while ((scanner.nextLine()).trim().compareTo("LINKS (") != 0) {
        }
        log.debug("Reading links... ");

        while ((temp = scanner.nextLine()) != null) {
            temp = temp.trim();
            if (temp.length() == 1) {
                break;
            }

            Pattern p;
            Matcher m;

            p = Pattern.compile("[a-zA-Z0-9\\.]+");
            m = p.matcher(temp);
            String[] temp1 = new String[7];
            int count = 0;
            while (m.find()) {
                temp1[count] = m.group(0);
                count++;
                if (count == 7)
                    break;
            }

            vertex1 = graph.getVertex(temp1[1]);
            vertex2 = graph.getVertex(temp1[2]);

            EdgeElement edge1 = new EdgeElement(temp1[0] + ".1", vertex1, vertex2,
                    graph);
            EdgeElement edge2 = new EdgeElement(temp1[0] + ".2", vertex2, vertex1,
                    graph);


            double distance = Math.sqrt(Math.pow(vertex1.getXCoord()
                    - vertex2.getXCoord(), 2)
                    + Math
                    .pow(vertex1.getYCoord() - vertex2.getYCoord(),
                            2));

            double delay = distance / 29.9792458; // (in ms)

            EdgeParams params1 = new BasicEdgeParams(edge1, delay, 1, Double.valueOf(temp1[5]));
            edge1.setEdgeParams(params1);
            graph.addEdge(edge1);
            log.debug(edge1.getEdgeID());

            if (bidirectionalLinks) {
                EdgeParams params2 = new BasicEdgeParams(edge2, delay, 1, Double.valueOf(temp1[5]));
                edge2.setEdgeParams(params2);
                graph.addEdge(edge2);
                log.debug(edge2.getEdgeID());
            }
        }

        while ((scanner.nextLine()).trim().compareTo("DEMANDS (") != 0) {
        }
        log.debug("Reading traffic demands... ");

        while ((scanner.nextLine()).trim().compareTo("ADMISSIBLE_PATHS (") != 0) {
        }

        ArrayList<EdgeElement> listOfIntermediateLinks;
        List<String> listOfNodes;
        while ((temp = scanner.nextLine()) != null) {
            temp = temp.trim();
            if (temp.length() == 1) {
                break;
            }
            listOfNodes = new ArrayList<>();
            listOfIntermediateLinks = new ArrayList<>();
            String[] tmpNodes = temp.split("-");
            Collections.addAll(listOfNodes, tmpNodes);

            for (int i = 0; i < listOfNodes.size() - 1; i++) {
                for (EdgeElement link : graph.getEdgeSet()) {
                    if (link.getSourceVertex().getVertexID().equals(listOfNodes.get(i)) && link.getDestinationVertex().getVertexID().equals(listOfNodes.get(i + 1))) {
                        listOfIntermediateLinks.add(link);
                        break;
                    }
                }
            }

            PathElement pathElement = new PathElementImpl(graph, graph.getVertex(listOfNodes.get(0)), graph.getVertex(listOfNodes.get(listOfNodes.size() - 1)), listOfIntermediateLinks);
            paths.add(pathElement);
            log.debug("Path Element: " + pathElement.getVertexSequence());
        }
    }

    public static Gcontroller getGraph() {
        return graph;
    }

    public static List<PathElement> getPaths() {
        return paths;
    }
}
