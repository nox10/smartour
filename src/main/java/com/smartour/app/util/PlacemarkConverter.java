package com.smartour.app.util;

import com.smartour.app.data.entity.map.Placemark;
import com.smartour.app.util.xml.entity.XmlPlacemark;

public class PlacemarkConverter {
    public static Placemark apply(XmlPlacemark xmlPlacemark) {
        Placemark placemark = new Placemark();
        placemark.setName(xmlPlacemark.getName());
        placemark.setStyleUrl(xmlPlacemark.getStyleUrl());
        placemark.setDescription(xmlPlacemark.getDescription());
        placemark.setData(xmlPlacemark.getExtendedData().get(0).getValue());

        String[] latLonHeigth = xmlPlacemark.getPoint().getCoordinates().split(",");

        placemark.setLatitude(Double.parseDouble(latLonHeigth[0]));
        placemark.setLongitude(Double.parseDouble(latLonHeigth[1]));
        placemark.setHeight(Double.parseDouble(latLonHeigth[2]));

        return placemark;
    }
}
