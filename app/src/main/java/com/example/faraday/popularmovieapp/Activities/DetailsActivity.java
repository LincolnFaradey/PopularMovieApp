package com.example.faraday.popularmovieapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.example.faraday.popularmovieapp.Fragments.DetailsActivityFragment;
import com.example.faraday.popularmovieapp.R;

public class DetailsActivity extends AppCompatActivity {
    final static private String TAG = DetailsActivity.class.getCanonicalName();
    private DetailsActivityFragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        if (savedInstanceState != null || b != null) {
            //Restore the fragment's instance
//            mContent = (DetailsActivityFragment) getSupportFragmentManager().getFragment(
//                    savedInstanceState, "mContent");
//            Log.e(TAG, "onCreate: it's there");®
        }else {
            DetailsActivityFragment df = (DetailsActivityFragment)getSupportFragmentManager().findFragmentByTag("DTAG");
            if (df == null) {
                Log.e(TAG, "onCreate: df is null");
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.details_fragment, new DetailsActivityFragment())
                        .commit();
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
