package com.example.mramankumar.parkszap1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    ImageView usrimageView;
    TextView Username,Email,usergender,birthday,userage,addrress;
    Button loggout;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loggout = findViewById(R.id.loogout);
        usrimageView = findViewById(R.id.imageView2);
        Username = findViewById(R.id.nameview);
        Email = findViewById(R.id.emailview);
        usergender = findViewById(R.id.genderview);
        birthday = findViewById(R.id.birthview);
        userage = findViewById(R.id.ageview);
        addrress = findViewById(R.id.addressview);
        mContext = this;
          loggout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  LoginManager.getInstance().logOut();
                  Intent i=new Intent(Home.this,MainActivity.class);
                  startActivity(i);
              }
          });
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            GPSTracker gps = new GPSTracker(mContext, Home.this);

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                getUserProfile(AccessToken.getCurrentAccessToken());
                gps = new GPSTracker(mContext, Home.this);
                MainActivity info = new MainActivity();
                info.getUserProfile(AccessToken.getCurrentAccessToken());
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/geocode/").addConverterFactory(GsonConverterFactory.create());

                Retrofit retrofit=builder.build();
                Api service = retrofit.create(Api.class);

                double latitude = gps.getLatitude();


                double longitude = gps.getLongitude();
                String lat = String.valueOf(latitude);
                String lon = String.valueOf(longitude);




                String  location_type= "ROOFTOP";
                String latlng= lat+","+lon;
                String key="*******************";
                
                Call<Address> call = service.groupList(latlng,location_type,key);
                call.enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {




                        String displayResponse = "";

                        Address resource = response.body();

                        List<Address.Datum> datumList = resource.results;



                        for (Address.Datum datum : datumList) {
                            displayResponse = datum.formatted_address;
                        }

                        addrress.setText(displayResponse);

                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();

                        call.cancel();
                    }
                });

            }

            else{
                Toast.makeText(mContext, "You need to grant location permission", Toast.LENGTH_SHORT).show();
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();



            }





        }
    }


    @Override
    protected void onStart() {
        super.onStart();

       // getUserProfile(AccessToken.getCurrentAccessToken());
    }

    public void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            //  String birthday = object.getString("user_birthday");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            Username.setText(first_name + " " + last_name);
                            Email.setText(email);
                            Picasso.get().load(image_url).into(usrimageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }
}

