package com.fragmentoestudio.agronodo.Editar_SubPredio;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.Clases.SubCampos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Mapa_Editar_SubPredio extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private View view;
    private MapView mapView;
    public FloatingActionButton fabVolver;

    public ArrayList<LatLng> coordenadas = new ArrayList<>();
    ArrayList<LatLng> coordenadas_padre = new ArrayList<>();

    ArrayList<SubCampos> subCampos = new ArrayList<>();

    SubCampos subCampo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa_editar_sub_predio, container, false);

        fabVolver = view.findViewById(R.id.fab_volver);
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
        moveMarker();
        setMapLongClick(mMap);

        int ID = getArguments().getInt("ID");
        subCampo = SQLITE.obtenerSubCampo(getContext(), ID);

        Campos campo = SQLITE.obtenerCampo(getContext(), subCampo.getID_Padre());
        subCampos = SQLITE.obtenerSubCampos(getContext(), subCampo.getID_Padre());

        try {
            JSONArray JSONcoordenadas = new JSONArray("[" + campo.getCoordenadas() + "]");
            for (int i = 0; i < JSONcoordenadas.length(); i++) {
                coordenadas_padre.add(new LatLng(Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Latitud")), Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Longitud"))));
            }

            JSONArray JSONcoordenadas_edit = new JSONArray("[" + subCampo.getCoordenadas() + "]");
            for (int i = 0; i < JSONcoordenadas.length(); i++) {
                coordenadas.add(new LatLng(Double.parseDouble(JSONcoordenadas_edit.getJSONObject(i).getString("Latitud")), Double.parseDouble(JSONcoordenadas_edit.getJSONObject(i).getString("Longitud"))));
            }

            dibujarCampo();
            LatLngBounds latLngBounds = getPolygonLatLngBounds(coordenadas_padre);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        fabVolver.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (coordenadas.size() > 0) {
                    coordenadas.remove(coordenadas.size() - 1);
                    mMap.clear();
                    dibujarCampo();
                    if (coordenadas.isEmpty()) {
                        fabVolver.setVisibility(View.GONE);
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
                if (verificarDentroDelArea(marker.getPosition(), coordenadas_padre)) {
                    Boolean libre = true;
                    for (SubCampos subCampo : subCampos) {
                        try {
                            ArrayList<LatLng> coordenadas_hijo = new ArrayList<>();
                            JSONArray JSONcoordenadas = new JSONArray("[" + subCampo.getCoordenadas() + "]");
                            for (int i = 0; i < JSONcoordenadas.length(); i++) {
                                coordenadas_hijo.add(new LatLng(Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Latitud")), Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Longitud"))));
                            }
                            if (verificarDentroDelArea(marker.getPosition(), coordenadas_hijo))
                                libre = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {

                        }
                    }
                    if (libre) {
                        coordenadas.set(Integer.parseInt(String.valueOf(marker.getTag())), marker.getPosition());
                        mMap.clear();
                        dibujarCampo();
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    } else {
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                        dialogo1.setMessage("El Marcador no puede estar dentro de otros SubCampos");
                        dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                mMap.clear();
                                dibujarCampo();
                            }
                        });
                        dialogo1.show();
                    }
                } else {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                    dialogo1.setMessage("El Marcador debe estar dentro del Predio");
                    dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            mMap.clear();
                            dibujarCampo();
                        }
                    });
                    dialogo1.show();
                }
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
                if (verificarDentroDelArea(latLng, coordenadas_padre)) {
                    Boolean libre = true;
                    for (SubCampos subCampo : subCampos) {
                        try {
                            ArrayList<LatLng> coordenadas_hijo = new ArrayList<>();
                            JSONArray JSONcoordenadas = new JSONArray("[" + subCampo.getCoordenadas() + "]");
                            for (int i = 0; i < JSONcoordenadas.length(); i++) {
                                coordenadas_hijo.add(new LatLng(Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Latitud")), Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Longitud"))));
                            }
                            if (verificarDentroDelArea(latLng, coordenadas_hijo))
                                libre = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {

                        }
                    }
                    if (libre) {
                        map.clear();
                        coordenadas.add(latLng);
                        fabVolver.setVisibility(View.VISIBLE);
                        dibujarCampo();
                    } else {
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                        dialogo1.setMessage("El Marcador no puede estar dentro de otros SubCampos");
                        dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        });
                        dialogo1.show();
                    }
                } else {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                    dialogo1.setMessage("El Marcador debe estar dentro del Predio");
                    dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();
                }
            }
        });
    }

    void dibujarCampo() {
        try {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.width(7);
            polylineOptions.color(Color.argb(170, 0, 148, 81));
            for (int i = 0; i < coordenadas_padre.size(); i++) {
                polylineOptions.add(coordenadas_padre.get(i));
            }
            mMap.addPolyline(polylineOptions);
        } catch (Exception e) {
        }

        try {
            for (SubCampos subCampo : subCampos) {
                try {
                    ArrayList<LatLng> coordenadas_hijo = new ArrayList<>();
                    JSONArray JSONcoordenadas = new JSONArray("[" + subCampo.getCoordenadas() + "]");
                    for (int i = 0; i < JSONcoordenadas.length(); i++) {
                        coordenadas_hijo.add(new LatLng(Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Latitud")), Double.parseDouble(JSONcoordenadas.getJSONObject(i).getString("Longitud"))));
                    }

                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.width(7);
                    polylineOptions.color(Color.argb(170, 0, 50, 151));
                    for (int i = 0; i < coordenadas_hijo.size(); i++) {
                        polylineOptions.add(coordenadas_hijo.get(i));
                    }
                    mMap.addPolyline(polylineOptions);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }

        try {
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
        } catch (Exception e) {
        }
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

    private boolean verificarDentroDelArea(LatLng tap, ArrayList<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }
        return (intersectCount % 2) == 1; // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX);               // Rise over run
        double bee = (-aX) * m + aY;                // y = mx + b
        double x = (pY - bee) / m;                  // algebra is neat!

        return x > pX;
    }
}
