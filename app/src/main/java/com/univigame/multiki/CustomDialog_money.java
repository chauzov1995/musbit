package com.univigame.multiki;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomDialog_money {
    private EditText dialogEditBox;
    private TextView dialogInfo;
    private Button dialogButton;
    private Dialog dialog;
    private MainActivity activity;
    int money;
    ImageButton button4;

    public CustomDialog_money(MainActivity activity, int money) {
        this.activity = activity;
        this.money = money;
        init();
    }

    private void init() {

        dialog = new Dialog(activity, R.style.CustomDialog);

        dialog.setContentView(R.layout.dialog_money);
        dialog.setCancelable(false);


        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button textView7 = (Button) dialog.findViewById(R.id.btn_money);
        button4 = (ImageButton) dialog.findViewById(R.id.button4);
        dialogButton = (Button) dialog.findViewById(R.id.dialog_button);

        textView7.setText("Монеты "+money);
        button4.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {
                dialog.dismiss();
            }
        });

        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {
                dialog.dismiss();
                Intent intent = new Intent(activity, nagrada_za_reklamu.class);
                intent.putExtra("type_nagr", 1);
                activity.startActivity(intent);


            }
        });
    }

    public void show() {
        dialog.show();
    }


}