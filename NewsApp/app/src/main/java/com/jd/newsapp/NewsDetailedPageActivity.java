package com.jd.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NewsDetailedPageActivity extends AppCompatActivity {

    String fetchedArticleId, detailedPageUrl, data;
    int dday, dmonth, dyear;
    String detailedNewsUrl = "https://androidnewsappbackend.wl.r.appspot.com/detailedarticle?articleId=";
    String detailedtitle, detailedtime, detailedsource, detailedlink, detailedarticleid, detailedimagelink, detaileddescription, fulldate, fullpage;
    Context mContext;
    ImageView dimage;
    List<OurData> NewsList;
    OurData ourData;
    TextView dtitle, dtime, dsource, dlink, darticleid, ddescription, fpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detailed_page);
        setTheme(R.style.detailedPage);
        NewsList = new ArrayList<OurData>();



        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toptoolbar);
        setSupportActionBar(toolbar);
        // add activity to manifest
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        //check if id exists or not
        if(getIntent().hasExtra("sentArticleId" )){
            fetchedArticleId = getIntent().getStringExtra("sentArticleId");
        }

        final ProgressBar progressBaR = findViewById(R.id.card_progressbar);
        final TextView progresstext = findViewById(R.id.card_progressbartext);
        detailedPageUrl = detailedNewsUrl + fetchedArticleId;
        StringRequest stringRequest = new StringRequest(detailedPageUrl, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                data = response;
                //Log.d("data of page : ", data);
                try{
                    JSONObject jsonObject = new JSONObject(data);
                    detailedarticleid = jsonObject.getString("id");
                    detailedtitle = jsonObject.getString("title");
                    fulldate = jsonObject.getString("date");
                    detailedsource = jsonObject.getString("source");
                    detailedlink = jsonObject.getString("url");
                    detailedimagelink = jsonObject.getString("imagecheck");
                    detaileddescription = jsonObject.getString("description");

                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(fulldate);
                    ZonedDateTime zdtAmericalLA1 = zonedDateTime
                            .withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ) );

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                    detailedtime = dateTimeFormatter.format(zdtAmericalLA1);


                    dtitle = (TextView) findViewById(R.id.detailedtitle);
                    dsource = (TextView) findViewById(R.id.detailedsource);
                    dtime = (TextView) findViewById(R.id.detailedtime);
                    ddescription = (TextView) findViewById(R.id.detaileddesc);
                    dimage = (ImageView) findViewById(R.id.detailedimage);
                    dlink = (TextView) findViewById(R.id.detailedlink);
                    fpage = (TextView) findViewById(R.id.detailedlink);

                    dtitle.setText(detailedtitle);
                    dsource.setText(detailedsource);
                    dtime.setText(detailedtime);
                    ddescription.setText(Html.fromHtml(detaileddescription));
                    Glide.with(mContext).load(detailedimagelink).into(dimage);
                    getSupportActionBar().setTitle(detailedtitle);
                    dlink.setText(Html.fromHtml("<a href=\""+ detailedlink + "\">" + "View Full Article" + "</a>"));
                    dlink.setClickable(true);
                    dlink.setMovementMethod (LinkMovementMethod.getInstance());
                    ourData = new OurData(detailedarticleid,detailedtitle,detailedsource,detailedlink, detailedtime, detailedimagelink,fulldate);
                    fpage.setVisibility(View.VISIBLE);
                    if(data!=""){
                        progressBaR.setVisibility(View.GONE);
                        progresstext.setText("");
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailed_action_bar_menu, menu);
        SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("MyFavNews", 0);
        SharedPreferences.Editor editor = pref.edit();
        if(pref.contains(fetchedArticleId)){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(mContext, R.drawable.detailedfilledbookmark));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("MyFavNews", 0);
        SharedPreferences.Editor editor = pref.edit();

        int id = item.getItemId();
        if(id == R.id.dbookmark){
            if(!pref.contains(fetchedArticleId)){
                Gson g = new Gson();
                String jsondata = g.toJson(ourData);
                editor.putString(fetchedArticleId, jsondata);
                Toast.makeText(mContext, detailedtitle+" was added to favorites", Toast.LENGTH_SHORT).show();
                item.setIcon(ContextCompat.getDrawable(mContext, R.drawable.detailedfilledbookmark));
                editor.commit();
            }
            else if(pref.contains(fetchedArticleId)){
                Toast.makeText(mContext, detailedtitle+" was removed from favorites", Toast.LENGTH_SHORT).show();
                editor.remove(fetchedArticleId);

                item.setIcon(ContextCompat.getDrawable(mContext, R.drawable.detailedbookmarkbigsize));
                editor.commit();
            }
        }

        if(id == R.id.dtwitter){
            String twitter_initial = "https://twitter.com/intent/tweet?text=";
            String twitter_end =  Uri.encode("Check out this Link:" + "\n" + detailedlink + "\n" + "#CSCI571NewsSearch");
            String twitterlink = twitter_initial + twitter_end;
            Uri uri = Uri.parse(twitterlink);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
            //Toast.makeText(this, "tweeted news", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
