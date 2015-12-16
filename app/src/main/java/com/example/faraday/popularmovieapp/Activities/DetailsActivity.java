package com.example.faraday.popularmovieapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.faraday.popularmovieapp.Fragments.DetailsActivityFragment;
import com.example.faraday.popularmovieapp.R;

public class DetailsActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getCanonicalName();
    private DetailsActivityFragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            Log.e(TAG, "onCreate: Saved");
            mContent = (DetailsActivityFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        return false;
//    }

}
