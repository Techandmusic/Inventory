package com.apps.adam.inventory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BookListFragment extends Fragment {

    private ArrayList<Card> cards;
    private Context mContext;

    public BookListFragment() {

    }

    @SuppressLint("NewApi")
    public Context getmContext() {
        mContext = this.getContext();
        return mContext;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_list, parent, false);

        //Create RecyclerView set fixed size and layout manager

        //Create ArrayList of cards
        Activity main = getActivity();




        return view;

    }
}
