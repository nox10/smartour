package com.smartour.app.data.entity.map;

import com.smartour.app.data.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Placemark extends AbstractEntity {
    private String name;
    private String normalizedName;

    @Column(length = 50000)
    private String description;

    @Column(length = 50000)
    private String normalizedDescription;

    @Column(length = 4000)
    private String styleUrl;

    @Column(length = 50000)
    private String data;

    private Double latitude;
    private Double longitude;
    private Double height;

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

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

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getNormalizedDescription() {
        return normalizedDescription;
    }

    public void setNormalizedDescription(String normalizedDescription) {
        this.normalizedDescription = normalizedDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Placemark placemark = (Placemark) o;
        return Objects.equals(name, placemark.name) && Objects.equals(normalizedName, placemark.normalizedName) && Objects.equals(description, placemark.description) && Objects.equals(normalizedDescription, placemark.normalizedDescription) && Objects.equals(styleUrl, placemark.styleUrl) && Objects.equals(data, placemark.data) && Objects.equals(latitude, placemark.latitude) && Objects.equals(longitude, placemark.longitude) && Objects.equals(height, placemark.height) && Objects.equals(source, placemark.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, normalizedName, description, normalizedDescription, styleUrl, data, latitude, longitude, height, source);
    }

    @Override
    public String toString() {
        return "Placemark{" +
                "name='" + name + '\'' +
                ", normalizedName='" + normalizedName + '\'' +
                ", description='" + description + '\'' +
                ", normalizedDescription='" + normalizedDescription + '\'' +
                ", styleUrl='" + styleUrl + '\'' +
                ", data='" + data + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", source='" + source + '\'' +
                '}';
    }
}
