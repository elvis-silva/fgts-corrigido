package com.elvis.fgtscorrigido.app.context;

import android.app.Activity;
import android.content.Context;

import com.elvis.fgtscorrigido.app.data.DataHandler;
import com.elvis.fgtscorrigido.app.utils.GUI;

/**
 * Created by elvis on 17/06/15.
 */
public class Contexts {

    private static Contexts instance;
    private Context context;
    private Activity activity, lastActivity;
    private DataHandler dataHandler;

    private Contexts() {
    }

    public static Contexts getInstance() {
        if(instance == null) {
            synchronized (Contexts.class) {
                if (instance == null) {
                    instance = new Contexts();
                }
            }
        }
        return instance;
    }

    public boolean initActivity(Activity pActivity) {
        if(context == null) {
            initApp(pActivity, pActivity);
        }
        if(activity != pActivity) {
            lastActivity = activity;
            activity = pActivity;
            return true;
        }
        return false;
    }

    private synchronized boolean initApp(Object pAppInitial, Context pContext) {
        if(context == null) {
            context = pContext.getApplicationContext();
            activity = (Activity) pAppInitial;
            initDataHandler();
            return true;
        }
        return false;
    }

    private void initDataHandler() {
        dataHandler = new DataHandler(context);
        dataHandler.open();
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public boolean returnToLastActivity(){
        return initActivity(lastActivity);
    }

    public Activity getCurrentActivity() {
        return activity;
    }

    public Context getContext() {
        return context;
    }

    public Activity getMainActivity() {
        return activity;
    }
}
