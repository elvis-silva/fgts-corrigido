package com.elvis.fgtscorrigido.app.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.elvis.fgtscorrigido.app.R;


public class FGTSDataRow extends TableRow {

    private String month;
    private String year;
    private String deposito;
    private String tr;
    private String inpc;
    private String dif;

    public FGTSDataRow(Context pContext, String pMonth, String pYear, String pDeposito, String pTr, String pInpc, String pDif, int pIndex) {
        super(pContext);

        setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        month = pMonth;
        year = pYear;
        deposito = pDeposito;
        tr = pTr;
        inpc = pInpc;
        dif = pDif;

        String indexToString = String.valueOf(pIndex);
        int indexLength = indexToString.length() - 1;
        char indexLastChar = indexToString.charAt(indexLength);
        int bgColor =   String.valueOf(indexLastChar).equals("0") || String.valueOf(indexLastChar).equals("2") ||
                String.valueOf(indexLastChar).equals("4") ||String.valueOf(indexLastChar).equals("6") ||
                String.valueOf(indexLastChar).equals("8") ? Color.argb(255,170,200,255) : Color.argb(255,140,170,255);

        Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);

        TextView monthTextView = new TextView(pContext);
        monthTextView.setBackgroundColor(bgColor);
        monthTextView.setText(month);
        monthTextView.setTypeface(bold);
        monthTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(monthTextView);
        TextView yearTextView = new TextView(pContext);
        yearTextView.setBackgroundColor(bgColor);
        yearTextView.setText(year);
        yearTextView.setTypeface(bold);
        yearTextView.setGravity(Gravity.RIGHT);
        yearTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(yearTextView);
        TextView depositoTextView = new TextView(pContext);
        depositoTextView.setBackgroundColor(bgColor);
        depositoTextView.setText(deposito);
        depositoTextView.setGravity(Gravity.RIGHT);
        depositoTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(depositoTextView);
        TextView trTextView = new TextView(pContext);
        trTextView.setBackgroundColor(bgColor);
        trTextView.setText(tr);
        trTextView.setGravity(Gravity.RIGHT);
        trTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(trTextView);
        TextView inpcTextView = new TextView(pContext);
        inpcTextView.setBackgroundColor(bgColor);
        inpcTextView.setText(inpc);
        inpcTextView.setGravity(Gravity.RIGHT);
        inpcTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(inpcTextView);
        TextView difTextView = new TextView(pContext);
        difTextView.setBackgroundColor(bgColor);
        difTextView.setText(dif);
        difTextView.setTypeface(bold);
        difTextView.setGravity(Gravity.RIGHT);
        difTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(difTextView);
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDeposito() {
        return deposito;
    }

    public String getTr() {
        return tr;
    }

    public String getInpc() {
        return inpc;
    }

    public String getDif() {
        return dif;
    }
}
