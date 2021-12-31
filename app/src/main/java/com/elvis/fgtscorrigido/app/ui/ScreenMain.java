package com.elvis.fgtscorrigido.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.elvis.fgtscorrigido.app.FGTSCorrigido;
import com.elvis.fgtscorrigido.app.R;
import com.elvis.fgtscorrigido.app.context.Contexts;
import com.elvis.fgtscorrigido.app.data.DataHandler;
import com.elvis.fgtscorrigido.app.impl.IActivity;
import com.elvis.fgtscorrigido.app.model.FGTSDataModel;
import com.elvis.fgtscorrigido.app.model.FGTSDataRow;
import com.elvis.fgtscorrigido.app.model.TitleDataRow;
import com.elvis.fgtscorrigido.app.utils.Calcule;
import com.elvis.fgtscorrigido.app.utils.DetailListHelper;
import com.elvis.fgtscorrigido.app.utils.GUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elvis on 16/06/15.
 */
public class ScreenMain implements IActivity, View.OnClickListener {

    private Context context;
    private View view;
    private TextView title;
    private LinearLayout container;
    private String nameToLoad = "Primeiro";
    private List<FGTSDataRow> dataRowList = new ArrayList<>();
    private DetailListHelper detailListHelper;

    @Override
    public void setContentView(View pView) {
        context = Contexts.getInstance().getContext();
        view = pView;

        container = (LinearLayout) findViewById(R.id.ccontainer);
//        findViewById(R.id.btnPlus).setOnClickListener(this);

        detailListHelper = new DetailListHelper();
        ListView listView = (ListView) findViewById(R.id.list_view);
        detailListHelper.setup(listView, "Primeiro");
        FGTSCorrigido.getInstance().registerForContextMenu(listView);

        reloadData();
    }

