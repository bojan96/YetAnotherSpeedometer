package org.unibl.etf.yetanotherspeedometer.routemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.unibl.etf.yetanotherspeedometer.R;
import org.unibl.etf.yetanotherspeedometer.db.AppDatabase;
import org.unibl.etf.yetanotherspeedometer.db.entity.Recording;
import org.unibl.etf.yetanotherspeedometer.db.entity.RecordingMaxSpeedPoint;
import org.unibl.etf.yetanotherspeedometer.db.entity.RecordingPoint;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RouteMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_RECORDING_ID = "extra_recording_id";
    @Inject
    public AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        var recordingId = getIntent().getIntExtra(EXTRA_RECORDING_ID, -1);
        database.getRecordingDao()
                .getRecordingWithPoints(recordingId).observe(this, recordingWithPoints ->
                {
                    database.getRecordingDao().getRecordingWithMaxSpeedPoint(recordingId).observe(this, recordingWithMaxSpeedPoint ->
                    {
                        displayRoute(googleMap, recordingWithPoints, recordingWithMaxSpeedPoint);
                    });

                });
    }

    private void displayRoute(GoogleMap googleMap, Map<Recording, List<RecordingPoint>> recordingWithPoints, Map<Recording, List<RecordingMaxSpeedPoint>> recordingWithMaxSpeedPoint)
    {

        var points = List.copyOf(recordingWithPoints.values()).get(0);
        var boundsBuilder = LatLngBounds.builder();
        points.forEach(p -> boundsBuilder.include(new LatLng(p.latitude, p.longitude)));

        drawRoute(googleMap, points);
        drawMaxSpeedMarker(googleMap, points, recordingWithMaxSpeedPoint);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics())));
    }

    private void drawRoute(GoogleMap googleMap, List<RecordingPoint> points)
    {
        var routePolyline = new PolylineOptions();
        routePolyline.width(20);
        routePolyline.color(Color.rgb(0, 0, 255));
        routePolyline.addAll(points.stream().map(p -> new LatLng(p.latitude, p.longitude)).collect(Collectors.toList()));
        googleMap.addPolyline(routePolyline);
    }

    private void drawMaxSpeedMarker(GoogleMap googleMap, List<RecordingPoint> points, Map<Recording, List<RecordingMaxSpeedPoint>> recordingWithMaxSpeedPoint)
    {
        if(recordingWithMaxSpeedPoint.size() == 0)
            return;

        var maxSpeedPointList = List.copyOf(recordingWithMaxSpeedPoint.values()).get(0);
        var maxSpeedPoint = maxSpeedPointList.get(0);
        var point = points.get(maxSpeedPoint.maxSpeedPointIndex);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("Max Speed"));
    }
}