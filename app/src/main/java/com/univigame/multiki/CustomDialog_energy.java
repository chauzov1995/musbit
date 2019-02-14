package com.univigame.multiki;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomDialog_energy {
    private EditText dialogEditBox;
    private TextView dialogInfo;
    private Button dialogButton,button4;
    private Dialog dialog;
    private MainActivity activity;
    long tek_energy;

    public CustomDialog_energy(MainActivity activity, long tek_energy ){
        this.activity = activity;
        this.tek_energy=tek_energy;
        init();
    }

    private void init(){

        dialog = new Dialog(activity, R.style.CustomDialog);
       // dialog.setTitle("Подсказки");
        dialog.setContentView(R.layout.dialog_energy);
        dialog.setCancelable(false);



        TextView  textView7 = (TextView) dialog.findViewById( R.id.textView7 );
                button4 = (Button) dialog.findViewById( R.id.button4 );
        dialogButton = (Button) dialog.findViewById( R.id.dialog_button );

        textView7.setText(tek_energy+"/12");
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                dialog.dismiss();
            }
        });

        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View r) {
                dialog.dismiss();
                Intent intent = new Intent(activity, nagrada_za_reklamu.class);
                intent.putExtra("type_nagr", 3);
                activity.startActivity(intent);



            }
        });
    }

    public void show(){
        dialog.show();
    }




}