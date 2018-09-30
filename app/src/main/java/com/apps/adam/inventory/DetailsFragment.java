package com.apps.adam.inventory;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.apps.adam.inventory.data.BookContract.BookEntry;

public class DetailsFragment extends Fragment {

    //Loader ID constant
    private static final int DETAILS_LOADER = 0;
    //Current Uri
    private Uri mCurrentBookUri;
    //TextView for title
    private TextView bookTitle;
    //TextView for author
    private TextView bookAuthor;
    //TextView for price
    private TextView bookPrice;
    //TextView for Quantity
    private TextView bookQuantity;
    //TextView for supplier name
    private TextView bookSupplier;
    //TextView for supplier number
    private TextView bookSupPhone;
    //Layout inflater
    private LayoutInflater inflater;
    private Loader<Cursor> loader;


    public DetailsFragment() {
        //Required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // get data from bundle
           // to pass to this fragment


        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancceState) {
        View view = inflater.inflate(R.layout.product_details, container, false);
        return view;

    }


}
