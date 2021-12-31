package com.elvis.fgtscorrigido.app.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elvis.fgtscorrigido.app.R;

import java.util.List;

/**
 * Created by elvis on 27/12/15.
 */
public class ArrayAdapterDetail extends RecyclerView.Adapter<ArrayAdapterDetail.ViewHolder> {
    private static String TAG = "ArrayAdapterDetail";
    private List<AdapterRowView> adapterRowViews;

    public ArrayAdapterDetail(List<AdapterRowView> pAdapterRowViews) {
        adapterRowViews = pAdapterRowViews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AdapterRowView adapterRowView = adapterRowViews.get(position);
        String monthText = adapterRowView.getName() + " / " + adapterRowView.getYear();
        holder.month.setText(monthText);
        holder.deposito.setText(adapterRowView.getDeposito());
        holder.tr.setText(adapterRowView.getCorrectTrValue());
        holder.inpc.setText(adapterRowView.getCorrectInpcValue());
        holder.perdas.setText(adapterRowView.getTotalPerdas());
    }

    @Override
    public int getItemCount() {
        return adapterRowViews.size();
    }

    public void add(int pPosition, AdapterRowView pAdapterRowView) {
        adapterRowViews.add(pPosition, pAdapterRowView);
        notifyItemInserted(pPosition);
    }

    public void remove(AdapterRowView pAdapterRowView) {
        int position = adapterRowViews.indexOf(pAdapterRowView);
        adapterRowViews.remove(pAdapterRowView);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView month, deposito, tr, inpc, perdas;

        public ViewHolder(View itemView) {
            super(itemView);
            month = (TextView) itemView.findViewById(R.id.dt_month);
            deposito = (TextView) itemView.findViewById(R.id.dt_deposito);
            tr = (TextView) itemView.findViewById(R.id.dt_tr);
            inpc = (TextView) itemView.findViewById(R.id.dt_inpc);
            perdas = (TextView) itemView.findViewById(R.id.dt_perdas);
        }
    }
}

