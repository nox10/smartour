package com.smartour.app.util.xml.parser;

import com.smartour.app.util.xml.entity.XmlPlacemark;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class TestKmlParser {
    public static void main(String[] args) {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        try (InputStream is = getXMLFileAsStream()) {

            SAXParser saxParser = factory.newSAXParser();

            // parse XML and map to object, it works, but not recommend, try JAXB
            KmlParserHandler handler = new KmlParserHandler();

            saxParser.parse(is, handler);

            // print all
            List<XmlPlacemark> result = handler.getResult();
            result.forEach(System.out::println);

            System.out.println(Arrays.toString(result.toArray()));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    // get XML file from resources folder.
    private static InputStream getXMLFileAsStream() {
        return TestKmlParser.class.getClassLoader().getResourceAsStream("sampledata/antique.kml");
    }
}
