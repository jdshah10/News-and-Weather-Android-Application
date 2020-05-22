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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class TechnologyHeadlinesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    String data;
    RecyclerView recyclerView;
    CardRecyclerAdapter cradapter;
    List<OurData> NewsList;
    private Context mContext;
    String title, time, source, link, articleid, imagelink,lateTime, fulldate;
    private SwipeRefreshLayout swipeRefreshLayout;

    String latestNewsUrl = "https://androidnewsappbackend.wl.r.appspot.com/headlinessectioncard?newsSection=technology";

    public TechnologyHeadlinesFragment() {
        // Required empty public constructor
    }

    public static TechnologyHeadlinesFragment newInstance(String param1, String param2) {
        TechnologyHeadlinesFragment fragment = new TechnologyHeadlinesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_technology_headlines, container, false);


        NewsList = new ArrayList<OurData>();

        recyclerView = (RecyclerView) view.findViewById(R.id.technologycardrecyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.card_swiperefresh);
        cradapter = new CardRecyclerAdapter(NewsList, getActivity());
        recyclerView.setAdapter(cradapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsList = new ArrayList<OurData>();
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
        data = null;
        final ProgressBar progressBaR = (ProgressBar) view.findViewById(R.id.card_progressbar);
        final TextView progresstext = (TextView) view.findViewById(R.id.card_progressbartext);
        StringRequest stringRequest = new StringRequest(latestNewsUrl,new Response.Listener<String>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                data = response;
                try{
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
                    if(data!= null){
                        progressBaR.setVisibility(View.GONE);
                        progresstext.setText("");
                        cradapter = new CardRecyclerAdapter(NewsList, getActivity());
                        Log.d("api home ", "again");
                        recyclerView.setAdapter(cradapter);

                        cradapter.notifyDataSetChanged();
                    }

//                    Log.d("latest news data : ", articleid);
//                    Log.d("latest news data : ", title);
//                    Log.d("latest news data : ", time);
//                    Log.d("latest news data : ", source);
//                    Log.d("latest news data : ", link);
//                    Log.d("latest news data : ", imagelink);
                    //Log.d("latest news data : ", NewsList.toString());

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
        RequestQueue rqueue = Volley.newRequestQueue(getActivity());
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
