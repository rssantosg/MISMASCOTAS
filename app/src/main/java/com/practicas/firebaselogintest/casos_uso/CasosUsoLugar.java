package com.practicas.firebaselogintest.casos_uso;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.practicas.firebaselogintest.Aplicacion;
import com.practicas.firebaselogintest.Firebase.AdaptadorLugaresFirestore;
import com.practicas.firebaselogintest.Firebase.LugaresAsinc;
import com.practicas.firebaselogintest.MainActivity;
import com.practicas.firebaselogintest.R;
import com.practicas.firebaselogintest.datos.LugaresBD;
import com.practicas.firebaselogintest.datos.RepositorioLugares;
import com.practicas.firebaselogintest.modelo.GeoPunto;
import com.practicas.firebaselogintest.modelo.Lugar;
import com.practicas.firebaselogintest.presentacion.AcercaDeActivity;
import com.practicas.firebaselogintest.presentacion.AdaptadorLugaresBD;
import com.practicas.firebaselogintest.presentacion.EdicionLugarActivity;
import com.practicas.firebaselogintest.presentacion.MainActivity3;
import com.practicas.firebaselogintest.presentacion.VistaLugarActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.practicas.firebaselogintest.casos_uso.CasosUsoLocalizacion.solicitarPermiso;

public class CasosUsoLugar {

    private Activity actividad;
    //private RepositorioLugares lugares;
    //PERMISO GALERIA READ_EXTERNAL_STORAGE
    private static final int SOLICITUD_PERMISO_LECTURA=0;

    //sqlite
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;

    //FIREBASE
    private AdaptadorLugaresFirestore adaptadorLugaresFirestore;
    public LugaresAsinc lugaresAsinc;
    private CollectionReference instanciaColeccion = FirebaseFirestore.getInstance().collection("lugares");



    //constructor
    public CasosUsoLugar(Activity actividad, LugaresAsinc lugares, AdaptadorLugaresFirestore adaptador) {
        this.actividad = actividad;
        this.lugaresAsinc = lugares;
        this.adaptadorLugaresFirestore =adaptador;
        Query query = FirebaseFirestore.getInstance() .collection("lugares") .limit(50);
        FirestoreRecyclerOptions<Lugar> opciones = new FirestoreRecyclerOptions .Builder<Lugar>().setQuery(query,Lugar.class).build();
        adaptadorLugaresFirestore = new AdaptadorLugaresFirestore(opciones, actividad.getApplicationContext());
    }

    // OPERACIONES BÁSICAS
    public void mostrar(int pos) {
        Intent mostrar = new Intent(actividad, VistaLugarActivity.class);
        mostrar.putExtra("pos", pos);
        actividad.startActivity(mostrar);
    }

    public void editar(int pos, int codigoSolicitud) {
        Intent intent_ed_lugar = new Intent(actividad, EdicionLugarActivity.class);
        intent_ed_lugar.putExtra("pos",pos);
        actividad.startActivityForResult(intent_ed_lugar,codigoSolicitud);
    }

    public void
    actualizaPosLugar(int pos, Lugar lugar) {
        String id = adaptadorLugaresFirestore.getKey(pos);
        guardar(id, lugar);
    }

    public void guardar(String id, Lugar nuevoLugar){
        lugaresAsinc.actualiza(id,nuevoLugar);
        adaptadorLugaresFirestore.notifyDataSetChanged();
        //adaptador.notifyDataSetChanged();
    }



