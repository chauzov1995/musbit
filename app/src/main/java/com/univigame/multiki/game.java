package com.univigame.multiki;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.univigame.multiki.MainActivity.peremeshatb;


public class game extends AppCompatActivity {

    //настройки
    int pokaz_rekl_kajd_n_otv = 4;
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
    CountDownTimer timer1;
    AVLoadingIndicatorView progressBar2;
    ConstraintLayout body;
    int random_vopt_btn2, random_vopt_btn1;
    int[] varianti1, varianti2;
    boolean first_fifty = true, first_zanogo = true;
    dial_perehod customDialog1;
    private AdView mAdView;
    private RewardedAd rewardedAd;
    LinearLayout LL_money;

    boolean first_gameover = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d("oncreate", "sadad");
        tekactiviti = this;

        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");


        //всплывающяя реклама
        mejstranrekl_first();
        //всплывающяя реклама


        LL_money = (LinearLayout) findViewById(R.id.LL_money);
        body = (ConstraintLayout) findViewById(R.id.body);
        progressBar2 = (AVLoadingIndicatorView) findViewById(R.id.progressBar2);
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
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        rewardedAd = new RewardedAd(this,
                getString(R.string.vozn_game_over));

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);


        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        relativeLayout.getLayoutParams().height = (width * 720 / 1280)+1;

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

        //завершение первого видео
        videoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                body.setVisibility(View.INVISIBLE);
                progressBar2.setVisibility(View.VISIBLE);


                if (prav1) {


                    customDialog1 = new dial_perehod(game.this, money, spisokvsego.get(level));
                    customDialog1.show();


                    //межстраничная реклма
                    mejstranrekl();
                    //межстраничная реклма


                } else {


                    if (first_gameover == false) {
                        dial_prodoljvideo customDialog1 = new dial_prodoljvideo(game.this, money, spisokvsego.get(level));
                        customDialog1.show();
                        first_gameover = true;
                    } else {

                        game_over();
                    }
                }


                if (level + 1 == lengtht) {
                    level = 0;
                } else {
                    level++;
                }


            }
        });


        videoView2.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                body.setVisibility(View.INVISIBLE);
                progressBar2.setVisibility(View.VISIBLE);


                if (prav2) {


                    dial_perehod customDialog1 = new dial_perehod(game.this, money, spisokvsego.get(level));
                    customDialog1.show();


                    //межстраничная реклма
                    mejstranrekl();
                    //межстраничная реклма


                } else {

                    if (first_gameover == false) {
                        dial_prodoljvideo customDialog1 = new dial_prodoljvideo(game.this, money, spisokvsego.get(level));
                        customDialog1.show();
                        first_gameover = true;
                    } else {

                        game_over();
                    }
                    /*

                     */
                }

                if (level + 1 == lengtht) {
                    level = 0;
                } else {
                    level++;
                }


            }
        });


        mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();


        Cursor cursor2 = mDb.rawQuery("SELECT * FROM records ", null);
        cursor2.moveToFirst();
        level = (cursor2.getInt(cursor2.getColumnIndex("level")));
        cursor2.close();


        load_spus_vsego();
        lengtht = spisokvsego.size();

        body.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        get_money();//обновим  монеты
        load_new_vopr(level);//новый вопрос при старте

    }


    void load_spus_vsego() {
        spisokvsego = new ArrayList<class_spis_vsego>();
        Cursor c = mDb.rawQuery("SELECT id, name, url, applemusikurl, ispoln FROM musbit where podtverjd=1", null);
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
        c.close();


    }


    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer1 != null) timer1.cancel();

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
        prav_otvet_dlyaperemesh = spisokvsego.get(level).id;
        Log.d("start_ugadka", "level - " + level);
        first_video = true;
        prav1 = false;
        get_money();//обновим  монеты
