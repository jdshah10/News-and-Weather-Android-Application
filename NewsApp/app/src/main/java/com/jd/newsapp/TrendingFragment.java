package com.jd.newsapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class TrendingFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LineChart lineChart;
    String initial_trendingUrl = "https://androidnewsappbackend.wl.r.appspot.com/googlesearchtrends?q=";
    String data;
    Context mContext;
    int colorArray[] = {R.color.selectedicon};
    EditText searchKeyword;
    String initialLegend = "Trending Chart for ", value;
    private String mParam1;
    private String mParam2;


    public TrendingFragment() {
        // Required empty public constructor
    }

    public static TrendingFragment newInstance(String param1, String param2) {
        TrendingFragment fragment = new TrendingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_trending, container, false);

        lineChart = (LineChart) view.findViewById(R.id.trendingchartid);
        searchKeyword = (EditText) view.findViewById(R.id.trendingsearch);

        searchKeyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_ENTER) || (event.getAction() == KeyEvent.ACTION_DOWN)) {
                    //Toast.makeText(getContext(),"searchKeyword.getText()", Toast.LENGTH_SHORT).show();
                    value = searchKeyword.getText().toString().trim();
                    //Log.d("datavalue : ", value);
                    if(value != null){
                        forSearchTrendingDataApiCall(view, value);
                    }
                }
                return false;
            }
        });
        String hint = searchKeyword.getHint().toString().trim();
        //Log.d("hint : ", hint);

        forTrendingDataApiCall(view);




        return view;
    }

    public void forSearchTrendingDataApiCall(View view, final String value) {
        String initialTrendingUrl = "https://androidnewsappbackend.wl.r.appspot.com/googlesearchtrends?q=";

        String url  = initial_trendingUrl + value;
        //.d("value passed", url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                data = response;
                try {
                    // Log.d("linechart data : ", data);

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray valuearray = jsonObject.getJSONArray("valuearray");
                    ArrayList<Entry> dataSet = new ArrayList<Entry>();
                    //LineDataSet lineDataSet = new LineDataSet(lineChartDataset(), "dataSet");
                    for(int i = 0 ; i < valuearray.length() ; i++){
                        int valueObj = valuearray.getInt(i);
                        dataSet.add(new Entry(i,valueObj));
                    }
                    //Log.d("value is : ", String.valueOf(dataSet));
                    LineDataSet lineDataSet = new LineDataSet(dataSet, "Trending Chart for "+value);
                    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                    iLineDataSets.add(lineDataSet);
                    LineData lineData = new LineData(iLineDataSets);
                    lineChart.setData(lineData);
                    lineDataSet.setColor(R.color.selectedicon);
                    lineDataSet.setCircleColor(R.color.selectedicon);
                    lineDataSet.setCircleHoleColor(R.color.selectedicon);
                    Legend legend = lineChart.getLegend();
                    LegendEntry[] entries = legend.getEntries();
                    LegendEntry entry = entries[0];
                    entry.formColor = getResources().getColor(R.color.selectedicon);
                    legend.setEnabled(true);
                    legend.setTextColor(R.color.blackcolor);
                    legend.setTextSize(13f);

                    legend.setFormSize(15f);
                    lineData.setValueTextSize(10f);
                    lineData.setValueTextColor(R.color.selectedicon);
                    lineDataSet.setLineWidth(2f);
                    lineDataSet.setCircleRadius(3f);

                    XAxis xl = lineChart.getXAxis();
                    xl.setDrawGridLines(false);

                    lineChart.getAxisLeft().setDrawAxisLine(false);

                    YAxis aa = lineChart.getAxisRight();
                    aa.setDrawGridLines(false);

                    YAxis leftAxis = lineChart.getAxisLeft();
                    leftAxis.setDrawGridLines(false);
                    //leftAxis.setEnabled(false);
                    lineChart.invalidate();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rqueue = Volley.newRequestQueue(getActivity());
        rqueue.add(stringRequest);

    }


    public void forTrendingDataApiCall(View view){


        String initialTrendingUrl = "https://androidnewsappbackend.wl.r.appspot.com/googlesearchtrends?q=CoronaVirus";
        //String url  = initial_trendingUrl + value;
        StringRequest stringRequest = new StringRequest(initialTrendingUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                data = response;
                try {
                   // Log.d("linechart data : ", data);

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray valuearray = jsonObject.getJSONArray("valuearray");
                    ArrayList<Entry> dataSet = new ArrayList<Entry>();
                    //LineDataSet lineDataSet = new LineDataSet(lineChartDataset(), "dataSet");
                    for(int i = 0 ; i < valuearray.length() ; i++){
                        int valueObj = valuearray.getInt(i);
                        dataSet.add(new Entry(i,valueObj));
                    }
                    //Log.d("value is : ", String.valueOf(dataSet));
                    LineDataSet lineDataSet = new LineDataSet(dataSet, "Trending Chart for Coronavirus");
                    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                    iLineDataSets.add(lineDataSet);
                    LineData lineData = new LineData(iLineDataSets);
                    lineChart.setData(lineData);
                    lineDataSet.setColor(R.color.selectedicon);
                    lineDataSet.setCircleColor(R.color.selectedicon);
                    lineDataSet.setCircleHoleColor(R.color.selectedicon);
                    Legend legend = lineChart.getLegend();
                    legend.setEnabled(true);
                    legend.setTextColor(R.color.blackcolor);
                    legend.setTextSize(13f);
                    legend.setFormSize(15f);
                    LegendEntry[] entries = legend.getEntries();
                    LegendEntry entry = entries[0];
                    entry.formColor = getResources().getColor(R.color.selectedicon);
                    lineData.setValueTextColor(R.color.selectedicon);

                    lineData.setValueTextSize(10f);
                    lineDataSet.setLineWidth(2f);
                    lineDataSet.setCircleRadius(3f);

                    lineChart.getAxisLeft().setDrawAxisLine(false);
                    XAxis xl = lineChart.getXAxis();
                    xl.setDrawGridLines(false);

                    YAxis aa = lineChart.getAxisRight();
                    aa.setDrawGridLines(false);

                    YAxis leftAxis = lineChart.getAxisLeft();
                    leftAxis.setDrawGridLines(false);
                    //leftAxis.setEnabled(false);
                    lineChart.invalidate();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rqueue = Volley.newRequestQueue(getActivity());
        rqueue.add(stringRequest);
    }
}
