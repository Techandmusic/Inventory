package com.apps.adam.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.apps.adam.inventory.data.BookContract.BookEntry;
import com.apps.adam.inventory.data.BookDbHelper;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //Loader constant
    private static final int BOOK_LOADER = 0;
    //Database helper object
    private BookDbHelper mDbHelper;
    //ListView for books
    private ListView productView;
    //CursorAdapter
    private BookCursorAdapter mCursorAdapter;
    //TextView for Book Quantity
    private TextView bookQuantity;
    //TextView for book title
    private TextView bookTitle;
    //Uri variable
    private Uri mCurrentBookUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //  fetchData();
        bookQuantity = (TextView) findViewById(R.id.productQuantity);
        bookTitle = (TextView) findViewById(R.id.productName);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBookIntent = new Intent(MainActivity.this, AddBookActivity.class);
                startActivity(addBookIntent);

            }
        });

        //Instantiate DbHelper object
        mDbHelper = new BookDbHelper(this);
        //Instantiate ListView
        ListView bookListView = (ListView) findViewById(R.id.mainList);
        //Set emptyView
        View emptyView = (TextView) findViewById(R.id.defaultText);
        bookListView.setEmptyView(emptyView);
        //Instantiate CursorAdapter
        mCursorAdapter = new BookCursorAdapter(this, null);
        //Call set adapter method on ListView
        bookListView.setAdapter(mCursorAdapter);

        //Set onItemClickListener for ListView
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
                //Set Uri with appended id
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                //Call intent.setData method with uri passed as argument
                detailsIntent.setData(currentBookUri);
                //call startActivity
                startActivity(detailsIntent);
            }
        });



        getLoaderManager().initLoader(BOOK_LOADER, null, this);


    }








    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY
        };
        return new CursorLoader(this, BookEntry.CONTENT_URI, projection, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


}
