package com.jd.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity  {

    Bundle bundle = new Bundle();
    int id;

//    private BottomNavigationView homeNav;
//    private FrameLayout homeFrame;
//    private TempFragment tempFragment;
    private String provider;
    private Handler handler;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    List<String> stringList = new ArrayList<>();
    SearchView.SearchAutoComplete searchAutoComplete;
    //private AutoSuggestAdapter autoSuggestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toptoolbar);
        setSupportActionBar(toolbar);
        getLocationPermission();

        //Log.i("fagment", "called here for temperature");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                id = item.getItemId();
                if(id == R.id.bottomNavigationHomeMenuId){
                    getLocationPermission();
                    //manageCardFragment();
                    return true;
                }
                else if(id == R.id.bottomNavigationHeadlinesMenuId) {
                    //getSupportFragmentManager().beginTransaction().replace(R.id.headlines_fragment);
                    getheadlinesFragment();
                    return true;
                }
                else if(id == R.id.bottomNavigationTrendingMenuId) {
                    getTrendingFragment();
                    return true;
                }
                else if(id == R.id.bottomNavigationBookmarkshMenuId) {
                    getBookmarksFragment();
                    return true;
                }

                return true;
            }
        });


    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    boolean isGPSEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    protected LocationManager locationManager;
    String cityName, stateName;
    private static String initial_url = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static String end_url = "&units=metric&appid=45f574bafa34291f8f493df0cc2adcf4";
    //Context mContext;

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                //Log.i("Location not permitted","location not found");
//            }
//            else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            //}
        } else {
            //Log.i("permission ", "called");
            try {

                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(!isGPSEnabled){
                    latitude=34.027389;
                    longitude=-118.289899;
                }
                else{
                    if(locationManager!=null){
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if(addresses.size() > 0){

                            }
                            cityName = addresses.get(0).getLocality();
                            stateName = addresses.get(0).getAdminArea();
                            bundle.putString("city", cityName);
                            bundle.putString("state", stateName);
                            canGetLocation = true;
                            temperatureAPICall();
                        }
                    }
                }
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }

    public void temperatureAPICall(){
        //Log.i("in api call ", "function");
        String url = initial_url + cityName + end_url;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("JSON object : ", response);
                bundle.putString("jsondata", response);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragmenttemp, homeFragment);
                fragmentTransaction.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rqueue = Volley.newRequestQueue(this);
        rqueue.add(stringRequest);
    }

    public void getheadlinesFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HeadlinesFragment headlinesFragment = new HeadlinesFragment();
        fragmentTransaction.replace(R.id.fragmenttemp, headlinesFragment);
        fragmentTransaction.commit();
    }

    public void getTrendingFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        TrendingFragment trendingFragment = new TrendingFragment();
        fragmentTransaction.replace(R.id.fragmenttemp, trendingFragment);
        fragmentTransaction.commit();
    }

    public void getBookmarksFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BookmarkFragment bookmarkFragment = new BookmarkFragment();
        fragmentTransaction.replace(R.id.fragmenttemp, bookmarkFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_icon, menu);
        MenuItem searchMenu = menu.findItem(R.id.topsearch_icon);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);


        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String queryString=(String)parent.getItemAtPosition(position);
                searchAutoComplete.setText("" + queryString);
                //Toast.makeText(HomeActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
            }
        });
//        searchAutoComplete.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { }
//
//            @Override
//            public void afterTextChanged(Editable s) { }
//        });
//
//        handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                if (msg.what == TRIGGER_AUTO_COMPLETE) {
//                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
//                        cogFunc(searchAutoComplete.getText().toString());
//                    }
//                }
//                return false;
//            }
//        });
        final Context mContext = this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("response : ", stringList.toString());
                Intent intent = new Intent(HomeActivity.this, SearchResultsActivity.class);

                intent.putExtra("query",query);
                startActivity(intent);
//                AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
//                alertDialog.setMessage("Search keyword is " + query);
//                alertDialog.show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() >= 2){
                    cogFunc(newText, mContext);
                }
                return true;
            }
        });


//        searchAutoComplete.setBackgroundColor(Color.BLUE);
//        searchAutoComplete.setTextColor(Color.GREEN);
//        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);

        return super.onCreateOptionsMenu(menu);
    }

    private void cogFunc(String toString, final Context mContext) {
        //Log.d("entered", "cogFunc");
        stringList = new ArrayList<>();
        String url = "https://jainam-shah.cognitiveservices.azure.com/bing/v7.0/suggestions?q="+toString;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray suggestionsArray = response.getJSONArray("suggestionGroups");
                    JSONObject suggest = suggestionsArray.getJSONObject(0);
                    JSONArray searchSuggestions = suggest.getJSONArray("searchSuggestions");

                    if(searchSuggestions.length() >= 5){
                        for(int i = 0 ; i < 5 ; i++){
                            JSONObject jsonPair = searchSuggestions.getJSONObject(i);
                            String displayText = jsonPair.getString("displayText");
                            stringList.add(displayText);
                            //Log.d("response : ", displayText);
                        }
                    }
                    else {
                        for(int i = 0 ; i < searchSuggestions.length() ; i++){
                            JSONObject jsonPair = searchSuggestions.getJSONObject(i);
                            String displayText = jsonPair.getString("displayText");
                            stringList.add(displayText);
                            //Log.d("response : ", jsonPair.toString());
                        }

                    }
                    //Log.d("response : ", stringList.toString());
                    ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, stringList);
                    //AbsListView searchAutoComplete;
                    searchAutoComplete.setAdapter(newsAdapter);




                } catch (JSONException jsonexc){
                    jsonexc.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","error => "+error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Ocp-Apim-Subscription-Key", "6263c587b09e47ea9a944c8bad817531");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        if(id == R.id.bottomNavigationBookmarkshMenuId){
//            getBookmarksFragment();
//        }
//    }
}
//45f574bafa34291f8f493df0cc2adcf4