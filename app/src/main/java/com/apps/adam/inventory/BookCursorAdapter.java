package com.apps.adam.inventory;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.adam.inventory.data.BookContract;
import com.apps.adam.inventory.data.BookContract.BookEntry;

public class BookCursorAdapter extends CursorAdapter {
    //Context variable
    private Context mContext;

    private Activity main_activity;

    //Initialize TextViews


    //Class constructor
    public BookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    //newView method to inflate layout to list_item.xml
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    //Implementation of bindView method
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mContext = context;
        //Declare and initialize TextViews
        TextView titleTextView = view.findViewById(R.id.productName);
        TextView priceTextView = view.findViewById(R.id.productPrice);
        final TextView quantityTextView = view.findViewById(R.id.productQuantity);
        //Create int variables for column indices
        int idColumnIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
        final int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
        //Extract column indices to appropriate variables
        final String id = cursor.getString(idColumnIndex);
        String bookTitle = cursor.getString(nameColumnIndex);
        Double bookPrice = cursor.getDouble(priceColumnIndex);
        final int bookQuantity = cursor.getInt(quantityColumnIndex);

        //Set variables to TextViews
        titleTextView.setText(bookTitle);
        priceTextView.setText(Double.toString(bookPrice));
        quantityTextView.setText(Integer.toString(bookQuantity));

        Button saleButton = (Button) view.findViewById(R.id.sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity(bookQuantity, id);
            }
        });






    }

    public void decrementQuantity(int quantity, String id) {
        if (quantity >= 1) {
            quantity --;
        } else {
            quantity = 0;
        }
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_QUANTITY, quantity);
        Uri currentUri = Uri.withAppendedPath(BookEntry.CONTENT_URI, id);
        mContext.getContentResolver().update(currentUri, values, null, null);

    }
}
