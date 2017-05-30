package com.epicodus.myrestaurants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {
    String msg = "Android cycle: ";

    @Bind(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @Bind(R.id.locationEditText) EditText mLocationEditText;
    @Bind(R.id.appNameTextView) TextView mAppNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(msg, "The onCreate() event");

        ButterKnife.bind(this);

        Typeface sushiFont = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf");
        mAppNameTextView.setTypeface(sushiFont);

        //BUTTON
        mFindRestaurantsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                // Toast.makeText(MainActivity.this, "You Clicked Me!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent (MainActivity.this, RestaurantListActivity.class);
                String location = mLocationEditText.getText().toString();
                intent.putExtra("location",location);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event. About to visible");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "Resume, Visible now");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "Pause, another activity is becoming focused");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event, when activity is not visible");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "Before activity is destroyed");
    }

}
