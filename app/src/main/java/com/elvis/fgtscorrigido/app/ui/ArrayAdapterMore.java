package com.elvis.fgtscorrigido.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elvis.fgtscorrigido.app.R;

import java.util.List;

/**
 * Created by elvis on 04/01/16.
 */
public class ArrayAdapterMore extends ArrayAdapter<AdapterRowView> {
    private final Context context;
    private final List<AdapterRowView> adapterRowViews;

    //  private final String[] titles;
    //  private final Integer[] wallpaperThumbIntegers;
    /*
        public ArrayAdapterView(Context pContext, Integer[] pWallpaperThumbIntegers, String[] pTitles) {
            super(pContext, -1, pTitles);
            context = pContext;
            titles = pTitles;
            wallpaperThumbIntegers = pWallpaperThumbIntegers;
        }
    */
    public ArrayAdapterMore(Context pContext, List<AdapterRowView> pAdapterRowViews) {
        super(pContext, -1, pAdapterRowViews);
        context = pContext;
        adapterRowViews = pAdapterRowViews;
    }

    @Override
    public View getView(int pPosition, View pConvertView, ViewGroup pParent) {
        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        if(pConvertView == null) {
            pConvertView = inflater.inflate(R.layout.adapter_more, pParent, false);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) pConvertView.findViewById(R.id.adapter_more_thumbnail);
            holder.title = (TextView) pConvertView.findViewById(R.id.adapter_more_title);
            // holder.credits = (TextView) pConvertView.findViewById(R.id.credits);
            pConvertView.setTag(holder);
        } else {
            holder = (ViewHolder) pConvertView.getTag();
        }
        //View rowView = inflater.inflate(R.layout.adapter_layout, pParent, false);
        //TextView title = (TextView) pConvertView.findViewById(R.id.title);
        //ImageView thumbnail = (ImageView) pConvertView.findViewById(R.id.thumbnail);
        holder.title.setText(adapterRowViews.get(pPosition).getAppName());//titles[pPosition]);
        //  holder.credits.setText(adapterRowViews.get(pPosition).getPackageName());
        // change the icon for Windows and iPhone
        //String s = titles[position];
        //if (s.startsWith("iPhone")) {
        holder.thumbnail.setImageResource(adapterRowViews.get(pPosition).getDrawableRes());//wallpaperThumbIntegers[pPosition]);//R.drawable.thumb1);
        //} else {
        //    imageView.setImageResource(R.drawable.ok);
        //}

        return pConvertView;
    }

    // Store as a tag in the row’s view after inflating it
    private class ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        // public TextView credits;
    }
}