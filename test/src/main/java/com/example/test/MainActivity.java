package com.example.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream stream = null;
        try {
            stream = getAssets().open("sda.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        MovieGifView  gifView = (MovieGifView) findViewById(R.id.movieGifView);
         gifView = new MovieGifView(this, stream);






    }
}
