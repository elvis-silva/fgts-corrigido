package com.elvis.fgtscorrigido.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.elvis.fgtscorrigido.app.context.Contexts;
import com.elvis.fgtscorrigido.app.data.DataHandler;
import com.elvis.fgtscorrigido.app.impl.IActivity;
import com.elvis.fgtscorrigido.app.model.FGTSDataRow;
import com.elvis.fgtscorrigido.app.model.TitleDataRow;
import com.elvis.fgtscorrigido.app.ui.CustomDialog;
import com.elvis.fgtscorrigido.app.ui.FGTSPeriodos;
import com.elvis.fgtscorrigido.app.ui.INPC;
import com.elvis.fgtscorrigido.app.ui.MoreApps;
import com.elvis.fgtscorrigido.app.ui.ScreenMain;
import com.elvis.fgtscorrigido.app.ui.TR;
import com.elvis.fgtscorrigido.app.utils.Indice;
import com.elvis.fgtscorrigido.app.utils.IntentUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FGTSCorrigido extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static FGTSCorrigido instance;

    public static DataHandler dataHandler;
    public static INPC inpc;
    public static TR tr;
    public static FGTSPeriodos fgtsPeriodos;
    public static MoreApps moreApps;
    public static ArrayList<Indice> indices;
    public static ArrayList<Indice> indices480;

    public ArrayList<TitleDataRow> titlesContainer = new ArrayList<>();
    public ArrayList<TitleDataRow> totalsContainer =  new ArrayList<>();
    public ArrayList<FGTSDataRow> container = new ArrayList<>();
    public ArrayList<Indice> indicesINPC = new ArrayList<>();
    public ArrayList<Indice> indicesTR = new ArrayList<>();
    public ArrayList<TableRow> tableRows = new ArrayList<>();
    public ArrayList<TextView> titles = new ArrayList<>();
    public boolean dataSaved = false;
    public int showInterstitialControl = 0;
    private ProgressBar progressBar;
    public ProgressBarRunnable progressBarRunnable = new ProgressBarRunnable();
    public ArrayList<TableLayout> tableLayoutList = new ArrayList<>();

    public TableLayout tableLayout;

//    private AdView adView;
//    private InterstitialAd interstitial;

    public ScreenMain screenMain = new ScreenMain();

    private IActivity currentActivity = screenMain;

    public enum STATE {
        EMPTY,
        VALUES_INSERT,
        DATA_SAVED,
        READY
    }

    public STATE currentState = STATE.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("FGTSCorrigido");
        setContentView(R.layout.activity_fgtscorrigido);

        instance = FGTSCorrigido.this;

        Contexts.getInstance().initActivity(FGTSCorrigido.this);

        tableLayout = new TableLayout(this);

