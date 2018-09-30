package com.apps.adam.inventory;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.adam.inventory.data.BookContract.BookEntry;

import com.apps.adam.inventory.data.BookDbHelper;

public class EditorFragment extends Fragment {
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
    //Layout inflater
    private LayoutInflater inflater;

    //Constructor
    public EditorFragment() {
        //Required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         /* if (getArguments() != null) {
            get data from bundle
            to pass to this fragment

        } */



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editor, parent, false);
        //Set TextViews to xml layout
        mTitle = view.findViewById(R.id.addTitle);
        mAuthor = view.findViewById(R.id.addAuthor);
        mPrice = view.findViewById(R.id.addPrice);
        mQuantity = view.findViewById(R.id.addQuantity);
        mSupplierName = view.findViewById(R.id.addSupplierName);
        mSupplierNo = view.findViewById(R.id.addSupplierNo);
        //Return view
        return view;
    }

    public void saveBookInfo() {
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

        rowsAffected = getContext().getContentResolver().update(mCurrentBookUri, values, null, null);

        if (rowsAffected == 0) {
            Toast.makeText(getContext(), getString(R.string.save_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
        }
    }
}
