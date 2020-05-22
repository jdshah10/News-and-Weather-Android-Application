package com.jd.newsapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String weatherType;
    private String tempMeasure;
    String data;
    RecyclerView recyclerView;
    CardRecyclerAdapter cradapter;

    List<OurData> NewsList = new ArrayList<OurData>();;
    private Context mContext;
    String title, time, source, link, articleid, imagelink,lateTime, fulldate;

    private SwipeRefreshLayout swipeRefreshLayout;


    String latestNewsUrl = "https://androidnewsappbackend.wl.r.appspot.com/homelatestnews";
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = getArguments();

        //Log.i("json daya", bundle.getString("jsondata"));
        String Apidata = bundle.getString("jsondata");
        //Log.d("myapi daya : ", bundle.getString("jsondata"));
        try {

            JSONObject jsonObject = new JSONObject(Apidata);
            //get weather type data
            JSONArray weather = jsonObject.getJSONArray("weather");
            JSONObject data = (JSONObject)weather.get(0);
            weatherType = data.getString("main");
            //Log.d("weather type : ", weatherType);
            //get temperature measure
            JSONObject tempData = (JSONObject)jsonObject.get("main");
            tempMeasure = tempData.getString("temp");
            //Log.d("weather type : ", tempMeasure);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        TextView cityname = (TextView) view.findViewById(R.id.cityname);
        TextView statename = (TextView) view.findViewById(R.id.statename);
        TextView tempnumber = (TextView) view.findViewById(R.id.tempnumber);
        TextView atmosphere = (TextView) view.findViewById(R.id.atmosphere);
        LinearLayout weather_image = (LinearLayout) view.findViewById(R.id.weather_image);

        cityname.setText(bundle.getString("city"));
        statename.setText(bundle.getString("state"));
        tempnumber.setText(Integer.toString(Math.round(Float.valueOf(tempMeasure))));
        atmosphere.setText(weatherType);
        String x = weatherType;
        //Log.d("weather rigth now : ", x);
        switch (x){
            case "Clouds":
                weather_image.setBackgroundResource(R.drawable.cloudy_weather);
                break;
            case "Clear":
                weather_image.setBackgroundResource(R.drawable.clear_weather);
                break;
            case "Snow":
                weather_image.setBackgroundResource(R.drawable.snowy_weather);
                break;
            case "Rain":
                weather_image.setBackgroundResource(R.drawable.rainy_weather);
                break;
            case "Drizzle":
                weather_image.setBackgroundResource(R.drawable.rainy_weather);
                break;
            case "Thunderstorm":
                weather_image.setBackgroundResource(R.drawable.thunder_weather);
                break;
            default:
                //Log.d("weather type : ", weatherType);
                weather_image.setBackgroundResource(R.drawable.sunny_weather);
                break;
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.cardrecyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.card_swiperefresh);
        cradapter = new CardRecyclerAdapter(NewsList, getActivity());
       // Log.d("api home ", "again");
        recyclerView.setAdapter(cradapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Log.d("is swipe ", "called");
                //NewsList = new ArrayList<OurData>();
                getCards(view);
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });
        getCards(view);

        return view;
    }

    private void getCards(View view) {
        //data = null;
        final ProgressBar progressBaR = (ProgressBar) view.findViewById(R.id.card_progressbar);
        final TextView progresstext = (TextView) view.findViewById(R.id.card_progressbartext);
        final StringRequest stringRequest = new StringRequest(latestNewsUrl,new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                data = response;
                try{
                    NewsList.clear();
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray cardArray = jsonObject.getJSONArray("cards");
                    for (int i = 0 ; i < cardArray.length() ; i++){
                        JSONObject cardObj = (JSONObject) cardArray.get(i);
                        articleid = cardObj.getString("id");
                        title = cardObj.getString("title");

                        fulldate = cardObj.getString("date");
                        LocalDateTime ldt = LocalDateTime.now();
                        ZoneId zoneId = ZoneId.of( "America/Los_Angeles" );
                        ZonedDateTime zdtAmericaLA = ldt.atZone( zoneId );

                        ZonedDateTime zonedDateTime = ZonedDateTime.parse(fulldate);
                        ZonedDateTime zdtAmericalLA1 = zonedDateTime
                                .withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ) );
                        //Log.d("date check", fulldate);
                        long years = ChronoUnit.YEARS.between(zdtAmericalLA1, zdtAmericaLA);
                        long months = ChronoUnit.MONTHS.between(zdtAmericalLA1, zdtAmericaLA);
                        //long weeks = ChronoUnit.WEEKS.between(zdtAmericalLA1, zdtAmericaLA);
                        long days = ChronoUnit.DAYS.between(zdtAmericalLA1, zdtAmericaLA);
                        long hours = ChronoUnit.HOURS.between(zdtAmericalLA1, zdtAmericaLA);
                        long minutes = ChronoUnit.MINUTES.between(zdtAmericalLA1, zdtAmericaLA);
                        long seconds = ChronoUnit.SECONDS.between(zdtAmericalLA1, zdtAmericaLA);

                        if(years != 0){
                            time = Long.toString(years) + "y ago";
                        }
                        else{
                            if(months != 0){
                                time = Long.toString(months) + "M ago";
                            }
                            else{
                                if(days != 0){
                                    time = Long.toString(days) + "d ago";
                                }
                                else{
                                    if(hours != 0){
                                        time = Long.toString(hours) + "h ago";
                                    }
                                    else{
                                        if(minutes != 0){
                                            time = Long.toString(minutes) + "m ago";
                                        }
                                        else{
                                            if(seconds != 0){
                                                time = Long.toString(seconds) + "s ago";
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //Log.d("final difference",time);

                        source = cardObj.getString("source");
                        link = cardObj.getString("url");
                        imagelink = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                        if(cardObj.has("imagecheck")){
                            if(cardObj.getString("imagecheck") != ""){
                                imagelink = cardObj.getString("imagecheck");
                            }
                        }

                        NewsList.add(
                                new OurData(
                                        articleid,
                                        title,
                                        source,
                                        link,
                                        time,
                                        imagelink, fulldate));


                    }
                    if(NewsList.size() != 0){
                        progressBaR.setVisibility(View.GONE);
                        progresstext.setText("");
                        //CardRecyclerAdapter cradapter = new CardRecyclerAdapter(NewsList, getActivity());
                        //Log.d("api home ", "again");
                        recyclerView.setAdapter(cradapter);

                        cradapter.notifyDataSetChanged();
                    }


                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        final RequestQueue rqueue = Volley.newRequestQueue(getActivity());
        rqueue.add(stringRequest);

    }
    @Override
    public void onResume() {
        super.onResume();
        if(cradapter!=null){

            recyclerView.setAdapter(cradapter);
            cradapter.notifyDataSetChanged();
        }
    }


}


//
//NewsList.add(
//        new OurData(
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles",
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles jvajdcv cajhvcja ja cja  avcac a scja",
//        "Anustralia News",
//        "link in the ackak c kas cak ak kaa ca ck",
//        "10m ago",
//        "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png"));
//        NewsList.add(
//        new OurData(
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles",
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles jvajdcv cajhvcja ja cja  avcac a scja",
//        "Anustralia News",
//        "link in the ackak c kas cak ak kaa ca ck",
//        "10m ago",
//        "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png"));
//        NewsList.add(
//        new OurData(
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles",
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles jvajdcv cajhvcja ja cja  avcac a scja",
//        "Anustralia News",
//        "link in the ackak c kas cak ak kaa ca ck",
//        "10m ago",
//        "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png"));
//        NewsList.add(
//        new OurData(
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles",
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles jvajdcv cajhvcja ja cja  avcac a scja",
//        "Anustralia News",
//        "link in the ackak c kas cak ak kaa ca ck",
//        "10m ago",
//        "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png"));
//        NewsList.add(
//        new OurData(
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles",
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles jvajdcv cajhvcja ja cja  avcac a scja",
//        "Anustralia News",
//        "link in the ackak c kas cak ak kaa ca ck",
//        "10m ago",
//        "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png"));
//        NewsList.add(
//        new OurData(
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles",
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles jvajdcv cajhvcja ja cja  avcac a scja",
//        "Anustralia News",
//        "link in the ackak c kas cak ak kaa ca ck",
//        "10m ago",
//        "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png"));
//        NewsList.add(
//        new OurData(
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles",
//        "Hello, Jainam I hope you are doing well. How it's in Los Angeles jvajdcv cajhvcja ja cja  avcac a scja",
//        "Anustralia News",
//        "link in the ackak c kas cak ak kaa ca ck",
//        "10m ago",
//        "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png"));