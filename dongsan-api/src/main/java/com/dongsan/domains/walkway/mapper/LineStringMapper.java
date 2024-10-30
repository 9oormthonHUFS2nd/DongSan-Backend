package com.dongsan.domains.walkway.mapper;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class LineStringMapper {

    public static List<List<Double>> toList(LineString lineString) {
        List<List<Double>> coordinatesList = new ArrayList<>();

        for (Coordinate coordinate : lineString.getCoordinates()) {
            List<Double> point = List.of(coordinate.getX(), coordinate.getY());
            coordinatesList.add(point);
        }

        return coordinatesList;
    }

    public static LineString toLineString(List<List<Double>> course) {
        // 경로
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinateList = new Coordinate[course.size()];

        for(int i = 0; i < course.size(); i++) {
            List<Double> point = course.get(i);
            coordinateList[i] = new Coordinate(point.get(0), point.get(1));
        }

        return geometryFactory.createLineString(coordinateList);
    }
}
