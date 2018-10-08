package com.apps.adam.inventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.adam.inventory.data.BookContract.BookEntry;
import com.apps.adam.inventory.data.BookDbHelper;

public class EditBookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //Loader constant
    private static final int CURRENT_BOOK_LOADER = 0;
    //EditText field to add book title
    private EditText mTitle;
    //EditText field to add book author
    private EditText mAuthor;
    //EditText field to add book price
    private EditText mPrice;
    //EditText field to add quantity of book in stock
    private EditText mQuantity;
    //EditText field to add name of supplier for book
    private EditText mSupplierName;
    //EditText field to add supplier's phone number
    private EditText mSupplierNo;
    //Variable for rows affected when adding a new book
    //Variable for rows affected when adding a new book
    int rowsAffected;
    //Database helper instance
    private BookDbHelper mDbHelper;
    //Variable for current book uri
    private Uri mCurrentBookUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        //Set TextViews to xml layout
        mTitle = findViewById(R.id.addTitle);
        mAuthor = findViewById(R.id.addAuthor);
        mPrice = findViewById(R.id.addPrice);
        mQuantity = findViewById(R.id.addQuantity);
        mSupplierName = findViewById(R.id.addSupplierName);
        mSupplierNo = findViewById(R.id.addSupplierNo);
        //Get the incoming data
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();


        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });

        getLoaderManager().initLoader(CURRENT_BOOK_LOADER, null, this);

    }

    public void saveBook() {
        //Get values from EditText fields
        String titleString = mTitle.getText().toString().trim();
        String authorString = mAuthor.getText().toString().trim();
        String priceDouble = mPrice.getText().toString().trim();
        String quantityInt = mQuantity.getText().toString().trim();
        String supNameString = mSupplierName.getText().toString().trim();
        String supPhoneString = mSupplierNo.getText().toString().trim();

        //Parse numerical values accordingly
        Double itemPrice = 0.00;
        if (!TextUtils.isEmpty(priceDouble)) {
            itemPrice = Double.parseDouble(priceDouble);
        }
        int itemQuantity = 0;
        if (!TextUtils.isEmpty(quantityInt)) {
            itemQuantity = Integer.parseInt(quantityInt);
        }
        //Bail early if no data is present in EditText fields
        if (mCurrentBookUri == null && TextUtils.isEmpty(titleString) && TextUtils.isEmpty(authorString) &&
                TextUtils.isEmpty(priceDouble) && TextUtils.isEmpty(quantityInt) && TextUtils.isEmpty(supNameString) &&
                TextUtils.isEmpty(supPhoneString)) {
            return;
        }

        //Create a ContentValues object where column names are keys and
        //book attributes are values
        ContentValues values = new ContentValues();
        //Add items to values
        values.put(BookEntry.COLUMN_PRODUCT_NAME, titleString);
        values.put(BookEntry.COLUMN_AUTHOR_NAME, authorString);
        values.put(BookEntry.COLUMN_PRICE, itemPrice);
        values.put(BookEntry.COLUMN_QUANTITY, itemQuantity);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, supNameString);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE, supPhoneString);


        rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

        //Display toast message to show if save was successful or not
        if (rowsAffected == 0) {
            Toast.makeText(this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
        }
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
        return new CursorLoader(this, mCurrentBookUri, projection, null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            //Get the column indices we're interested in
            int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_AUTHOR_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
            int supNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int supPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE);
            //Extract the value for each given column index
            String Title = cursor.getString(titleColumnIndex);
            String Author = cursor.getString(authorColumnIndex);
            Double Price = cursor.getDouble(priceColumnIndex);
            int Quantity = cursor.getInt(quantityColumnIndex);
            String SupplierName = cursor.getString(supNameColumnIndex);
            String SupplierPhone = cursor.getString(supPhoneColumnIndex);
            //Update the EditText fields with the values from the database
            mTitle.setText(Title);
            mAuthor.setText(Author);
            mPrice.setText(Double.toString(Price));
            mQuantity.setText(Integer.toString(Quantity));
            mSupplierName.setText(SupplierName);
            mSupplierNo.setText(SupplierPhone);
        }

    }

    public void onLoaderReset(Loader<Cursor> loader) {
        //If the loader is invalidated set fields to empty strings
        mTitle.setText("");
        mAuthor.setText("");
        mPrice.setText("");
        mQuantity.setText("");
        mSupplierName.setText("");
        mSupplierNo.setText("");
    }
}
