package com.elvis.fgtscorrigido.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elvis.fgtscorrigido.app.R;

import java.util.ArrayList;

/**
 * Created by elvis on 23/06/16.
 */

public class About extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
