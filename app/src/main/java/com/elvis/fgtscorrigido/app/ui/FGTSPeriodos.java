package com.elvis.fgtscorrigido.app.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.elvis.fgtscorrigido.app.AppManager;
import com.elvis.fgtscorrigido.app.FGTSCorrigido;
import com.elvis.fgtscorrigido.app.MainActivity;
import com.elvis.fgtscorrigido.app.context.Contexts;
import com.elvis.fgtscorrigido.app.utils.GUI;
import com.elvis.fgtscorrigido.app.utils.Indice;
import com.elvis.fgtscorrigido.app.R;
import com.elvis.fgtscorrigido.app.data.DataHandler;
import com.elvis.fgtscorrigido.app.model.FGTSDataModel;
import com.elvis.fgtscorrigido.app.model.FGTSDataRow;
import com.elvis.fgtscorrigido.app.model.TitleDataRow;
import com.elvis.fgtscorrigido.app.utils.Calcule;
//import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class FGTSPeriodos extends AbstractFragment implements View.OnClickListener {
    private static final String TAG = "FGTSPeriodos";

//    public AdView adView;

    public String currentyActivity = "FGTSPeriodos";

    private String[] monthsList = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    private final String SAVE = "Salvar";
    private final String CALCULATE = "Calcular";
    private Context context;
    private TableLayout tableLayout;
    private TextView textViewResume, nameSet;
    private int step = 0;
    public String firstMonth = "";
    public String firstYear = "";
    public String lastMonth = "";
    public String lastYear = "";
    public Button btnCalcule, btnGetSave, btnAddPeriod, btnClean;
    private int btnHeight = 0;
    private float btnTextSize = 0;
    private String message = "";
    private String messageSaved = "";
    private float totalINPCResume = 0;
    private float totalTRResume = 0;
    private float totalDifResume = 0;
    private float difValue = 0;
    private int cols = 3;
    public boolean dadosSaved = false;
    public boolean periodAdded = false;
    private boolean calculated = false;
    private boolean periodVisible = false;
    private EditText currentField;
    public LinearLayout container, titlesContainer, totalsContainer;
    public ArrayList<View> containers = new ArrayList<>();
    private ArrayList<String> currentMonths     = new ArrayList<>();
    private ArrayList<String> currentYears      = new ArrayList<>();
    private ArrayList<String> currentValues     = new ArrayList<>();
    private ArrayList<Float> currentInpcIndices = new ArrayList<>();
    private ArrayList<Float> currentTrIndices   = new ArrayList<>();
    private ArrayList<Float> currentDepositos   = new ArrayList<>();
    public ArrayList<View> buttons = new ArrayList<>();
    private ArrayList<EditText> valuesList;
    private ArrayList<String> monthsYearList = new ArrayList<>();
    private ArrayList<Indice> indicesINPC, indicesTR;
    public ArrayList<String> yearsList = new ArrayList<>();
    public int progress = 0;
//    public String nameSave;
    public TableRow btnContainer, tableRowTitleContainer, tableRowTotalsContainer;
    public ScrollView tableLayoutContainer, scrollView;
    private RecyclerView listViewContainer;
    private List<AdapterRowView> adapterRowViews = new ArrayList<>();
    private List<AdapterRowView> adapterRowDetails = new ArrayList<>();
    private ArrayAdapterView arrayAdapterView;
    private ArrayAdapterDetail arrayAdapterDetail;
    private LinearLayout revealView;
    boolean hidden = true;
    private ImageButton btnDone, btnClear;
    private String firstPeriod, lastPeriod;
    private Map<String, AdapterRowView> mapMonthsList = new HashMap<>();

    public STATE state = AppManager.CURRENT_STATE;
    private boolean restored;

    public enum STATE {
        NONE,
        CALCULATE,
        SAVING,
        PERIOD_ADDED,
        LOAD_DATA,
        LOADING_DATA,
        DATA_READY, SAVING_PROGRESS, DELETE_DATA
    }
