package com.smartour.app.util.xml.entity;

import lombok.Data;

import java.util.List;

@Data
public class XmlPlacemark {
    private String name;
    private String description;
    private String styleUrl;
    private List<XmlPlacemarkData> extendedData;
    private XmlPoint point;
    private String source;
}
