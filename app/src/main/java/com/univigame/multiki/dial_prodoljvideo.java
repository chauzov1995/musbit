package com.univigame.multiki;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;


public class dial_prodoljvideo {

    private Button perehod_btn;
    private Dialog dialog;
    private game activity;
    int money;
    class_spis_vsego musik;
    CountDownTimer timer1;

    boolean loadetsd = false;
    boolean prosmotrel = false;


    public dial_prodoljvideo(game activity, int money, class_spis_vsego musik) {
        this.activity = activity;
        this.money = money;
        this.musik = musik;
        init();
    }


    private void init() {

        dialog = new Dialog(activity, R.style.CustomDialog);

        dialog.setContentView(R.layout.dial_prodoljvideo);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(dialogInterface -> close_Ad_fail());
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        perehod_btn = (Button) dialog.findViewById(R.id.perehod_btn);
        ImageButton button4 = (ImageButton) dialog.findViewById(R.id.button4);
        final TextView textView6 = (TextView) dialog.findViewById(R.id.textView6);

        button4.setOnClickListener(r -> {
                    close_Ad_fail();
                }
        );
        perehod_btn.setOnClickListener(r -> {
            if (timer1 != null) timer1.cancel();



            Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
                @Override
                public void onRewardedVideoLoaded(boolean isPrecache) {
                    Log.d("Appodeal", "onRewardedVideoLoaded");
                }
                @Override
                public void onRewardedVideoFailedToLoad() {
                    Log.d("Appodeal", "onRewardedVideoFailedToLoad");
                    close_Ad_fail();
                }
                @Override
                public void onRewardedVideoShown() {
                    Log.d("Appodeal", "onRewardedVideoShown");
                }
                @Override
                public void onRewardedVideoFinished(double amount, String name) {
                    Log.d("Appodeal", "onRewardedVideoFinished");
                    prosmotrel = true;
                }
                @Override
                public void onRewardedVideoClosed(boolean finished) {
                    Log.d("Appodeal", "onRewardedVideoClosed");

                    // Ad closed.
                    if (prosmotrel)//единственный успешный вход
                    {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        activity.prodolj_dialog(musik);


                    } else {
                        close_Ad_fail();
                    }

                }
                @Override
                public void onRewardedVideoExpired() {
                    Log.d("Appodeal", "onRewardedVideoExpired");
                }
            });
            Appodeal.show(activity, Appodeal.REWARDED_VIDEO);

        });


        timer1 = new CountDownTimer(10000, 1000) {

            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                Log.d("осталось тимер1", millisUntilFinished / 1000 + "");
                textView6.setText((millisUntilFinished / 1000) + "");
            }

            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
            public void onFinish() {

                close_Ad_fail();

            }
        }.start();


    }


    private void close_Ad_fail() {


        if (timer1 != null) timer1.cancel();
        if (dialog != null && dialog.isShowing()) {
        dialog.dismiss();
        }
        activity.game_over();

    }

    void show() {
        dialog.show();
    }


}