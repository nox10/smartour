package com.smartour.app.data.entity.map;

import java.util.Objects;

public class Placemark {
    private String name;
    private String description;
    private String styleUrl;
    private String data;
    private Double latitude;
    private Double longitude;
    private Double height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyleUrl() {
        return styleUrl;
    }

    public void setStyleUrl(String styleUrl) {
        this.styleUrl = styleUrl;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placemark placemark = (Placemark) o;
        return Objects.equals(name, placemark.name) && Objects.equals(description, placemark.description) && Objects.equals(styleUrl, placemark.styleUrl) && Objects.equals(data, placemark.data) && Objects.equals(latitude, placemark.latitude) && Objects.equals(longitude, placemark.longitude) && Objects.equals(height, placemark.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, styleUrl, data, latitude, longitude, height);
    }

    @Override
    public String toString() {
        return "Placemark{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", styleUrl='" + styleUrl + '\'' +
                ", data='" + data + '\'' +
                ", lat=" + latitude +
                ", lon=" + longitude +
                ", height=" + height +
                '}';
    }
}
