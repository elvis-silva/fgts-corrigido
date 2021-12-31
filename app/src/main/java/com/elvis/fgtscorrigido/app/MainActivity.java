package com.elvis.fgtscorrigido.app;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableRow;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.elvis.fgtscorrigido.app.context.Contexts;
import com.elvis.fgtscorrigido.app.ui.About;
import com.elvis.fgtscorrigido.app.ui.AbstractFragment;
import com.elvis.fgtscorrigido.app.ui.Blog;
import com.elvis.fgtscorrigido.app.ui.CustomDialog;
import com.elvis.fgtscorrigido.app.ui.FGTSPeriodos;
import com.elvis.fgtscorrigido.app.ui.INPC;
import com.elvis.fgtscorrigido.app.ui.MoreApps;
import com.elvis.fgtscorrigido.app.ui.TR;
import com.elvis.fgtscorrigido.app.utils.GUI;
import com.elvis.fgtscorrigido.app.utils.Indice;
import com.elvis.fgtscorrigido.app.utils.IntentUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.nineoldandroids.animation.Animator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.perm.Perm;
import fr.arnaudguyon.perm.PermResult;
import im.delight.android.webview.AdvancedWebView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int PERMISSION_INTERNET_REQUEST = 100;
    private static final int PERMISSION_NETWORK_REQUEST = 101;
    public static String TAG = MainActivity.class.getSimpleName();
    private static MainActivity instance;
    private AbstractFragment fragment;
    public FloatingActionButton fabMain, fabAddNew, fabLoad;
    public List<Indice> indices;
    private int fabMainVisibility, fabAddNewVisibility, fabLoadVisibility;
    private boolean screenSaved;
    public int exitControl = 0;
    private boolean fabsShown;
    private InterstitialAd interstitialAd;
    private AdView adView;
    private boolean interstitialAdShowed;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        Contexts.getInstance().initActivity(MainActivity.this);

        initPermissions();
        initAds();
        initViews();
    }

    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.INTERNET)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET},
                        PERMISSION_INTERNET_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_NETWORK_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        PERMISSION_NETWORK_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void initViews() {
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        fabMain = (FloatingActionButton) findViewById(R.id.fab_main);
        fabMain.setOnClickListener(this);

        fabAddNew = (FloatingActionButton) findViewById(R.id.fab_add_new);
        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(CustomDialog.TYPE.ESCOLHER_PERIODO);
                customDialog.show();
            }
        });

        fabLoad = (FloatingActionButton) findViewById(R.id.fab_load);
        fabLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog(CustomDialog.TYPE.LOAD_CALCULE);
                customDialog.show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Contexts.getInstance().initActivity(MainActivity.this);

        loadXml("indices.xml");

        //showFGTSPeriodos();
        showBlog();
    }

    private void initAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4768510961285493~2421209564");

        AdRequest adRequest = new AdRequest.Builder().build();

        final TableRow tabRowAdView = (TableRow) findViewById(R.id.main_adview);
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.admob_banner_id));
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (adView.getParent() == null) tabRowAdView.addView(adView);
            }
        });

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_id));
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public AbstractFragment getFragment() {
        return fragment;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            exitControl = 0;
        } else if (fragment.onBackPressed()) {
            if(exitControl == 1) {
                super.onBackPressed();
            } else {
                exitControl++;
                //GUI.toastLong("Pressione novamente para sair.");
            }
        }/* else {
            exitControl = 0;
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fgts_periodos) {
            exitControl = 0;
            if(!AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.FGTS_PERIODOS)) showFGTSPeriodos();
        } else if (id == R.id.nav_inpc_table) {
            exitControl = 0;
            if(!AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.TABELA_INPC)) showTabelaINPC();
        } else if (id == R.id.nav_tr_table) {
            exitControl = 0;
            if(!AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.TABELA_TR)) showTabelaTR();
        } else if (id == R.id.nav_more_apps) {
            exitControl = 0;
            if(!AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.MORE)) showScreenMore();
        } else if (id == R.id.nav_about) {
            exitControl = 0;
            if(!AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.ABOUT)) showAboutScreen();
        } else if (id == R.id.nav_site) {
            exitControl = 0;
            if(!AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.BLOG)) showBlog();
        } else if (id == R.id.nav_rate) {
            exitControl = 0;
            IntentUtils.intentGooglePlay(getPackageName());
        } else if(id == R.id.nav_share) {
            exitControl = 0;
            showShareOptions();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showShareOptions() {
        String message = "\n\nBaixe você também o app " + getString(R.string.app_name) +
                " e fique sempre antenado nas principais novidades sobre a correção do seu FGTS.\n " +
                "Grátis na Google Play Store.\n Compartilhe com seus amigos:\n\n " +
                "https://play.google.com/store/apps/details?id=" + getPackageName();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");

        startActivity(sendIntent);
    }

    public void showFabDone() {
        fabMain.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_custom)));
        fabMain.setRippleColor(getResources().getColor(R.color.income_bg_s));
        fabMain.setImageResource(R.drawable.ic_done_white);
    }

    public void hideFab() {
        if(fabMain.isShown()) fabMain.hide();
        if(fabsShown) {
            if (fabLoad.isShown()) fabLoad.hide();
            if (fabAddNew.isShown()) fabAddNew.hide();
        }
    }

    private void hideFabs() {
        fabMainVisibility = fabMain.getVisibility();
        fabAddNewVisibility = fabAddNew.getVisibility();
        fabLoadVisibility = fabLoad.getVisibility();

        if(fabLoadVisibility == View.VISIBLE) {
            fabMain.hide();
            fabLoad.hide();
            fabAddNew.hide();
        } else if (fabMainVisibility == View.VISIBLE) {
            fabMain.hide();
        }
    }

    public void showFab() {
        if(!fabMain.isShown()) fabMain.show(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onShown(FloatingActionButton fab) {
                super.onShown(fab);
                fab.setClickable(true);
            }
        });
        if(fabsShown) {
            //     if (!fabLoad.isShown()) {
            fabLoad.show(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onShown(FloatingActionButton fab) {
                    super.onShown(fab);
                    fab.setClickable(true);
                }
            });
            //     }
            //     if (!fabAddNew.isShown()) {
            fabAddNew.show(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onShown(FloatingActionButton fab) {
                    super.onShown(fab);
                    fab.setClickable(true);
                }
            });
            //     }
            fabLoad.setVisibility(View.VISIBLE);
            fabAddNew.setVisibility(View.VISIBLE);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adView != null) {
            adView.resume();
        }
        if(interstitialAd != null && !interstitialAd.isLoaded()) {
            requestNewInterstitial();
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
        Contexts.getInstance().getDataHandler().close();
        if(adView != null) {
            adView.destroy();
        }
        System.exit(0);
        super.onDestroy();
    }

    public void showInitBtns(final int pPx, final int pPy, final int pStartRadius, final int pEndRadius) {
        if (Contexts.getInstance().getDataHandler().returnNamesData().getCount() > 0) {
            YoYo.with(Techniques.SlideOutDown).duration(300).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                    fabAddNew.setClickable(false);
                    fabLoad.setClickable(false);

                    YoYo.with(Techniques.SlideOutDown).duration(300).withListener(
                            new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                    YoYo.with(Techniques.SlideOutDown).duration(300).withListener(
                                            new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {
                                                }

                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    fabMain.setImageResource(R.drawable.ic_add_white_fb);
                                                    fabMain.setBackgroundTintList(ColorStateList.valueOf(
                                                            getResources().getColor(R.color.colorPrimary)));
                                                    fabMain.setRippleColor(getResources().getColor(R.color.other_fgd));
                                                    fabMain.setClickable(true);
                                                    fabsShown = false;
                                                    YoYo.with(Techniques.SlideInUp).duration(300).withListener(
                                                            new Animator.AnimatorListener() {
                                                                @Override
                                                                public void onAnimationStart(Animator animation) {

                                                                }

                                                                @Override
                                                                public void onAnimationEnd(Animator animation) {
                                                                    if (!(pPx == 0 && pPy == 0 && pEndRadius == 0 && pStartRadius == 0)) {
                                                                        fabMain.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                                                                            @Override
                                                                            public void onHidden(FloatingActionButton fab) {
                                                                                super.onHidden(fab);
                                                                                fragment.handleRevelView(pPx, pPy, pStartRadius, pEndRadius);
                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onAnimationCancel(Animator animation) {

                                                                }

                                                                @Override
                                                                public void onAnimationRepeat(Animator animation) {

                                                                }
                                                            }
                                                    ).playOn(fabMain);
                                                }

                                                @Override
                                                public void onAnimationCancel(Animator animation) {
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animator animation) {
                                                }
                                            }).playOn(fabMain);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    findViewById(R.id.fab_add_new).setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }
                            }).playOn(findViewById(R.id.fab_add_new));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    findViewById(R.id.fab_load).setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            }).playOn(findViewById(R.id.fab_load));
        } else {
            fabMain.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    fragment.handleRevelView(pPx, pPy, pStartRadius, pEndRadius);
                }
            });
        }
    }

    public void showInterstitialAd(final View pView) {
        if(AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.BLOG)) {
            Log.i(TAG, "====>     Current screen: Blog!!!");
            if(AppManager.INTERSTITIAL_CONTROL_BLOG >= 3) {
                if (interstitialAd != null && interstitialAd.isLoaded() && !interstitialAdShowed) {
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            interstitialAdShowed = true;
                            pView.setVisibility(View.VISIBLE);
                            super.onAdClosed();
                        }
                    });
                    interstitialAd.show();
                    return;
                }
                pView.setVisibility(View.VISIBLE);
            } else {
                pView.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (interstitialAd != null && interstitialAd.isLoaded() && !interstitialAdShowed) {
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    fragment.startCalcule();
                    interstitialAdShowed = true;
                    super.onAdClosed();
                }
            });
            interstitialAd.show();
        } else {
            fragment.startCalcule();
        }
    }

    private void showFGTSPeriodos() {
        if(screenSaved) {
            restoreButtons();
        } else {
            showFab();
        }
        AppManager.CURRENT_SCREEN = AppManager.SCREEN.FGTS_PERIODOS;
        Class fragmentClass = FGTSPeriodos.class;
        try {
            fragment = (AbstractFragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private void showTabelaINPC() {
        if(AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.FGTS_PERIODOS)) saveScreen();
        AppManager.CURRENT_SCREEN = AppManager.SCREEN.TABELA_INPC;
        Class fragmentClass = INPC.class;
        try {
            fragment = (AbstractFragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private void showTabelaTR() {
        if(AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.FGTS_PERIODOS)) saveScreen();
        AppManager.CURRENT_SCREEN = AppManager.SCREEN.TABELA_TR;
        Class fragmentClass = TR.class;
        try {
            fragment = (AbstractFragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private void showScreenMore() {
        if(AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.FGTS_PERIODOS)) saveScreen();
        AppManager.CURRENT_SCREEN = AppManager.SCREEN.MORE;
        Class fragmentClass = MoreApps.class;
        try {
            fragment = (AbstractFragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private void showAboutScreen() {
        if(AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.FGTS_PERIODOS)) saveScreen();
        AppManager.CURRENT_SCREEN = AppManager.SCREEN.ABOUT;
        Class fragmentClass = About.class;
        try {
            fragment = (AbstractFragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private void showBlog() {
        if(AppManager.CURRENT_SCREEN.equals(AppManager.SCREEN.FGTS_PERIODOS)) saveScreen();
        hideFabs();
        AppManager.CURRENT_SCREEN = AppManager.SCREEN.BLOG;
        Class fragmentClass = Blog.class;
        try {
            fragment = (AbstractFragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private void saveScreen() {
        screenSaved = true;
        fabMainVisibility = fabMain.getVisibility();
        fabAddNewVisibility = fabAddNew.getVisibility();
        fabLoadVisibility = fabLoad.getVisibility();

        if(fabLoadVisibility == View.VISIBLE) {
            fabMain.hide();
            fabLoad.hide();
            fabAddNew.hide();
        } else if (fabMainVisibility == View.VISIBLE) {
            fabMain.hide();
        }
    }

    public void restoreButtons() {
        screenSaved = false;
        if(fabLoadVisibility == View.VISIBLE) {
            fabMain.show();
            fabAddNew.show();
            fabLoad.show();
        } else if (fabMainVisibility == View.VISIBLE) {
            fabMain.show();
        }

        fabMain.setVisibility(fabMainVisibility);
        fabAddNew.setVisibility(fabAddNewVisibility);
        fabLoad.setVisibility(fabLoadVisibility);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            CustomDialog customDialog = new CustomDialog(CustomDialog.TYPE.ABOUT);
            customDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    private void loadXml (String xmlFile) {
        indices = new ArrayList<>();
        Indice indice;
        String[] monthsList = getResources().getStringArray(R.array.months_list);
        String[] tr = getResources().getStringArray(R.array.tr);
        String[] inpc = getResources().getStringArray(R.array.inpc);
        for(int i = 0; i < monthsList.length; i++) {
            indice = new Indice();
            indice.type = "tr";
            indice.name = monthsList[i];
            indice.value = tr[i];
            indices.add(indice);
        }
        for(int i = 0; i < monthsList.length; i++) {
            indice = new Indice();
            indice.type = "inpc";
            indice.name = monthsList[i];
            indice.value = inpc[i];
            indices.add(indice);
        }

        buildIndicesData();
/*
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open(xmlFile);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser, xmlFile);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        */
    }

    private void parseXML (XmlPullParser parser, String _xmlFile) throws XmlPullParserException, IOException {
   /*     int eventType = parser.getEventType();
        Indice currentIndice;
        String indiceType = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    if (_xmlFile.equals("indices.xml")) indices = new ArrayList<>();
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
                            break;
                        }
                    }
                    currentIndice = new Indice();
                    currentIndice.type = indiceType;
                    currentIndice.name = parser.getName();
                    currentIndice.value = parser.nextText();
                    if (_xmlFile.equals("indices.xml")) indices.add(currentIndice);
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }
        buildIndicesData();*/
    }

    private void buildIndicesData() {
        int i = 0;
        while (i < indices.size()) {
            if (indices.get(i).type.equals("inpc")) {
                AppManager.INPC.add(indices.get(i));
            } else if (indices.get(i).type.equals("tr")) {
                AppManager.TR.add(indices.get(i));
            }
            i++;
        }
    }

    @Override
    public void onClick(View v) {
        switch (AppManager.CURRENT_SCREEN) {
            case FGTS_PERIODOS:
                handleClick();
                break;
            case TABELA_INPC:
                break;
            case TABELA_TR:
                break;
        }
    }

    public void handleClick() {
        fabMain.setClickable(false);
        if(fabLoad.getVisibility() == View.INVISIBLE) {
            if(Contexts.getInstance().getDataHandler().returnNamesData().getCount() > 0) {

                YoYo.with(Techniques.SlideOutDown).duration(300).withListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                fabMain.setImageResource(R.drawable.ic_clear_white_fb);
                                fabMain.setBackgroundTintList(ColorStateList.valueOf(
                                        getResources().getColor(R.color.red_custom)));
                                fabMain.setRippleColor(getResources().getColor(R.color.colorAccent));
                                fabMain.setClickable(true);
                                YoYo.with(Techniques.SlideInUp).duration(300).withListener(
                                        new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                findViewById(R.id.fab_add_new).setVisibility(View.VISIBLE);
                                                YoYo.with(Techniques.SlideInUp).duration(300).
                                                        playOn(findViewById(R.id.fab_add_new));
                                                findViewById(R.id.fab_load).setVisibility(View.VISIBLE);
                                                YoYo.with(Techniques.SlideInUp).duration(300).
                                                        playOn(findViewById(R.id.fab_load));
                                                fabsShown = true;
                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                fabAddNew.setClickable(true);
                                                fabLoad.setClickable(true);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        }).playOn(fabMain);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).playOn(fabMain);
            } else {
                CustomDialog customDialog = new CustomDialog(CustomDialog.TYPE.ESCOLHER_PERIODO);
                customDialog.show();
            }
        } else {
            fragment.clearScreen();
            YoYo.with(Techniques.SlideOutDown).duration(300).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                    fabAddNew.setClickable(false);
                    fabLoad.setClickable(false);

                    YoYo.with(Techniques.SlideOutDown).duration(300).withListener(
                            new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                    YoYo.with(Techniques.SlideOutDown).duration(300).withListener(
                                            new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {
                                                }

                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    fabMain.setImageResource(R.drawable.ic_add_white_fb);
                                                    fabMain.setBackgroundTintList(ColorStateList.valueOf(
                                                            getResources().getColor(R.color.colorPrimary)));
                                                    fabMain.setRippleColor(getResources().getColor(R.color.other_fgd));
                                                    fabMain.setClickable(true);
                                                    fabsShown = false;
                                                    YoYo.with(Techniques.SlideInUp).duration(300).playOn(fabMain);
                                                }

                                                @Override
                                                public void onAnimationCancel(Animator animation) {
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animator animation) {
                                                }
                                            }).playOn(fabMain);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    findViewById(R.id.fab_add_new).setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }
                            }).playOn(findViewById(R.id.fab_add_new));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    findViewById(R.id.fab_load).setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            }).playOn(findViewById(R.id.fab_load));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_INTERNET_REQUEST) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "=====>     internet permission result granted!!");
            } else {
                Log.i(TAG, "=====>     internet permission result denied!!");
            }
        }

        if(requestCode == PERMISSION_NETWORK_REQUEST) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "=====>     network permission result granted!!");
            } else {
                Log.i(TAG, "=====>     network permission result denied!!");
            }
        }
    }
}

