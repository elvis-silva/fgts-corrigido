package com.elvis.fgtscorrigido.app.impl;

import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

public interface IActivity {
    void setContentView(View pView);
    View findViewById(int pViewId);
    void onResume();
    void onPause();
    void onDestroy();
    void onBackPressed();
    boolean onKeyDown(int keyCode, KeyEvent keyEvent);
    View rootView();
    void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);
    boolean onContextItemSelected(MenuItem item);
    void setMode(int pMode);
    void reloadData();
}
