package com.practicas.firebaselogintest.presentacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.practicas.firebaselogintest.LoginActivity;
import com.practicas.firebaselogintest.R;

public class UsuarioFragment extends Fragment {
    private TextView nombre, correo, telefono;
    private Button btnCerrarSesion;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_usuario, contenedor, false);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        nombre = vista.findViewById(R.id.nombre);
        nombre.setText(usuario.getDisplayName());
        correo = vista.findViewById(R.id.correo);
        correo.setText(usuario.getEmail());
        telefono = vista.findViewById(R.id.tel);
        telefono.setText(usuario.getPhoneNumber());

        usuario.reload(); btnCerrarSesion=vista.findViewById(R.id.btn_cerrar_sesion);
        /*btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AuthUI.getInstance().signOut(getActivity()) .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override public void onComplete(Task<Void> task) {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i); getActivity() .finish();
                    }
                });
            }
        });*/
        RequestQueue colaPeticiones = Volley.newRequestQueue(getActivity().getApplicationContext());
        ImageLoader lectorImagenes = new ImageLoader(colaPeticiones, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) { cache.put(url, bitmap); }
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
        });
        Uri urlImagen = usuario.getPhotoUrl();
        if (urlImagen != null) {
                NetworkImageView fotoUsuario = vista.findViewById(R.id.imagen);
            fotoUsuario.setImageUrl(urlImagen.toString(), lectorImagenes);
        } return vista;
    }
}