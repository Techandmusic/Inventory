package com.apps.adam.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.adam.inventory.data.BookContract.BookEntry;
import com.apps.adam.inventory.data.BookDbHelper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layout;

    private ArrayList<Card> cards;

    public CardAdapter mAdapter;

    private Uri mCurrentBookUri;

    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Database helper instance
        mDbHelper = new BookDbHelper(this);
        //Empty TextView
        TextView emptyText = (TextView) findViewById(R.id.defaultText);
        //Create ArrayList of cards
        cards = new ArrayList<>();
        //CreateRecyclerView
        RecyclerView recycle = (RecyclerView) findViewById(R.id.recycler);
        //Initialize layout manager


        //Initialize mAdapter and set it to the RecyclerView
        mAdapter = new CardAdapter(this, cards);
        recycle.setAdapter(mAdapter);
        //Set has fixed size value and layout manager
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        //Get data and load into cards ArrayList
        fetchData();





        if (mAdapter.getItemCount() > 0) {
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

    public void onClickSale() {
        //Get text as string
        TextView quantityText = findViewById(R.id.productQuantity);
        String quantity = quantityText.getText().toString();
        int currentQuantity = Integer.parseInt(quantity);
        int newQuantity = currentQuantity - 1;
        quantityText.setText(newQuantity);
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
            //Notify adapter of change
            mAdapter.swapData(cards);
            cursor.close();
        }


    }




}
