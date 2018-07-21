package com.tr33hgr.filmnight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    //Called when 'Host' button is pressed
    public void openEventCreate(View view){

        Intent openEventCreateAct = new Intent(this, EventCreateActivity.class);
        startActivity(openEventCreateAct);
    }
}
