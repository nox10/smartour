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

import java.util.List;

@PageTitle("Create Route")
@Route(value = "tour", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class CreateRouteView extends VerticalLayout {

    private GoogleMap gmaps;
    private TextField searchQueryField;
    private Button searchButton;

    public CreateRouteView(@Autowired PlacemarkService placemarkService) {
        setSizeFull();

        String apiKey = "AIzaSyCEQdmi84t3titEn8xHkf7pCZ2ll5Otcv4";

        gmaps = new GoogleMap(apiKey, null, null);
        gmaps.setMapType(GoogleMap.MapType.ROADMAP);
        gmaps.setSizeFull();
        gmaps.setCenter(new LatLon(59.9311, 30.3609));

        // add click listener to get latitude and longitude on left click
        gmaps.addClickListener(
                ev ->
                        Notification.show(
                                "Left click at latitude: "
                                        + ev.getLatitude()
                                        + "; Longitude: "
                                        + ev.getLongitude()));

        // add click listener to get latitude and longitude on right click
        gmaps.addRightClickListener(
                ev ->
                        Notification.show(
                                "Right click at latitude: "
                                        + ev.getLatitude()
                                        + "; Longitude: "
                                        + ev.getLongitude()));

        searchQueryField = new TextField("Route topic");
        searchQueryField.setAutofocus(true);

        searchButton = new Button("Create");
        searchButton.addClickListener(e -> {
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
            });
        });

        HorizontalLayout searchLayout = new HorizontalLayout(searchQueryField, searchButton);
        searchLayout.setVerticalComponentAlignment(Alignment.BASELINE, searchQueryField, searchButton);

        add(searchLayout, gmaps);
    }

}
