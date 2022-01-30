package com.smartour.app.util.xml.entity;

import java.util.Objects;

public class XmlPoint {
    private String coordinates;

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlPoint point = (XmlPoint) o;
        return Objects.equals(coordinates, point.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }

    @Override
    public String toString() {
        return "Point{" +
                "coordinates='" + coordinates + '\'' +
                '}';
    }
}
