package com.smartour.app.util.xml.entity;

import java.util.List;
import java.util.Objects;

public class XmlPlacemark {
    private String name;
    private String description;
    private String styleUrl;
    private List<XmlPlacemarkData> extendedData;
    private XmlPoint point;
    private String source;

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

    public List<XmlPlacemarkData> getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(List<XmlPlacemarkData> extendedData) {
        this.extendedData = extendedData;
    }

    public XmlPoint getPoint() {
        return point;
    }

    public void setPoint(XmlPoint point) {
        this.point = point;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlPlacemark that = (XmlPlacemark) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(styleUrl, that.styleUrl) && Objects.equals(extendedData, that.extendedData) && Objects.equals(point, that.point) && Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, styleUrl, extendedData, point, source);
    }

    @Override
    public String toString() {
        return "XmlPlacemark{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", styleUrl='" + styleUrl + '\'' +
                ", extendedData=" + extendedData +
                ", point=" + point +
                ", source='" + source + '\'' +
                '}';
    }
}
