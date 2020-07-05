package com.example.cameraalbumtest;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil{
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static void sendOkHttpRequestGet(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequestPost(String address, RequestBody requestBody, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequestPostFile(String address, File file, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
//        填入文件路径
        Request request = new Request.Builder()
                .url(address)
                .post(RequestBody.create(file,MEDIA_TYPE_MARKDOWN))
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequestGetFile(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpRequestSendMultipart(String address, MultipartBody.Builder builder, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Authorization","")
                .url(address)
                .post(builder.build())
                .build();
        client.newCall(request).enqueue(callback);
    }
}
