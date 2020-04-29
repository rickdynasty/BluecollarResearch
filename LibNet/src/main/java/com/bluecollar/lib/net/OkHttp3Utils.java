package com.bluecollar.lib.net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class OkHttp3Utils {
    public static String getSync(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();  //创建url的请求

        try {
            Response response = client.newCall(request).execute();  //execute() : 同步, enqueue() : 异步
            return response.body().string();  //获取数据
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String postSync(String url, String body) {  //body为参数列表
        String s = "";
        OkHttpClient client = new OkHttpClient();
        //"application/x-www-form-urlencoded"  form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), body);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
