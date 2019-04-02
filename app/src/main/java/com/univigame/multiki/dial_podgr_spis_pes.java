package com.univigame.multiki;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class dial_podgr_spis_pes {
    private EditText dialogEditBox;
    private TextView dialogInfo;
    private Button dialogButton;
    private Dialog dialog;
    private MainActivity activity;
    int money;
    ImageButton button4;
    private RewardedAd rewardedAd;

    public dial_podgr_spis_pes(MainActivity activity) {
        this.activity=activity;
        init();
    }

    private void init() {

        dialog = new Dialog(activity, R.style.CustomDialog);

        dialog.setContentView(R.layout.dial_podgr_spis_pesen);
        dialog.setCancelable(false);


        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));





    }

    public void destroy(){
        dialog.cancel();

    }

    public void show() {
        dialog.show();
    }


}