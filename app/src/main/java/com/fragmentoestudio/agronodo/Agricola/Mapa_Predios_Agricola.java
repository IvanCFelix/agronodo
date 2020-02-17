package com.fragmentoestudio.agronodo.Agricola;

import android.Manifest;
import android.content.Intent;
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

import com.fragmentoestudio.agronodo.Agregar_Campo.Activity_Agregar_Campo;
import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.Menu_Agricola;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Mapa_Predios_Agricola extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private View view;
    private MapView mapView;
    public static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;
    public static final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    FloatingActionButton fabAgregar;

    ArrayList<Campos> campos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa_predios_agricola, container, false);

        getActivity().setTitle(" Mis Predios");

        fabAgregar = view.findViewById(R.id.fab_agregar);

        PedirPermisos();

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
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(location.getLatitude(), location.getLongitude())), 14.0f));
                        }
                    }
                });
        moveMarker();

        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
                    if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(getContext(), Activity_Agregar_Campo.class));
                    }
                } else {
                    startActivity(new Intent(getContext(), Activity_Agregar_Campo.class));
                }
            }
        });
        desplegarCampos();
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

    public void PedirPermisos() {
        if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            //Si alguno de los permisos no esta concedido lo solicita
            ActivityCompat.requestPermissions(getActivity(), permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .detach(Menu_Agricola.mapa_prediosAgronomo)
                    .attach(Menu_Agricola.mapa_prediosAgronomo)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMap!=null){
            desplegarCampos();
        }
    }

    void desplegarCampos(){
        mMap.clear();
        campos = SQLITE.obtenerCampos(getContext());
        for (Campos campo : campos){
            ArrayList<LatLng> listacoordeandas = new ArrayList<>();
            try {
                JSONArray coordenadas = new JSONArray("[" + campo.getCoordenadas() + "]");
                for(int i=0; i<coordenadas.length(); i++){
                    listacoordeandas.add(new LatLng(Double.parseDouble(coordenadas.getJSONObject(i).getString("Latitud")), Double.parseDouble(coordenadas.getJSONObject(i).getString("Longitud"))));
                }
                PolygonOptions polygonOptions = new PolygonOptions();
                for (int i = 0; i<coordenadas.length(); i++) {
                    polygonOptions.add(listacoordeandas.get(i));
                }
                polygonOptions.strokeColor(Color.rgb(36,133,60));
                polygonOptions.clickable(true);
                polygonOptions.fillColor(Color.argb(0, 47, 123, 255));
                mMap.addPolygon(polygonOptions);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
