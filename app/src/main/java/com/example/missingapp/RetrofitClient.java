package com.example.missingapp;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final Object lock = new Object();
    private static String baseUrl = "http://223.130.152.183:8080";

    public static Retrofit getClient(String token) {
        if (retrofit == null || !retrofit.baseUrl().toString().equals(baseUrl)) {
            synchronized (lock) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(chain -> {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Authorization", "Bearer " + token); // "Bearer "를 포함한 형식
                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        })
                        .addInterceptor(loggingInterceptor)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build();

                retrofit = new Retrofit.Builder()
                        .client(client)
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }
}
