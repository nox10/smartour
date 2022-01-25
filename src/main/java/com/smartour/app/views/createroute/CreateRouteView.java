package com.smartour.app.views.createroute;

import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
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

import java.util.Arrays;
import java.util.List;

@PageTitle("Create Route")
@Route(value = "tour", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class CreateRouteView extends VerticalLayout {

    private GoogleMap gmaps;
    private TextField searchQueryField;
    private Button searchButton;

    private List<LatLon> points = Arrays.asList(
            new LatLon(59.942349742927796, 30.281962732436185),
            new LatLon(59.950989629918396, 30.299901346327786),
            new LatLon(59.95455668758736, 30.334834436537747),
            new LatLon(59.94572664333772, 30.371486976520313),
            new LatLon(59.94301503285979, 30.352775886432422),
            new LatLon(59.9344160677453, 30.369856193439258),
            new LatLon(59.91979270910294, 30.384619071857227),
            new LatLon(59.91092953816051, 30.34281952656914)
    );

    public CreateRouteView() {
        setSizeFull();

        searchQueryField = new TextField("Route topic");
        searchButton = new Button("Create");
        searchButton.addClickListener(e -> {
            Notification.show("Loading route \"" + searchQueryField.getValue() + "\"...");
        });

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

        points.forEach(point -> {
            String name = "Sightseeing " + Double.toString(point.getLat()).substring(0, 5);
            GoogleMapMarker marker = gmaps.addMarker(
                    name,
                    point,
                    false,
                    null
            );
            marker.addInfoWindow("<h1>" + name + "</h1>" + "<text>Sightseeing description</text>");
        });

        HorizontalLayout searchLayout = new HorizontalLayout(searchQueryField, searchButton);
        searchLayout.setVerticalComponentAlignment(Alignment.BASELINE, searchQueryField, searchButton);

        add(searchLayout, gmaps);
    }

}
