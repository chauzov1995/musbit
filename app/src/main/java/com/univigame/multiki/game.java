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
    Timer timer , timer3;
    // public class_spis_vsego varotv1, varotv2, varotv3, varotv4;
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


        final class_spis_vsego varotv1 = spisokvsego.get(varianti[0]);
        final class_spis_vsego varotv2 = spisokvsego.get(varianti[1]);
        final class_spis_vsego varotv3 = spisokvsego.get(varianti[2]);
        final class_spis_vsego varotv4 = spisokvsego.get(varianti[3]);

        otv1.setText(varotv1.nazv);
        otv2.setText(varotv2.nazv);
        otv3.setText(varotv3.nazv);
        otv4.setText(varotv4.nazv);

        otv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                otvnvibor(r, varotv1);
            }
        });
        otv2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                otvnvibor(r, varotv2);
            }
        });
        otv3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                otvnvibor(r, varotv3);
            }
        });
        otv4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                otvnvibor(r, varotv4);
            }
        });


        if (level + 1 == lengtht) {
            //если больше нет вопросов то всё с нуля начать, напиши ещё код для перемешки
            mDb.execSQL("UPDATE `records` SET level=0");
        } else {
            mDb.execSQL("UPDATE `records` SET level=level+1");
        }


        //старт видео
        String videoSource = spisokvsego.get(level).url;
        ;

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


                otv1.setEnabled(false);
                otv2.setEnabled(false);
                otv3.setEnabled(false);
                otv4.setEnabled(false);


                if (level + 1 == lengtht) {
                    level = 0;
                } else {
                    level++;
                }
                load_new_vopr2(false);


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
                        pravotv = otv1;
                        break;
                    case 1:
                        pravotv = otv2;
                        break;
                    case 2:
                        pravotv = otv3;
                        break;
                    default:
                        pravotv = otv4;
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

                        // load_new_vopr(false);

/*
                        if (prav) {



                            dial_perehod customDialog1 = new dial_perehod(game.this, money);
                            customDialog1.show();


                        } else {

                            finish();
                            Intent intent = new Intent(tekactiviti, game_over.class);
                            intent.putExtra("gameover_money", gameover_money);
                            intent.putExtra("gameover_schore", gameover_schore);
                            startActivity(intent);
                        }
*/

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

    int random_vopt_btn2;

     void load_new_vopr2(boolean first) {

        //     get_money();//обновим  монеты
        //   prav = false;

        //  body.setVisibility(View.INVISIBLE);
        //    progressBar2.setVisibility(View.VISIBLE);


        //рисвоим кнопкам ид правильного ответа
        random_vopt_btn2 = (int) (Math.random() * 4);
        int[] varianti = {-1, -1, -1, -1};
        varianti[random_vopt_btn2] = level;
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


        final class_spis_vsego    varotv1 = spisokvsego.get(varianti[0]);
        final class_spis_vsego    varotv2 = spisokvsego.get(varianti[1]);
        final class_spis_vsego  varotv3 = spisokvsego.get(varianti[2]);
        final class_spis_vsego  varotv4 = spisokvsego.get(varianti[3]);

        /*
        otv1.setText(varotv1.nazv);
        otv2.setText(varotv2.nazv);
        otv3.setText(varotv3.nazv);
        otv4.setText(varotv4.nazv);

        otv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                otvnvibor(r, varotv1);
            }
        });
        otv2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                otvnvibor(r, varotv2);
            }
        });
        otv3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                otvnvibor(r, varotv3);
            }
        });
        otv4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                otvnvibor(r, varotv4);
            }
        });






        if (level + 1 == lengtht) {
            //если больше нет вопросов то всё с нуля начать, напиши ещё код для перемешки
            mDb.execSQL("UPDATE `records` SET level=0");
        } else {
            mDb.execSQL("UPDATE `records` SET level=level+1");
        }

*/
        //старт видео
        String videoSource = spisokvsego.get(level).url;
        ;

 videoView3 = new VideoView(game.this);
         videoView3.setVideoURI(Uri.parse(videoSource));
        timer3 = new Timer();
        timer3.schedule(new MyTimerTask2(), 500, 500);


    }
    VideoView videoView3;
    private class MyTimerTask2 extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (videoView3.getBufferPercentage() == 100) {
                        videoView.stopPlayback();
                        //videoView=videoView2;
                        videoView3.start();
                        timer.cancel();
             //           start_ugadka();
                    }
                }
            });
        }
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