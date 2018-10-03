package com.apps.adam.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.apps.adam.inventory.data.BookContract.BookEntry;

public class DetailsActivity extends AppCompatActivity {
    //Current Uri
    private Uri mCurrentBookUri;
    //TextView for title
    private TextView bookTitle;
    //TextView for author
    private TextView bookAuthor;
    //TextView for price
    private TextView bookPrice;
    //TextView for Quantity
    private TextView bookQuantity;
    //TextView for supplier name
    private TextView bookSupplier;
    //TextView for supplier number
    private TextView bookSupPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //Set TextViews to xml layout
        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookPrice = findViewById(R.id.bookPrice);
        bookQuantity = findViewById(R.id.bookQuantity);
        bookSupplier = findViewById(R.id.bookSupplier);
        bookSupPhone = findViewById(R.id.bookSupPhone);

        //Get intent with book data
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();
        //TODO finish filling in this activity

        Button increaseQuantity = (Button) findViewById(R.id.increase);
        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuantity();
                updateBookInfo();
            }
        });

        Button decreaseQuantity = (Button) findViewById(R.id.decrease);
        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractQuantity();
                updateBookInfo();
            }
        });


        Button order = (Button) findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supplierCall = bookSupPhone.getText().toString();
                Intent orderIntent = new Intent(Intent.ACTION_DIAL);
                orderIntent.setData(mCurrentBookUri.parse("tel:" + supplierCall));
                startActivity(orderIntent);
            }
        });


        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
            }
        });


    }

    public void addQuantity() {
        //Get TextView text as a String variable
        String quantityText = bookQuantity.getText().toString();
        //Convert String to Integer variable
        int quantityNumber = Integer.parseInt(quantityText);
        //Add 1 to current TextView Text
        int newQuantity = quantityNumber + 1;
        //Set the updated quantity text to the TextView
        bookQuantity.setText(Integer.toString(newQuantity));
        //Update the database with the new value
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_QUANTITY, newQuantity);
        this.getContentResolver().update(mCurrentBookUri, values, null, null);
    }

    public void subtractQuantity() {
        //Get TextView text as a String variable
        String quantityText = bookQuantity.getText().toString();
        //Convert String to Integer variable
        int quantityNumber = Integer.parseInt(quantityText);
        //Add 1 to current TextView Text
        int newQuantity = quantityNumber - 1;
        if (newQuantity < 0) {
            newQuantity = 0;
        }
        //Set the updated quantity text to the TextView
        bookQuantity.setText(Integer.toString(newQuantity));
        //Update database with new value
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_QUANTITY, newQuantity);
        this.getContentResolver().update(mCurrentBookUri, values, null, null);

    }

    public void deleteBook() {
        if (mCurrentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateBookInfo() {
        String quantityText = bookQuantity.getText().toString();
        //Convert String to Integer variable
        int quantityNumber = Integer.parseInt(quantityText);
        String titleText = bookTitle.getText().toString();
        //Create a content values
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, titleText);
        values.put(BookEntry.COLUMN_QUANTITY, quantityNumber);
        //Update info in database
        int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
        if (rowsAffected == 0) {
            Toast.makeText(this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
        }
    }
}
