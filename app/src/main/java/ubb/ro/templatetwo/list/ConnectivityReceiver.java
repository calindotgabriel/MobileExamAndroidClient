package ubb.ro.templatetwo.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by calin on 23.01.2017.
 */

public class ConnectivityReceiver extends BroadcastReceiver {
    private static final String TAG = "TemplateApp";

    ConnectivityStateListener listener;

    private boolean mConnected;

    public void setListener(ConnectivityStateListener listener) {
        this.listener = listener;
        notifyListener();
    }

    public void notifyListener() {
        if (listener != null) {
            if (mConnected){
                listener.onConnected();
            } else {
                listener.onDisconnected();
            }
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();
        if (ni != null) {
            final NetworkInfo.State netState = ni.getState();
            Log.d(TAG, netState.toString());
            if (netState == NetworkInfo.State.CONNECTED) {
                mConnected = true;
                notifyListener();
            } else if (netState == NetworkInfo.State.DISCONNECTED) {
                mConnected = false;
                notifyListener();
            }
        } else {
            mConnected = false;
            notifyListener();
        }

    }
}
