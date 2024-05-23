package com.example.missingapp;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenericCallback<T> implements Callback<T> {
    private Context context;
    private String action;

    public GenericCallback(Context context, String action) {
        this.context = context;
        this.action = action;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            Toast.makeText(context, action + "이(가) 변경되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, action + " 변경 실패: " + response.code(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Toast.makeText(context, action + " 변경 중 서버 연결 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
