package com.practicas.firebaselogintest;

import android.app.Application;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.practicas.firebaselogintest.Firebase.LugaresAsinc;
import com.practicas.firebaselogintest.Firebase.LugaresFirestore;
import com.practicas.firebaselogintest.datos.LugaresBD;
import com.practicas.firebaselogintest.datos.LugaresLista;
import com.practicas.firebaselogintest.datos.RepositorioLugares;
import com.practicas.firebaselogintest.modelo.GeoPunto;
import com.practicas.firebaselogintest.presentacion.AdaptadorLugares;
import com.practicas.firebaselogintest.presentacion.AdaptadorLugaresBD;

public class Aplicacion extends Application {

    //public RepositorioLugares lugares = new LugaresLista();
    public LugaresBD lugares;
    //public AdaptadorLugares adaptador = new AdaptadorLugares(lugares);
    //ADAPTADOR BASE DE DATOS SQLITE
    public AdaptadorLugaresBD adaptador;
    public LugaresAsinc lugaresAsinc;

    public GeoPunto posicionActual = new GeoPunto(0.0,0.0);

    @Override
    public void onCreate() {
        super.onCreate();
        lugares = new LugaresBD(this);
        adaptador = new AdaptadorLugaresBD(lugares, lugares.extraeCursor());
        lugaresAsinc= new LugaresFirestore();
    }

    public RepositorioLugares getLugares() {
        return lugares;
    }
}