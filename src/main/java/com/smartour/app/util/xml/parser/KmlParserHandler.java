package com.smartour.app.util.xml.parser;

import com.smartour.app.util.xml.entity.XmlPlacemark;
import com.smartour.app.util.xml.entity.XmlPlacemarkData;
import com.smartour.app.util.xml.entity.XmlPoint;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KmlParserHandler extends DefaultHandler {

    private final StringBuilder currentValue = new StringBuilder();
    private XmlPlacemark currentPlacemark;
    private XmlPlacemarkData currentData;
    private List<XmlPlacemarkData> currentExtendedData;
    private List<XmlPlacemark> result;
    private XmlPoint currentPoint;

    boolean skip = true;

    public List<XmlPlacemark> getResult() {
        return result;
    }

    @Override
    public void startDocument() {
        result = new ArrayList<>();
    }

    @Override
    public void endDocument() {
        log.info("End of the document");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (qName.equalsIgnoreCase("Placemark")) {
            currentPlacemark = new XmlPlacemark();
            skip = false;
        }

        if (!skip) {
            currentValue.setLength(0);

            if (qName.equalsIgnoreCase("ExtendedData")) {
                currentExtendedData = new ArrayList<>();
            }

            if (qName.equalsIgnoreCase("Data")) {
                currentData = new XmlPlacemarkData();
                currentData.setName(attributes.getValue("name"));
            }

            if (qName.equalsIgnoreCase("Point")) {
                currentPoint = new XmlPoint();
            }

        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (!skip) {
            if (qName.equalsIgnoreCase("name")) {
                currentPlacemark.setName(currentValue.toString());
            }

            if (qName.equalsIgnoreCase("description")) {
                currentPlacemark.setDescription(currentValue.toString());
            }

            if (qName.equalsIgnoreCase("styleUrl")) {
                currentPlacemark.setStyleUrl(currentValue.toString());
            }

            if (qName.equalsIgnoreCase("value")) {
                currentData.setValue(currentValue.toString());
            }

            if (qName.equalsIgnoreCase("Data")) {
                currentExtendedData.add(currentData);
            }

            if (qName.equalsIgnoreCase("ExtendedData")) {
                currentPlacemark.setExtendedData(currentExtendedData);
            }

            if (qName.equalsIgnoreCase("coordinates")) {
                currentPoint.setCoordinates(currentValue.toString());
            }

            if (qName.equalsIgnoreCase("Point")) {
                currentPlacemark.setPoint(currentPoint);
            }

            if (qName.equalsIgnoreCase("Placemark")) {
                result.add(currentPlacemark);
            }
        }
    }

    // http://www.saxproject.org/apidoc/org/xml/sax/ContentHandler.html#characters%28char%5B%5D,%20int,%20int%29
    // SAX parsers may return all contiguous character data in a single chunk,
    // or they may split it into several chunks
    @Override
    public void characters(char ch[], int start, int length) {
        currentValue.append(ch, start, length);
    }
}
