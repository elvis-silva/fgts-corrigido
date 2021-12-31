package com.elvis.fgtscorrigido.app;

import android.view.View;

import com.elvis.fgtscorrigido.app.ui.AdapterRowView;
import com.elvis.fgtscorrigido.app.ui.ArrayAdapterDetail;
import com.elvis.fgtscorrigido.app.ui.ArrayAdapterView;
import com.elvis.fgtscorrigido.app.ui.FGTSPeriodos;
import com.elvis.fgtscorrigido.app.utils.Indice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by elvis on 18/12/15.
 */
public class AppManager {

    public static FGTSPeriodos.STATE CURRENT_STATE = FGTSPeriodos.STATE.NONE;
    public static String DISPLAY = "Display";
    public static List<Indice> TR = new ArrayList<>();
    public static List<Indice> INPC = new ArrayList<>();
    static SCREEN CURRENT_SCREEN = SCREEN.BLOG;
    public static int REVEL_VISIBILITY = View.INVISIBLE;
    public static String NAME_SET, FIRST_MONTH, FIRST_YEAR, LAST_MONTH, LAST_YEAR, FIRST_PERIOD,
            LAST_PERIOD, VIEW_RESUME_TEXT, NAME_SET_TEXT;
    public static List<AdapterRowView> ADAPTER_ROW_VIEWS, ADAPTER_ROW_DETAILS;
    public static ArrayList<String> YEARS_LIST, MONTHS_YEAR_LIST, CURRENT_MONTHS;
    public static Map<String, AdapterRowView> MAP_MONTHS_LIST;
    public static ArrayAdapterView ARRAY_ADAPTER_VIEW;
    public static int PY_REVEL_VIEW, PX_REVEL_VIEW;
    public static boolean HIDDEN_REVEL_VIEW;
    public static ArrayAdapterDetail ARRAY_ADAPTER_DETAIL;
    public static int INTERSTITIAL_CONTROL = 0;
    public static int INTERSTITIAL_CONTROL_BLOG = 0;

    enum SCREEN {
        FGTS_PERIODOS,
        TABELA_INPC,
        MORE, TABELA_TR, BLOG, ABOUT
    }

    public static String[] MONTHS = {
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto",
            "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    public static String[] YEARS = {
            "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008",
            "2009", "2010", "2011", "2012", "2013", "2014", "2015"
    };
}
