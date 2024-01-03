package org.car.routing.gui.data;

import de.cm.osm2po.model.LatLon;

import java.util.ArrayList;
import java.util.List;

public class RoutingData {

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTurnDescription() {
        return turnDescription;
    }

    public void setTurnDescription(String turnDescription) {
        this.turnDescription = turnDescription;
    }
    public List<LatLon> getPointList() {
        return this.pointList;
    }

    public RoutingData(double distance, String turnDescription, List<LatLon> pointList) {
        this.distance = distance;
        this.turnDescription = turnDescription;
        this.pointList = pointList;
    }

    public RoutingData() {
    }

    private double distance;
    private String turnDescription;
    private List<LatLon> pointList = new ArrayList<>();
}
