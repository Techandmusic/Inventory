package com.apps.adam.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.apps.adam.inventory.data.BookContract.BookEntry;
import com.apps.adam.inventory.data.BookDbHelper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layout;

    private ArrayList<Card> cards;


    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Database helper instance
        mDbHelper = new BookDbHelper(this);
        //Empty TextView
        TextView emptyText = (TextView) findViewById(R.id.defaultText);
        //Initialize layout manager if needed
        layout = new RecyclerView.LayoutManager() {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return null;
            }
        };
        //Create ArrayList of cards
        cards = new ArrayList<>();
        //Create RecyclerView set fixed size and layout manager
        RecyclerView recycle = (RecyclerView) findViewById(R.id.recycler);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(layout);
        //Create RecyclerView adapter and set it to the RecyclerView
        CardAdapter adapter = new CardAdapter(this, cards);
        recycle.setAdapter(adapter);


        //Get data and load into cards ArrayList
        fetchData();


        if (adapter.getItemCount() > 0) {
            emptyText.setVisibility(View.GONE);
            recycle.setVisibility(View.VISIBLE);

        } else {
            emptyText.setVisibility(View.VISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBookIntent = new Intent(MainActivity.this, AddBookActivity.class);
                startActivity(addBookIntent);

            }
        });


    }

    public void fetchData() {
        //Projection of columns to load
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_AUTHOR_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE};
        //Cursor of data loaded from database
        Cursor cursor = this.getContentResolver().query(BookEntry.CONTENT_URI, projection, null, null, null);
        if (cursor == null) {
            return;
        } else {
            while (cursor.moveToNext()) {
                //Get column indices
                int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
                int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
                int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_AUTHOR_NAME);
                int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
                int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
                int supNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
                int supPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE);
                //Extract indices to appropriate variables
                int id = cursor.getInt(idColumnIndex);
                String bookTitle = cursor.getString(titleColumnIndex);
                String bookAuthor = cursor.getString(authorColumnIndex);
                Double bookPrice = cursor.getDouble(priceColumnIndex);
                int bookQuantity = cursor.getInt(quantityColumnIndex);
                String bookSupName = cursor.getString(supNameColumnIndex);
                String bookSupPhone = cursor.getString(supPhoneColumnIndex);
                //Add to new card object in cards arraylist
                cards.add(new Card(id, bookTitle, bookAuthor, bookPrice, bookQuantity, bookSupName, bookSupPhone));


            }
            cursor.close();
        }


    }


}
