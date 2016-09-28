package com.example.yunnnn.musicplayer;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yunnnn on 2016/9/22.
 */

public class MyIntentService extends IntentService {
    public MyIntentService(String name) {
        super(name);
    }

    public MyIntentService() {
        super(null);
    }

    public interface onServiceImp {
        void onService();
    }

    private onServiceImp mOnServiceImp;

    public void setmOnServiceImp(onServiceImp mOnServiceImp) {
        this.mOnServiceImp = mOnServiceImp;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
       MyIntentService serializableExtra = (MyIntentService) intent.getSerializableExtra("service");
        mOnServiceImp = (onServiceImp) serializableExtra;
        try {
            Thread.sleep(1000);
            Log.e("LOG Service", "我睡了一秒 哈哈哈哈哈");
            Log.e("LOG Service", "onHandleIntent:" + Thread.currentThread().getName());
            mOnServiceImp.onService();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e("LOG Service", intent.toString());
    }

    @Override
    public void onCreate() {
        Log.e("LOG Service", "onCreate:" + Thread.currentThread().getName());
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("LOG Service", "onDestroy");
        super.onDestroy();
    }


}
