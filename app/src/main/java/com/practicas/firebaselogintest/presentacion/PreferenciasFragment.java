package com.practicas.firebaselogintest.presentacion;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.practicas.firebaselogintest.R;

public class PreferenciasFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
