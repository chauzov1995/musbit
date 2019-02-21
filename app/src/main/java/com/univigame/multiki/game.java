package com.univigame.multiki;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class game extends AppCompatActivity {

    //настройки
    int pokaz_rekl_kajd_n_otv = 5;


    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    Button otv1, otv2, otv3, otv4, button3, button5, fiftyfifty;
    ArrayList<class_spis_vsego> spisokvsego;
    int lengtht;
    VideoView videoView, videoView2;

    TextView textView, textView2;
    game tekactiviti;

    int random_vopt_btn;
    int money, level;
    boolean[] btn_enabl = {true, true, true, true};
    private InterstitialAd mInterstitialAd;
    int rekl_n_otv = 0;
    Timer timer;
    public class_spis_vsego varotv1, varotv2, varotv3, varotv4;
    Button selectedotv;
    boolean prav = false;
    // public MyFragment2 fragment2;
    int gameover_money = 0;
    int gameover_schore = 0;
    CountDownTimer timer1, timer2;
    ProgressBar progressBar2;
    ConstraintLayout body;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tekactiviti = this;

        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");
        MobileAds.initialize(this, getString(R.string.rekl_id_app));


        //всплывающяя реклама
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.perehod_rekl));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //всплывающяя реклама


        body = (ConstraintLayout) findViewById(R.id.body);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        fiftyfifty = (Button) findViewById(R.id.fiftyfifty);
        button5 = (Button) findViewById(R.id.button5);
        button3 = (Button) findViewById(R.id.button3);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        //  linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView2 = (VideoView) findViewById(R.id.videoView2);
        otv1 = (Button) findViewById(R.id.otv1);
        otv2 = (Button) findViewById(R.id.otv2);
        otv3 = (Button) findViewById(R.id.otv3);
        otv4 = (Button) findViewById(R.id.otv4);



     /*   button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {

            }
        });
        fiftyfifty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {

            }
        });


        mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();


        spisokvsego = new ArrayList<class_spis_vsego>();
        Cursor c = mDb.rawQuery("SELECT id, name, url FROM musbit ", null);
        if (c.moveToFirst()) {
            int id = c.getColumnIndex("id");
            int nazv = c.getColumnIndex("name");
            int url = c.getColumnIndex("url");
            do {
                spisokvsego.add(new class_spis_vsego(c.getInt(id), c.getString(nazv), c.getString(url)));
            } while (c.moveToNext());
        }
        lengtht = spisokvsego.size();


        Cursor cursor2 = mDb.rawQuery("SELECT * FROM records ", null);
        cursor2.moveToFirst();
        level = (cursor2.getInt(cursor2.getColumnIndex("level")));
        cursor2.close();

*/
        //load_new_vopr(true);//новый вопрос при старте

        MyFragment1 asdasd=     new MyFragment1().newInstance(5);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
       ft.replace(R.id.linearLayout, asdasd);
        ft.commit();


        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                asdasdasd.start();
            }
        });

    }

 public    MyFragment1 asdasdasd;


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer1 != null) timer1.cancel();
        if (timer2 != null) timer2.cancel();
        if (timer != null) timer.cancel();
    }
































    void enabled_btn_otv(int nomen) {
        btn_enabl[nomen] = false;
        switch (nomen) {
            case 0:
                otv1.setVisibility(View.INVISIBLE);
                break;
            case 1:
                otv2.setVisibility(View.INVISIBLE);
                break;
            case 2:
                otv3.setVisibility(View.INVISIBLE);
                break;
            case 3:
                otv4.setVisibility(View.INVISIBLE);
                break;
        }


/*
        otv1.setEnabled(btn_enabl[0]);
        otv2.setEnabled(btn_enabl[1]);
        otv3.setEnabled(btn_enabl[2]);
        otv4.setEnabled(btn_enabl[3]);
        otv1.setVisibility(View.INVISIBLE);
        otv2.setBackground(getDrawable(android.R.color.holo_orange_light));;
        otv3.setEnabled(btn_enabl[2]);
        otv4.setEnabled(btn_enabl[3]);
*/
    }

    void enabled_btn_new() {
        btn_enabl[0] = true;
        btn_enabl[1] = true;
        btn_enabl[2] = true;
        btn_enabl[3] = true;

        //   otv1.setEnabled(btn_enabl[0]);
        //    otv2.setEnabled(btn_enabl[1]);
        //   otv3.setEnabled(btn_enabl[2]);
        //   otv4.setEnabled(btn_enabl[3]);
        otv1.setVisibility(View.VISIBLE);
        otv2.setVisibility(View.VISIBLE);
        otv3.setVisibility(View.VISIBLE);
        otv4.setVisibility(View.VISIBLE);
    }

    public void ubratb_1nepr() {
        if (money - 5 >= 0) {
            boolean stop = true;
            while (stop) {
                int randomn = (int) (Math.random() * 4);

                if (randomn != random_vopt_btn && btn_enabl[randomn] == true) {
                    enabled_btn_otv(randomn);
                    stop = false;
                    //  minus_monetka(5);
                }
                if ((0 == random_vopt_btn || btn_enabl[0] == false) &&
                        (1 == random_vopt_btn || btn_enabl[1] == false) &&
                        (2 == random_vopt_btn || btn_enabl[2] == false) &&
                        (3 == random_vopt_btn || btn_enabl[3] == false)
                ) stop = false;


            }
        } else {

            //  CustomDialog2 customDialog2 = new CustomDialog2(tekactiviti);
            // customDialog2.show();

        }
    }

    void minus_monetka(int value) {

        mDb.execSQL("UPDATE `records` SET money=money-" + value);
       // get_money();


    }





}
