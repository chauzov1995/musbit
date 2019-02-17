package com.univigame.multiki;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

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

        byn_start.setEnabled(false);

        byn_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                if (tek_energy > 0) {

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
                } else {
                    CustomDialog_energy customDialog1 = new CustomDialog_energy(tekactiviti, tek_energy);
                    customDialog1.show();
                }
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
        mDb = mDBHelper.getWritableDatabase();


        new load_spis_valut().execute();;//загрузим песни с сайта


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


    private class load_spis_valut extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {
            BufferedReader reader = null;
            String content;
            try {

                URL url = new URL("https://teplogico.ru/musbit");
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();

                 reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                 StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                // return (buf.toString());


                Log.d("json", buf.toString());

                JSONArray jsonArray = new JSONArray(buf.toString());

                if (jsonArray.length() > 0) mDb.execSQL("DELETE FROM `musbit` ");

                Cursor curs = mDb.rawQuery("SELECT id FROM `musbit` ", null);




                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject zakaz = jsonArray.getJSONObject(i);
                    Log.d("json", ""+zakaz.getString("name"));
                    String name = zakaz.getString("name");
                    String imageurl = zakaz.getString("url");

                    Log.d("ыйд", "INSERT INTO `musbit` ( `id`,`name`, `url`,`sort`)" +
                            " VALUES ('" + (i+1) + "', '" + name + "', '" + imageurl + "', '" + (i+1) + "')");

                    mDb.execSQL("INSERT INTO `musbit` ( `id`,`name`, `url`,`sort`)" +
                            " VALUES ('" + (i+1) + "', '" + name + "', '" + imageurl + "', '" + (i+1) + "')");

                }


                content = "";
            } catch (Exception e) {
                content=  e.getMessage();

            }

            return content;
        }


        @Override
        protected void onPostExecute(String content) {

            byn_start.setEnabled(true);
        }


    }
}
