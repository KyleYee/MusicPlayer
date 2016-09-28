package com.example.yunnnn.musicplayer;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import libcore.io.DiskLruCache;

public class MainActivity extends AppCompatActivity {
    private static final int SIZE_CACHE = 1024 * 1024 * 50;//50M

    ListView mList;
    ImageView imageView;
    ServiceConnection connection;
    DiskLruCache mDiskLruCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageview);
      /*  connection = new conn();
        final Intent intent = new Intent(this, CatService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);*/

   /*     MyIntentService intentService = new MyIntentService();

        intentService.setmOnServiceImp(new MyIntentService.onServiceImp() {
            @Override
            public void onService() {
                Log.e("LOG Activity", "回调成功!");

            }
        });

        final Intent intent = new Intent(this, intentService.getClass());
        Bundle bundle = new Bundle();
        bundle.putSerializable("service", (Serializable) intentService);
        Log.e("LOG Activity", intent.toString() + "-----" + Thread.currentThread().getName());
        startService(intent);
        Log.e("LOG Activity1", intent.toString());
        startService(intent);

        Log.e("LOG Activity2", intent.toString());
        startService(intent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    startService(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/


      /*
        通过looper获取handler
      Looper looper = this.getMainLooper();

        Handler handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(MainActivity.this, "主线程的handler", Toast.LENGTH_SHORT).show();
            }
        };

        handler.sendEmptyMessage(1);
*/

//        线程池学习
//        studyThreadPool();
//         图片加载学习
        studyBitmapFactory();

        //DiskLruCache学习
        studyDiskLruCache();

    }

    /**
     * DiskLruCache学习
     */
    private void studyDiskLruCache() {
        File file = this.getCacheDir();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.e("cache file name", file.getPath());

        try {
            mDiskLruCache  = DiskLruCache.open(file,1,1,SIZE_CACHE);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void studyBitmapFactory() {
        myAsyncTask myAsyncTask = new myAsyncTask(imageView);
        myAsyncTask.execute("http://img4.imgtn.bdimg.com/it/u=1663084906,677407956&fm=21&gp=0.jpg");
    }

    public class myAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        Bitmap bitmap;

        public myAsyncTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            InputStream input = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                input = conn.getInputStream();
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inSampleSize = 2;
                op.inJustDecodeBounds = true;//为true不生成bitmap对象 只加载图片信息
                bitmap = BitmapFactory.decodeStream(input, null, op);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() * 1024 * 1024 / 8;

            LruCache<String, Bitmap> lru = new LruCache<String, Bitmap>(memClass) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount() / 1024;
                }
            };

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }

        }
    }

    /**
     * 线程池学习
     */
    private void studyThreadPool() {
        BlockingDeque<Runnable> blockingDeque = new LinkedBlockingDeque<>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10, 100, TimeUnit.MICROSECONDS, blockingDeque, new ThreadPoolExecutor.DiscardPolicy());

        for (int i = 0; i < 1000; i++) {

            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    Log.e("thread", Thread.currentThread() + "");
                }
            });
        }
    }


    public class conn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ICat mBinder = ICat.Stub.asInterface(service); //asInterface
            try {
                Log.e("LOG", mBinder.getColor() + "");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}