//        interstitial = new InterstitialAd(this);
//        interstitial.setAdUnitId("ca-app-pub-4768510961285493/7262227962");
//
//        adView = (AdView) findViewById(R.id.adView);
//
//        AdRequest adRequest = new AdRequest.Builder().
//                addTestDevice("BA88524536C8E9B361CB4B25CD051452").build();
//
//        adView.loadAd(adRequest);
//        interstitial.loadAd(adRequest);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        dataHandler = new DataHandler(this);
        dataHandler.open();

        loadXml("indices.xml");
        loadXml("indices480.xml");

        inpc = new INPC();
        tr = new TR();
        fgtsPeriodos = new FGTSPeriodos();
        moreApps = new MoreApps();

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                fgtsPeriodos.setCols(6);
                inpc.mountGridXLarge();
                tr.mountGridXLarge();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                fgtsPeriodos.setCols(4);
                inpc.mountGridScreen480(indices480);
                tr.mountGridScreen480(indices480);
                break;
            default:
                fgtsPeriodos.setCols(3);
                inpc.mountGridScreenCel();
                tr.mountGridScreenCel();
                break;
        }
    }

    public static FGTSCorrigido getInstance () {
        return instance;
    }

    public IActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(IActivity pIActivity) {
        currentActivity = pIActivity;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
        /*        mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
            case 8:
        */        mTitle = getString(R.string.title_section8);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.fgtscorrigido, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (item.getItemId()) {
           /* case R.id.action_help:
                buildTips();
                return true;
           */ case R.id.action_rate:
                buildRate();
                return true;
            case R.id.action_about:
                buildAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(showInterstitialControl == 0) {
            showInterstitialControl++;
            Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_LONG).show();
        } else {
            showOfferWall();
        }
    }

    private void showOfferWall() {
//        if (interstitial.isLoaded()) {
//            interstitial.show();
//            interstitial.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    instance.finish();
//                }
//            });
//        } else {
        instance.finish();
//        }
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public void onResume() {
        super.onResume();

//        WVersionManager versionManager = new WVersionManager(this);
//        versionManager.setTitle("Nova versão disponível");
//        versionManager.setVersionContentUrl("https://copy.com/3Mu6afL2mVvP");
//        versionManager.setUpdateNowLabel("Atualizar");
//        versionManager.setRemindMeLaterLabel("Depois");
//        versionManager.setIgnoreThisVersionLabel("Ignorar");
//        versionManager.setUpdateUrl("https://play.google.com/store/apps/details?id=com.elvis.fgtscorrigido.app"); // this is the link will execute when update now clicked. default will go to google play based on your package name.
//        versionManager.setReminderTimer(10); // this mean checkVersion() will not take effect within 10 minutes
//        versionManager.checkVersion();

//        if (adView != null) {
//            adView.resume();
//        }
    }

    @Override
    public void onPause() {
//        if (adView != null) {
//            adView.pause();
//        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
//        if (adView != null) {
//            adView.destroy();
//        }
        dataHandler.close();
        System.exit(0);
        super.onDestroy();
    }

    public void buildSavedData() {
        CustomDialog customDialog = new CustomDialog(this, "C Á L C U L O S    S A L V O S", "", CustomDialog.TYPE_SAVED);

        customDialog.show();
    }

    public void buildAbout() {
        CustomDialog customDialog = new CustomDialog(this, "S O B R E", "", CustomDialog.TYPE_ABOUT);
        customDialog.show();
    }

    public void buildTips() {
        CustomDialog customDialog = new CustomDialog(this, "D I C A S", "", CustomDialog.TYPE_TIPS);
        customDialog.show();
    }

    public void buildRate() {
        IntentUtils.intentGooglePlay("com.elvis.fgtscorrigido.app");
    }

    private void loadXml (String xmlFile) {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = getAssets().open(xmlFile);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser, xmlFile);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXML (XmlPullParser parser, String _xmlFile) throws XmlPullParserException, IOException {

        int eventType = parser.getEventType();
        Indice currentIndice;
        String indiceType = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    if (_xmlFile.equals("indices.xml")) indices = new ArrayList<>();
                    if (_xmlFile.equals("indices480.xml")) indices480 = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    String name = parser.getName();
                    if (name.equals("indices")) {
                        eventType = parser.nextTag();
                        if (eventType == XmlPullParser.START_TAG) name = parser.getName();
                    }
                    if (name.equals("tr")) {
                        indiceType = "tr";
                        eventType = parser.nextTag();
                        if (eventType == XmlPullParser.START_TAG) {
                            currentIndice = new Indice();
                            currentIndice.type = indiceType;
                            currentIndice.name = parser.getName();
                            currentIndice.value = parser.nextText();
                            if (_xmlFile.equals("indices.xml")) indices.add(currentIndice);
                            if (_xmlFile.equals("indices480.xml")) indices480.add(currentIndice);
                            break;
                        }
                    }
                    if (name.equals("inpc")) {
                        indiceType = "inpc";
                        eventType = parser.nextTag();
                        if (eventType == XmlPullParser.START_TAG) {
                            currentIndice = new Indice();
                            currentIndice.type = indiceType;
                            currentIndice.name = parser.getName();
                            currentIndice.value = parser.nextText();
                            if (_xmlFile.equals("indices.xml")) indices.add(currentIndice);
                            if (_xmlFile.equals("indices480.xml")) indices480.add(currentIndice);
                            break;
                        }
                    }
                    currentIndice = new Indice();
                    currentIndice.type = indiceType;
                    currentIndice.name = parser.getName();
                    currentIndice.value = parser.nextText();
                    if (_xmlFile.equals("indices.xml")) indices.add(currentIndice);
                    if (_xmlFile.equals("indices480.xml")) indices480.add(currentIndice);
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }
        buildIndicesData();
    }

    private void buildIndicesData() {
        int i = 0;
        while (i < indices.size()) {
            if (indices.get(i).type.equals("inpc")) {
                indicesINPC.add(FGTSCorrigido.indices.get(i));
            } else if (indices.get(i).type.equals("tr")) {
                indicesTR.add(FGTSCorrigido.indices.get(i));
            }
            i++;
        }
    }

    public FGTSPeriodos getFgtsPeriodos () {
        return fgtsPeriodos;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            int argSectionInt = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (argSectionInt) {
                case 1:
//                    rootView = inflater.inflate(R.layout.fragment_fgts_periodos, container, false);
//                    fgtsPeriodos.createView(rootView);

                    rootView = inflater.inflate(R.layout.activity_screen_main, container, false);
                    FGTSCorrigido.getInstance().setCurrentActivity(
                            FGTSCorrigido.getInstance().screenMain);
                    FGTSCorrigido.getInstance().screenMain.setContentView(rootView);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_inpc, container, false);
                    inpc.createGrid(rootView);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_tr, container, false);
                    tr.createGrid(rootView);
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_how_to_use, container, false);
                    break;
                case 5:
                    rootView = inflater.inflate(R.layout.fragment_info, container, false);
                    break;/*
                case 6:
                    rootView = null;
                    showSite();
                    break;
                case 7:
                    rootView = null;
                    showFacebook();
                    break;*/
                default:
                    rootView = inflater.inflate(R.layout.fragment_more, container, false);
                    moreApps.createView(rootView);
                    break;
            }
            return rootView;
        }

        private void showFacebook() {
            Uri uri = Uri.parse("https://www.facebook.com/FGTS.Corrigido.android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            FGTSCorrigido.getInstance().startActivity(intent);
        }

        private void showSite() {
            Uri uri = Uri.parse("http://elviscarlossouza.tk");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            FGTSCorrigido.getInstance().startActivity(intent);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((FGTSCorrigido) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void showProgressBar () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new FGTSCorrigido.DoInBackground().execute();
            }
        });
    }

    public class ProgressBarRunnable implements Runnable {

        @Override
        public void run() {
        /*
         * Code you want to run on the thread goes here
         */
            new FGTSCorrigido.DoInBackground().execute();
        }

        public void stop() {
            FGTSCorrigido.getInstance().getProgressBar().setVisibility(View.INVISIBLE);
        }
    }

    public class DoInBackground extends AsyncTask<Void, Void, Void>
            implements DialogInterface.OnCancelListener {
        private ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = ProgressDialog.show(FGTSCorrigido.getInstance(),
                    "", "Calculando. Por favor aguarde...", true);
        }

        protected Void doInBackground(Void... unused) {
//            do_update();
            FGTSCorrigido.getInstance().getFgtsPeriodos().calcule();
            return null;
        }

        protected void onPostExecute(Void unused) {
            dialog.dismiss();
            FGTSCorrigido.getInstance().getFgtsPeriodos().buildData();
//            populate_listview();
        }

        public void onCancel(DialogInterface dialog) {
            cancel(true);
            dialog.dismiss();
        }
    }

}
