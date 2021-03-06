package com.example.faraday.popularmovieapp.Activities;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.faraday.popularmovieapp.Fragments.DetailsActivityFragment;
import com.example.faraday.popularmovieapp.R;
import com.example.faraday.popularmovieapp.SettingsActivity;

//import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    final static private String TAG = MainActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Picasso.Builder builder = new Picasso.Builder(this);
//        Picasso built = builder.build();
//        built.setLoggingEnabled(true);
//
//        Picasso.setSingletonInstance(built);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.details_fragment_container) != null) {
            if (savedInstanceState == null) {
                Log.e(TAG, "onCreate: main container is here");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.details_fragment_container, new DetailsActivityFragment(), "DTAG")
                .commit();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
