package com.bluecollar.research;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bluecollar.lib.net.OkHttp3Utils;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MyHandler(this);

        setContentView(R.layout.activity_main);

        findViewById(R.id.get_baidu).setOnClickListener(this);
        findViewById(R.id.get_weather).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_baidu:
                getBaidu();
                break;
            case R.id.get_weather:
                break;
        }
    }

    private void getBaidu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = OkHttp3Utils.getSync("https://www.baidu.com");
                //异步大师要我们按规范做事
                Message message = handler.obtainMessage();
                message.obj = s;
                handler.sendMessage(message);
            }
        }).start();
    }

    private String getTodayWeather() {
        String apiurl = "http://openapi.tuling123.com/openapi/api/v2";
        String apiKey = "1a962774982540bfaf043648e745bf6d";
        String userId = "585719";
        return OkHttp3Utils.getSync("www.baidu.com");
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = mActivity.get();
            if (null == activity)
                return;

            if (null != msg.obj) {
                String str = msg.obj.toString();
                Log.d(TAG, str);
                Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "str is null", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
