package ubb.ro.templatetwo;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import ubb.ro.templatetwo.list.ConnectivityReceiver;
import ubb.ro.templatetwo.list.ConnectivityStateListener;

/**
 * Created by calin on 23.01.2017.
 */

public class EventApplication extends Application {

    private ConnectivityReceiver mReceiver;

    public void setConnectivityListener(ConnectivityStateListener listener) {
        mReceiver.setListener(listener);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new ConnectivityReceiver();
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(mReceiver);
    }
}
