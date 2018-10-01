package com.apps.adam.inventory;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.adam.inventory.data.BookDbHelper;
import com.apps.adam.inventory.data.BookContract.BookEntry;

public class AddBookFragment extends Fragment {
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
    int rowsAffected;
    //Database helper instance
    private BookDbHelper mDbHelper;
    //Variable for current book uri
    private Uri mCurrentBookUri;
    //Layout inflater
    private LayoutInflater inflater;
    //Context variable
    private Context mContext;


    //Constructor method
    public AddBookFragment() {
        //Empty constructor
    }

    public Context getmContext() {
        mContext = this.getActivity();
        return mContext;
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
        //Add logic to save button
        Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
        //Return view
        return view;
    }

    public void addBook() {
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

        Uri newUri = getmContext().getContentResolver().insert(BookEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(getmContext(), getString(R.string.save_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getmContext(), getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
        }


    }
}
