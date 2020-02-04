package com.fragmentoestudio.agronodo.Agregar_Campo;


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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class Mapa_Agregar_Campo extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private View view;
    private MapView mapView;

    public ArrayList<LatLng> coordenadas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa_agregar_campo, container, false);
        mapView = view.findViewById(R.id.mapview);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
        mapView.onResume();
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(location.getLatitude(), location.getLongitude())), 17.0f));
                        }
                    }
                });
        moveMarker();
        setMapLongClick(mMap);
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

                PolylineOptions polylineOptions= new PolylineOptions();
                polylineOptions.width(7);
                polylineOptions.color(Color.GREEN);

                for (int i=0; i<coordenadas.size(); i++) {
                    Marker marcador = mMap.addMarker(new MarkerOptions()
                            .position(coordenadas.get(i))
                            .title("Marcador #" + (i+1)));
                            //.draggable(true)
                            //.icon(getMarkerIcon("#002402")))
                    marcador.setTag(String.valueOf(i));
                    polylineOptions.add(coordenadas.get(i));
                }
                polylineOptions.add(coordenadas.get(0));
                map.addPolyline(polylineOptions);
                /*int i= 0;
                String cd= "";
                for (int x=0; x<coordenadas_polygono.size(); x++){
                    cd+= coordenadas_polygono.get(x);
                }
                cd+= coordenadas_polygono.get(0);
                PolylineOptions polylineOptions= new PolylineOptions();
                polylineOptions.width(10);
                polylineOptions.color(Color.GREEN);
                StringTokenizer st = new StringTokenizer(cd, ":;");
                while (st.hasMoreTokens()) {
                    String Latitud = st.nextToken();
                    String Longitud = st.nextToken();
                    Marker marcador = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(Latitud),Double.parseDouble(Longitud)))
                            .title("Marcador #" + (i+1))
                            .draggable(true)
                            .icon(getMarkerIcon("#002402")));
                    marcador.setTag(String.valueOf(i));
                    fab_Limpiar.setVisibility(View.VISIBLE);
                    fab_Limpiar.setAnimation(anim_slide_down);
                    polylineOptions.add(new LatLng(Double.parseDouble(Latitud),Double.parseDouble(Longitud)));
                    i++;
                }
                */
            }
        });
    }
}