/*
    public FGTSPeriodos () {
        defineBtnsSizes();
        indicesINPC = FGTSCorrigido.getInstance().indicesINPC;
        indicesTR = FGTSCorrigido.getInstance().indicesTR;
    }
*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_fgts_periodos, container, false);
        context = contentView.getContext();
        createView(contentView);
        return contentView;
    }

    private void defineBtnsSizes() {
        btnHeight = 20;
        btnTextSize = 18;
    }

    public void setCols (int colsQuant) {
        cols = colsQuant;
    }

    @Override
    public void setState(STATE pState) {
        state = pState;
        AppManager.CURRENT_STATE = state;
        if(state.equals(STATE.SAVING_PROGRESS) || state.equals(STATE.LOADING_DATA) ||
                state.equals(STATE.DELETE_DATA)) {
                executeProgress();
        }
    }

    public View createView (View _view) {
        //context = _view.getContext();

//        FGTSCorrigido.getInstance().showInterstitialControl = 0;

        textViewResume = (TextView) _view.findViewById(R.id.textViewResume);
        tableLayout = (TableLayout) _view.findViewById(R.id.tableLayout);
        titlesContainer = (LinearLayout) _view.findViewById(R.id.titlesContainer);
        totalsContainer = (LinearLayout) _view.findViewById(R.id.totalsContainer);
        container = (LinearLayout) _view.findViewById(R.id.dataContainer);
        btnContainer = (TableRow) _view.findViewById(R.id.tableRowButton);
        tableLayoutContainer = (ScrollView) _view.findViewById(R.id.tableLayoutContainer);
        tableRowTitleContainer = (TableRow) _view.findViewById(R.id.tableRowTitleContainer);
        scrollView = (ScrollView) _view.findViewById(R.id.scrollView);
        tableRowTotalsContainer = (TableRow) _view.findViewById(R.id.tableRowTotalsContainer);
        nameSet = (TextView) _view.findViewById(R.id.fragmentNameSet);
        nameSet.setText("");
        listViewContainer = (RecyclerView) _view.findViewById(R.id.listViewContainer);
        arrayAdapterView = new ArrayAdapterView(adapterRowViews);
        arrayAdapterDetail = new ArrayAdapterDetail(adapterRowDetails);
        listViewContainer.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Contexts.getInstance().getContext());
        listViewContainer.setLayoutManager(linearLayoutManager);
        listViewContainer.setAdapter(arrayAdapterDetail);
        listViewContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDist = 0;
            boolean isVisible = true;
            static final float MINIMUM = 25;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(state.equals(STATE.NONE)) {
                    if (isVisible && scrollDist > MINIMUM) {
                        MainActivity.getInstance().hideFab();
                        scrollDist = 0;
                        isVisible = false;
                    } else if (!isVisible && scrollDist < -MINIMUM) {
                        MainActivity.getInstance().showFab();
                        scrollDist = 0;
                        isVisible = true;
                    }
                    if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
                        scrollDist += dy;
                    }
                }
            }
        });

        btnDone = (ImageButton) _view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals(STATE.DATA_READY)) {
                    state = STATE.SAVING;
                    AppManager.CURRENT_STATE = state;
                    CustomDialog customDialog = new CustomDialog(CustomDialog.TYPE.SET_NAME);
                    customDialog.show();
                } else {
                    verifyTotal();
                }
            }
        });

        btnClear = (ImageButton) _view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearScreen();
                handleBtns();
            }
        });

        revealView = (LinearLayout) _view.findViewById(R.id.reveal_items);
        revealView.setVisibility(AppManager.REVEL_VISIBILITY);

        switch (AppManager.CURRENT_STATE) {
            case NONE:
                restoreDataNone();
                break;
            case PERIOD_ADDED:
                restorePeriodAdded();
                break;
            case DATA_READY:
                restoreDataReady();
                break;
        }

        //listViewContainer.setOnItemClickListener(new MyOnItemClickListener());
/*
        btnGetSave = (Button) _view.findViewById(R.id.btnGetSaves);
        btnCalcule = (Button) _view.findViewById(R.id.btnCalcule);
        btnAddPeriod = (Button) _view.findViewById(R.id.btnAddPeriod);
        btnClear = (Button) _view.findViewById(R.id.btnClear);

        btnGetSave.setOnClickListener(this);
        btnCalcule.setOnClickListener(this);
        btnAddPeriod.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        buttons.add(btnGetSave);
        buttons.add(btnCalcule);
        buttons.add(btnAddPeriod);
        buttons.add(btnClear);
*/
/*
        if(FGTSCorrigido.getInstance().currentState.equals(FGTSCorrigido.STATE.READY)) {
            showReadyView();
            showBtns(null, "save", null, 3);
        } else if (FGTSCorrigido.getInstance().currentState.equals(FGTSCorrigido.STATE.VALUES_INSERT)) {
            showValuesInsertView();
            showBtns(null, "calc", null, 3);
        } else if (FGTSCorrigido.getInstance().currentState.equals(FGTSCorrigido.STATE.DATA_SAVED)) {
            showDataSavedView();
            showBtns(null, null, null, 3);
        } else {
            showBtnInitScreen();
        }
*/
        return _view;
    }

    private void restoreDataNone() {
        state = STATE.NONE;
        textViewResume.setText(AppManager.VIEW_RESUME_TEXT);
        nameSet.setText(AppManager.NAME_SET_TEXT);
        if(AppManager.ARRAY_ADAPTER_DETAIL != null) {
            adapterRowDetails = AppManager.ADAPTER_ROW_DETAILS;
            arrayAdapterDetail = AppManager.ARRAY_ADAPTER_DETAIL;

            if(!listViewContainer.getAdapter().equals(arrayAdapterDetail)) {
                listViewContainer.setAdapter(arrayAdapterDetail);
            }
            arrayAdapterDetail.notifyDataSetChanged();
        }
    }

    private void restoreDataReady() {
        state = STATE.DATA_READY;

        textViewResume.setText(AppManager.VIEW_RESUME_TEXT);

        adapterRowDetails = AppManager.ADAPTER_ROW_DETAILS;
        arrayAdapterDetail = AppManager.ARRAY_ADAPTER_DETAIL;

        if(!listViewContainer.getAdapter().equals(arrayAdapterDetail)) {
            listViewContainer.setAdapter(arrayAdapterDetail);
        }

        arrayAdapterDetail.notifyDataSetChanged();

        hidden = AppManager.HIDDEN_REVEL_VIEW;
        monthsYearList = AppManager.MONTHS_YEAR_LIST;
        mapMonthsList = AppManager.MAP_MONTHS_LIST;

        btnDone.setBackgroundResource(R.drawable.btn_save);
    }

    private void restorePeriodAdded() {
        state = STATE.PERIOD_ADDED;

        textViewResume.setText(AppManager.VIEW_RESUME_TEXT);
        firstMonth = AppManager.FIRST_MONTH;
        firstYear = AppManager.FIRST_YEAR;
        lastMonth = AppManager.LAST_MONTH;
        lastYear = AppManager.LAST_YEAR;

        yearsList.clear();
        monthsYearList.clear();
        mapMonthsList.clear();
        currentMonths.clear();
        adapterRowViews.clear();

        hidden = AppManager.HIDDEN_REVEL_VIEW;
        firstPeriod = AppManager.FIRST_PERIOD;
        lastPeriod = AppManager.LAST_PERIOD;
        monthsYearList = AppManager.MONTHS_YEAR_LIST;
        currentMonths = AppManager.CURRENT_MONTHS;
        mapMonthsList = AppManager.MAP_MONTHS_LIST;
        adapterRowViews = AppManager.ADAPTER_ROW_VIEWS;
        arrayAdapterView = AppManager.ARRAY_ADAPTER_VIEW;
        arrayAdapterView.yearsList = AppManager.YEARS_LIST;

        if(!listViewContainer.getAdapter().equals(arrayAdapterView)) {
            listViewContainer.setAdapter(arrayAdapterView);
        }

        arrayAdapterView.notifyDataSetChanged();
    }

    @Override
    public void clearScreen() {
/*        if(!state.equals(STATE.DATA_READY)) {
            handleBtns();
        }*/
        state = STATE.NONE;
        textViewResume.setText("");
        nameSet.setText("");
        adapterRowViews.clear();
        adapterRowDetails.clear();
        arrayAdapterView.notifyDataSetChanged();
        arrayAdapterDetail.notifyDataSetChanged();
        AppManager.CURRENT_STATE = state;
        AppManager.VIEW_RESUME_TEXT = "";
        AppManager.NAME_SET = "";
        AppManager.ADAPTER_ROW_VIEWS = adapterRowViews;
        AppManager.ADAPTER_ROW_DETAILS = adapterRowDetails;
        AppManager.ARRAY_ADAPTER_VIEW = arrayAdapterView;
        AppManager.ARRAY_ADAPTER_DETAIL = arrayAdapterDetail;
    }

    @Override
    public void handleBtns() {
        if(state.equals(STATE.DELETE_DATA)) {
            state = STATE.NONE;
            AppManager.CURRENT_STATE = state;
            GUI.toastLong("Cálculo de " + AppManager.NAME_SET + " apagado!");
            if(Contexts.getInstance().getDataHandler().returnNamesData().getCount() == 0) {
                MainActivity.getInstance().handleClick();
            }
            return;
        }
        if(state.equals(STATE.LOADING_DATA)) {
            state = STATE.NONE;
            AppManager.CURRENT_STATE = state;
            MainActivity.getInstance().showInitBtns(0, 0, 0, 0);
        } else {
            FloatingActionButton fab = MainActivity.getInstance().fabMain;
            int px;
            int py;
            if (state.equals(STATE.DATA_READY)) {
                px = revealView.getWidth() - (btnDone.getLeft() + btnDone.getRight()) / 2;
                py = (revealView.getTop() + revealView.getBottom()) / 2;
            } else {
                    px = (fab.getLeft() + fab.getRight()) / 2;
                    py = revealView.getTop();
            }

            int radius = Math.max(revealView.getWidth(), revealView.getHeight());

            if(state.equals(STATE.PERIOD_ADDED)) {
                MainActivity.getInstance().showInitBtns(px, py, 0, radius);
            } else {
                handleRevelView(px, py, 0, radius);
            }
        }
    }

    @Override
    public void handleRevelView (int pPx, int pPy, int pStartRadius, int pEndRadius) {

        if(pPx == 0 && pPy == 0 && pEndRadius == 0 && pStartRadius == 0) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            FloatingActionButton fab = MainActivity.getInstance().fabMain;
            SupportAnimator animator =
                    ViewAnimationUtils.createCircularReveal(revealView, pPx, pPy, pStartRadius, pEndRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(350);

            SupportAnimator animator_reverse = animator.reverse();

            if (hidden) {
                btnDone.setClickable(true);
                btnClear.setClickable(true);
                revealView.setVisibility(View.VISIBLE);
                animator.start();
                hidden = false;
                AppManager.REVEL_VISIBILITY = View.VISIBLE;
                AppManager.HIDDEN_REVEL_VIEW = false;
                if (fab.getVisibility() == FloatingActionButton.VISIBLE) fab.hide();
            } else {
                btnDone.setClickable(false);
                btnClear.setClickable(false);
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        revealView.setVisibility(View.INVISIBLE);
                        AppManager.REVEL_VISIBILITY = View.INVISIBLE;
                        hidden = true;
                        AppManager.HIDDEN_REVEL_VIEW = true;
                        if (state.equals(STATE.DATA_READY)) {
                            btnDone.setBackgroundResource(R.drawable.btn_save);
                            handleBtns();
                        } else {
                            MainActivity.getInstance().showFab();
                            btnDone.setBackgroundResource(R.drawable.btn_done);
                        }
//                        calculeData();
                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });
                animator_reverse.start();
            }
        } else {
            if (hidden) {
                btnDone.setClickable(true);
                btnClear.setClickable(true);
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(
                        revealView, pPx, pPy, pStartRadius, pEndRadius);
                anim.setDuration(350);
                revealView.setVisibility(View.VISIBLE);
                AppManager.REVEL_VISIBILITY = View.VISIBLE;
                anim.start();
                hidden = false;
                AppManager.HIDDEN_REVEL_VIEW = false;
                //if(fabMain.getVisibility() == FloatingActionButton.VISIBLE) fabMain.hide();
            } else {
                btnDone.setClickable(false);
                btnClear.setClickable(false);
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(
                        revealView, pPx, pPy, pEndRadius, pStartRadius);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        revealView.setVisibility(View.INVISIBLE);
                        AppManager.REVEL_VISIBILITY = View.INVISIBLE;
                        hidden = true;
                        AppManager.HIDDEN_REVEL_VIEW = true;

                        if (state.equals(STATE.DATA_READY)) {
                            btnDone.setBackgroundResource(R.drawable.btn_save);
                            handleBtns();
                        } else {
                            MainActivity.getInstance().showFab();
                            btnDone.setBackgroundResource(R.drawable.btn_done);
                        }
                    }
                });
                anim.setDuration(350);
                anim.start();
            }
        }
    }

    private void showSaveBtn() {
//        FloatingActionButton fabMain = MainActivity.getInstance().fabMain;
        btnDone.setBackgroundResource(R.drawable.btn_save);
        int px = revealView.getWidth() - (btnDone.getLeft() + btnDone.getRight())/2;
        int py = (revealView.getTop() + revealView.getBottom())/2;

        int radius = Math.max(revealView.getWidth(), revealView.getHeight());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            SupportAnimator animator =
                    ViewAnimationUtils.createCircularReveal(revealView, px, py, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(350);

            SupportAnimator animator_reverse = animator.reverse();

            if (hidden) {
                btnDone.setClickable(true);
                btnClear.setClickable(true);
                revealView.setVisibility(View.VISIBLE);
                AppManager.REVEL_VISIBILITY = View.VISIBLE;
                animator.start();
                hidden = false;
            } else {
                btnDone.setClickable(false);
                btnClear.setClickable(false);
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        revealView.setVisibility(View.INVISIBLE);
                        AppManager.REVEL_VISIBILITY = View.INVISIBLE;
                        hidden = true;
                        MainActivity.getInstance().showFab();
                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });
                animator_reverse.start();
            }
        } else {
            if (hidden) {
                btnDone.setClickable(true);
                btnClear.setClickable(true);
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(
                        revealView, px, py, 0, radius);
                anim.setDuration(350);
                revealView.setVisibility(View.VISIBLE);
                AppManager.REVEL_VISIBILITY = View.VISIBLE;
                anim.start();
                hidden = false;
            } else {
                btnDone.setClickable(false);
                btnClear.setClickable(false);
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(
                        revealView, px, py, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        revealView.setVisibility(View.INVISIBLE);
                        AppManager.REVEL_VISIBILITY = View.INVISIBLE;
                        hidden = true;
                        MainActivity.getInstance().showFab();
                    }
                });
                anim.setDuration(350);
                anim.start();
            }
        }
    }

    @Override
    public void createPeriodo(String pMonthInitial, String pYearInitial, String pMonthFinal, String pYearFinal) {
/*        Log.i(TAG, "calcular de " + pMonthInitial + " de " + pYearInitial + " á " +
                pMonthFinal + " de " + pYearFinal
        );*/
        nameSet.setText("");

        textViewResume.setText(R.string.preencha_os_campos_abaixo);

        firstMonth = pMonthInitial;
        firstYear = pYearInitial;
        lastMonth = pMonthFinal;
        lastYear = pYearFinal;

        firstPeriod = pMonthInitial.substring(0, 3).toLowerCase() + pYearInitial;
        lastPeriod = pMonthFinal.substring(0, 3).toLowerCase() + pYearFinal;

        yearsList.clear();
        monthsYearList.clear();
        mapMonthsList.clear();
        currentMonths.clear();
        adapterRowViews.clear();

        if(!listViewContainer.getAdapter().equals(arrayAdapterView)) {
            listViewContainer.setAdapter(arrayAdapterView);
        }

        int currentMonthIndex = 0;
        while (currentMonthIndex < AppManager.MONTHS.length) {
            if(pMonthInitial.equals(AppManager.MONTHS[currentMonthIndex])) {
                break;
            }
            currentMonthIndex++;
        }

        String currentMonth;
        String lastMonthYear = pMonthFinal + pYearFinal;

        int currentYear = Float.valueOf(pYearInitial).intValue();
        int lastYearInt = Float.valueOf(pYearFinal).intValue();
        while(currentYear <= lastYearInt) {
            AdapterRowView adapterRowView = new AdapterRowView("Ano " + String.valueOf(currentYear), "Valor (R$)");
            adapterRowViews.add(adapterRowView);
            yearsList.add(String.valueOf(currentYear));
            while (currentMonthIndex < 12) {
                yearsList.add(String.valueOf(currentYear));
                currentMonth = AppManager.MONTHS[currentMonthIndex];

                String currentYearString = String.valueOf(currentYear);
                String currentMonthYear = currentMonth.substring(0, 3).toLowerCase() + currentYearString;
                monthsYearList.add(currentMonthYear);
                currentMonths.add(currentMonth);

                adapterRowView = new AdapterRowView(currentMonth, currentYearString, "0,00", "", "", "", "", "");
                adapterRowViews.add(adapterRowView);

                mapMonthsList.put(currentMonthYear, adapterRowView);

                currentMonthIndex++;
                currentMonthIndex = currentMonthIndex == 12 ? 0 : currentMonthIndex;

                if(lastMonthYear.equals(String.valueOf(currentMonth + currentYear))) break;
                if(currentMonthIndex == 0) break;
            }
            currentYear += 1;
        }
        arrayAdapterView.yearsList = yearsList;
        arrayAdapterView.notifyDataSetChanged();

        state = STATE.PERIOD_ADDED;

        AppManager.CURRENT_STATE = state;
        AppManager.VIEW_RESUME_TEXT = textViewResume.getText().toString();
        AppManager.FIRST_MONTH = firstMonth;
        AppManager.FIRST_YEAR = firstYear;
        AppManager.LAST_MONTH = lastMonth;
        AppManager.LAST_YEAR = lastYear;
        AppManager.FIRST_PERIOD = firstPeriod;
        AppManager.LAST_PERIOD = lastPeriod;
        AppManager.ADAPTER_ROW_VIEWS = adapterRowViews;
        AppManager.YEARS_LIST = yearsList;
        AppManager.MONTHS_YEAR_LIST = monthsYearList;
        AppManager.CURRENT_MONTHS = currentMonths;
        AppManager.MAP_MONTHS_LIST = mapMonthsList;
        AppManager.ARRAY_ADAPTER_VIEW = arrayAdapterView;
    }

    private void verifyTotal() {
        float totalValue = 0;
        int i = 0;
        while (i < adapterRowViews.size()) {
            totalValue += Calcule.getFloat(adapterRowViews.get(i).getDeposito());
            i++;
        }
        if(totalValue == 0) {
            GUI.toastLong("Não há valores para calcular!");
            return;
        }
        if(AppManager.INTERSTITIAL_CONTROL == 0) {
            MainActivity.getInstance().showInterstitialAd(null);
        } else {
            int intControl = AppManager.INTERSTITIAL_CONTROL;
            intControl++;
            AppManager.INTERSTITIAL_CONTROL = intControl == 3 ? 0 : intControl;
            startCalcule();
        }
    }

    @Override
    public void startCalcule() {
        state = STATE.CALCULATE;
        AppManager.CURRENT_STATE = state;
        executeProgress();
    }

    private void calculeData() {
        Log.i(TAG, "calculateData");

        message = "";

        boolean firstMonthAdded = false;

        currentInpcIndices.clear();
        currentTrIndices.clear();
        int i = 0;
        int j = 0;
        while (i < AppManager.INPC.size()) {
            if (firstMonthAdded) {
                currentInpcIndices.add(Float.valueOf(AppManager.INPC.get(i).value));
                mapMonthsList.get(AppManager.INPC.get(i).name).setIndiceINPC(AppManager.INPC.get(i).value);
            } else if (AppManager.INPC.get(i).name.equals(firstPeriod)) {
                currentInpcIndices.add(Float.valueOf(AppManager.INPC.get(i).value));
                mapMonthsList.get(AppManager.INPC.get(i).name).setIndiceINPC(AppManager.INPC.get(i).value);
                j = i;
                firstMonthAdded = true;
            }
            if (AppManager.INPC.get(i).name.equals(lastPeriod)) {
                if (i != j) {
                    currentInpcIndices.add(Float.valueOf(AppManager.INPC.get(i).value));
                    mapMonthsList.get(AppManager.INPC.get(i).name).setIndiceINPC(AppManager.INPC.get(i).value);
                }
                firstMonthAdded = false;
            }
            i++;
        }

        String key;
        i = 0;
        while (i < AppManager.TR.size()) {
            if (firstMonthAdded) {
                currentTrIndices.add(Float.valueOf(AppManager.TR.get(i).value));
                mapMonthsList.get(AppManager.TR.get(i).name).setIndiceTR(AppManager.TR.get(i).value);
            } else if (AppManager.TR.get(i).name.equals(firstPeriod)) {
                currentTrIndices.add(Float.valueOf(AppManager.TR.get(i).value));
                mapMonthsList.get(AppManager.TR.get(i).name).setIndiceTR(AppManager.TR.get(i).value);
                j = i;
                firstMonthAdded = true;
            }
            if (AppManager.TR.get(i).name.equals(lastPeriod)) {
                if (i != j) {
                    currentTrIndices.add(Float.valueOf(AppManager.TR.get(i).value));
                    mapMonthsList.get(AppManager.TR.get(i).name).setIndiceTR(AppManager.TR.get(i).value);
                }
                firstMonthAdded = false;
            }
            i++;
        }

        currentDepositos.clear();
        currentValues.clear();
        float currentDepositoValue;
        float totalINPCValue = 0;
        float totalTRValue = 0;
        difValue = 0;
        i = 0;
        while (i < monthsYearList.size()) {
            float juros = 0;
            key = monthsYearList.get(i);
            AdapterRowView currentMonthMap = mapMonthsList.get(key);
            String currentValue = String.valueOf(currentMonthMap.getDeposito()).replace(",",".");
            float value = currentValue.equals("") ? 0 : Float.valueOf(String.valueOf(currentValue));

            currentDepositos.add(value);
            currentValues.add(Calcule.moneyFormat(value));

            float correctTrValue = 0;
            float correctInpcValue = 0;
            j = i;
            while (j < monthsYearList.size()) {
                float currentTrIndex = currentTrIndices.get(j) <= 0 ? 0 : currentTrIndices.get(j) * 0.01f;
                float currentInpcIndex = currentInpcIndices.get(j) <= 0 ? 0 : currentInpcIndices.get(j) * 0.01f;

                currentDepositoValue = currentDepositos.get(i);
                if (j >= i && currentDepositoValue != 0) {
                    String currentMonth = currentMonths.get(j).toLowerCase();
                    juros += 0.0025f;
                    correctTrValue = j == i ? value + value * currentTrIndex :
                            correctTrValue + correctTrValue * currentTrIndex;
                    correctInpcValue = j == i ? value + value * currentInpcIndex :
                            correctInpcValue + correctInpcValue * currentInpcIndex;
                    correctTrValue = currentMonth.equals("dezembro") || j == monthsYearList.size() - 1 ?
                            correctTrValue + correctTrValue * juros : correctTrValue;
                    correctInpcValue = currentMonth.equals("dezembro") || j == monthsYearList.size() - 1 ?
                            correctInpcValue + correctInpcValue * juros : correctInpcValue;
                    juros = currentMonth.equals("dezembro") ? 0 : juros;
                }
                j++;
            }

            totalINPCValue += correctInpcValue;
            totalTRValue += correctTrValue;
            difValue = (totalINPCValue - totalTRValue) < 0 ? 0 : (totalINPCValue - totalTRValue);
/*
            float totalPerdas = (correctInpcValue - correctTrValue) < 0 ? 0 : (correctInpcValue - correctTrValue);
            currentMonthMap.setCorrectInpcValue(Calcule.moneyFormat(correctInpcValue));
            currentMonthMap.setCorrectTrValue(Calcule.moneyFormat(correctTrValue));
            currentMonthMap.setTotalPerdas(Calcule.moneyFormat(totalPerdas));

            Log.i(TAG, "Mês " + currentMonthMap.getName() + " de " + currentMonthMap.getYear() + ", " +
                    "índice TR: " + currentMonthMap.getIndiceTR() + ", " +
                    "índice INPC: " + currentMonthMap.getIndiceINPC() + ", " +
                    "correção pela TR: " + currentMonthMap.getCorrectTrValue() + ", " +
                    "correção pelo INPC: " + currentMonthMap.getCorrectInpcValue() + ", " +
                    "perdas: " + currentMonthMap.getTotalPerdas()
            );
*/
            i++;
        }

        int depositoSize = monthsYearList.size();
        float totalDeposito = 0;
        float totalTr = 0;
        float totalInpc = 0;
        float totalDif = 0;
        float totalTrValue = 0;
        float totalInpcValue = 0;
        i = 0;
        while(i < depositoSize) {
            float juros = 0;
            float correctTrValue = 0;
            float correctInpcValue = 0;
            currentDepositoValue = currentDepositos.get(i);

            key = monthsYearList.get(i);
            AdapterRowView currentMonthMap = mapMonthsList.get(key);

            j = i;
            while(j < depositoSize) {
                float currentTrIndex = currentTrIndices.get(j) <= 0 ? 0 : currentTrIndices.get(j) * 0.01f;
                float currentInpcIndex = currentInpcIndices.get(j) <= 0 ? 0 : currentInpcIndices.get(j) * 0.01f;
                if(j >= i && currentDepositoValue != 0) {
                    String currentMonth = currentMonths.get(j).toLowerCase();
                    juros += 0.0025f;
                    correctTrValue = j == i ? currentDepositoValue + (currentDepositoValue + totalTrValue)
                            * currentTrIndex : correctTrValue + correctTrValue * currentTrIndex;
                    correctInpcValue = j == i ? currentDepositoValue + (currentDepositoValue + totalInpcValue)
                            * currentInpcIndex : correctInpcValue + correctInpcValue * currentInpcIndex;
                    correctTrValue = currentMonth.equals("dezembro") || j == depositoSize - 1 ?
                            correctTrValue + correctTrValue * juros : correctTrValue;
                    correctInpcValue = currentMonth.equals("dezembro") || j == depositoSize - 1 ?
                            correctInpcValue + correctInpcValue * juros : correctInpcValue;
                    juros = currentMonth.equals("dezembro") ? 0 : juros;
                }
                j++;
            }
            float dif = (correctInpcValue - correctTrValue) <= 0 ? 0 : correctInpcValue - correctTrValue;
            String currentTrString = Calcule.moneyFormat(correctTrValue);
            String currentInpcString = Calcule.moneyFormat(correctInpcValue);
            String currentDifString = Calcule.moneyFormat(dif);

            currentMonthMap.setCorrectInpcValue(currentInpcString);
            currentMonthMap.setCorrectTrValue(currentTrString);
            currentMonthMap.setTotalPerdas(currentDifString);

            totalDeposito += currentDepositos.get(i);
            totalTr += correctTrValue;
            totalInpc += correctInpcValue;
            totalDif += dif;
            i++;
        }

        String totalDepositosString = Calcule.moneyFormat(totalDeposito);
        String totalTrString = Calcule.moneyFormat(totalTr);
        String totalInpcString = Calcule.moneyFormat(totalInpc);
        String totalPerdasString = Calcule.moneyFormat(totalDif);

        String footer1 = firstMonth + " de " + firstYear;
        String footer2 = lastMonth + " de " + lastYear;

        AdapterRowView adapterRowView = new AdapterRowView(footer1, footer2, totalDepositosString, "", "",
                totalTrString, totalInpcString, totalPerdasString);

        monthsYearList.add(adapterRowView.getName());
        mapMonthsList.put(adapterRowView.getName(), adapterRowView);
        adapterRowDetails.add(adapterRowView);

        calculated = true;

        state = STATE.DATA_READY;
        AppManager.CURRENT_STATE = state;
        AppManager.MONTHS_YEAR_LIST = monthsYearList;
        AppManager.MAP_MONTHS_LIST = mapMonthsList;
        AppManager.ADAPTER_ROW_DETAILS = adapterRowDetails;
/*
        Log.i(TAG,  "totalINPCValue: " + String.valueOf(totalInpcValue) + ", " +
                    "totalTRValue: " + String.valueOf(totalTrValue) + ", " +
                    "difValue: " + String.valueOf(difValue)
        );
*/
    }

    private void showDataReady() {
        String title = firstMonth + " de " + firstYear + " á " + lastMonth + " de " + lastYear;
        textViewResume.setText(title);
/*
        adapterRowViews.clear();

        int i = 0;
        while (i < mapMonthsList.size()) {
            adapterRowViews.add(mapMonthsList.get(monthsYearList.get(i)));
            i++;
        }

        arrayAdapterView.notifyDataSetChanged();
*/

        adapterRowDetails.clear();

        if(!listViewContainer.getAdapter().equals(arrayAdapterDetail)) {
            listViewContainer.setAdapter(arrayAdapterDetail);
        }

        int i = 0;
        while (i < mapMonthsList.size()) {
            adapterRowDetails.add(mapMonthsList.get(monthsYearList.get(i)));
            i++;
        }

        arrayAdapterDetail.notifyDataSetChanged();

        AppManager.VIEW_RESUME_TEXT = textViewResume.getText().toString();
        AppManager.ARRAY_ADAPTER_DETAIL = arrayAdapterDetail;
    }

    public void addNameData() {
        Log.i(TAG, "addNameData");
        nameSave = AppManager.NAME_SET;
        Contexts.getInstance().getDataHandler().addNameData(nameSave);
    }

    public void buildSavingProgress() {
        Log.i(TAG, "buildSavingProgress");

        DataHandler dataHandler = Contexts.getInstance().getDataHandler();
        int i = 0;
        int mapSize = mapMonthsList.size() - 1;
        while (i < mapSize) {
            //FGTSDataRow dataRow = (FGTSDataRow)container.getChildAt(i);
            AdapterRowView adapterRowView = mapMonthsList.get(monthsYearList.get(i));
            dataHandler.addFGTSData(nameSave, adapterRowView.getName(), adapterRowView.getYear(),
                    adapterRowView.getDeposito(), adapterRowView.getCorrectTrValue(),
                    adapterRowView.getCorrectInpcValue(), adapterRowView.getTotalPerdas());
            i++;
        }

        String textSet = nameSave + "\nTotal de perdas: R$ " +
                mapMonthsList.get(monthsYearList.get(monthsYearList.size() - 1)).getTotalPerdas();

        nameSet.setText(textSet);
        AppManager.NAME_SET_TEXT = textSet;
/*
        TextView title = buildTitle();


        String initialMonth = ((FGTSDataRow) container.getChildAt(0)).getMonth();
        String initialYear = ((FGTSDataRow) container.getChildAt(0)).getYear();
        String finalMonth = ((FGTSDataRow) container.getChildAt(container.getChildCount() - 1)).getMonth();
        String finalYear = ((FGTSDataRow) container.getChildAt(container.getChildCount() - 1)).getYear();

        title.setText("Cálculo de " + nameSave + " - Período: " + initialMonth + "/" + initialYear + " á "
                + finalMonth + "/" + finalYear);
        titlesContainer.addView(title, 0);
        FGTSCorrigido.getInstance().titles.clear();
        FGTSCorrigido.getInstance().titles.add(title);

        btnCalcule.setText(CALCULATE);
        FGTSCorrigido.getInstance().dataSaved = true;

        FGTSCorrigido.getInstance().currentState = FGTSCorrigido.STATE.DATA_SAVED;
*/
        GUI.toastLong("Cálculo de " + nameSave + " salvo com sucesso.");

        state = STATE.NONE;
        AppManager.CURRENT_STATE = state;
    }

    public void loadData(String pName) {
        nameSave = pName;
        //executeProgress();
        mapMonthsList.clear();
        monthsYearList.clear();
        int i = 0;
        DataHandler dataHandler = Contexts.getInstance().getDataHandler();
        List<FGTSDataModel> fgtsDataModels = dataHandler.getDataModel(nameSave);
        int dataSize = fgtsDataModels.size();
        while (i < dataSize) {
            FGTSDataModel fgtsDataModel = fgtsDataModels.get(i);
            String monthYear = fgtsDataModel.getMonth() + fgtsDataModel.getYear();
            monthsYearList.add(monthYear);
            mapMonthsList.put(monthYear, new AdapterRowView(fgtsDataModel.getMonth(),
                    fgtsDataModel.getYear(), fgtsDataModel.getDeposito(), "", "",
                    fgtsDataModel.getTr(), fgtsDataModel.getInpc(),
                    fgtsDataModel.getDiference()));
            i++;
        }
    }

    private void showDataLoaded() {
        adapterRowDetails.clear();

        if(!listViewContainer.getAdapter().equals(arrayAdapterDetail)) {
            listViewContainer.setAdapter(arrayAdapterDetail);
        }

        float totalInpcValue = 0;
        float totalTrValue = 0;
        float totalDepositoValue = 0;
        float totalPerdasValue = 0;
        int i = 0;
        while (i < mapMonthsList.size()) {
            AdapterRowView adapterRowView = mapMonthsList.get(monthsYearList.get(i));
            adapterRowDetails.add(adapterRowView);
            totalDepositoValue += Calcule.getFloat(adapterRowView.getDeposito());
            totalInpcValue += Calcule.getFloat(adapterRowView.getCorrectInpcValue());
            totalTrValue += Calcule.getFloat(adapterRowView.getCorrectTrValue());
            totalPerdasValue += Calcule.getFloat(adapterRowView.getTotalPerdas());
            i++;
        }
        String totalDepositosString = Calcule.moneyFormat(totalDepositoValue);
        String totalTrString = Calcule.moneyFormat(totalTrValue);
        String totalInpcString = Calcule.moneyFormat(totalInpcValue);
        String totalPerdasString = Calcule.moneyFormat(totalPerdasValue);

        firstMonth = mapMonthsList.get(monthsYearList.get(0)).getName();
        firstYear = mapMonthsList.get(monthsYearList.get(0)).getYear();
        lastMonth = mapMonthsList.get(monthsYearList.get(monthsYearList.size() -1)).getName();
        lastYear = mapMonthsList.get(monthsYearList.get(monthsYearList.size() -1)).getYear();

        String title = firstMonth + " de " + firstYear + " á " + lastMonth + " de " + lastYear;
        textViewResume.setText(title);

        String footer1 = firstMonth + " de " + firstYear;
        String footer2 = lastMonth + " de " + lastYear;

        AdapterRowView adapterRowView = new AdapterRowView(footer1, footer2, totalDepositosString, "", "",
                totalTrString, totalInpcString, totalPerdasString);

        adapterRowDetails.add(adapterRowView);

        arrayAdapterDetail.notifyDataSetChanged();

        String nameSetText = nameSave + "\nTotal de perdas: R$ " + totalPerdasString;

        nameSet.setText(nameSetText);
        AppManager.VIEW_RESUME_TEXT = title;
        AppManager.NAME_SET_TEXT = nameSetText;
        AppManager.ADAPTER_ROW_DETAILS = adapterRowDetails;
        AppManager.ARRAY_ADAPTER_DETAIL = arrayAdapterDetail;
    }

    private void buildDeleteData() {
        DataHandler dataHandler= Contexts.getInstance().getDataHandler();
        dataHandler.deleteNameData(AppManager.NAME_SET);
        dataHandler.deleteFGTSData(AppManager.NAME_SET);
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(view.findViewById(R.id.header_title) == null) {
                final CustomDialog customDialog = new CustomDialog(CustomDialog.TYPE.CHANGE_VALUE);
                final String promptValue = ((TextView) view.findViewById(R.id.value)).getText().toString();
                String title = ((TextView) view.findViewById(R.id.month)).getText().toString() +
                        " de " + yearsList.get(position);
                customDialog.changeTitle(title);
                customDialog.adapterRowView = adapterRowViews.get(position);
                customDialog.arrayAdapterView = arrayAdapterView;
                customDialog.lastViewSelected = view.findViewById(R.id.value);

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
    }

    public void showBtnInitScreen() {
        DataHandler dataHandler = FGTSCorrigido.dataHandler;
        Cursor cursor = dataHandler.returnFGTSData();
        if(cursor.getCount() > 0) {
            showBtns(0, null, 2, null);
        } else {
            showBtns(null, null, 2, null);
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        /*
        switch (v.getId()) {
            case R.id.btnAddPeriod:
                if (container.getChildCount() > 0 || tableLayout.getChildCount() > 0) {
                    CustomDialog customDialog = new CustomDialog(context, "A V I S O", "", CustomDialog.TYPE_CLEAR);
                    customDialog.currentActivity = currentyActivity;
                    customDialog.show();
                } else{
                    if (!periodAdded) {
                        createPeriod();
                    }
                }
                break;
            case R.id.btnCalcule:
                if (String.valueOf(btnCalcule.getText()).equals(CALCULATE)) {

                    if(FGTSCorrigido.getInstance().currentState.equals(FGTSCorrigido.STATE.VALUES_INSERT)) {
                        state = STATE.CALCULATE;
                        executeProgress();
                    } else {
                        CustomDialog customDialog = new CustomDialog(context,"AVISO",
                        "Inicie um cálculo primeiro.", "");
                        customDialog.show();
                    }
                } else if (String.valueOf(btnCalcule.getText()).equals(SAVE)) {
                    state = STATE.SAVING;
                    save();
                } else {
                    CustomDialog customDialog = new CustomDialog(context, "A V I S O", "Escolha o período!", "");
                    customDialog.show();
                }
                break;
            case R.id.btnClear:
                if (container.getChildCount() > 0 || containers.size() > 0 || tableLayout.getChildCount() > 0) {
                    CustomDialog customDialog = new CustomDialog(context, "A V I S O", "", CustomDialog.TYPE_CLEAR);
                    customDialog.currentActivity = currentyActivity;
                    customDialog.show();
                } else {
                    CustomDialog customDialog = new CustomDialog(context, "A V I S O", "Nada para ser limpo.", "");
                    customDialog.show();
                }
                break;
            case R.id.btnGetSaves:
                state = STATE.LOAD_DATA;
                FGTSCorrigido.getInstance().buildSavedData();
                break;
        }
        */
    }

    public void clear() {
        step = 0;
        totalINPCResume = 0;
        totalTRResume = 0;
        totalDifResume = 0;
        messageSaved = "";
        periodAdded = false;
        dadosSaved = false;
        btnCalcule.setText(CALCULATE);
        calculated = false;
        periodVisible = false;
        if(containers.size() > 0) containers.clear();

        refresh();
    }

    private void save() {
        CustomDialog customDialog = new CustomDialog(context, "Digite um nome", "", CustomDialog.TYPE_SAVE);
        customDialog.show();
    }

    private void refresh () {
        FGTSCorrigido.getInstance().currentState = FGTSCorrigido.STATE.EMPTY;
        container.removeAllViews();
        titlesContainer.removeAllViews();
        totalsContainer.removeAllViews();
        tableLayout.removeAllViews();
        textViewResume.setText("");
    }

    public void createPeriod () {
        state = STATE.PERIOD_ADDED;

        refresh();
        calculated = false;
        periodVisible = false;
        CustomDialog customDialog = new CustomDialog(context, "", "", CustomDialog.TYPE_MONTHS);
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                periodAdded = false;
                if (dadosSaved) btnCalcule.setText(CALCULATE);
            }
        });
        customDialog.show();

        periodAdded = true;
    }

    public void addPeriod () {
        FGTSCorrigido.getInstance().currentState = FGTSCorrigido.STATE.VALUES_INSERT;

        currentMonths.clear();
        currentYears.clear();
        currentValues.clear();
        currentDepositos.clear();
        currentInpcIndices.clear();
        currentTrIndices.clear();

        valuesList = new ArrayList<>();
        monthsYearList = new ArrayList<>();
        float firstYearNum = Float.valueOf(firstYear);
        float lastYearNum = Float.valueOf(lastYear);
        float currentYear = firstYearNum;

        yearsList.clear();

        while (currentYear <= lastYearNum) {
            String correctYear = String.valueOf(currentYear).substring(0, 4);
            yearsList.add(correctYear);
            currentYear++;
        }
    }

    public void showBtns(Object pLoad, String pCalcSave, Object pInit, Object pClear) {
        btnContainer.removeAllViews();
        buttons.clear();

        buttons.add(btnGetSave);
        buttons.add(btnCalcule);
        buttons.add(btnAddPeriod);
        buttons.add(btnClear);

        if(pLoad != null && (int)pLoad == 0) btnContainer.addView(buttons.get((int)pLoad));
        if(pCalcSave != null) {
            switch (pCalcSave) {
                case "calc":
                    ((Button) buttons.get(1)).setText(CALCULATE);
                    btnContainer.addView(buttons.get(1));
                    break;
                case "save":
                    ((Button) buttons.get(1)).setText(SAVE);
                    btnContainer.addView(buttons.get(1));
                    break;
            }
        }
        if(pInit != null && (int)pInit == 2) btnContainer.addView(buttons.get((int)pInit));
        if(pClear != null && (int)pClear == 3) btnContainer.addView(buttons.get((int)pClear));
    }

    public void buildPeriod() {
        FGTSCorrigido.getInstance().tableLayoutList.clear();
        tableLayout.removeAllViews();
        tableLayout.setStretchAllColumns(true);
        valuesList.clear();

        String currentMonthString = firstMonth;

        Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT, 1f);

        TextView title = new TextView(context);
        title.setLayoutParams(rowParams);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setText("Preencha os valores abaixo conforme seu EXTRATO do FGTS");
        title.setBackgroundColor(Color.argb(255,240,240,140));
        tableLayout.addView(title);

        int j = 0;
        while (j < yearsList.size()) {
            boolean nextYear = false;
            String currentYearString = yearsList.get(j);
            TableRow tableRowYear = new TableRow(context);
            tableRowYear.setLayoutParams(rowParams);
            tableRowYear.getLayoutParams();
            tableRowYear.setPadding(0, 0, 0, 1);
            tableLayout.addView(tableRowYear);
            TextView btn = new TextView(context);
            btn.setLayoutParams(rowParams);
            btn.setTypeface(bold);
            btn.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            btn.setBackgroundColor(Color.argb(255,140,170,255));
            btn.setClickable(false);
            btn.setText(currentYearString);
            btn.setTextSize(btnTextSize);
            tableRowYear.addView(btn);
            FGTSCorrigido.getInstance().tableRows.add(tableRowYear);

            int monthsTotal = 12;

            while (monthsTotal > 0) {
                if (nextYear) break;
                TableRow tableRowMonths = new TableRow(context);
                tableRowMonths.setPadding(0, 0, 0, 1);
                tableRowMonths.setLayoutParams(rowParams);
                TableRow tableRowValues = new TableRow(context);
                tableRowValues.setPadding(0, 0, 0, 1);
                tableRowValues.setLayoutParams(rowParams);
                for (int i = 0; i < cols; i++) {
                    if (nextYear) break;

                    currentYears.add(currentYearString);

                    btn = new TextView(context);
                    btn.setLayoutParams(rowParams);
                    btn.setTypeface(bold);
                    btn.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    btn.setBackgroundColor(Color.argb(255, 170, 200, 255));
                    btn.setClickable(false);
                    btn.setText(currentMonthString);
                    btn.setTextSize(btnTextSize);
                    tableRowMonths.addView(btn);
                    final EditText editText = new EditText(context);
                    editText.setLayoutParams(rowParams);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    editText.setHint(currentMonthString.substring(0, 3) + "/" + currentYearString);
                    editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    editText.setId(valuesList.size());
                    editText.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
                    tableRowValues.addView(editText);
                    editText.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                currentField = (EditText) view;
                                view.requestFocus();
                                InputMethodManager imm = (InputMethodManager)
                                        context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                            }
                            return false;
                        }
                    });
                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                            switch (actionId) {
                                case EditorInfo.IME_ACTION_DONE:
                                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                                            hideSoftInputFromWindow(textView.getWindowToken(), 0);
                                    return false;
                                case EditorInfo.IME_ACTION_NEXT:
                                    valuesList.get(textView.getId() + 1).requestFocus();
                                    currentField = valuesList.get(textView.getId() + 1);
                                    return false;
                                default:
                                    return false;
                            }
                        }
                    });
                    valuesList.add(editText);
                    String curMonthPeriod = currentMonthString.substring(0, 3).toLowerCase() + currentYearString;
                    monthsYearList.add(curMonthPeriod);
                    currentMonths.add(currentMonthString);

                    for (int k = 0; k < monthsList.length; k++) {
                        if (currentMonthString.equals("Dezembro")) {
                            currentMonthString = "Janeiro";
                            monthsTotal--;
                            nextYear = true;
                            break;
                        }
                        if (monthsList[k].equals(currentMonthString)) {
                            currentMonthString = monthsList[k + 1];
                            nextYear = lastMonth.equals(monthsList[k]) && yearsList.get(j).equals(lastYear);
                            monthsTotal--;
                            break;
                        }
                    }
                }
                tableLayout.addView(tableRowMonths);
                tableLayout.addView(tableRowValues);

                FGTSCorrigido.getInstance().tableRows.add(tableRowMonths);
                FGTSCorrigido.getInstance().tableRows.add(tableRowValues);
            }
            j++;
        }
        valuesList.get(valuesList.size() - 1).setImeOptions(EditorInfo.IME_ACTION_DONE);
        btnCalcule.setText(CALCULATE);
        periodVisible = true;

        FGTSCorrigido.getInstance().tableLayoutList.add(tableLayout);
        FGTSCorrigido.getInstance().currentState = FGTSCorrigido.STATE.VALUES_INSERT;
    }

    public void calcule () {
        String firstPeriod = firstMonth.substring(0, 3).toLowerCase() + firstYear;
        String lastPeriod = lastMonth.substring(0, 3).toLowerCase() + lastYear;

        message = "";

        boolean firstMonthAdded = false;

        int i = 0;
        int j = 0;
        while (i < indicesINPC.size()) {
            if (firstMonthAdded) {
                currentInpcIndices.add(Float.valueOf(indicesINPC.get(i).value));
            } else if (indicesINPC.get(i).name.equals(firstPeriod)) {
                currentInpcIndices.add(Float.valueOf(indicesINPC.get(i).value));
                j = i;
                firstMonthAdded = true;
            }
            if (indicesINPC.get(i).name.equals(lastPeriod)) {
                if (i != j) {
                    currentInpcIndices.add(Float.valueOf(indicesINPC.get(i).value));
                }
                firstMonthAdded = false;
            }
            i++;
        }

        i = 0;
        while (i < indicesTR.size()) {
            if (firstMonthAdded) {
                currentTrIndices.add(Float.valueOf(indicesTR.get(i).value));
            } else if (indicesTR.get(i).name.equals(firstPeriod)) {
                currentTrIndices.add(Float.valueOf(indicesTR.get(i).value));
                j = i;
                firstMonthAdded = true;
            }
            if (indicesTR.get(i).name.equals(lastPeriod)) {
                if (i != j) {
                    currentTrIndices.add(Float.valueOf(indicesTR.get(i).value));
                }
                firstMonthAdded = false;
            }
            i++;
        }

        float currentDepositoValue;
        float totalINPCValue = 0;
        float totalTRValue = 0;
        difValue = 0;
        i = 0;
        while (i < monthsYearList.size()) {
            float juros = 0;
            float value = String.valueOf(valuesList.get(i).getText()).equals("") ? 0 :
                    Float.valueOf(String.valueOf(valuesList.get(i).getText()));

            currentDepositos.add(value);
            currentValues.add(Calcule.moneyFormat(value));

            float correctTrValue = 0;
            float correctInpcValue = 0;
            j = 0;
            while (j < monthsYearList.size()) {
                float currentTrIndex = currentTrIndices.get(j) <= 0 ? 0 : currentTrIndices.get(j) * 0.01f;
                float currentInpcIndex = currentInpcIndices.get(j) <= 0 ? 0 : currentInpcIndices.get(j) * 0.01f;

                currentDepositoValue = currentDepositos.get(i);
                if (j >= i && currentDepositoValue != 0) {
                    String currentMonth = currentMonths.get(j).toLowerCase();
                    juros += 0.0025f;
                    correctTrValue = j == i ? value + value * currentTrIndex :
                            correctTrValue + correctTrValue * currentTrIndex;
                    correctInpcValue = j == i ? value + value * currentInpcIndex :
                            correctInpcValue + correctInpcValue * currentInpcIndex;
                    correctTrValue = currentMonth.equals("dezembro") || j == monthsYearList.size() - 1 ?
                            correctTrValue + correctTrValue * juros : correctTrValue;
                    correctInpcValue = currentMonth.equals("dezembro") || j == monthsYearList.size() - 1 ?
                            correctInpcValue + correctInpcValue * juros : correctInpcValue;
                    juros = currentMonth.equals("dezembro") ? 0 : juros;
                }
                j++;
            }
            totalINPCValue += correctInpcValue;
            totalTRValue += correctTrValue;
            difValue = totalINPCValue - totalTRValue;

            i++;
        }

        calculated = true;

        FGTSCorrigido.getInstance().currentState = FGTSCorrigido.STATE.READY;
    }

    public void buildData() {
        btnCalcule.setText(SAVE);
        tableLayout.removeAllViews();

        FGTSCorrigido.getInstance().titlesContainer.clear();
        FGTSCorrigido.getInstance().totalsContainer.clear();
        FGTSCorrigido.getInstance().container.clear();

        TitleDataRow titleDataRow = new TitleDataRow(context, "", "", "", "Correção", "Correção", "");
        titlesContainer.addView(titleDataRow);
        FGTSCorrigido.getInstance().titlesContainer.add(titleDataRow);
        titleDataRow = new TitleDataRow(context, "Mês", "Ano", "Depósito", "pela TR", "pelo INPC", "Diferença");
        titlesContainer.addView(titleDataRow);
        FGTSCorrigido.getInstance().titlesContainer.add(titleDataRow);
        int depositoSize = currentDepositos.size();
        float currentDepositoValue;
        float totalDeposito = 0;
        float totalTr = 0;
        float totalInpc = 0;
        float totalDif = 0;
        float totalTrAcumulado = 0;
        float totalInpcAcumulado = 0;
        int i = 0;
        while(i < depositoSize) {
            float juros = 0;
            float correctTrValue = 0;
            float correctInpcValue = 0;
            currentDepositoValue = currentDepositos.get(i);
            int j = 0;
            while(j < depositoSize) {
                float currentTrIndex = currentTrIndices.get(j) <= 0 ? 0 : currentTrIndices.get(j) * 0.01f;
                float currentInpcIndex = currentInpcIndices.get(j) <= 0 ? 0 : currentInpcIndices.get(j) * 0.01f;
                if(j >= i && currentDepositoValue != 0) {
                    String currentMonth = currentMonths.get(j).toLowerCase();
                    juros += 0.0025f;
                    correctTrValue = j == i ? currentDepositoValue + (currentDepositoValue + totalTrAcumulado)
                            * currentTrIndex : correctTrValue + correctTrValue * currentTrIndex;
                    correctInpcValue = j == i ? currentDepositoValue + (currentDepositoValue + totalInpcAcumulado)
                            * currentInpcIndex : correctInpcValue + correctInpcValue * currentInpcIndex;
                    correctTrValue = currentMonth.equals("dezembro") || j == depositoSize - 1 ?
                            correctTrValue + correctTrValue * juros : correctTrValue;
                    correctInpcValue = currentMonth.equals("dezembro") || j == depositoSize - 1 ?
                            correctInpcValue + correctInpcValue * juros : correctInpcValue;
                    juros = currentMonth.equals("dezembro") ? 0 : juros;
                }
                j++;
            }
            float dif = correctInpcValue - correctTrValue;
            String currentTrString = Calcule.moneyFormat(correctTrValue);
            String currentInpcString = Calcule.moneyFormat(correctInpcValue);
            String currentDifString = Calcule.moneyFormat(dif);
            FGTSDataRow dataRow = new FGTSDataRow(context, currentMonths.get(i), currentYears.get(i),
                    currentValues.get(i), currentTrString, currentInpcString, currentDifString, i);
            container.addView(dataRow);
            FGTSCorrigido.getInstance().container.add(dataRow);
            totalDeposito += currentDepositos.get(i);
            totalTr += correctTrValue;
            totalInpc += correctInpcValue;
            totalDif += dif;
            i++;
        }
        String totalDepositosString = Calcule.moneyFormat(totalDeposito);
        String totalTrString = Calcule.moneyFormat(totalTr);
        String totalInpsString = Calcule.moneyFormat(totalInpc);
        String totalDifString = Calcule.moneyFormat(totalDif);

        titleDataRow = new TitleDataRow(context, "", "TOTAIS:", totalDepositosString, totalTrString,
                totalInpsString, totalDifString);
        totalsContainer.addView(titleDataRow);
        FGTSCorrigido.getInstance().totalsContainer.add(titleDataRow);
        FGTSCorrigido.getInstance().dataSaved = false;

        if (containers.size() > 0) containers.clear();
        containers.add(titlesContainer);
        containers.add(container);
        containers.add(totalsContainer);

        FGTSCorrigido.getInstance().currentState = FGTSCorrigido.STATE.READY;
    }

    private void showReadyView() {
        if(containers.get(0).getParent() != null) {
            ((TableRow) containers.get(0).getParent()).removeView(containers.get(0));
        }
        tableRowTitleContainer.removeAllViews();
        tableRowTitleContainer.addView(containers.get(0));
        titlesContainer = (LinearLayout) containers.get(0);

        if(containers.get(1).getParent() != null) {
            ((ScrollView) containers.get(1).getParent()).removeView(containers.get(1));
        }
        scrollView.removeAllViews();
        scrollView.addView(containers.get(1));
        container = (LinearLayout) containers.get(1);

        if(containers.get(2).getParent() != null) {
            ((TableRow) containers.get(2).getParent()).removeView(containers.get(2));
        }
        tableRowTotalsContainer.removeAllViews();
        tableRowTotalsContainer.addView(containers.get(2));
        totalsContainer = (LinearLayout) containers.get(2);

        btnCalcule.setText(SAVE);
    }

    private void showValuesInsertView() {
        tableLayoutContainer.removeView(tableLayout);
        tableLayout = FGTSCorrigido.getInstance().tableLayoutList.get(0);
        if(tableLayout.getParent() != null) {
            ((ScrollView) tableLayout.getParent()).
                    removeView(tableLayout);
        }
        tableLayoutContainer.addView(tableLayout);
        if(currentField != null) currentField.requestFocus();
    }

    private void showDataSavedView() {
        if(containers.get(0).getParent() != null) {
            ((TableRow) containers.get(0).getParent()).removeView(containers.get(0));
        }
        tableRowTitleContainer.removeAllViews();
        tableRowTitleContainer.addView(containers.get(0));
        titlesContainer = (LinearLayout) containers.get(0);

        if(containers.get(1).getParent() != null) {
            ((ScrollView) containers.get(1).getParent()).removeView(containers.get(1));
        }
        scrollView.removeAllViews();
        scrollView.addView(containers.get(1));
        container = (LinearLayout) containers.get(1);

        if(containers.get(2).getParent() != null) {
            ((TableRow) containers.get(2).getParent()).removeView(containers.get(2));
        }
        tableRowTotalsContainer.removeAllViews();
        tableRowTotalsContainer.addView(containers.get(2));
        totalsContainer = (LinearLayout) containers.get(2);
    }

    public void saveData(String pName) {
        nameSave = pName;
        executeProgress();
    }

    public void buildSaveData() {
        DataHandler dataHandler = FGTSCorrigido.dataHandler;
        int i = 0;
        while (i < container.getChildCount()) {
            FGTSDataRow dataRow = (FGTSDataRow)container.getChildAt(i);
            dataHandler.addFGTSData(nameSave, dataRow.getMonth(), dataRow.getYear(), dataRow.getDeposito(),
                    dataRow.getTr(), dataRow.getInpc(), dataRow.getDif());
            i++;
        }
        TextView title = buildTitle();

        String initialMonth = ((FGTSDataRow) container.getChildAt(0)).getMonth();
        String initialYear = ((FGTSDataRow) container.getChildAt(0)).getYear();
        String finalMonth = ((FGTSDataRow) container.getChildAt(container.getChildCount() - 1)).getMonth();
        String finalYear = ((FGTSDataRow) container.getChildAt(container.getChildCount() - 1)).getYear();

        title.setText("Cálculo de " + nameSave + " - Período: " + initialMonth + "/" + initialYear + " á "
                + finalMonth + "/" + finalYear);
        titlesContainer.addView(title, 0);
        FGTSCorrigido.getInstance().titles.clear();
        FGTSCorrigido.getInstance().titles.add(title);

        btnCalcule.setText(CALCULATE);
        FGTSCorrigido.getInstance().dataSaved = true;

        FGTSCorrigido.getInstance().currentState = FGTSCorrigido.STATE.DATA_SAVED;

        Toast.makeText(context, "Cálculo de " + nameSave + " salvo com sucesso.", Toast.LENGTH_LONG).show();
    }

    public void addLoadData() {
        FGTSCorrigido.getInstance().currentState = FGTSCorrigido.STATE.DATA_SAVED;
        FGTSCorrigido.getInstance().titles.clear();
        FGTSCorrigido.getInstance().titlesContainer.clear();
        FGTSCorrigido.getInstance().totalsContainer.clear();
        FGTSCorrigido.getInstance().container.clear();
    }

    public void buildDataLoaded() {
        titlesContainer.removeAllViews();
        container.removeAllViews();
        totalsContainer.removeAllViews();

        TextView title = buildTitle();
        titlesContainer.addView(title);
        TitleDataRow titleDataRow = new TitleDataRow(context, "", "", "", "Correção", "Correção", "");
        titlesContainer.addView(titleDataRow);
        FGTSCorrigido.getInstance().titlesContainer.add(titleDataRow);
        titleDataRow = new TitleDataRow(context, "Mês", "Ano", "Depósito", "pela TR", "pelo INPC", "Diferença");
        titlesContainer.addView(titleDataRow);
        FGTSCorrigido.getInstance().titlesContainer.add(titleDataRow);

        float totalDeposito = 0f;
        float totalTr = 0f;
        float totalInpc = 0f;
        float totalDif = 0f;

        DataHandler dataHandler = FGTSCorrigido.dataHandler;
        Cursor cursor = dataHandler.returnFGTSData();
        cursor.moveToFirst();
        int i = 0;
        while (i < cursor.getCount()) {
            FGTSDataModel fgtsDataModel = dataHandler.parseFGTSData(cursor);
            if(fgtsDataModel.getName().equals(nameSave)) {
                FGTSDataRow dataRow = new FGTSDataRow(context, fgtsDataModel.getMonth(), fgtsDataModel.getYear(),
                        fgtsDataModel.getDeposito(), fgtsDataModel.getTr(), fgtsDataModel.getInpc(),
                        fgtsDataModel.getDiference(), i);
                container.addView(dataRow);
                FGTSCorrigido.getInstance().container.add(dataRow);
                totalDeposito += Float.valueOf(fgtsDataModel.getDeposito().replace(".", "").replace(",", "."));
                totalTr += Float.valueOf(fgtsDataModel.getTr().replace(".", "").replace(",", "."));
                totalInpc += Float.valueOf(fgtsDataModel.getInpc().replace(".", "").replace(",", "."));
                totalDif += Float.valueOf(fgtsDataModel.getDiference().replace(".", "").replace(",", "."));
            }
            cursor.moveToNext();
            i++;
        }
        cursor.close();

        String initialMonth = ((FGTSDataRow) container.getChildAt(0)).getMonth();
        String initialYear = ((FGTSDataRow) container.getChildAt(0)).getYear();
        String finalMonth = ((FGTSDataRow) container.getChildAt(container.getChildCount() - 1)).getMonth();
        String finalYear = ((FGTSDataRow) container.getChildAt(container.getChildCount() - 1)).getYear();

        title.setText("Cálculo de " + nameSave + " - Período: " + initialMonth + "/" + initialYear + " á "
                + finalMonth + "/" + finalYear);
        FGTSCorrigido.getInstance().titles.add(title);

        String totalDepositosString = Calcule.moneyFormat(totalDeposito);
        String totalTrString = Calcule.moneyFormat(totalTr);
        String totalInpsString = Calcule.moneyFormat(totalInpc);
        String totalDifString = Calcule.moneyFormat(totalDif);
        titleDataRow = new TitleDataRow(context, "", "TOTAIS:", totalDepositosString, totalTrString,
                totalInpsString, totalDifString);
        totalsContainer.addView(titleDataRow);
        FGTSCorrigido.getInstance().totalsContainer.add(titleDataRow);
        btnCalcule.setText(CALCULATE);

        containers.clear();
        containers.add(titlesContainer);
        containers.add(container);
        containers.add(totalsContainer);
    }

    public void deleteData(String pName) {
        nameSave = pName;
        state = STATE.DELETE_DATA;
        executeProgress();
    }

    public void buildDataDeleted() {
        DataHandler dataHandler = FGTSCorrigido.dataHandler;
        dataHandler.deleteFGTSData(nameSave);
        dataHandler.deleteNameData(nameSave);
    }

    private TextView buildTitle() {
        Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);

        TextView title = new TextView(context);
        title.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        title.setTypeface(bold);
        title.setBackgroundColor(Color.argb(255, 200, 225, 180));
        title.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        return title;
    }

    public void showMessage(String pMessage) {
        Toast.makeText(context, pMessage, Toast.LENGTH_LONG).show();
    }

    public void executeProgress() {
        new DoInBackground().execute();
    }

    public class DoInBackground extends AsyncTask<Void, Void, Void>
            implements DialogInterface.OnCancelListener {
        private ProgressDialog dialog;

        protected void onPreExecute() {
            CharSequence message = state.equals(STATE.CALCULATE) ? "Calculando. Por favor aguarde..." :
                    state.equals(STATE.PERIOD_ADDED) ? "Criando período. Por favor aguarde..." :
                            state.equals(STATE.SAVING_PROGRESS) ? "Salvando. Por favor aguarde..." :
                                    state.equals(STATE.LOADING_DATA) ? "Carregando " + nameSave
                                            + ". Por favor aguarde..." :
                                            "Removendo " + nameSave + ". Por favor aguarde...";

            dialog = ProgressDialog.show(MainActivity.getInstance(), "", message, true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    handleBtns();
                }
            });
        }

        protected Void doInBackground(Void... unused) {
            if( state.equals(STATE.CALCULATE)) {
                Log.i(TAG, "executeProgress: doInBackground " + STATE.CALCULATE.toString());
                calculeData();
          //      FGTSCorrigido.getInstance().getFgtsPeriodos().calcule();
            } else if (state.equals(STATE.LOADING_DATA)) {
                Log.i(TAG, "executeProgress: doInBackground " + STATE.LOADING_DATA.toString() +
                        "\nexecuteProgress: doInBackground loading " + AppManager.NAME_SET);
                loadData(AppManager.NAME_SET);
           //     FGTSCorrigido.getInstance().getFgtsPeriodos().addPeriod();
            } else if (state.equals(STATE.DELETE_DATA)) {
                Log.i(TAG, "executeProgress: doInBackground " + STATE.DELETE_DATA.toString());
                buildDeleteData();
         //       FGTSCorrigido.getInstance().getFgtsPeriodos().buildDataDeleted();
            } else if (state.equals(STATE.SAVING_PROGRESS)) {
                Log.i(TAG, "executeProgress: doInBackground " + STATE.SAVING_PROGRESS.toString());
                addNameData();
            } else {
                Log.i(TAG, "executeProgress: doInBackground " + STATE.LOAD_DATA.toString());
         //       FGTSCorrigido.getInstance().getFgtsPeriodos().addLoadData();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            if(state.equals(STATE.CALCULATE)) {
                Log.i(TAG, "executeProgress: onPostExecute " + STATE.CALCULATE.toString());
                //FGTSCorrigido.getInstance().getFgtsPeriodos().buildData();
            } else if (state.equals(STATE.PERIOD_ADDED)) {
                Log.i(TAG, "executeProgress: onPostExecute " + STATE.PERIOD_ADDED.toString());
                //FGTSCorrigido.getInstance().getFgtsPeriodos().buildPeriod();
            } else if (state.equals(STATE.SAVING_PROGRESS)) {
                Log.i(TAG, "executeProgress: onPostExecute " + STATE.SAVING_PROGRESS.toString());
                buildSavingProgress();
                //FGTSCorrigido.getInstance().getFgtsPeriodos().buildSaveData();
            } else if (state.equals(STATE.LOADING_DATA)) {
                Log.i(TAG, "executeProgress: onPostExecute " + STATE.LOADING_DATA.toString() +
                        "\nexecuteProgress: onPostExecute load " + AppManager.NAME_SET);
                showDataLoaded();
                //FGTSCorrigido.getInstance().getFgtsPeriodos().buildDataLoaded();
            } else if (state.equals(STATE.DATA_READY)) {
                Log.i(TAG, "executeProgress: onPostExecute " + STATE.DATA_READY.toString());
                showDataReady();
                //FGTSCorrigido.getInstance().getFgtsPeriodos().buildDataLoaded();
            } else {
                Log.i(TAG, "executeProgress: onPostExecute " + state.toString());
                //Toast.makeText(context, "Cálculo de " + nameSave + " foi removido.", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }

        public void onCancel(DialogInterface dialog) {
            cancel(true);
            dialog.dismiss();
        }
    }
}
