package com.example.yunnnn.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 *
 * Created by yunnnn on 2016/9/20.
 */

public class CatService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("LOG", "onBind");

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LOG", "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("LOG", "onDestroy");
        super.onDestroy();
    }

    ICat.Stub mBinder = new ICat.Stub() {
        @Override
        public String getColor() throws RemoteException {
            return "红色";
        }

        @Override
        public double getWeight() throws RemoteException {
            return 0;
        }

    };
}
