package com.jd.newsapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LatestNewsCard extends Fragment {

    public LatestNewsCard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_latest_news_card, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cardrecyclerview);
        List<OurData> NewsList = new ArrayList<OurData>();
        CardRecyclerAdapter cradapter = new CardRecyclerAdapter(NewsList, getContext());
        recyclerView.setAdapter(cradapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
}
