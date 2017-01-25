package cz.muni.fi.pv256.movio2.fk410022.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cz.muni.fi.pv256.movio2.fk410022.R;

public abstract class MovioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
    }
}
