package com.practicas.firebaselogintest.presentacion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.practicas.firebaselogintest.Aplicacion;
import com.practicas.firebaselogintest.Firebase.AdaptadorLugaresFirestore;
import com.practicas.firebaselogintest.Firebase.LugaresAsinc;
import com.practicas.firebaselogintest.MainActivity;
import com.practicas.firebaselogintest.R;
import com.practicas.firebaselogintest.casos_uso.CasosUsoActividades;
import com.practicas.firebaselogintest.casos_uso.CasosUsoLocalizacion;
import com.practicas.firebaselogintest.casos_uso.CasosUsoLugar;
import com.practicas.firebaselogintest.datos.LugaresBD;
import com.practicas.firebaselogintest.datos.RepositorioLugares;
import com.practicas.firebaselogintest.modelo.Lugar;

public class MainActivity3 extends AppCompatActivity {

    //private RepositorioLugares lugares;
    private CasosUsoLugar usoLugar;
    private CasosUsoActividades usoActividades;
    static final int RESULTADO_PREFERENCIAS =0;
    private RecyclerView recyclerView;
    //private AdaptadorLugares adaptador;
    private static  final int SOLICITUD_PERMISO_LOCALIZACION=1;
    private CasosUsoLocalizacion usoLocalizacion;

    private AdaptadorLugaresBD adaptador;
    private LugaresBD lugares;
    //ADAPTOR DE FIRESTORE
    private AdaptadorLugaresFirestore adaptadorLugaresFirestore;
    private LugaresAsinc lugaresAsinc;
    private CollectionReference instanciaMain =
            FirebaseFirestore.getInstance().collection("lugares");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        adaptador = ((Aplicacion) getApplication()).adaptador;
        lugaresAsinc = ((Aplicacion) getApplication()).lugaresAsinc;
        lugares = ((Aplicacion) getApplication()).lugares;
        usoLugar = new
                CasosUsoLugar(this, lugaresAsinc, adaptadorLugaresFirestore);
        usoActividades = new CasosUsoActividades(this);
        usoLocalizacion = new CasosUsoLocalizacion(this, SOLICITUD_PERMISO_LOCALIZACION);

        //recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(adaptadorLugaresFirestore);

        /*
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = (Integer) v.getTag(); //recyclerView.getChildAdapterPosition(v);
                usoLugar.mostrar(posicion);
            }
        });
        */

        /*Log.d("tag","on create main");
        recyclerView = findViewById(R.id.recyclerView);

        lugares = ((Aplicacion) getApplication()).lugares;
        usoLugar = new CasosUsoLugar(this);
        usoActividades = new CasosUsoActividades(this);
        adaptador = ((Aplicacion) getApplication()).adaptador;
        usoLocalizacion= new CasosUsoLocalizacion(this, SOLICITUD_PERMISO_LOCALIZACION);


        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = recyclerView.getChildAdapterPosition(v);
                usoLugar.mostrar(posicion);
            }
        });*/

