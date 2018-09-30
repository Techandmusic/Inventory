package com.apps.adam.inventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

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
}
