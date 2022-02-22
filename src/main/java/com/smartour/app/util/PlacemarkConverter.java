package com.smartour.app.util;

import com.github.demidko.aot.WordformMeaning;
import com.smartour.app.data.entity.map.Placemark;
import com.smartour.app.util.xml.entity.XmlPlacemark;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.github.demidko.aot.WordformMeaning.lookupForMeanings;

public class PlacemarkConverter {
    public static Placemark apply(XmlPlacemark xmlPlacemark) {
        Placemark placemark = new Placemark();
        placemark.setName(xmlPlacemark.getName());
        placemark.setStyleUrl(xmlPlacemark.getStyleUrl());
        placemark.setDescription(xmlPlacemark.getDescription());
        placemark.setData(xmlPlacemark.getExtendedData().get(0).getValue());
        placemark.setSource(xmlPlacemark.getSource());

        String[] latLonHeigth = xmlPlacemark.getPoint().getCoordinates().split(",");

        placemark.setLatitude(Double.parseDouble(latLonHeigth[1]));
        placemark.setLongitude(Double.parseDouble(latLonHeigth[0]));
        placemark.setHeight(Double.parseDouble(latLonHeigth[2]));

        // TODO: don't include links etc
        placemark.setNormalizedDescription(getNormalizedPhrase(xmlPlacemark.getDescription()));
        placemark.setNormalizedName(getNormalizedPhrase(xmlPlacemark.getName()));

        return placemark;
    }

    public static String getNormalizedPhrase(String text) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String word : getLettersAndDigitsArray(text)) {
            stringBuilder.append(getNormalizedWord(word)).append(" ");
        }

        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    public static String getNormalizedWord(String word) {
        String normalized;
        try {
            List<WordformMeaning> meanings = lookupForMeanings(word);
            if (meanings.isEmpty()) {
                normalized = word;
            } else {
                normalized = meanings.get(0).getLemma().toString();
            }
        } catch (IOException e) {
            normalized = word;
        }

        return normalized.toLowerCase();
    }

    private static List<String> getLettersAndDigitsArray(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetterOrDigit(s.charAt(i))) {
                sb.append(s.charAt(i));
            } else {
                sb.append(" ");
            }
        }

        return Arrays.stream(sb.toString().split(" ")).filter(str -> !str.isBlank()).toList();
    }
}
