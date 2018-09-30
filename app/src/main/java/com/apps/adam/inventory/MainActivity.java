package com.apps.adam.inventory;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apps.adam.inventory.data.BookContract;
import com.apps.adam.inventory.data.BookContract.BookEntry;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getBookDetails() {


        //Get full book details from database
        //return bundle to pass to Details fragment
        Bundle bundle = new Bundle();


    }



    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_AUTHOR_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE};
        return new CursorLoader(this, BookEntry.CONTENT_URI, projection, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //TODO complete this method
    }

    public void onLoaderReset() {
        //TODO complete and fix this method
    }

}
