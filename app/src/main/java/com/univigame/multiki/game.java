package com.univigame.multiki;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
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
    VideoView videoView;

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
    public MyFragment2 fragment2;
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


        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                finish();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
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
        Cursor c = mDb.rawQuery("SELECT id, name FROM musbit ", null);
        if (c.moveToFirst()) {
            int id = c.getColumnIndex("id");
            int nazv = c.getColumnIndex("name");
            do {
                spisokvsego.add(new class_spis_vsego(c.getInt(id), c.getString(nazv)));
            } while (c.moveToNext());
        }
        lengtht = spisokvsego.size();


        load_new_vopr(true);//новый вопрос при старте


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer1 != null) timer1.cancel();
        if (timer2 != null) timer2.cancel();
        if (timer != null) timer.cancel();
    }

    void load_new_vopr(boolean first) {

        get_money();//обновим  монеты
        prav = false;

        body.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.VISIBLE);




        Cursor cursor2 = mDb.rawQuery("SELECT * FROM records ", null);
        cursor2.moveToFirst();
        level = (cursor2.getInt(cursor2.getColumnIndex("level")));
        cursor2.close();



        // enabled_btn_new();//очистим кнопочки

        //рисвоим кнопкам ид правильного ответа
        random_vopt_btn = (int) (Math.random() * 4);
        int[] varianti = {-1, -1, -1, -1};
        varianti[random_vopt_btn] = level;
        for (int i = 0; i < varianti.length; i++) {
            if (varianti[i] == -1) {
                boolean zikl = true;
                while (zikl) {
                    int rand = (int) (Math.random() * lengtht); // Генерация 1-го чис
                    if (rand != varianti[0] && rand != varianti[1] && rand != varianti[2] && rand != varianti[3]) {
                        varianti[i] = rand;
                        zikl = false;
                    }
                }
            }
        }


        varotv1 = spisokvsego.get(varianti[0]);
        varotv2 = spisokvsego.get(varianti[1]);
        varotv3 = spisokvsego.get(varianti[2]);
        varotv4 = spisokvsego.get(varianti[3]);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.linearLayout, new MyFragment2());
        ft.commit();


        //получим ссылку на видео
        Cursor cursor = mDb.rawQuery("SELECT * FROM musbit where id =" + spisokvsego.get(level).id, null);
        cursor.moveToFirst();
        String video_url = (cursor.getString(cursor.getColumnIndex("url")));
        cursor.close();


        if (level + 1 == lengtht) {
            //сли угадал все то с нуля начать, напиши ещё код для перемешки
            mDb.execSQL("UPDATE `records` SET level=0");
        } else {
            mDb.execSQL("UPDATE `records` SET level=level+1");
        }


        //старт видео
        String videoSource = video_url;
        videoView.setVideoURI(Uri.parse(videoSource));
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 500, 500);


    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (videoView.getBufferPercentage() == 100) {
                        // this.cancel();+
                        timer.cancel();
                        start_ugadka();
                    }
                }
            });
        }
    }



    void start_ugadka() {

        body.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);


        videoView.start();
        timer1 = new CountDownTimer(10000, 1000) {

            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                Log.d("осталось тимер1", millisUntilFinished / 1000 + "");
            }

            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
            public void onFinish() {




                fragment2.otv1.setEnabled(false);
                fragment2.otv2.setEnabled(false);
                fragment2.otv3.setEnabled(false);
                fragment2.otv4.setEnabled(false);




                if (prav) {

                    mDb.execSQL("UPDATE `records` SET money=money+10");

                    gameover_money += 10;
                    gameover_schore += 100;
                    mDb.execSQL("UPDATE `records` SET score=" + gameover_schore + " where score<" + gameover_schore);
                    get_money();

           /*
            load_new_vopr();

            //межстраничная реклма
            if (rekl_n_otv >= pokaz_rekl_kajd_n_otv) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd = new InterstitialAd(this);
                    mInterstitialAd.setAdUnitId(getString(R.string.perehod_rekl));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                rekl_n_otv = -1;
            }
            rekl_n_otv++;
            //межстраничная реклма
*/
                } else {
                    if (selectedotv != null) {
                        selectedotv.getBackground().setColorFilter(getResources().getColor(R.color.otvetpnerav), PorterDuff.Mode.MULTIPLY);
                    }

                }


                Button pravotv;
                switch (random_vopt_btn) {
                    case 0:
                        pravotv = fragment2.otv1;
                        break;
                    case 1:
                        pravotv = fragment2.otv2;
                        break;
                    case 2:
                        pravotv = fragment2.otv3;
                        break;
                    default:
                        pravotv = fragment2.otv4;
                        break;
                }
                pravotv.getBackground().setColorFilter(getResources().getColor(R.color.otvetprav), PorterDuff.Mode.MULTIPLY);


                timer2 = new CountDownTimer(8000, 1000) {



                    //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
                    public void onTick(long millisUntilFinished) {
                        Log.d("осталось ", millisUntilFinished / 1000 + "");
                    }

                    //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
                    public void onFinish() {



                        videoView.stopPlayback();

                        if (prav) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.linearLayout, new MyFragment1());
                            ft.commit();
                        } else {

                            finish();
                            Intent intent = new Intent(tekactiviti, game_over.class);
                            intent.putExtra("gameover_money", gameover_money);
                            intent.putExtra("gameover_schore", gameover_schore);
                            startActivity(intent);
                        }


                    }
                }.start();
            }
        }.start();


    }


    void otvnvibor(View r, class_spis_vsego tag) {


        selectedotv = (Button) r;
        prav = false;
        if (tag.id == spisokvsego.get(level).id) {
            prav = true;
        }


        selectedotv.getBackground().setColorFilter(getResources().getColor(R.color.preotvet), PorterDuff.Mode.MULTIPLY);


        // videoView.SEE
        videoView.pause();
        videoView.seekTo(10000);
        videoView.start();
        timer1.cancel();
        timer1.onFinish();
        //   enabled_btn_otv(nombtn - 1);


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
        get_money();


    }


    void get_money() {
        Cursor cursor = mDb.rawQuery("SELECT * FROM records ", null);
        cursor.moveToFirst();


        money = (cursor.getInt(cursor.getColumnIndex("money")));
      //  level = (cursor.getInt(cursor.getColumnIndex("level")));
        //  textView.setText(score);
        textView2.setText(money + "");
        textView.setText("Счёт: " + (gameover_schore));
        cursor.close();

    }


}
