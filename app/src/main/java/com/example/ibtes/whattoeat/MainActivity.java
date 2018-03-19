package com.example.ibtes.whattoeat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Coordinates;
import com.yelp.fusion.client.models.SearchResponse;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private TextView displayText;

    private RadioButton foodType = null;
    private RadioButton price = null;

    private FusedLocationProviderClient mFusedLocationClient;

    private Location userLocation;

    private YelpFusionApi yelpFusionApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayText = findViewById(R.id.displayText);

        getLocation();

        String yelpApiKey = getString(R.string.yelpApiKey);

        try {
            yelpFusionApi =  new YelpFusionApiFactory().createAPI(yelpApiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getLocation(){

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Checks if the permission is granted
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            getLocation();
            return;

        }
        else{ //If the permission is granted

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                TextView locationName = findViewById(R.id.locationText);

                                locationName.setText("We gucci");

                                userLocation = location;
                            }
                        }
                    });
        }

    }

    public String yelpSearch(String term){

        String reply = null;

        Map<String, String> params =  new HashMap<>();

        params.put("term", term);
        params.put("latitude", userLocation.getLatitude() + "");
        params.put("longitude", userLocation.getLongitude() + "");

        Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                SearchResponse searchResponse = response.body();

                Business result = searchResponse.getBusinesses().get(0);

                reply = result.getName();.


            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

            }
        };

        call.enqueue(callback);

        return reply;

    }

    public void onFoodTypeClick(View view){

        foodType = (RadioButton) view;

    }

    public void onPriceClick(View view){

        price = (RadioButton) view;

    }

    public void search(View view){

        if(foodType == null || price == null){
            Toast.makeText(this, "Please select a price and type", Toast.LENGTH_SHORT).show();
            return;
        }

        //Conditional Logic to find food

        String location = yelpSearch(foodType.getText().toString());

        if(location == null){
            Toast.makeText(this, "Search Failed", Toast.LENGTH_SHORT).show();
        }
        else{
            displayText.setText(location);
        }

        foodType.setChecked(false);
        price.setChecked(false);

        foodType = null;
        price = null;

    }

}
