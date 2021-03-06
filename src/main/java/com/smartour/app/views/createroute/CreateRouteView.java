package com.smartour.app.views.createroute;

import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.smartour.app.data.entity.map.Placemark;
import com.smartour.app.data.service.PlacemarkService;
import com.smartour.app.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@PageTitle("Create Route")
@Route(value = "tour", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class CreateRouteView extends VerticalLayout {

    private GoogleMap gmaps;
    private TextField searchQueryField;
    private Button searchButton;
    private List<GoogleMapMarker> markers = new ArrayList<>();
    private PlacemarkService placemarkService;

    public CreateRouteView(@Autowired PlacemarkService placemarkService) {
        this.placemarkService = placemarkService;

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

        searchButton = new Button("Search");
        searchButton.addClickListener(e -> {
            for (GoogleMapMarker marker : markers) {
                gmaps.removeMarker(marker);
            }
            markers = new ArrayList<>();

            if (searchQueryField.getValue().isBlank()) {
                Notification.show("Loading default placemarks...");
                loadDefaultPlacemarks();
            } else {
                Notification.show("Loading route \"" + searchQueryField.getValue() + "\"...");
                loadPlacemarksByQuery(searchQueryField.getValue());
            }
        });

        HorizontalLayout searchLayout = new HorizontalLayout(searchQueryField, searchButton);
        searchLayout.setVerticalComponentAlignment(Alignment.BASELINE, searchQueryField, searchButton);

        add(searchLayout, gmaps);

        loadDefaultPlacemarks();
    }

    private void loadPlacemarks(Collection<Placemark> placemarks) {
        placemarks.forEach(p -> {
            GoogleMapMarker marker = gmaps.addMarker(
                    p.getName(),
                    new LatLon(p.getLatitude(), p.getLongitude()),
                    false,
                    null
            );
            marker.addInfoWindow("<h1>" + p.getName() + "</h1>\n\n" + p.getDescription());
            marker.addClickListener(ev -> {
                for (GoogleMapMarker gmarker : markers) {
                    if (!gmarker.equals(marker)) {
                        gmarker.setInfoWindowVisible(false);
                    }
                }
            });
            markers.add(marker);
        });

        Notification.show("Loaded " + placemarks.size() + " placemarks");
    }

    private void loadDefaultPlacemarks() {
        loadPlacemarks(placemarkService.findAll());
    }

    private void loadPlacemarksByQuery(String query) {
        loadPlacemarks(placemarkService.findByPhrase(query));
    }
}
