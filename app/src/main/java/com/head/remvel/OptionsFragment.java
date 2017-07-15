package com.head.remvel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class OptionsFragment extends Fragment implements View.OnClickListener{

    Button zad,pered,cep,tormoz,sis,ped,dinamo,tre;
    ImageView imageMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.options_layout, container, false);

        imageMenu = (ImageView)rootView.findViewById(R.id.imageMenu);

         MainActivity.zoom(imageMenu);

        zad = (Button) rootView.findViewById( R.id.zad);
        zad.setOnClickListener(this);
        pered = (Button)  rootView.findViewById( R.id.pered);
        pered.setOnClickListener(this);
        cep = (Button) rootView. findViewById( R.id.cep);
        cep.setOnClickListener(this);
        tormoz = (Button) rootView.findViewById( R.id.tormoz);
        tormoz.setOnClickListener(this);
        sis = (Button)  rootView.findViewById( R.id.sis);
        sis.setOnClickListener(this);
        ped = (Button)  rootView.findViewById( R.id.ped);
        ped.setOnClickListener(this);
        dinamo = (Button)rootView. findViewById( R.id.dinamo);
        dinamo.setOnClickListener(this);
        tre = (Button) rootView.findViewById( R.id.tre);
        tre.setOnClickListener(this);

        return rootView;
    }

    int translateIdToIndex(int id) {
        int index = -1;
        switch (id) {
            case R.id.zad:
                index =10;
                break;
            case  R.id.pered:
                index=29;
                break;
            case  R.id.cep:
                index=56;
                break;
            case  R.id.tormoz:
                index=64;
                break;
            case  R.id.sis:
                index=110;
                break;
            case  R.id.ped:
                index=124;
                break;
            case  R.id.dinamo:
                index=136;
                break;
            case  R.id.tre:
                index=147;
                break;

        }
        return index;
    }

    @Override
    public void onClick(View v) {
        int buttonIndex = translateIdToIndex(v.getId());
        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
        listener.onButtonSelected(buttonIndex);
    }

    public interface OnSelectedButtonListener {
        void onButtonSelected(int buttonIndex);
    }

}
