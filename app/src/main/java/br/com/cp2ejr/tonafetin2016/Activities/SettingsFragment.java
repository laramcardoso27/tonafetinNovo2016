package br.com.cp2ejr.tonafetin2016.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.cp2ejr.tonafetin2016.R;

/**
 * Created by Lara on 04/07/2016.
 */
public class SettingsFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    //Call schedule_layout fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.settings_layout, container, false);



        return myView;
    }
}
