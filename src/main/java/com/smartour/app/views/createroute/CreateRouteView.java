package com.smartour.app.views.createroute;

import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.smartour.app.data.entity.map.Placemark;
import com.smartour.app.data.service.PlacemarkService;
import com.smartour.app.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Create Route")
@Route(value = "tour", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class CreateRouteView extends VerticalLayout {

    private GoogleMap gmaps;
    private TextField searchQueryField;
    private Button searchButton;
    private Button showAllButton;
    private List<GoogleMapMarker> markers = new ArrayList<>();

    public CreateRouteView(@Autowired PlacemarkService placemarkService) {
        setSizeFull();

        String apiKey = "AIzaSyCEQdmi84t3titEn8xHkf7pCZ2ll5Otcv4";

        gmaps = new GoogleMap(apiKey, null, null);
        gmaps.setMapType(GoogleMap.MapType.ROADMAP);
        gmaps.setSizeFull();
        gmaps.disableStreetViewControl(true);
        gmaps.disableFullScreenControl(true);
        gmaps.disableMapTypeControl(true);
        gmaps.setCenter(new LatLon(59.9311, 30.3609));

        searchQueryField = new TextField("Route topic");
        searchQueryField.setAutofocus(true);

        searchButton = new Button("Create route");
        searchButton.addClickListener(e -> {
            if (searchQueryField.getValue().isBlank()) {
                Notification.show("Search query cannot be empty");
                return;
            }

            for (GoogleMapMarker marker : markers) {
                gmaps.removeMarker(marker);
            }
            markers = new ArrayList<>();

            Notification.show("Loading route \"" + searchQueryField.getValue() + "\"...");

            List<Placemark> placemarks = placemarkService.findByPhrase(searchQueryField.getValue());

            placemarks.forEach(p -> {
                GoogleMapMarker marker = gmaps.addMarker(
                        p.getName(),
                        new LatLon(p.getLatitude(), p.getLongitude()),
                        false,
                        null
                );
                marker.addInfoWindow("<h1>" + p.getName() + "</h1>\n\n" + p.getDescription());
                markers.add(marker);
            });
        });

        showAllButton = new Button("Show all placemarks");
        showAllButton.addClickListener(e -> {
            for (GoogleMapMarker marker : markers) {
                gmaps.removeMarker(marker);
            }
            markers = new ArrayList<>();

            Notification.show("Loading all placemarks...");

            List<Placemark> placemarks = placemarkService.findAll();

            placemarks.forEach(p -> {
                GoogleMapMarker marker = gmaps.addMarker(
                        p.getName(),
                        new LatLon(p.getLatitude(), p.getLongitude()),
                        false,
                        null
                );
                marker.addInfoWindow("<h1>" + p.getName() + "</h1>\n\n" + p.getDescription());
                markers.add(marker);
            });

            Notification.show("Loaded " + placemarks.size() + " placemarks");
        });

        FlexLayout showAllButtonWrapper = new FlexLayout(showAllButton);
        showAllButtonWrapper.setJustifyContentMode(JustifyContentMode.END);

        HorizontalLayout searchLayout = new HorizontalLayout(searchQueryField, searchButton, showAllButtonWrapper);
        searchLayout.setWidth("100%");
        searchLayout.setVerticalComponentAlignment(Alignment.BASELINE, searchQueryField, searchButton, showAllButtonWrapper);
        searchLayout.expand(showAllButtonWrapper);

        add(searchLayout, gmaps);
    }
}
