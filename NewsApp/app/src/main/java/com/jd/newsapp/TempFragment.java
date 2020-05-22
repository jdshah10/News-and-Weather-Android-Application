//package com.jd.newsapp;
//
////import android.Manifest;
////import android.content.Context;
////import android.content.pm.PackageManager;
////import android.location.Address;
////import android.location.Geocoder;
////import android.location.Location;
////import android.location.LocationManager;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//
////import androidx.core.app.ActivityCompat;
////import androidx.core.content.ContextCompat;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.Fragment;
//
////import android.util.Log;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Switch;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonParser;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Text;
////import android.widget.Toast;
////
////import com.android.volley.Request;
////import com.android.volley.RequestQueue;
////import com.android.volley.Response;
////import com.android.volley.VolleyError;
////import com.android.volley.toolbox.JsonObjectRequest;
////import com.android.volley.toolbox.StringRequest;
////import com.android.volley.toolbox.Volley;
////
////import org.json.JSONArray;
////import org.json.JSONObject;
////
////import java.util.List;
////import java.util.Locale;
////import java.util.concurrent.ExecutionException;
//
//
//public class TempFragment extends Fragment {
//
//    // TODO: Rename and change types of parameters
//
//    private String weatherType;
//    private String tempMeasure;
//    public TempFragment() {
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        Log.i("fagment", "called here for temperature");
////        getLocationPermission();
//
//    }
//
////    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
////    boolean isGPSEnabled = false;
////    boolean canGetLocation = false;
////    Location location;
////    double latitude;
////    double longitude;
////    protected LocationManager locationManager;
////    String cityName, stateName;
////    private static String initial_url = "https://api.openweathermap.org/data/2.5/weather?q=";
////    private static String end_url = "&units=metric&appid=45f574bafa34291f8f493df0cc2adcf4";
//
////    public void getLocationPermission() {
////        if (ContextCompat.checkSelfPermission(getContext(),
////                Manifest.permission.ACCESS_FINE_LOCATION)
////                != PackageManager.PERMISSION_GRANTED) {
////
////            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
////                    Manifest.permission.ACCESS_FINE_LOCATION)) {
////                Log.i("Location not permitted","location not found");
////            }
////            else {
////
////                ActivityCompat.requestPermissions(getActivity(),
////                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
////                        MY_PERMISSIONS_REQUEST_LOCATION);
////            }
////        } else {
////            Log.i("permission ", "called");
////            try {
////
////                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
////                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
////                if(!isGPSEnabled){
////                    latitude=34.027389;
////                    longitude=-118.289899;
////                }
////                else{
////                    if(locationManager!=null){
////                        location = locationManager
////                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
////                        if(location != null){
////                            latitude = location.getLatitude();
////                            longitude = location.getLongitude();
////                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
////                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
////                            if(addresses.size() > 0){
////
////                            }
////                            Log.v("log_tag", "latitude : " + latitude);
////                            Log.v("log_tag", "longitude : " + longitude);
//////                            Log.v("log_tag", "addresses+)_+++ : " + addresses.get(0).getLocality());
////                            cityName = addresses.get(0).getLocality();
////                            canGetLocation = true;
////                            temperatureAPICall();
////                        }
////                    }
////                }
////            }
////            catch (Exception exception){
////                exception.printStackTrace();
////            }
////        }
////    }
////
////    public void temperatureAPICall(){
////        Log.i("in api call ", "function");
////        String url = initial_url + cityName + end_url;
////        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
////            @Override
////            public void onResponse(String response) {
////                Log.d("JSON object", response);
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
////            }
////        });
////
////        RequestQueue rqueue = Volley.newRequestQueue(getContext());
////        rqueue.add(stringRequest);
////    }
//
//    //called automatically based on whether user has given location permission or not.
////    @Override
////    public void onRequestPermissionsResult(int requestCode,
////                                           String permissions[], int[] grantResults) {
////        Log.i("cheking results here : ", String.valueOf(grantResults));
////        switch (requestCode) {
////            case MY_PERMISSIONS_REQUEST_LOCATION: {
////                // If request is cancelled, the result arrays are empty.
////
////                if (grantResults.length > 0
////                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////
////                    // permission was granted, yay! Do the
////                    // location-related task you need to do.
////                    if (ContextCompat.checkSelfPermission(getContext(),
////                            Manifest.permission.ACCESS_FINE_LOCATION)
////                            == PackageManager.PERMISSION_GRANTED) {
////                    }
////
////                } else {
////
////                    // permission denied, boo! Disable the
////                    // functionality that depends on this permission.
////
////                }
////                return;
////            }
////
////        }
////    }
////
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        Bundle bundle = getArguments();
//        //Log.i("json daya", bundle.getString("jsondata"));
//        String Apidata = bundle.getString("jsondata");
//        //Log.d("myapi daya : ", bundle.getString("jsondata"));
//        try {
//
//            JSONObject jsonObject = new JSONObject(Apidata);
//            //get weather type data
//            JSONArray weather = jsonObject.getJSONArray("weather");
//            JSONObject data = (JSONObject)weather.get(0);
//            weatherType = data.getString("main");
//            //Log.d("weather type : ", weatherType);
//            //get temperature measure
//            JSONObject tempData = (JSONObject)jsonObject.get("main");
//            tempMeasure = tempData.getString("temp");
//            //Log.d("weather type : ", tempMeasure);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_temp, container, false);
//        TextView cityname = (TextView) view.findViewById(R.id.cityname);
//        TextView statename = (TextView) view.findViewById(R.id.statename);
//        TextView tempnumber = (TextView) view.findViewById(R.id.tempnumber);
//        TextView atmosphere = (TextView) view.findViewById(R.id.atmosphere);
//        LinearLayout weather_image = (LinearLayout) view.findViewById(R.id.weather_image);
//
//        cityname.setText(bundle.getString("city"));
//        statename.setText(bundle.getString("state"));
//        tempnumber.setText(Integer.toString(Math.round(Float.valueOf(tempMeasure))));
//        atmosphere.setText(weatherType);
//        String x = weatherType;
//        //Log.d("wea : ", x);
//        switch (x){
//            case "Clouds":
//                weather_image.setBackgroundResource(R.drawable.cloudy_weather);
//                break;
//            case "Clear":
//                weather_image.setBackgroundResource(R.drawable.clear_weather);
//                break;
//            case "Snow":
//                weather_image.setBackgroundResource(R.drawable.snowy_weather);
//                break;
//            case "Rain":
//                weather_image.setBackgroundResource(R.drawable.rainy_weather);
//                break;
//            case "Drizzle":
//                weather_image.setBackgroundResource(R.drawable.rainy_weather);
//                break;
//            case "Thunderstorm":
//                weather_image.setBackgroundResource(R.drawable.thunder_weather);
//                break;
//            default:
//                //Log.d("weather type : ", weatherType);
//                weather_image.setBackgroundResource(R.drawable.sunny_weather);
//                break;
//        }
//        //weather_image.setBackgroundResource(R.drawable.clear_weather);
//        //weather_image.setImageDrawable(R.drawable.clear_weather);
//
//        return view;
//    }
//}
