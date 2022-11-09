package com.practicas.firebaselogintest.presentacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.practicas.firebaselogintest.Aplicacion;
import com.practicas.firebaselogintest.R;
import com.practicas.firebaselogintest.modelo.GeoPunto;
import com.practicas.firebaselogintest.modelo.Lugar;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mapa;
    //private RepositorioLugares lugares;
    //visualizar los datos de base de datos sqlite

    private AdaptadorLugaresBD adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        //lugares =((Aplicacion)getApplication()).lugares;
        adaptador= (AdaptadorLugaresBD) ((Aplicacion)getApplication()).adaptador;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);//modificar el tipo de mapa a visualizar
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(true);//habilitar el zoom en el mapa para acercar o alejar con gestos de los dedos
            mapa.getUiSettings().setCompassEnabled(true);//brujula
        }
        if (adaptador.getItemCount()>0){//lugares.tamaño()
            GeoPunto p = adaptador.lugarPosicion(0).getPosicion(); //lugares.elemento(0).getPosicion();
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(p.getLatitud(),p.getLongitud()),13));//zooom 12 [2 a 21] indica que tan lejos quiere ver al principio el mapa o que tan cerca
        }
        for (int n=0;n<adaptador.getItemCount();n++){//lugares.tamaño()
            Lugar lugar = adaptador.lugarPosicion(n); //lugares.elemento(n);
            GeoPunto p= lugar.getPosicion();
            if (p!=null && p.getLatitud() !=0){
                Bitmap iGrande = BitmapFactory.decodeResource(
                        getResources(),lugar.getTipo().getRecurso());
                Bitmap icono = Bitmap.createScaledBitmap(iGrande,
                        iGrande.getWidth()/7,
                        iGrande.getHeight()/7,false);
                mapa.addMarker(new MarkerOptions()
                                .position(new LatLng(p.getLatitud(),p.getLongitud()))
                                .title(lugar.getNombre())
                                .snippet(lugar.getDireccion())
                                .icon(BitmapDescriptorFactory.fromBitmap(icono))
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))// el pin o chincheta roja
                );
            }
        }

        mapa.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for (int pos=0;pos<adaptador.getItemCount();pos++){//lugares.tamaño()
            if (adaptador.lugarPosicion(pos).getNombre().equals(marker.getTitle())){
                Intent intent = new Intent(this,VistaLugarActivity.class);
                intent.putExtra("pos",pos);
                startActivity(intent);
                break;
            }
        }
    }
}
