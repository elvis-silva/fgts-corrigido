package com.elvis.fgtscorrigido.app.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.elvis.fgtscorrigido.app.FGTSCorrigido;
import com.elvis.fgtscorrigido.app.MainActivity;
import com.elvis.fgtscorrigido.app.context.Contexts;

/**
 * Created by elvis on 22/06/15.
 */
public class IntentUtils {

    static public void intentGooglePlay(String pPackageName) {
        String url;
        Activity mainActivity = Contexts.getInstance().getMainActivity();
        try {
            mainActivity.getPackageManager().getPackageInfo("com.android.vending", 0);

            url = "market://details?id=" + pPackageName;
        } catch ( final Exception e ) {
            url = "https://play.google.com/store/apps/details?id=" + pPackageName;
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mainActivity.startActivity(intent);
    }

    static public void intentBrowser(String pUrl) {
        Uri uri = Uri.parse(pUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Contexts.getInstance().getMainActivity().startActivity(intent);
    }
}
