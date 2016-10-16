package br.com.cp2ejr.tonafetin2016.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.cp2ejr.tonafetin2016.R;

/**
 * Created by William on 26/09/2016.
 */
public class AboutFragment extends Fragment {

    View view;

    @Nullable
    @Override
//Call schedule_layout fragment

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.about_layout, container, false);
        return view;
    }
}
