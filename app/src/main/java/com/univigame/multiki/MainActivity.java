package com.univigame.multiki;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    MainActivity tekactiviti;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    Button byn_start, byn_start2, btn_energ, btn_money, btn_videomodey;
    int money;
    long tek_energy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        tekactiviti = this;

        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");
        //инит рекл
        MobileAds.initialize(this, getString(R.string.rekl_id_app));


        byn_start = (Button) findViewById(R.id.byn_start);
        byn_start2 = (Button) findViewById(R.id.byn_start2);
        btn_energ = (Button) findViewById(R.id.btn_energ);
        btn_money = (Button) findViewById(R.id.btn_money);
        btn_videomodey = (Button) findViewById(R.id.btn_videomodey);


        byn_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {

//вычтем энергию за игру
                Cursor cursor = mDb.rawQuery("SELECT energy FROM records ", null);
                cursor.moveToFirst();

                int energy = (cursor.getInt(cursor.getColumnIndex("energy")));
                cursor.close();
                long unixTime = System.currentTimeMillis() / 1000L;
                if (energy >= unixTime)
                    mDb.execSQL("UPDATE `records` SET energy=energy+" + 600);
                else
                    mDb.execSQL("UPDATE `records` SET energy=" + (unixTime + 600));
                //Log.d("asdasd",energy+" "+unixTime);
//вычтем энергию за игру

                Intent intent = new Intent(tekactiviti, game.class);
                startActivity(intent);
            }
        });
        btn_energ.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                CustomDialog_energy customDialog1 = new CustomDialog_energy(tekactiviti, tek_energy);
                customDialog1.show();
            }
        });
        btn_money.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                CustomDialog_money customDialog1 = new CustomDialog_money(tekactiviti, money);
     customDialog1.show();
            }
        });
        btn_videomodey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                Intent intent = new Intent(MainActivity.this, nagrada_za_reklamu.class);
                intent.putExtra("type_nagr", 1);
                MainActivity.this.startActivity(intent);
            }
        });


        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textView10 = (TextView) findViewById(R.id.textView9);
        Cursor cursor = mDb.rawQuery("SELECT * FROM records ", null);
        cursor.moveToFirst();


        int energy = (cursor.getInt(cursor.getColumnIndex("energy")));
        int score = (cursor.getInt(cursor.getColumnIndex("score")));
         money = (cursor.getInt(cursor.getColumnIndex("money")));
        long unixTime = System.currentTimeMillis() / 1000L;


        if (unixTime >= energy)
            tek_energy = 12;
        else
            tek_energy = 11 - ((energy - unixTime) / 600);


        textView10.setText("Рекорд: " + score + "");
        btn_energ.setText("Энергия: " + tek_energy + "/12");
        btn_money.setText("Монеты: " + money + "");

    }
}
