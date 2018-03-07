package com.evmcstudios.cryptotracker.MainPages;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.evmcstudios.cryptotracker.R;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Ultranova on 2/18/2018.
 */

public class CTAbout extends AppCompatActivity {

    private  FirebaseAnalytics FirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_ctabout);


        FirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseAnalytics.setCurrentScreen(this, getString(R.string.app_about), null );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_watch);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.setTitle(R.string.app_about);


    }
}
