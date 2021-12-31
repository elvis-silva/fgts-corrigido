package com.elvis.fgtscorrigido.app.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elvis.fgtscorrigido.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elvis on 19/12/15.
 */
public class ArrayAdapterView extends RecyclerView.Adapter<ArrayAdapterView.ViewHolder> {
    private static final String TAG = "ArrayAdapterView";
    public ArrayList<String> yearsList;
//    private final Context context;
    private List<AdapterRowView> adapterRowViews;

    public ArrayAdapterView(Context pContext, List<AdapterRowView> pAdapterRowViews) {
        //super(pContext, -1, pAdapterRowViews);
//        context = pContext;
//        adapterRowViews = pAdapterRowViews;
    }

    public ArrayAdapterView(List<AdapterRowView> pAdapterRowViews) {
        adapterRowViews = pAdapterRowViews;
    }

    public void add(int position, AdapterRowView pAdapterRowView) {
        adapterRowViews.add(position, pAdapterRowView);
        notifyItemInserted(position);
    }

    public void remove(AdapterRowView pAdapterRowView) {
        int position = adapterRowViews.indexOf(pAdapterRowView);
        adapterRowViews.remove(position);
        notifyItemRemoved(position);
    }
/*
//    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
    //    ViewHolder holder;
    //    if(convertView == null) {
    //        holder = new ViewHolder();
            if(adapterRowViews.get(position).getType().equals(AdapterRowView.TYPE.HEADER)) {
                convertView = inflater.inflate(R.layout.adapter_header, parent, false);
                /*holder.title = *//*((TextView) convertView.findViewById(R.id.header_title)).
                        setText(adapterRowViews.get(position).getName());
            } else {
                convertView = inflater.inflate(R.layout.adapter_insert, parent, false);
*/                /*holder.title = *//*((TextView) convertView.findViewById(R.id.month)).
                        setText(adapterRowViews.get(position).getName());
                /*holder.value = *//*((TextView) convertView.findViewById(R.id.value)).
                        setText(adapterRowViews.get(position).getDeposito());
            }
    /*        convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(adapterRowViews.get(position).getName());
        if(holder.value != null) holder.value.setText(adapterRowViews.get(position).getDeposito());*/
/*        return convertView;
    }
*/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_insert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AdapterRowView adapterRowView = adapterRowViews.get(position);
        holder.title.setText(adapterRowView.getName());
        holder.value.setText(adapterRowView.getDeposito());
        ((View) holder.title.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((TextView) v.findViewById(R.id.month)).getText().toString().contains("Ano")) {
                    final CustomDialog customDialog = new CustomDialog(CustomDialog.TYPE.CHANGE_VALUE);
                    final String promptValue = ((TextView) v.findViewById(R.id.value)).getText().toString();
                    String title = ((TextView) v.findViewById(R.id.month)).getText().toString() +
                            " de " + yearsList.get(position);
                    customDialog.changeTitle(title);
                    customDialog.adapterRowView = adapterRowViews.get(position);
                    customDialog.arrayAdapterView = ArrayAdapterView.this;
                    customDialog.lastViewSelected = v.findViewById(R.id.value);

                    customDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            String valueToShow = promptValue.equals("0,00") ? "0" : promptValue;
                            customDialog.showInPrompt(valueToShow);
                        }
                    });
                    customDialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adapterRowViews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView value;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.month);
            value = (TextView) itemView.findViewById(R.id.value);
        }
    }
}
