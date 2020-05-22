package com.jd.newsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    String keyword, data;
    RecyclerView recyclerView;
    CardRecyclerAdapter cradapter;
    String title, time, source, link, articleid, imagelink,lateTime, fulldate;
    Context mContext;
    List<OurData> NewsList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toptoolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().hasExtra("query" )){
            keyword = getIntent().getStringExtra("query");
        }
        swipeRefreshLayout = findViewById(R.id.card_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsList = new ArrayList<OurData>();
                getCard(keyword);
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        getCard(keyword);
        getSupportActionBar().setTitle("Search Results for "+keyword);
    }

    private void getCard(String keyword) {
        NewsList = new ArrayList<OurData>();
        //Log.d("keyword : ",keyword);

        recyclerView = (RecyclerView) findViewById(R.id.search_results);
        cradapter = new CardRecyclerAdapter(NewsList, mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //data=null;
        final ProgressBar progressBaR = findViewById(R.id.card_progressbar);
        final TextView progresstext = findViewById(R.id.card_progressbartext);
        String url = "https://androidnewsappbackend.wl.r.appspot.com/search?queryword="+keyword;
        //Log.d("url ", url);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                try {
                    data = response;
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray cardArray = jsonObject.getJSONArray("cards");
                    //Log.d("cards search : ", cardArray.toString());
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
                        //Log.d("api home ", "again");
                        if(NewsList.size() !=0){
                            progressBaR.setVisibility(View.GONE);
                            progresstext.setText("");
                            cradapter = new CardRecyclerAdapter(NewsList, mContext);
                            Log.d("api home ", "again");
                            recyclerView.setAdapter(cradapter);

                            cradapter.notifyDataSetChanged();
                        }
//                        recyclerView.setAdapter(cradapter);
//
//                        cradapter.notifyDataSetChanged();


                    }

                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue rqueue = Volley.newRequestQueue(this);
        rqueue.add(stringRequest);
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(cradapter !=null){
            cradapter.notifyDataSetChanged();
        }
    }
}
