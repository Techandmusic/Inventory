package com.apps.adam.inventory;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BookListFragment extends Fragment {
    private RecyclerView.LayoutManager layout;
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
        //Initialize layout manager if needed
        layout = new RecyclerView.LayoutManager() {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return null;
            }
        };
        //Create RecyclerView set fixed size and layout manager
        RecyclerView recycle = (RecyclerView) view.findViewById(R.id.recycler);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(layout);
        //Create ArrayList of cards
        cards = new ArrayList<>();

        //Create RecyclerView adapter and set it to the RecyclerView
        CardAdapter adapter = new CardAdapter(getmContext(), cards);
        recycle.setAdapter(adapter);

        return view;

    }
}