//оставим для первого старта
        body.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.VISIBLE);


        btn_visualization_otv(varianti1);

        int sled_level;
        if (level + 1 == lengtht) {
//если больше нет вопросов то всё с нуля начать, напиши ещё код для перемешки
            mDb.execSQL("UPDATE `records` SET level=0");
            sled_level = 0;

            peremeshatb(mDb);
            load_spus_vsego();


        } else {
            mDb.execSQL("UPDATE `records` SET level=level+1");
            sled_level = level + 1;
        }
        load_new_vopr2(sled_level);

        vv_1();
        videoView.start();


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
                    try {
                        Games.getLeaderboardsClient(game.this, GoogleSignIn.getLastSignedInAccount(game.this))
                                .submitScore(getString(R.string.leaderboard), gameover_schore);
                    } catch (Exception e) {
                    }

                    get_money();


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
                        Log.d("старт", "" + videoView2.getBufferPercentage());
                        start_sled = false;

                        timer3.cancel();
                        start_ugadka2();
                    }
                }
            });
        }
    }


    void start_ugadka2() {
        prav_otvet_dlyaperemesh = spisokvsego.get(level).id;
        Log.d("start_ugadka", "level - " + level);
        first_video = false;
        prav2 = false;
        get_money();//обновим  монеты
        button3.setVisibility(View.VISIBLE);
        body.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);

        btn_visualization_otv(varianti2);

        int sled_level;
        if (level + 1 == lengtht) {
//если больше нет вопросов то всё с нуля начать, напиши ещё код для перемешки
            mDb.execSQL("UPDATE `records` SET level=0");
            sled_level = 0;

            peremeshatb(mDb);
            load_spus_vsego();


        } else {
            mDb.execSQL("UPDATE `records` SET level=level+1");
            sled_level = level + 1;
        }
        load_new_vopr(sled_level);

        vv_2();
        videoView2.start();


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
                    try {
                        Games.getLeaderboardsClient(game.this, GoogleSignIn.getLastSignedInAccount(game.this))
                                .submitScore(getString(R.string.leaderboard), gameover_schore);
                    } catch (Exception ignored) {
                    }
                    get_money();


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


            }
        }.start();


    }

    public void vv_1() {

        videoView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        videoView2.getLayoutParams().height = 2;
        videoView.bringToFront();
        videoView.requestLayout();
        videoView2.requestLayout();
    }

    public void vv_2() {

        videoView2.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        videoView.getLayoutParams().height = 2;

        videoView2.bringToFront();
        videoView2.requestLayout();
        videoView.requestLayout();
    }

    int prav_otvet_dlyaperemesh;

    void otvnvibor(View r, class_spis_vsego otv_sel_btn_elem) {

        if (first_video) {
            selectedotv = (Button) r;
            prav1 = false;
            if (otv_sel_btn_elem.id == prav_otvet_dlyaperemesh) {
                prav1 = true;
                YoYo.with(Techniques.Pulse).playOn(selectedotv);
                YoYo.with(Techniques.Swing).playOn(textView);
                YoYo.with(Techniques.Swing).playOn(LL_money);

            }else{
                YoYo.with(Techniques.Shake).playOn(selectedotv);
                button3.setVisibility(View.INVISIBLE);
            }


        //    videoView.pause();
            videoView.seekTo(10000);
         //   videoView.start();
            timer1.cancel();
            timer1.onFinish();


        } else {
            selectedotv = (Button) r;
            prav2 = false;
            if (otv_sel_btn_elem.id == prav_otvet_dlyaperemesh) {
                prav2 = true;
                YoYo.with(Techniques.Pulse).playOn(selectedotv);
                YoYo.with(Techniques.Swing).playOn(textView);
                YoYo.with(Techniques.Swing).playOn(LL_money);

            }else{
                YoYo.with(Techniques.Shake).playOn(selectedotv);
                button3.setVisibility(View.INVISIBLE);

            }




            // videoView.SEE
         //   videoView2.pause();
            videoView2.seekTo(10000);
         //   videoView2.start();
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
            Drawable img = getResources().getDrawable(R.drawable.podskcoins_design);
            fiftyfifty.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
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
            Drawable img = getResources().getDrawable(R.drawable.podskcoins_design);
            muz_zanogo.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

        } else {
            if (money >= 100) {
                mDb.execSQL("UPDATE `records` SET money=money-100");
                get_money();
            } else {
                return;
            }
        }


        // muz_zanogo.setEnabled(false);
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

    void mejstranrekl() {


        rekl_n_otv++;
        if (rekl_n_otv >= pokaz_rekl_kajd_n_otv) {

            if (mInterstitialAd.isLoaded()) {
                customDialog1.setbutton_enabled(false);

                mInterstitialAd.show();


            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            rekl_n_otv = 0;
        }

        //заагрузка новой рекламы
        if (rekl_n_otv == 1)
            mejstranrekl_first();

    }

    void mejstranrekl_first() {
        mInterstitialAd = new InterstitialAd(game.this);
        mInterstitialAd.setAdUnitId(getString(R.string.perehod_rekl));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if(customDialog1!=null)
                customDialog1.setbutton_enabled(true);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                if(customDialog1!=null)
                customDialog1.setbutton_enabled(true);
            }
        });

    }

    public void game_over() {



        if (rewardedAd.isLoaded()) {
           // Activity activityContext = ...;
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                public void onRewardedAdClosed() {
                    // Ad closed.

                    game_over2();
                }

                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.

                }

                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display
                    game_over2();
                }
            };
            rewardedAd.show(game.this, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
            game_over2();
        }





    }

    void game_over2(){

        onBackPressed();
        Intent intent = new Intent(tekactiviti, game_over.class);
        intent.putExtra("gameover_money", gameover_money);
        intent.putExtra("gameover_schore", gameover_schore);
        startActivity(intent);
    }


}