package com.apps.adam.inventory;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.apps.adam.inventory.data.BookContract.BookEntry;
import com.apps.adam.inventory.data.BookDbHelper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private ArrayList<Card> cards;

    private TextView emptyText;

    private FragmentManager fragMan;

    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Database helper instance
        mDbHelper = new BookDbHelper(this);

        //Empty TextView
        emptyText = (TextView) findViewById(R.id.defaultText);

        //Get data and load into cards ArrayList
        fetchData();
        //TODO possibly refactor fetchData. BookListFragment Needs access to Cards ArrayList


        if (cards.size() > 0) {
            emptyText.setVisibility(View.GONE);
            fragMan = getFragmentManager();
            FragmentTransaction initialTransaction = fragMan.beginTransaction();
            BookListFragment bookList = new BookListFragment();
            initialTransaction.add(R.id.mainLayout, bookList);
        } else {
            emptyText.setText(View.VISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragMan = getFragmentManager();
                FragmentTransaction transaction = fragMan.beginTransaction();
                AddBookFragment addBook = new AddBookFragment();
                transaction.replace(R.id.recycler, addBook);
                transaction.commit();

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

    public void getBookDetails() {


        //Get full book details from database
        //return bundle to pass to Details fragment
        Bundle bundle = new Bundle();


    }


}