    @Override
    public View findViewById(int pViewId) {
        return view.findViewById(pViewId);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public View rootView() {
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void setMode(int pMode) {

    }

    @Override
    public void reloadData() {

        GUI.doBusy(Contexts.getInstance().getContext(), new GUI.BusyAdapter() {
            List<FGTSDataModel> detailList = null;

//            double expense;
//            double income;
//            int count;

            @Override
            public void run() {
                detailList = FGTSCorrigido.dataHandler.getDataModel("Primeiro");
//                count = dataProvider.countDetail(dateStart, dateEnd);
//                income = dataProvider.sumCategory(AccountType.INCOME, dateStart, dateEnd);
//                expense = dataProvider.sumSubcategory(AccountType.EXPENSE, dateStart, dateEnd);
            }

            @Override
            public void onBusyFinish() {
//                unknowView.setVisibility(TextView.GONE);
                detailListHelper.reloadData(detailList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        /*
        switch (v.getId()) {
            case R.id.btnPlus:
//                buildData();
                reloadData();
                break;
        }
        */
    }

    private void buildData() {
        loadData();
    }

    private void loadData() {
        executeProgress();
    }

    private void addLoadData() {
        dataRowList.clear();

        Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);

        title = new TextView(context);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setTypeface(bold);
        title.setBackgroundColor(Color.argb(255, 200, 225, 180));
        title.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        title.setText("TITLE SET");

//        TextView title = buildTitle();
//        titlesContainer.addView(title);
//        TitleDataRow titleDataRow = new TitleDataRow(context, "", "", "", "Correção", "Correção", "");
//        titlesContainer.addView(titleDataRow);
//        FGTSCorrigido.getInstance().titlesContainer.add(titleDataRow);
//        titleDataRow = new TitleDataRow(context, "Mês", "Ano", "Depósito", "pela TR", "pelo INPC", "Diferença");
//        titlesContainer.addView(titleDataRow);
//        FGTSCorrigido.getInstance().titlesContainer.add(titleDataRow);

//        float totalDeposito = 0f;
//        float totalTr = 0f;
//        float totalInpc = 0f;
//        float totalDif = 0f;

        DataHandler dataHandler = FGTSCorrigido.dataHandler;
        Cursor cursor = dataHandler.returnFGTSData();
        cursor.moveToFirst();
        int i = 0;
        while (i < cursor.getCount()) {
            FGTSDataModel fgtsDataModel = dataHandler.parseFGTSData(cursor);
            if(fgtsDataModel.getName().equals(nameToLoad)) {
                FGTSDataRow dataRow = new FGTSDataRow(context, fgtsDataModel.getMonth(),
                        fgtsDataModel.getYear(), fgtsDataModel.getDeposito(), fgtsDataModel.getTr(),
                        fgtsDataModel.getInpc(), fgtsDataModel.getDiference(), i);
                dataRowList.add(dataRow);
//                FGTSCorrigido.getInstance().container.add(dataRow);
//                totalDeposito += Float.valueOf(fgtsDataModel.getDeposito().replace(".", "").replace(",", "."));
//                totalTr += Float.valueOf(fgtsDataModel.getTr().replace(".", "").replace(",", "."));
//                totalInpc += Float.valueOf(fgtsDataModel.getInpc().replace(".", "").replace(",", "."));
//                totalDif += Float.valueOf(fgtsDataModel.getDiference().replace(".", "").replace(",", "."));
            }
            cursor.moveToNext();
            i++;
        }
        cursor.close();

//        String initialMonth = ((FGTSDataRow) container.getChildAt(0)).getMonth();
//        String initialYear = ((FGTSDataRow) container.getChildAt(0)).getYear();
//        String finalMonth = ((FGTSDataRow) container.getChildAt(container.getChildCount() - 1)).getMonth();
//        String finalYear = ((FGTSDataRow) container.getChildAt(container.getChildCount() - 1)).getYear();
//
//        title.setText("Cálculo de " + nameSave + " - Período: " + initialMonth + "/" + initialYear + " á " + finalMonth + "/" + finalYear);
//        FGTSCorrigido.getInstance().titles.add(title);
//
//        String totalDepositosString = Calcule.moneyFormat(totalDeposito);
//        String totalTrString = Calcule.moneyFormat(totalTr);
//        String totalInpsString = Calcule.moneyFormat(totalInpc);
//        String totalDifString = Calcule.moneyFormat(totalDif);
//        titleDataRow = new TitleDataRow(context, "", "TOTAIS:", totalDepositosString, totalTrString, totalInpsString, totalDifString);
//        totalsContainer.addView(titleDataRow);
//        FGTSCorrigido.getInstance().totalsContainer.add(titleDataRow);
//        btnCalcule.setText(CALCULATE);
//
//        containers.clear();
//        containers.add(titlesContainer);
//        containers.add(container);
//        containers.add(totalsContainer);
    }

    private void buildDataLoaded() {
        if(container.getChildCount() > 0) {
            container.removeAllViews();
            return;
        }

        container.addView(title);



        for (int i = 0; i < dataRowList.size(); i++) {
            container.addView(dataRowList.get(i));
        }
    }

    public void executeProgress() {
        new DoInBackground().execute();
    }

    public class DoInBackground extends AsyncTask<Void, Void, Void>
            implements DialogInterface.OnCancelListener {
        private ProgressDialog dialog;

        protected void onPreExecute() {
            CharSequence message = /*state.equals(STATE.CALCULATE) ? "Calculando. Por favor aguarde..." :
                    state.equals(STATE.PERIOD_ADDED) ? "Criando período. Por favor aguarde..." :
                            state.equals(STATE.SAVING) ? "Salvando. Por favor aguarde..." :
                                    state.equals(STATE.LOAD_DATA) ?*/ "Carregando " + nameToLoad + ". Por favor aguarde..."/* :
                                            "Removendo " + nameSave + ". Por favor aguarde..."*/;

            dialog = ProgressDialog.show(FGTSCorrigido.getInstance(), "", message, true);
        }

        protected Void doInBackground(Void... unused) {
//            if( state.equals(STATE.CALCULATE)) {
//                FGTSCorrigido.getInstance().getFgtsPeriodos().calcule();
//            } else if (state.equals(STATE.PERIOD_ADDED)) {
//                FGTSCorrigido.getInstance().getFgtsPeriodos().addPeriod();
//            } else if (state.equals(STATE.DELETE_DATA)) {
//                FGTSCorrigido.getInstance().getFgtsPeriodos().buildDataDeleted();
//            } else if (state.equals(STATE.SAVING)) {
//                FGTSCorrigido.getInstance().getFgtsPeriodos().addNameData();
//            } else {
                addLoadData();
//            }
            return null;
        }

        protected void onPostExecute(Void unused) {
//            if( state.equals(STATE.CALCULATE)) {
//                FGTSCorrigido.getInstance().getFgtsPeriodos().buildData();
//            } else if (state.equals(STATE.PERIOD_ADDED)) {
//                FGTSCorrigido.getInstance().getFgtsPeriodos().buildPeriod();
//            } else if (state.equals(STATE.SAVING)) {
//                FGTSCorrigido.getInstance().getFgtsPeriodos().buildSaveData();
//            } else if (state.equals(STATE.LOAD_DATA)) {
//                FGTSCorrigido.getInstance().getFgtsPeriodos().
                    buildDataLoaded();
//            } else {
//                Toast.makeText(context, "Cálculo de " + nameSave + " foi removido.", Toast.LENGTH_LONG).show();
//            }
            dialog.dismiss();
        }

        public void onCancel(DialogInterface dialog) {
            cancel(true);
            dialog.dismiss();
        }
    }
}
