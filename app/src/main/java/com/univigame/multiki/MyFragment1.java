package com.univigame.multiki;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

// класс для первого фрагмента
public class MyFragment1 extends Fragment {

    game ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.btn_start,
                container, false);

        Button nextButton = (Button) view.findViewById(R.id.button6);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r) {

                ctx = (game) getActivity();
                ctx.load_new_vopr();

            }
        });
        return view;
    }
}