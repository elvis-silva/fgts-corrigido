package com.elvis.fgtscorrigido.app.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TitleDataRow extends TableRow {

    public TitleDataRow(Context pContext, String pMonth, String pYear, String pDeposito, String pTr, String pInpc, String pDif) {
        super(pContext);

        setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);

        TextView monthTextView = new TextView(pContext);
        monthTextView.setTypeface(bold);
        monthTextView.setBackgroundColor(Color.argb(255, 255, 200, 255));
        monthTextView.setText(pMonth);
        monthTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(monthTextView);
        TextView yearTextView = new TextView(pContext);
        yearTextView.setTypeface(bold);
        yearTextView.setBackgroundColor(Color.argb(255, 255, 200, 255));
        yearTextView.setText(pYear);
        yearTextView.setGravity(Gravity.RIGHT);
        yearTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(yearTextView);
        TextView depositoTextView = new TextView(pContext);
        depositoTextView.setTypeface(bold);
        depositoTextView.setBackgroundColor(Color.argb(255, 255, 200, 255));
        depositoTextView.setText(pDeposito);
        depositoTextView.setGravity(Gravity.RIGHT);
        depositoTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(depositoTextView);
        TextView trTextView = new TextView(pContext);
        trTextView.setTypeface(bold);
        trTextView.setBackgroundColor(Color.argb(255, 255, 200, 255));
        trTextView.setText(pTr);
        trTextView.setGravity(Gravity.RIGHT);
        trTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(trTextView);
        TextView inpcTextView = new TextView(pContext);
        inpcTextView.setTypeface(bold);
        inpcTextView.setBackgroundColor(Color.argb(255, 255, 200, 255));
        inpcTextView.setText(pInpc);
        inpcTextView.setGravity(Gravity.RIGHT);
        inpcTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(inpcTextView);
        TextView difTextView = new TextView(pContext);
        difTextView.setTypeface(bold);
        difTextView.setBackgroundColor(Color.argb(255, 255, 200, 255));
        difTextView.setText(pDif);
        difTextView.setGravity(Gravity.RIGHT);
        difTextView.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        addView(difTextView);
    }
}
