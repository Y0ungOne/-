package com.example.missingapp;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class RetryInterceptor implements Interceptor {
    private int maxRetry;
    private int retryCount = 0;

    public RetryInterceptor(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        boolean responseOK = false;
        while (!responseOK && retryCount < maxRetry) {
            try {
                response = chain.proceed(request);
                responseOK = response.isSuccessful();
            } catch (Exception e) {
                retryCount++;
            }
        }
        if (response == null) {
            throw new IOException("Max retries reached");
        }
        return response;
    }
}
