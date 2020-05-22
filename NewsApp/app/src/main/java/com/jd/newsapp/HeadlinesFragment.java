package com.jd.newsapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeadlinesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeadlinesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem worldTab, businessTab, politicsTab, sportsTab, technologyTabs, scienceTabs;
    public HeadlinesPageAdapter pageAdapter;


    public HeadlinesFragment() {
        // Required empty public constructor
    }

    public static HeadlinesFragment newInstance(String param1, String param2) {
        HeadlinesFragment fragment = new HeadlinesFragment();
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
        View view = inflater.inflate(R.layout.fragment_headlines, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.headlines_tablayout);

        worldTab = (TabItem) view.findViewById(R.id.worldheadlines);
        businessTab = (TabItem) view.findViewById(R.id.businessheadlines);
        politicsTab = (TabItem) view.findViewById(R.id.politicsheadline);
        sportsTab = (TabItem) view.findViewById(R.id.sportsheadline);
        technologyTabs = (TabItem) view.findViewById(R.id.technologyheadlines);
        scienceTabs = (TabItem) view.findViewById(R.id.scienceheadlines);

        viewPager = view.findViewById(R.id.pagerheadlines);

        pageAdapter = new HeadlinesPageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout. addOnTabSelectedListener (new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    pageAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 1){
                    pageAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 2){
                    pageAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 3){
                    pageAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 4){
                    pageAdapter.notifyDataSetChanged();
                }
                else if(tab.getPosition() == 5){
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }
}
