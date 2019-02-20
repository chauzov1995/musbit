package com.univigame.multiki;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

// класс для первого фрагмента
public class MyFragment2 extends Fragment {

    //  game ctx;

    //   public  MyFragment2(class_spis_vsego btnname0,class_spis_vsego btnname1,class_spis_vsego btnname2,class_spis_vsego btnname3) {
    //  this.ctx=ctx;

    //  }


    game ctx;
    public Button otv1, otv2, otv3, otv4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.varianti_otv,
                container, false);

        ctx = (game) getActivity();

        otv1 = (Button) view.findViewById(R.id.otv1);
        otv2 = (Button) view.findViewById(R.id.otv2);
        otv3 = (Button) view.findViewById(R.id.otv3);
        otv4 = (Button) view.findViewById(R.id.otv4);


        otv1.setText(ctx.varotv1.nazv);
        otv2.setText(ctx.varotv2.nazv);
        otv3.setText(ctx.varotv3.nazv);
        otv4.setText(ctx.varotv4.nazv);

      //  ctx.fragment2 = MyFragment2.this;


        otv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                ctx.otvnvibor(r, ctx.varotv1);
            }
        });
        otv2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                ctx.otvnvibor(r, ctx.varotv2);
            }
        });
        otv3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                ctx.otvnvibor(r, ctx.varotv3);
            }
        });
        otv4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {
                ctx.otvnvibor(r, ctx.varotv4);
            }
        });

        return view;
    }


}