        //barra de acciones
        Toolbar toolbar = findViewById(R.id.toolbar_Main);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout_Main);
        toolbar.setTitle(getTitle());

        cargarInfoFromFirestore();
        adaptadorLugaresFirestore.startListening();


        //Boton flotante FAB circular
        /**/
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, R.string.mensaje_fab, Snackbar.LENGTH_LONG).setAction("Accion",null).show();
                //usoLugar.nuevo();
                Intent nuevo_lugar = new Intent(getApplicationContext(), EdicionLugarActivity.class);
                nuevo_lugar.putExtra("_id", "UID");
                startActivity(nuevo_lugar);
            }
        });

        Log.d("Tag en Main", "Mensaje prueba por el logcat");
        Log.d("MAIN", "tamaño base datos " + adaptador.getItemCount());

        //AGREGADO SPRINT 5 PAGINA 40
        //FirebaseFirestore firestoreDB_lugares = FirebaseFirestore.getInstance();
        /**///for(int id=0; id<adaptador.getItemCount();id++){
        //Log.d("MAIN","tamaño base datos ->"+adaptador.lugarPosicion(id));
        // firestoreDB_lugares.collection("lugares").add(adaptador.lugarPosicion(id));
        //}
    }


    public void cargarInfoFromFirestore(){
            Query query = FirebaseFirestore.getInstance()
                    .collection("lugares")
                    .limit(50);
            FirestoreRecyclerOptions<Lugar> opciones = new
                FirestoreRecyclerOptions
                        .Builder<Lugar>().setQuery(query,Lugar.class).build();
                    adaptadorLugaresFirestore = new AdaptadorLugaresFirestore(opciones,this);
                    Log.d("TAG MAIN","cargarfirestore " + query.toString() + "\nrecycler"+opciones.toString());
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adaptadorLugaresFirestore);

                    adaptadorLugaresFirestore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int posicion = recyclerView.getChildAdapterPosition(view);
                            Lugar item_lugar = adaptadorLugaresFirestore.getItem(posicion);
                            String id_lugar = adaptadorLugaresFirestore.getSnapshots().getSnapshot(posicion).getId();
                            Log.d("MAIN","clic adaptador id"+id_lugar + "posicion "+posicion+"itemlugar "+item_lugar.getTipo().getRecurso());
                            Log.d("TAG","vista lugar elegido "+id_lugar + " coleccion\n"
                                    + FirebaseFirestore.getInstance().collection("lugares").document(id_lugar));


                            Context context = getAppContext();
                            /**/Intent intent = new Intent(context,VistaLugarActivity.class);
                            intent.putExtra("lugar_fire",id_lugar);
                            intent.putExtra("pos",posicion);
                            intent.putExtra("icono_recurso",item_lugar.getTipo().getRecurso());
                                context.startActivity(intent);
                        }
                    });
        }
        public void lanzarAcercade(View view){
            Intent abrir = new Intent(this, AcercaDeActivity.class);
            startActivity(abrir);
        }

        public void lanzarVistaLugar(View view){
            final EditText entrada = new EditText(this);
            entrada.setText("0");
            entrada.setInputType(InputType.TYPE_CLASS_NUMBER);

            new AlertDialog.Builder(this)
                    .setTitle("Selección de lugar")
                    .setMessage("indica su id:")
                    .setIcon(R.mipmap.icono_app_round)
                    .setView(entrada)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String id = (entrada.getText().toString());
                                Log.d("TAG"," buscar " );
                        usoLugar.mostrar(Integer.parseInt(id));//MODIFICACION DEL MOSTRAR

                    }}).setNegativeButton("Cancelar", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.ajustes){
            usoActividades.lanzarPreferencias(RESULTADO_PREFERENCIAS);
            return true;
        }
        if (id == R.id.acercaDe){
            usoActividades.lanzarAcercaDe();
            return true;
        }
        if (id == R.id.menu_buscar){
            lanzarVistaLugar(null);
            Log.d("Tag main","clic a la opcion buscar");
            return true;
        }
        /*if (id == R.id.menu_usuario){
            usoActividades.lanzarUsuario();
            return true;
        }*/
        if (id == R.id.menu_mapa){
            usoActividades.lanzarMapa();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==SOLICITUD_PERMISO_LOCALIZACION
                && grantResults.length==1
                && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            usoLocalizacion.permisoConcedido();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adaptadorLugaresFirestore.startListening();
        currentcontext=this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        adaptadorLugaresFirestore.stopListening();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adaptadorLugaresFirestore.stopListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Tag","on pause main");
        usoLocalizacion.desactivar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tag","on resume main");
        usoLocalizacion.activar();
    }

    private static MainActivity3 currentcontext;

    public static MainActivity3 getCurrentContext() {
        return currentcontext;
    }

    public static Context getAppContext(){
        return MainActivity3.getCurrentContext();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*public void cargarInfoFromFirestore() {
        Query query = FirebaseFirestore.getInstance()
                .collection("lugares")
                .limit(50); FirestoreRecyclerOptions<Lugar> opciones = new
                FirestoreRecyclerOptions .Builder<Lugar>().setQuery(query,Lugar.class).build();
        adaptadorLugaresFirestore = new AdaptadorLugaresFirestore(opciones,this);
        Log.d("TAG MAIN","cargarfirestore " + query.toString() + "\nrecycler"+opciones.toString());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptadorLugaresFirestore);

        adaptadorLugaresFirestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicion = recyclerView.getChildAdapterPosition(view);
                Lugar item_lugar = adaptadorLugaresFirestore.getItem(posicion);
                String id_lugar = adaptadorLugaresFirestore.getSnapshots().getSnapshot(posicion).getId();
                Log.d("MAIN","clic adaptador id"+id_lugar + "posicion "+posicion+"itemlugar "+item_lugar.getTipo().getRecurso());
                Log.d("TAG","vista lugar elegido "+id_lugar + " coleccion\n" + FirebaseFirestore.getInstance().collection("lugares").document(id_lugar));
                Context context = getAppContext();*/
                /*Intent intent = new Intent(context,VistaLugarActivity.class);
                intent.putExtra("lugar_fire",id_lugar);
                intent.putExtra("pos",posicion);
                intent.putExtra("icono_recurso",item_lugar.getTipo().getRecurso());
                context.startActivity(intent);
            }
        });
    }






    /*public void lanzarVistaLugar(View view){
        final EditText entrada = new EditText(this);
        entrada.setText("0");
        new AlertDialog.Builder(this)
                .setTitle(R.string.elijaLugar)
                .setMessage("indica su id:")
                .setView(entrada)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int id = Integer.parseInt(entrada.getText().toString());
                        usoLugar.mostrar(id);
                    }})
                .setNegativeButton("Cancelar", null)
                .show();
    }*/





}