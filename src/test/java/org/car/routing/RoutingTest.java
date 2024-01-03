package org.car.routing;

import de.cm.osm2po.Main;
import de.cm.osm2po.logging.Log;
import de.cm.osm2po.routing.Graph;
import de.cm.osm2po.routing.RoutingResultSegment;
import de.cm.osm2po.routing.VertexRouter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class RoutingTest {

    @Test
    @Disabled("Datei hessen-latest.osm.pbf herunterladen")
    public void testGraphBuildingHessen() throws URISyntaxException {
        buildGraph("hessen-latest.osm.pbf");
    }

    @Test
    @Disabled("Datei bayern-latest.osm.pbf herunterladen")
    public void testGraphBuildingBayern() throws URISyntaxException {
        buildGraph("bayern-latest.osm.pbf");
    }

    @Test
    @Disabled("Datei germany-latest.osm.pbf herunterladen")
    public void testGraphBuildingGermany() throws URISyntaxException {
        buildGraph("germany-latest.osm.pbf");
    }

    private void buildGraph(String resourceAsName) throws URISyntaxException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceAsName);
        Path path = Paths.get(url.toURI());
        String pathToFile = path.toString();
        String parentFolder = path.getParent().toString();
        String[] args = new String[] { "prefix=hh", "tileSize=x", pathToFile, "cmd=tjsg", "workDir=" + parentFolder, pathToFile };
        Arrays.asList(args).stream().forEach(System.out::println);
        Main.main(args);
    }

    @Test
    @Disabled("Graph aus heruntergeladener Datei erzeugen")
    public void testGraphDeutschland() throws URISyntaxException {
        // Veitsbronn, Friedenstraße 1 -> Ansbach, Brückencenter
        traverseGraph("hh_2po_germany.gph", 49.50705008579196f, 10.878763367335853f, 52.36815706650397f, 7.993858763648658f);
    }

    @Test
    @Disabled("Graph aus heruntergeladener Datei erzeugen")
    public void testGraphFrankfurt() throws URISyntaxException {
        // Frankfurt, Goldbergweg 48 -> Frankfurt, Lyoner Str.
        traverseGraph("hh_2po_hessen.gph", 50.096226f, 8.719046f, 50.081368f, 8.628296f);
    }

    @Test
    @Disabled("Graph aus heruntergeladener Datei erzeugen")
    public void testGraphBayern() throws URISyntaxException {
        // Veitsbronn, Friedenstraße 1 -> Ansbach, Brückencenter
        traverseGraph("hh_2po_bayern.gph", 49.50705008579196f, 10.878763367335853f, 49.30636594213204f, 10.568799019356073f);
    }

    private void traverseGraph(String resourceAsName, float startLat, float startLon, float endLat, float endLon) throws URISyntaxException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceAsName);
        Path filePath = Paths.get(url.toURI());
        File fileForGraph = new File(filePath.toString());
        Graph graph = new Graph(fileForGraph);
        System.out.println("Graph Loaded. ID=" + graph.getGraphId());

        int sourceId = graph.findClosestVertexId(startLat, startLon);
        int targetId = graph.findClosestVertexId(endLat, endLon);

        // Call a simple route
        int [] path = new VertexRouter(graph, Log.stdout())
                .findShortestPath(sourceId, targetId);

        float kilometer = 0.0f;
        float time = 0.0f;
        if (path != null) { // Found!

            for (int i = 0; i < path.length; i++) {
                RoutingResultSegment rrs = graph.lookupSegment(path[i]);
                int segId = rrs.getId();
                int from = rrs.getSourceId();
                int to = rrs.getTargetId();
                String segName = rrs.getName().toString();
                System.out.println(from + "-" + to + "  " + segId + "/" + path[i] + " " + segName + ", km: " + rrs.getKm() + ", Zeit: " + rrs.getH());
                kilometer += rrs.getKm();
                time += rrs.getH();
            }
        } else {
            System.err.println("Sorry, route could not be found");
        }
        System.out.println("Kilometer gefahren: " + kilometer);
        System.out.println("Benötigte Zeit: " + time);
        graph.close();
    }
}
