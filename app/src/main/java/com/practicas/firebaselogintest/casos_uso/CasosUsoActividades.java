package com.practicas.firebaselogintest.casos_uso;

import android.app.Activity;
import android.content.Intent;

import com.practicas.firebaselogintest.presentacion.AcercaDeActivity;
import com.practicas.firebaselogintest.presentacion.MapaActivity;
import com.practicas.firebaselogintest.presentacion.PreferenciasActivity;
import com.practicas.firebaselogintest.presentacion.UsuarioActivity;

public class CasosUsoActividades {

    protected Activity actividad;
    //constructor
    public CasosUsoActividades(Activity actividad) {
        this.actividad = actividad;
    }

    public void lanzarAcercaDe() {
        actividad.startActivity(new Intent(actividad, AcercaDeActivity.class));
    }


    public void lanzarPreferencias(int codigoSolicitud) {
        actividad.startActivityForResult
                (new Intent(actividad, PreferenciasActivity.class),codigoSolicitud);
    }
    //public void lanzarUsuario(){ actividad.startActivity(new Intent(actividad, UsuarioActivity.class)); }

    public void lanzarMapa(){
        actividad.startActivity(new Intent(actividad, MapaActivity.class));
    }
}
