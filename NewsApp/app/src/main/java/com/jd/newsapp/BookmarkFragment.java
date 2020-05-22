package com.jd.newsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BookmarkFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String data;
    RecyclerView recyclerView;
    BookmarkAdapter bkAdapter;
    List<OurData> NewsList;
    private Context mContext;
    String btitle, btime, bsource, blink, barticleid, bimagelink, blateTime;
    TextView empty_bookmark;
    public BookmarkFragment() {
    }



    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = getContext();

       // Log.d("Bookmark","Fragment");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.bookmarksrecyclerview);
         empty_bookmark = (TextView) view.findViewById(R.id.empty_bookmark);

        NewsList = new ArrayList<OurData>();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        bkAdapter = new BookmarkAdapter(NewsList,mContext, empty_bookmark);

        getBookmarks();

        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getBookmarks() {
        final SharedPreferences pref = getActivity().getSharedPreferences("MyFavNews", 0);
        final SharedPreferences.Editor editor = pref.edit();
        Map<String,?> entries = pref.getAll();
        Set<String> keys = entries.keySet();
        //Log.d("bookmark", String.valueOf(pref.getAll().size()));

        //editor.clear();

        for (Object data : entries.values()){
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject jd = parser.parse(data.toString()).getAsJsonObject();
            Log.d("bookmark", String.valueOf(jd));
            barticleid = String.valueOf(jd.get("id")).replaceAll("^\"|\"$", "");
            btime = String.valueOf(jd.get("fulldate")).replaceAll("^\"|\"$", "");
            //btime = btime.replaceAll("^\"|\"$", "");
            Log.d("bookmark page date ", btime);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(btime);
            ZonedDateTime zdtAmericalLA1 = zonedDateTime
                    .withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ) );

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM");
            btime = dateTimeFormatter.format(zdtAmericalLA1);
            Log.d("bookmark date format : ", btime);
            bimagelink = String.valueOf(jd.get("imagecheck")).replaceAll("^\"|\"$", "");
            bsource = String.valueOf(jd.get("source")).replaceAll("^\"|\"$", "");
            btitle = String.valueOf(jd.get("title")).replaceAll("^\"|\"$", "");
            blink = String.valueOf(jd.get("url")).replaceAll("^\"|\"$", "");
            blateTime = String.valueOf(jd.get("date")).replaceAll("^\"|\"$", "");

            NewsList.add(
                    new OurData(
                            barticleid,
                            btitle,
                            bsource,
                            blink,
                            btime,
                            bimagelink, blateTime));

            Log.d("data in json : ", String.valueOf(jd));


        }
        if(NewsList.size() != 0){
            empty_bookmark.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(bkAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        if(bkAdapter!=null){
            NewsList.clear();
            getBookmarks();
            if(NewsList.size() == 0){
                empty_bookmark.setVisibility(View.VISIBLE);
            }
            recyclerView.setAdapter(bkAdapter);
            bkAdapter.notifyDataSetChanged();
        }
    }
}