    public void borrar(final int id) {
        new AlertDialog.Builder(actividad)
                .setTitle("Borrado de lugar")
                .setIcon(R.mipmap.icono_app)
                .setMessage("¿Seguro de eliminar este lugar?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        instanciaColeccion.document(String.valueOf(id)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) { Toast.makeText(actividad.getApplicationContext(), "Lugar eliminado",Toast.LENGTH_LONG).show();
                            } }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) { Toast.makeText(actividad.getApplicationContext(), "Error al eliminar lugar firestore "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }); //adaptador.setCursor(lugares.extraeCursor());
                        adaptadorLugaresFirestore.notifyDataSetChanged(); actividad.finish(); }}) .setNegativeButton("Cancelar", null) .show();
                    }


                        /*lugares.borrar(id);
                        adaptador = (AdaptadorLugaresBD) ((Aplicacion)actividad.getApplicationContext()).adaptador;
                        adaptador.setCursor(lugares.extraeCursor());
                        adaptador.notifyDataSetChanged();
                        actividad.finish();
                    }})
                .setNegativeButton("Cancelar", null)
                .show();
    }*/

    public void eliminar_Foto(final int id, ImageView foto, View v) {
        new AlertDialog.Builder(actividad)
                .setTitle("Eliminar foto")
                .setIcon(R.mipmap.icono_app)
                .setMessage("¿Seguro de eliminar esa foto?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Snackbar.make(v,"Imagen eliminada",Snackbar.LENGTH_LONG).show();
                        ponerFoto(id,"",foto);
                    }})
                .setNegativeButton("NO", null)
                .show();
    }





    // INTENCIONES
    public void compartir(Lugar lugar) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,"Observa este lugar " +lugar.getNombre() + " - " + lugar.getUrl()+ "\n" +lugar.getFoto()) ;
        actividad.startActivity(i);
    }
    public void llamarTelefono(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + lugar.getTelefono())));
    }

    public void verPgWeb(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(lugar.getUrl())));
    }

    public final void verMapa(Lugar lugar) {
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        Uri uri = lugar.getPosicion() != GeoPunto.SIN_POSICION
                ?Uri.parse("geo:" + lat + ',' +
                lon+"?z=18&q="+Uri.encode(lugar.getDireccion()))
                :Uri.parse("geo:0,0?q="+Uri.encode(lugar.getDireccion()));
        Log.d("tag casos uso lugar","vermapa " +uri + " " +
                Uri.encode(lugar.getDireccion())+ "\n" + lugar.getPosicion() + " geopto "+GeoPunto.SIN_POSICION);
        actividad.startActivity(new Intent("android.intent.action.VIEW",
                uri));
    }

    public void ponerDeGaleria(int codigoSolicitud){
        String action;
        if (Build.VERSION.SDK_INT>=19){
            action = Intent.ACTION_OPEN_DOCUMENT;
        }else {
            action = Intent.ACTION_PICK;
        }
        Intent intent = new Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        actividad.startActivityForResult(intent,codigoSolicitud);
    }

    public void ponerFoto(int pos, String uri, ImageView imageView){
        Lugar lugar =adaptador.lugarPosicion(pos);  //lugares.elemento(pos);
        lugar.setFoto(uri);
        visualizarFoto(lugar, imageView);
        actualizaPosLugar(pos,lugar);
    }

    public void nuevo() {
        String id = lugaresAsinc.nuevo();
        Intent nuevo_lugar = new Intent(actividad, EdicionLugarActivity.class);
        nuevo_lugar.putExtra("_id", id);
        actividad.startActivity(nuevo_lugar);
    }



    public void visualizarFoto(Lugar lugar, ImageView imageView){
        if(lugar.getFoto()!=null && !lugar.getFoto().isEmpty()){
            imageView.setImageBitmap(reduceBitmap(actividad, lugar.getFoto(), 1024, 1024));
        } else { imageView.setImageBitmap(null); } }

    public Uri tomarFoto(int codidoSolicitud) {
        try { Uri uriUltimaFoto; File file = File.createTempFile(
                "img_" + (System.currentTimeMillis() / 1000), ".jpg",
                actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            if (Build.VERSION.SDK_INT >= 24) {
                uriUltimaFoto = FileProvider.getUriForFile(
                        actividad, "misiontic.uis.mislugarestic.fileProvider", file);
            } else { uriUltimaFoto = Uri.fromFile(file); }
            Intent intento_tomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intento_tomarFoto.putExtra(MediaStore.EXTRA_OUTPUT, uriUltimaFoto);
            actividad.startActivityForResult(intento_tomarFoto, codidoSolicitud);
            return uriUltimaFoto;
        } catch (IOException ex) { Toast.makeText(actividad, "Error al crear fichero de imagen", Toast.LENGTH_LONG).show();
            return null;
        }
    }



    private Bitmap reduceBitmap(Context contexto, String uri,int maxAncho, int maxAlto) {
        try {
            InputStream input = null;
            Uri u = Uri.parse(uri);
            if (u.getScheme().equals("http") || u.getScheme().equals("https")) {
                input = new URL(uri).openStream();
            } else {
                input = contexto.getContentResolver().openInputStream(u);
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(input, null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Fichero/recurso de imagen no encontrado "+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Toast.makeText(contexto, "Error accediendo a imagen "+e.getMessage() ,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    /*public Uri tomarFoto(int codigoSolicitud){
        try{
            Uri uriUltimatoFoto;
            File file = File.createTempFile("img_"+
                            (System.currentTimeMillis()/1000),".jpg",
                    actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            if (ContextCompat.checkSelfPermission(actividad,
                    Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                solicitarPermiso(Manifest.permission.READ_EXTERNAL_STORAGE,
                        "Sin el permiso de lectura no podrá visualizar la foto que capturó",
                        SOLICITUD_PERMISO_LECTURA, actividad);
                uriUltimatoFoto= Uri.parse(String.valueOf(R.mipmap.icono_app));
            }

            else if (Build.VERSION.SDK_INT>=24){
                uriUltimatoFoto = FileProvider.getUriForFile(actividad,"misiontic.appmissitios.fileProvider",file);
            } else {
                uriUltimatoFoto = Uri.fromFile(file);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uriUltimatoFoto);
            actividad.startActivityForResult(intent,codigoSolicitud);
            return uriUltimatoFoto;
        } catch (IOException e) {
            Toast.makeText(actividad,"Error al crear el fichero de imagen "+e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return null;
        }
    }*/

       /*public void actualizaPosLugar(int pos, Lugar lugar){
        int id= adaptador.idPosicion(pos);
        guardar(id,lugar);
    }*/


}
