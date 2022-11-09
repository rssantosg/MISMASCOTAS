package com.practicas.firebaselogintest.presentacion;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.practicas.firebaselogintest.Aplicacion;
import com.practicas.firebaselogintest.Firebase.AdaptadorLugaresFirestore;
import com.practicas.firebaselogintest.Firebase.LugaresAsinc;
import com.practicas.firebaselogintest.R;
import com.practicas.firebaselogintest.casos_uso.CasosUsoLugar;
import com.practicas.firebaselogintest.datos.LugaresBD;
import com.practicas.firebaselogintest.modelo.GeoPunto;
import com.practicas.firebaselogintest.modelo.Lugar;
import com.practicas.firebaselogintest.modelo.TipoLugar;

public class EdicionLugarActivity extends AppCompatActivity {

    private CasosUsoLugar usoLugar;
    private int pos;
    private String _id;
    private Lugar lugar;
    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;
    private Toast msnToast;
    //base de datos sqlite
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;

    private AdaptadorLugaresFirestore adaptadorLugaresFirestore;
    public LugaresAsinc lugaresAsinc;
    private CollectionReference instanciaEdicion = FirebaseFirestore.getInstance().collection("lugares");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);
        lugar = new Lugar();
        lugares = ((Aplicacion) getApplication()).lugares;
        adaptador=((Aplicacion)getApplication()).adaptador;

        usoLugar = new CasosUsoLugar(this, lugaresAsinc, adaptadorLugaresFirestore);
        lugaresAsinc = ((Aplicacion) getApplication()).lugaresAsinc;

        actualizaVistas();

        Query query = FirebaseFirestore.getInstance()
                .collection("lugares")
                .limit(50);
                FirestoreRecyclerOptions<Lugar> opciones = new FirestoreRecyclerOptions
                .Builder<Lugar>().setQuery(query,Lugar.class).build();
                adaptadorLugaresFirestore = new
                        AdaptadorLugaresFirestore(opciones,this);

                Bundle extras = getIntent().getExtras();
                pos = extras.getInt("pos", 0);
                _id= extras.getString("_id"); String posEd = extras.getString("posEd");
                Log.d("TAG","pos edicion "+posEd );

                if (_id!=null) { setTitle("Nuevo lugar");
                    //lugar = new Lugar();//lugares.elemento(_id);
                    }
                else lugar = adaptadorLugaresFirestore.getItem(pos);
                //lugarPosicion(pos);//lugares.elemento(pos);
        }
    public void actualizaVistas() {
        nombre = findViewById(R.id.nombre);
        //nombre.setText(lugar.getNombre());

        direccion = findViewById(R.id.direccion);
        //direccion.setText(lugar.getDireccion());

        telefono = findViewById(R.id.telefono);
        //telefono.setText(Integer.toString(lugar.getTelefono()));

        url = findViewById(R.id.url);
        //url.setText(lugar.getUrl());

        comentario = findViewById(R.id.comentario);
        //comentario.setText(lugar.getComentario());

        tipo = findViewById(R.id.tipo);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        //tipo.setSelection(lugar.getTipo().ordinal());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicion_lugar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_guardar:
                lugar.setNombre(nombre.getText().toString());
                lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
                lugar.setDireccion(direccion.getText().toString());
                lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
                lugar.setUrl(url.getText().toString());
                lugar.setComentario(comentario.getText().toString());
                GeoPunto posicion = ((Aplicacion) getApplication()).posicionActual;
                    if (!posicion.equals(GeoPunto.SIN_POSICION)) {
                        lugar.setPosicion(posicion);
                    }
                    Log.d("TAG","posicion creada "+posicion);
                    Log.d("TAG","nuevo lugar "+lugar.toString());

                instanciaEdicion.add(lugar).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());

                    msnToast = Toast.makeText(getApplicationContext(),"Lugar creado exitosamente en Firestore",Toast.LENGTH_LONG);


                    msnToast = Toast.makeText(getApplicationContext(),"Cambios fueron guardados correctamente",Toast.LENGTH_LONG);
                    msnToast.setGravity(Gravity.CENTER,0,0);
                    msnToast.show();
                }
            }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(Exception e) {
                msnToast = Toast.makeText(getApplicationContext(),"Error al crear el lugar en firestore "+e.getMessage(),Toast.LENGTH_LONG);
                msnToast.setGravity(Gravity.CENTER,0,0); msnToast.show();
                 }
            });
            msnToast = Toast.makeText(getApplicationContext(),"Cambios guardados exitosamente",Toast.LENGTH_LONG);
            msnToast.setGravity(Gravity.CENTER,0,0);
            msnToast.show(); finish();
            return true;
            case R.id.accion_cancelar:
                if (_id!=null) { Log.d("TAG","EDL cancelar id"+_id);


                instanciaEdicion.document(_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Canceló operación", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error cancelar " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
                msnToast = Toast.makeText(getApplicationContext(),"Canceló la edición no hay cambios",Toast.LENGTH_LONG);
                msnToast.setGravity(Gravity.CENTER,0,0);
                msnToast.show();
                finish();
            return true;
        default:
                return super.onOptionsItemSelected(item);
      }
    }
}

    //si hace ud clic en su dispositivo a la opcioón de home o volver
    // se crea un nuevo registro en la base de datos
    //pero sin toda la información
    /*@Override
    protected void onStop() {
        super.onStop();
        Log.d("tag","on stop ela ");
        if (_id !=-1 & nombre.getText().toString().isEmpty()){
            Log.d("tag","borrar"+lugar.toString());
            lugares.borrar(_id);
        } else {
            Log.d("tag"," no borrar"+lugar.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag","on destroy ela");
        //pos, 0 _id -1 si es una edicion
        //pos, -1 _id 32 es un nuevo registro
        //if (_id!=-1)
        //else if (_id==-1) _id = adaptador.idPosicion(pos);
    }
}


    }*/

