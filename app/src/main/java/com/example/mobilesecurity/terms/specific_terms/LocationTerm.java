package com.example.mobilesecurity.terms.specific_terms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mobilesecurity.terms.TermLogin;
import com.example.mobilesecurity.terms.TermStatus;
import com.example.mobilesecurity.terms.TermsListenerInput;
import com.example.mobilesecurity.tools.AddressMaps;
import com.example.mobilesecurity.tools.Permissions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class LocationTerm extends TermLogin {
    private double radius = 0.05;
    private final String COUNTRY = "israel";
    private String city;
    private String street;
    private String number;

    public LocationTerm() {
        super();
        permmisionName = Manifest.permission.ACCESS_FINE_LOCATION;
        weight = 4;
    }

    @Override
    protected TermStatus isTermOK(TermsListenerInput listenerInput) {
        AppCompatActivity appCompatActivity = Permissions.getPermissions().getAppCompatActivity();
        LocationManager lm = (LocationManager) appCompatActivity.getSystemService(appCompatActivity.LOCATION_SERVICE);
        try {

            if (ActivityCompat.checkSelfPermission(appCompatActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appCompatActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                AddressMaps addressMaps = new AddressMaps(city,street,number);
                LatLng latLng = getLocationFromAddress(addressMaps.toString());
                Log.d("mylog","radious"+radius);
                if(latLng == null)
                    return new TermStatus(TermStatus.eStatus.WRONG,"");
                double distance = Math.sqrt((Math.pow((latitude - latLng.latitude),2) + Math.pow((longitude - latLng.longitude),2)));
                Log.d("mylog","distance"+distance);
                if(distance <= radius){
                    return new TermStatus(TermStatus.eStatus.OK,"");
                }
            }

        }catch (Exception e){}

        return new TermStatus(TermStatus.eStatus.WRONG,"");
    }

    @Override
    protected TermStatus isTermInputOK(TermsListenerInput listenerInput, boolean getFromListener) {
        if (getFromListener) {
            String[] location = listenerInput.getLocation();
            city = location[0];
            street = location[1];
            number = location[2];
            AddressMaps addressMaps = new AddressMaps(city,street,number);
            LatLng latLng = getLocationFromAddress(addressMaps.toString());
            if(latLng == null)
                return new TermStatus(TermStatus.eStatus.WRONG,"Address not found");

        }
        return new TermStatus(TermStatus.eStatus.OK,"");
    }

    @Override
    public void getValuesAfterPermissionOk() {

    }

    @Override
    public boolean isNeedPermission() {
        return true;
    }

    @Override
    public void requestPermission() {
        Permissions.getPermissions().requestLocation();
    }

    public double getRadius() {
        return radius;
    }

    public LocationTerm setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public String getCity() {
        return city;
    }

    public LocationTerm setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public LocationTerm setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public LocationTerm setNumber(String number) {
        this.number = number;
        return this;
    }
    @Override
    public String toString() {
        return "Radius Term";
    }
    public LatLng getLocationFromAddress(String strAddress) {
        AppCompatActivity appCompatActivity = Permissions.getPermissions().getAppCompatActivity();
        Geocoder coder = new Geocoder(appCompatActivity);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }
}
