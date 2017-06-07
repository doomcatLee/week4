package com.epicodus.myrestaurants.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.epicodus.myrestaurants.Constants;
import com.epicodus.myrestaurants.R;
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

public class RestaurantListActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private String mRecentAddress;
    private SharedPreferences.Editor mEditor;

    @Bind(R.id.locationTextView) TextView mLocationTextView;
    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    public static final String TAG = RestaurantListActivity.class.getSimpleName();
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        ButterKnife.bind(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);


        if (mRecentAddress != null) {
            getRestaurants(mRecentAddress);
        }
//        Intent intent = getIntent();
//        String location = intent.getStringExtra("location");
//        getRestaurants(location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query);
                getRestaurants(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
            RestaurantListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                     mAdapter = new RestaurantListAdapter(RestaurantListActivity
                            .this, mRestaurants);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
                    alphaAdapter.setDuration(1000);

                    mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(alphaAdapter));
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager
                            (RestaurantListActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);

                }
            });
        }
        });
    }
    private void addToSharedPreferences(String location) {
        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
    }
}
