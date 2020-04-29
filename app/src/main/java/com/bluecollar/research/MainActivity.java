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

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MyHandler(this);

        setContentView(R.layout.activity_main);

        findViewById(R.id.get_baidu).setOnClickListener(this);
        findViewById(R.id.get_baidu_async).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_baidu:
                getBaidu();
                break;
            case R.id.get_baidu_async:
                getBaiduAsync();
                break;
        }
    }

    // 同步获取需要开线程，否则会异常[防止阻塞主线程]
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

    // 异步获取数据不需要开线程
    private void getBaiduAsync() {
        OkHttp3Utils.getAsync("https://www.baidu.com", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.obj = "获取百度数据失败";
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = handler.obtainMessage();
                message.obj = response.body().string();
                ;
                handler.sendMessage(message);
            }
        });
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
