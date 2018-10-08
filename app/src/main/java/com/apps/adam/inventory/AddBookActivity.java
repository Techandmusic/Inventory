package com.apps.adam.inventory;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.adam.inventory.data.BookContract.BookEntry;

public class AddBookActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        //Set TextViews to xml layout
        mTitle = findViewById(R.id.addTitle);
        mAuthor = findViewById(R.id.addAuthor);
        mPrice = findViewById(R.id.addPrice);
        mQuantity = findViewById(R.id.addQuantity);
        mSupplierName = findViewById(R.id.addSupplierName);
        mSupplierNo = findViewById(R.id.addSupplierNo);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });


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

        //Input validation check
        if (TextUtils.isEmpty(titleString) || TextUtils.isEmpty(authorString) || TextUtils.isEmpty(priceDouble) ||
                TextUtils.isEmpty(supNameString) || TextUtils.isEmpty(supPhoneString)) {
            //Give user warning message
            Toast.makeText(this, R.string.empty_field_warning, Toast.LENGTH_SHORT).show();
            //Return early so that incomplete book is not added to database
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

        Uri newUri = this.getContentResolver().insert(BookEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.save_successful), Toast.LENGTH_SHORT).show();

        }
        finish();


    }
}
