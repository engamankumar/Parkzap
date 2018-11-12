package com.example.mramankumar.parkszap1;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Address {

    private String  address_components;
    private String   formatted_address;
    private String  place_id;
    public List<Datum> results = null;
   // List<Address> response;
   public class Datum {

      //// @SerializedName("address_components")
     //  public String address_components;
       @SerializedName("formatted_address")
       public String formatted_address;


   }
    public Address(String address_components, String formatted_address, String place_id) {
        this.address_components = address_components;
        this.formatted_address = formatted_address;
        this.place_id = place_id;


    }



    public String getAddress_components() {
        return address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public String getPlace_id() {
        return place_id;
    }
}
