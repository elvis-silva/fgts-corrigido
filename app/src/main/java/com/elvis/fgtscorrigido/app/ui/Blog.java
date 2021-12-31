package com.elvis.fgtscorrigido.app.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elvis.fgtscorrigido.app.AppManager;
import com.elvis.fgtscorrigido.app.MainActivity;
import com.elvis.fgtscorrigido.app.R;
import com.elvis.fgtscorrigido.app.context.Contexts;

import im.delight.android.webview.AdvancedWebView;

/**
 * Created by elvis on 16/01/17.
 */

public class Blog extends AbstractFragment implements AdvancedWebView.Listener{

    private static final String TAG = Blog.class.getSimpleName();
    private AdvancedWebView mWebView;
    private ProgressBar progressBar;
    private TextView messageConect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_blog, container, false);
        progressBar = (ProgressBar) contentView.findViewById(R.id.progress_bar);
        messageConect = (TextView) contentView.findViewById(R.id.message_conect);
        mWebView = (AdvancedWebView) contentView.findViewById(R.id.web_view);
        mWebView.setListener(Contexts.getInstance().getMainActivity(), this);
        mWebView.loadUrl("http://fgts-corrigido.blogspot.com.br");

        return contentView;
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        if(messageConect.getVisibility() == View.VISIBLE) messageConect.setVisibility(View.INVISIBLE);
        if(progressBar.getVisibility() == View.INVISIBLE) {
            mWebView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        ((MainActivity) Contexts.getInstance().getMainActivity()).exitControl = 0;
        //Log.i(TAG, "=====>    Page started successfull!!! ");
        //Log.i(TAG, "=====>    url:" + mWebView.getUrl());
    }

    @Override
    public void onPageFinished(String url) {
        if(progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
            ((MainActivity) Contexts.getInstance().getMainActivity()).showInterstitialAd(mWebView);
            //mWebView.setVisibility(View.VISIBLE);
        }
        AppManager.INTERSTITIAL_CONTROL_BLOG += 1;
        //Log.i(TAG, "=====>    Page finished successfull!!! " + AppManager.INTERSTITIAL_CONTROL_BLOG);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        mWebView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        messageConect.setVisibility(View.VISIBLE);
        //Log.i(TAG, "=====>    Page error!!!");
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType,
                                    long contentLength, String contentDisposition, String userAgent) {
    }

    @Override
    public void onExternalPageRequest(String url) {
    }

    @Override
    public boolean onBackPressed() {
        //Log.i(TAG, "=====>     " + String.valueOf(mWebView.onBackPressed()));
        return mWebView.onBackPressed();
    }
}
