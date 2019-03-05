package com.univigame.multiki;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class game extends AppCompatActivity {

    //настройки
    int pokaz_rekl_kajd_n_otv = 5;
    boolean first_video = false;//важно, для первого определения прав ответа


    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    Button otv1, otv2, otv3, otv4, button3, muz_zanogo, fiftyfifty;
    ArrayList<class_spis_vsego> spisokvsego;
    int lengtht;
    VideoView videoView, videoView2;
    public boolean start_sled = true;
    TextView textView, textView2;
    game tekactiviti;

    // int random_vopt_btn;
    int money, level;
    boolean[] btn_enabl = {true, true, true, true};
    private InterstitialAd mInterstitialAd;
    int rekl_n_otv = 0;
    Timer timer, timer3;
    // public class_spis_vsego varotv1, varotv2, varotv3, varotv4;
    Button selectedotv;
    boolean prav1 = false, prav2 = false;
    // public MyFragment2 fragment2;
    int gameover_money = 0;
    int gameover_schore = 0;
    CountDownTimer timer1, timer2;
    ProgressBar progressBar2;
    ConstraintLayout body;
    int random_vopt_btn2, random_vopt_btn1;
    int[] varianti1, varianti2;
    boolean first_fifty = true, first_zanogo = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d("oncreate", "sadad");
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
        muz_zanogo = (Button) findViewById(R.id.button5);
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
                onBackPressed();
            }
        });
        muz_zanogo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                muz_zanogo();
            }
        });
        fiftyfifty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                fiftyfifty();
            }
        });


        mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();


        spisokvsego = new ArrayList<class_spis_vsego>();
        Cursor c = mDb.rawQuery("SELECT id, name, url, applemusikurl, ispoln FROM musbit ", null);
        if (c.moveToFirst()) {
            int id = c.getColumnIndex("id");
            int nazv = c.getColumnIndex("name");
            int url = c.getColumnIndex("url");
            int ispoln = c.getColumnIndex("ispoln");
            int applemusikurl = c.getColumnIndex("applemusikurl");

            do {
                spisokvsego.add(new class_spis_vsego(c.getInt(id), c.getString(nazv), c.getString(url), c.getString(ispoln), c.getString(applemusikurl)));
            } while (c.moveToNext());
        }
        lengtht = spisokvsego.size();


        Cursor cursor2 = mDb.rawQuery("SELECT * FROM records ", null);
        cursor2.moveToFirst();
        level = (cursor2.getInt(cursor2.getColumnIndex("level")));
        cursor2.close();


        body.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        get_money();//обновим  монеты
        load_new_vopr(level);//новый вопрос при старте


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer1 != null) timer1.cancel();
        if (timer2 != null) timer2.cancel();
        if (timer != null) timer.cancel();
        if (timer3 != null) timer3.cancel();
    }

    void load_new_vopr(int level) {


        varianti1 = gener_otv_btn(level);

        //старт видео
        String videoSource = spisokvsego.get(level).url;
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
                    if (videoView.getBufferPercentage() == 100 && start_sled) {

                        start_sled = false;
                        timer.cancel();
                        start_ugadka();
                    }
                }
            });
        }
    }


    void start_ugadka() {
        first_video = true;
        prav1 = false;
        get_money();//обновим  монеты
//оставим для первого старта
        body.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);


        btn_visualization_otv(varianti1);

        int sled_level;
        if (level + 1 == lengtht) {
//если больше нет вопросов то всё с нуля начать, напиши ещё код для перемешки
            mDb.execSQL("UPDATE `records` SET level=0");
            sled_level = 0;
        } else {
            mDb.execSQL("UPDATE `records` SET level=level+1");
            sled_level = level + 1;
        }
        load_new_vopr2(sled_level);


        videoView.start();
        videoView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        videoView2.getLayoutParams().height = 0;
        videoView.requestLayout();

        timer1 = new CountDownTimer(10000, 1000) {

            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                Log.d("осталось тимер1", millisUntilFinished / 1000 + "");
            }

            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
            public void onFinish() {
                visualization_vremyaisteklo();





                if (prav1) {

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
                        selectedotv.setBackground(getResources().getDrawable(R.drawable.otvet_noprav_design));

                    }

                }
                selectedotv = null;

                Button pravotv;
                switch (random_vopt_btn1) {
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
                pravotv.setBackground(getResources().getDrawable(R.drawable.otvet_prav_design));
                pravotv.setTextColor(Color.BLACK);





                timer2 = new CountDownTimer(8000, 1000) {


                    //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
                    public void onTick(long millisUntilFinished) {
                        Log.d("осталось ", millisUntilFinished / 1000 + "");
                    }

                    //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
                    public void onFinish() {


                        videoView.stopPlayback();


                        if (prav1) {


                            dial_perehod customDialog1 = new dial_perehod(game.this, money, spisokvsego.get(level));
                            customDialog1.show();


                        } else {

                            onBackPressed();
                            Intent intent = new Intent(tekactiviti, game_over.class);
                            intent.putExtra("gameover_money", gameover_money);
                            intent.putExtra("gameover_schore", gameover_schore);
                            startActivity(intent);
                        }


                        if (level + 1 == lengtht) {
                            level = 0;
                        } else {
                            level++;
                        }

                    }
                }.start();
            }
        }.start();


    }


    void load_new_vopr2(int level) {


        varianti2 = gener_otv_btn(level);

        //старт видео
        String videoSource = spisokvsego.get(level).url;
        videoView2.setVideoURI(Uri.parse(videoSource));
        timer3 = new Timer();
        timer3.schedule(new MyTimerTask2(), 500, 500);
    }

    private class MyTimerTask2 extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (videoView2.getBufferPercentage() == 100 && start_sled) {
                        //       videoView.stopPlayback();
                        Log.d("старт", "2nfqvth");
                        start_sled = false;

                        timer3.cancel();
                        start_ugadka2();
                    }
                }
            });
        }
    }


    void start_ugadka2() {
        first_video = false;
        prav2 = false;
        get_money();//обновим  монеты


        btn_visualization_otv(varianti2);

        int sled_level;
        if (level + 1 == lengtht) {
//если больше нет вопросов то всё с нуля начать, напиши ещё код для перемешки
            mDb.execSQL("UPDATE `records` SET level=0");
            sled_level = 0;
        } else {
            mDb.execSQL("UPDATE `records` SET level=level+1");
            sled_level = level + 1;
        }
        load_new_vopr(sled_level);


        videoView2.start();
        videoView2.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        videoView.getLayoutParams().height = 0;
        videoView2.requestLayout();

        timer1 = new CountDownTimer(10000, 1000) {

            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                Log.d("осталось тимер1", millisUntilFinished / 1000 + "");
            }

            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
            public void onFinish() {
                visualization_vremyaisteklo();



                if (prav2) {

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
                        selectedotv.setBackground(getResources().getDrawable(R.drawable.otvet_noprav_design));

                    }

                }
                selectedotv = null;

                Button pravotv;
                switch (random_vopt_btn2) {
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
                pravotv.setBackground(getResources().getDrawable(R.drawable.otvet_prav_design));
                pravotv.setTextColor(Color.BLACK);

                timer2 = new CountDownTimer(8000, 1000) {


                    //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
                    public void onTick(long millisUntilFinished) {
                        Log.d("осталось ", millisUntilFinished / 1000 + "");
                    }

                    //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
                    public void onFinish() {


                        videoView2.stopPlayback();


                        if (prav2) {


                            dial_perehod customDialog1 = new dial_perehod(game.this, money, spisokvsego.get(level) );
                            customDialog1.show();


                        } else {

                            onBackPressed();
                            Intent intent = new Intent(tekactiviti, game_over.class);
                            intent.putExtra("gameover_money", gameover_money);
                            intent.putExtra("gameover_schore", gameover_schore);
                            startActivity(intent);
                        }

                        if (level + 1 == lengtht) {
                            level = 0;
                        } else {
                            level++;
                        }


                    }
                }.start();
            }
        }.start();


    }


    void otvnvibor(View r, class_spis_vsego otv_sel_btn_elem) {

        if (first_video) {
            selectedotv = (Button) r;
            prav1 = false;
            if (otv_sel_btn_elem.id == spisokvsego.get(level).id) {
                prav1 = true;
            }


            videoView.pause();
            videoView.seekTo(10000);
            videoView.start();
            timer1.cancel();
            timer1.onFinish();


        } else {
            selectedotv = (Button) r;
            prav2 = false;
            if (otv_sel_btn_elem.id == spisokvsego.get(level).id) {
                prav2 = true;
            }


            // videoView.SEE
            videoView2.pause();
            videoView2.seekTo(10000);
            videoView2.start();
            timer1.cancel();
            timer1.onFinish();


        }
    }


    int[] gener_otv_btn(int level) {
        //рисвоим кнопкам ид правильного ответа
        int random_vopt_btn = (int) (Math.random() * 4);
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
        if (!first_video) {//специально наоборот,т к ещё не присвоен стату новой песни
            random_vopt_btn1 = random_vopt_btn;
        } else {
            random_vopt_btn2 = random_vopt_btn;
        }
        return varianti;
    }


    void btn_visualization_otv(int[] varianti) {
        Log.d("virt", "virtualizzaasd");
        final class_spis_vsego varotv1 = spisokvsego.get(varianti[0]);
        final class_spis_vsego varotv2 = spisokvsego.get(varianti[1]);
        final class_spis_vsego varotv3 = spisokvsego.get(varianti[2]);
        final class_spis_vsego varotv4 = spisokvsego.get(varianti[3]);

        otv1.setEnabled(true);
        otv2.setEnabled(true);
        otv3.setEnabled(true);
        otv4.setEnabled(true);

        otv1.setVisibility(View.VISIBLE);
        otv2.setVisibility(View.VISIBLE);
        otv3.setVisibility(View.VISIBLE);
        otv4.setVisibility(View.VISIBLE);

        otv1.setBackground(getResources().getDrawable(R.drawable.otvet_do_design));
        otv2.setBackground(getResources().getDrawable(R.drawable.otvet_do_design));
        otv3.setBackground(getResources().getDrawable(R.drawable.otvet_do_design));
        otv4.setBackground(getResources().getDrawable(R.drawable.otvet_do_design));

        otv1.setTextColor(Color.WHITE);
        otv2.setTextColor(Color.WHITE);
        otv3.setTextColor(Color.WHITE);
        otv4.setTextColor(Color.WHITE);

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

        fiftyfifty.setEnabled(true);
        muz_zanogo.setEnabled(true);
    }

    void visualization_vremyaisteklo() {
        otv1.setEnabled(false);
        otv2.setEnabled(false);
        otv3.setEnabled(false);
        otv4.setEnabled(false);

        fiftyfifty.setEnabled(false);
        muz_zanogo.setEnabled(false);
    }


    void fiftyfifty() {


        if (first_fifty) {
            first_fifty = false;
        } else {
            if (money >= 100) {
                mDb.execSQL("UPDATE `records` SET money=money-100");
                get_money();
            } else {
                return;
            }
        }


        int vtoropravotv = 0;
        boolean stop = true;
        fiftyfifty.setEnabled(false);
        if (first_video) {


            while (stop) {
                int randomn = (int) (Math.random() * 4);

                if (randomn != random_vopt_btn1) {
                    vtoropravotv = randomn;
                    stop = false;
                }
            }

            if (vtoropravotv != 0 && random_vopt_btn1 != 0)
                otv1.setVisibility(View.INVISIBLE);
            if (vtoropravotv != 1 && random_vopt_btn1 != 1)
                otv2.setVisibility(View.INVISIBLE);
            if (vtoropravotv != 2 && random_vopt_btn1 != 2)
                otv3.setVisibility(View.INVISIBLE);
            if (vtoropravotv != 3 && random_vopt_btn1 != 3)
                otv4.setVisibility(View.INVISIBLE);
        } else {


            while (stop) {
                int randomn = (int) (Math.random() * 4);

                if (randomn != random_vopt_btn2) {
                    vtoropravotv = randomn;
                    stop = false;
                }
            }

            if (vtoropravotv != 0 && random_vopt_btn2 != 0)
                otv1.setVisibility(View.INVISIBLE);
            if (vtoropravotv != 1 && random_vopt_btn2 != 1)
                otv2.setVisibility(View.INVISIBLE);
            if (vtoropravotv != 2 && random_vopt_btn2 != 2)
                otv3.setVisibility(View.INVISIBLE);
            if (vtoropravotv != 3 && random_vopt_btn2 != 3)
                otv4.setVisibility(View.INVISIBLE);
        }

    }

    void muz_zanogo() {

        if (first_zanogo) {
            first_zanogo = false;
        } else {
            if (money >= 100) {
                mDb.execSQL("UPDATE `records` SET money=money-100");
                get_money();
            } else {
                return;
            }
        }


        muz_zanogo.setEnabled(false);
        if (first_video) {
            videoView.seekTo(0);

        } else {
            videoView2.seekTo(0);

        }
        timer1.cancel();
        timer1.start();
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