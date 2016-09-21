package br.com.cp2ejr.tonafetin2016.Activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import br.com.cp2ejr.tonafetin2016.R;

/**
 * Created by Lara on 04/07/2016.
 */
public class MainFragment extends Fragment {

    View myView;


    @Nullable
    @Override
    //Call vote_layout fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.content_main, container, false);
        return myView;
    }

}
