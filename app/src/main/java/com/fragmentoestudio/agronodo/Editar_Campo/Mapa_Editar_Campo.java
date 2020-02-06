package com.fragmentoestudio.agronodo.Editar_Campo;


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Mapa_Editar_Campo extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private View view;
    private MapView mapView;
    public FloatingActionButton fabVolver;

    public ArrayList<LatLng> coordenadas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa_editar_campo, container, false);
        fabVolver = view.findViewById(R.id.fab_volver);
        mapView = view.findViewById(R.id.mapview);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        moveMarker();
        setMapLongClick(mMap);

        int ID = getArguments().getInt("ID");
        Campos campo = SQLITE.obtenerCampo(getContext(), ID);

        try {
            JSONArray JSONcoordenadas = new JSONArray("[" + campo.getCoordenadas() + "]");
            for(int i=0; i<JSONcoordenadas.length(); i++){
                coordenadas.add(new LatLng(Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Latitud")), Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Longitud"))));
            }
            dibujarCampo();
            LatLngBounds latLngBounds = getPolygonLatLngBounds(coordenadas);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fabVolver.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (coordenadas.size() > 0) {
                    coordenadas.remove(coordenadas.size() - 1);
                    if (coordenadas.isEmpty()) {
                        fabVolver.setVisibility(View.GONE);
                        mMap.clear();
                    } else {
                        mMap.clear();
                        dibujarCampo();
                    }
                }
            }
        });


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.setIcon(getMarkerIcon("#1b3e9a"));
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                coordenadas.set(Integer.parseInt(String.valueOf(marker.getTag())), marker.getPosition());
                mMap.clear();
                dibujarCampo();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }
        });
    }

    public void moveMarker() {
        if (view != null &&
                view.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }
    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Animation anim_slide_down = AnimationUtils.loadAnimation(this, R.anim.slide_from_top_fab);
                map.clear();
                coordenadas.add(latLng);
                fabVolver.setVisibility(View.VISIBLE);
                dibujarCampo();
            }
        });
    }

    void dibujarCampo(){
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(7);
        polylineOptions.color(Color.GREEN);
        for (int i = 0; i < coordenadas.size(); i++) {
            Marker marcador = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas.get(i))
                    .draggable(true)
                    .title("Marcador #" + (i + 1)));
            marcador.setTag(String.valueOf(i));
            polylineOptions.add(coordenadas.get(i));
        }
        polylineOptions.add(coordenadas.get(0));
        mMap.addPolyline(polylineOptions);
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private static LatLngBounds getPolygonLatLngBounds(final List<LatLng> polygon) {
        final LatLngBounds.Builder centerBuilder = LatLngBounds.builder();
        for (LatLng point : polygon) {
            centerBuilder.include(point);
        }
        return centerBuilder.build();
    }
}
