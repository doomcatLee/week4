package com.epicodus.myrestaurants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.epicodus.myrestaurants.adapters.RestaurantListAdapter;
import com.epicodus.myrestaurants.models.Restaurant;
import com.epicodus.myrestaurants.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsActivity extends Activity {
    @Bind(R.id.locationTextView) TextView mLocationTextView;
    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    public static final String TAG = RestaurantsActivity.class.getSimpleName();
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        getRestaurants(location);
    }

    private void getRestaurants (String location){
        final YelpService yelpService = new YelpService();
        yelpService.findRestaurants(location, new Callback() {
        @Override
            public void onFailure(Call call, IOException e){
            e.printStackTrace();
        }
        @Override
            public void onResponse (Call call, Response response) {
            mRestaurants = yelpService.processResults(response);
            RestaurantsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                     mAdapter = new RestaurantListAdapter(RestaurantsActivity
                            .this, mRestaurants);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
                    alphaAdapter.setDuration(3000);

                    mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(alphaAdapter));
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager
                            (RestaurantsActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);

                }
            });
        }
        });
    }
}
