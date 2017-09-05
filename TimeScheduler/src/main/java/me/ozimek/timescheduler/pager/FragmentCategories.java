package me.ozimek.timescheduler.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.ozimek.timescheduler.R;

/**
 * Created by wojtek on 2017-08-17.
 */

public class FragmentCategories extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
        return v;
    }
/*
    private static final String Arg1 = "param1", Arg2 = "param2";

    private String p1;
    private String p2;

    public FragmentCategories() { }

    public static FragmentCategories newInstance(String param1, String param2) {
        FragmentCategories fragmentCategories = new FragmentCategories();
        Bundle args = new Bundle();
        args.putString(Arg1, param1);
        args.putString(Arg2, param2);
        fragmentCategories.setArguments(args);
        return fragmentCategories;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            p1 = getArguments().getString(Arg1);
            p2 = getArguments().getString(Arg2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_stats, container);
    } */
}
