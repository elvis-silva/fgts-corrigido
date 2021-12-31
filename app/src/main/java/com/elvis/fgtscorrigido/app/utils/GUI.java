package com.elvis.fgtscorrigido.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.widget.Toast;

import com.elvis.fgtscorrigido.app.R;
import com.elvis.fgtscorrigido.app.context.Contexts;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by elvis on 18/06/15.
 */
public class GUI {

    private static Context context = Contexts.getInstance().getCurrentActivity().getApplicationContext();
    public static final int NO_ICON_RES = 0x0;

    private static Handler guiHandler = new Handler();
    private static ScheduledExecutorService delayPostExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

    public static void toastShort(CharSequence pMessage){
        Toast.makeText(context, pMessage, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(CharSequence pMessage) {
        Toast.makeText(context, pMessage, Toast.LENGTH_LONG).show();
    }

    public static void alert(CharSequence pTitle, CharSequence pMessage, String pOkText, int pIcon){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        if(pTitle!=null){
            alertDialog.setTitle(pTitle);
        }
        alertDialog.setMessage(pMessage);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, pOkText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if(pIcon != NO_ICON_RES){
            alertDialog.setIcon(pIcon);
        }
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    static public void doBusy(Context pContext, String pMessage, Runnable pRunnable){
        doBusy(pContext, pMessage, pRunnable, 500);
    }

    static public void doBusy(Context pContext, IBusyRunnable pRunnable){
        doBusy(pContext, (Runnable) pRunnable);
    }

    static public void doBusy(Context pContext, String pMessage, IBusyRunnable pRunnable){
        doBusy(pContext, (Runnable) pRunnable);
    }
    static public void doBusy(Context pContext, Runnable pRunnable){
        doBusy(pContext ,pContext.getString(R.string.cmsg_busy), pRunnable);
    }

    static public void doBusy(Context pContext, String pMessage, Runnable pRunnable, final long pDelay) {
        final ProgressDialog progressDialog = new ProgressDialog(pContext);
        progressDialog.setMessage(pMessage);
        progressDialog.setTitle(null);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        if(pContext instanceof Activity) {
            lockOrientation((Activity) pContext);
        }
        final BusyRunnable busyRunnable = new BusyRunnable(pContext, progressDialog, pRunnable);
        singleExecutor.submit(busyRunnable);
        delayPost(new Runnable() {
            @Override
            public void run() {
                synchronized (busyRunnable) {
                    if (!busyRunnable.finish) {
                        progressDialog.show();
                        busyRunnable.showing = true;
                    }
                }
            }
        });
    }

    static public void delayPost(final Runnable r){
        delayPost(r, 50);
    }
    static public void delayPost(final Runnable r,final long delay){
        delayPostExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                }
                post(r);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    static public void lockOrientation(Activity pActivity){
        switch (pActivity.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                pActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                pActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    static public void post(Runnable r){
        guiHandler.post(new NothrowRunnable(r));
    }

    static private class NothrowRunnable implements Runnable{
        Runnable r;
        public NothrowRunnable(Runnable r){
            this.r = r;
        }
        @Override
        public void run() {
            try{
                r.run();
            }catch(Exception e){
//                Logger.e(e.getMessage(), e);
            }
        }
    }

    static private class BusyRunnable implements Runnable {
        ProgressDialog progressDialog;
        Context context;
        Runnable runnable;
        volatile boolean showing = false;
        volatile boolean finish = false;

        public BusyRunnable(Context pContext, ProgressDialog pProgressDialog, Runnable pRunnable) {
            context = pContext;
            progressDialog = pProgressDialog;
            runnable = pRunnable;
        }

        @Override
        public void run() {
            final FinalVar<Throwable> x = new FinalVar<>();
            try {
                runnable.run();
            } catch (Throwable t) {
                x.value = t;
            }

            // close dialog if it is showing
            synchronized (this) {
                finish = true;
                if(showing) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            showing = false;
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }

            // notify success of error
            if(runnable instanceof IBusyRunnable) {
                if(x.value == null) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            ((IBusyRunnable) runnable).onBusyFinish();
                        }
                    });
                } else {
                    if(runnable instanceof IBusyRunnable) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                ((IBusyRunnable) runnable).onBusyError(x.value);
                            }
                        });
                    }
                }
            }

            // release orientation lock
            post(new Runnable() {
                @Override
                public void run() {
                    if(context instanceof Activity) {
                        releaseOrientation((Activity) context);
                    }
                }
            });
        }
    }

    static public void releaseOrientation(Activity pActivity) {
        pActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * on busy event will be invoked in gui thread.
     */
    public interface IBusyRunnable extends Runnable{
        void onBusyFinish();
        void onBusyError(Throwable t);
    }

    public static abstract class BusyAdapter implements IBusyRunnable{

        @Override
        public void onBusyFinish() {
        }

        @Override
        public void onBusyError(Throwable t) {
//            Logger.e(t.getMessage(),t);
        }
    }
}
