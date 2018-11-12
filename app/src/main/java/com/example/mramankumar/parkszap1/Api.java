package com.example.mramankumar.parkszap1;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface Api {

    String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/";


   @GET("json")
  // Void getMyThing(@QueryMap Map<String, String> params,  Callback<String> callback);
  // void getPositionByZip(String latlng, @Query("latlan") String address, @Query("location_type") String loc, @Query("key") Callback<String> cb);

  //  @GET("/api/unknown")
  // Call<Address> doGetListResources(@QueryMap Map<String, String> params,  Callback<Address> tag);
    Call<Address> groupList(@Query("latlng") String latlng, @Query("location_type") String postcode, @Query("key") String key);
  //  Call<Address> doGetListResources();

   /* class Factory {
        private static Api service;

        public static Api getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(Api.class);
                return service;
            } else {
                return service;
            }
        }
    } */
}