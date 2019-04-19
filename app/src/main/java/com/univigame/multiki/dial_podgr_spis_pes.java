package com.univigame.multiki;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.RelativeLayout;

public class dial_podgr_spis_pes {

    private Dialog dialog;
    private MainActivity activity;



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