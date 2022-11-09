package com.practicas.firebaselogintest.presentacion;

import android.database.Cursor;

import com.practicas.firebaselogintest.datos.LugaresBD;
import com.practicas.firebaselogintest.datos.RepositorioLugares;
import com.practicas.firebaselogintest.modelo.Lugar;

public class AdaptadorLugaresBD extends AdaptadorLugares {
    protected Cursor cursor;

    //CONSTRUCTOR
    public AdaptadorLugaresBD(RepositorioLugares lugares, Cursor cursor) {
        super(lugares);
        this.cursor = cursor;
    }

    //METODOS GET Y SET DE CURSOR
    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Lugar lugarPosicion(int posicion){
        cursor.moveToPosition(posicion);
        return LugaresBD.extraeLugar(cursor);
    }

    public int idPosicion(int posicion){
        cursor.moveToPosition(posicion);
        if (cursor.getCount()>0){
            return cursor.getInt(0);
        }
        else {return -1;
        }
    }

    public int posicionId(int id){
        int pos =0;
        while(pos<getItemCount() &&idPosicion(pos)!=id)pos++;
        if(pos>= getItemCount()) return -1;
        else return pos;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position);
        Lugar lugar = lugarPosicion(position);
        holder.personaliza(lugar);
        holder.itemView.setTag(new Integer(position));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
