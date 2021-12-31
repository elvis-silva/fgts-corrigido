package com.elvis.fgtscorrigido.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableRow;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.elvis.fgtscorrigido.app.FGTSCorrigido;
import com.elvis.fgtscorrigido.app.R;
import com.elvis.fgtscorrigido.app.utils.IntentUtils;
/*import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;*/

import java.util.ArrayList;
import java.util.List;

public class MoreApps extends AbstractFragment {
    private Context context;

    //public AdView adView;

    private Integer[] thumbIntegers = {
            R.drawable.biblia, R.drawable.listadecompras, R.drawable.finances,
            R.drawable.embaixadinhas, R.drawable.gogojohnny,
            R.drawable.flightdrone, R.drawable.hexa, R.drawable.wp_polygon
    };

    private String[] appsNames = {
            "Mensagens Sagradas", "Lista de Compras - Planilha", "Financeirando",
            "Copa das Embaixadinhas", "Go! Go! Johnny", "Flight Drone",
            "Copa Rumo ao Hexa", "Wallpapers Polygon"
    };

    private String[] packages = {
            "com.elvis.biblia.msg", "com.elvis.shopping.list", "com.elvis.financeirando",
            "com.elvis.copadasembaixadinhas", "com.elvis.gogojohnny",
            "com.elvis.flightdrone", "com.elvis.coparumoaohexa.br",
            "com.elvis.wallpaper.polygon"
    };

    private int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_more, container, false);

        context = contentView.getContext();
        createView(contentView);
        return contentView;
    }

    public void createView (View _view) {
/*
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        final TableRow tabRowAdView = (TableRow) _view.findViewById(R.id.tabRowAdView);
        adView = new AdView(context.getApplicationContext());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(context.getString(R.string.admob_banner_id));
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (adView.getParent() == null) tabRowAdView.addView(adView);
            }
        });
*/
        YoYo.with(Techniques.BounceInRight).duration(1000).playOn(_view.findViewById(R.id.container_more));

        final List<AdapterRowView> adapterRowViews = new ArrayList<>();
        for(int i = 0; i < thumbIntegers.length; i++) {
            AdapterRowView adapterRowView = new AdapterRowView(thumbIntegers[i], appsNames[i], packages[i]);
            adapterRowViews.add(adapterRowView);
        }

        position = 0;

        //list view
        ListView listView = (ListView) _view.findViewById(R.id.gridView1);
        listView.setAdapter(new ArrayAdapterMore(context, adapterRowViews));//wallpaperThumbIntegers, wallpaperNames));//new ImageAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                AdapterRowView adapterRowView = adapterRowViews.get(position);
                IntentUtils.intentGooglePlay(adapterRowView.getPackageName());
            }
        });
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        if(adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onDestroy() {
        if(adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
*/
}
