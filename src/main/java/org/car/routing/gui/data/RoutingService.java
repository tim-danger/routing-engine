package org.car.routing.gui.data;

import de.cm.osm2po.logging.Log;
import de.cm.osm2po.routing.Graph;
import de.cm.osm2po.routing.RoutingResultSegment;
import de.cm.osm2po.routing.VertexRouter;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoutingService {

    private static RoutingService instance;
    private Graph graph;

    public static RoutingService getInstance() {
        if (instance == null) {
            instance = new RoutingService();
        }
        return instance;
    }

    private RoutingService() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("hh_2po_hessen.gph");
        Path filePath = null;
        try {
            filePath = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File fileForGraph = new File(filePath.toString());
        this.graph = new Graph(fileForGraph);
    }

    public List<RoutingData> routing(double fromLat, double fromLon, double toLat, double toLon) {
        int sourceId = graph.findClosestVertexId((float) fromLat, (float) fromLon);
        int targetId = graph.findClosestVertexId((float) toLat, (float) toLon);

        int [] path = new VertexRouter(graph, Log.stdout())
                .findShortestPath(sourceId, targetId);

        List<RoutingData> routingData = new ArrayList<>();

        if (path != null) {
            for (int i = 0; i < path.length - 1; i++) {
                RoutingResultSegment current = graph.lookupSegment(path[i]);
                routingData.add(new RoutingData(current.getKm(), current.getStreetName(), Arrays.asList(current.getLatLons())));
            }
        }

        return routingData;
    }
}
