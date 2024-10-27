package com.example.missingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class VideoStreamingActivity extends AppCompatActivity {

    private static final String TAG = "VideoStreamingActivity";
    private WebSocket webSocket;
    private OkHttpClient client;
    //private String webSocketUrl = "ws://server.com/"; // 실제 서버 URL로 변경
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_streaming);

        imageView = findViewById(R.id.imageView);

        client = new OkHttpClient();
        connectWebSocket();
    }

    private void connectWebSocket() {
        Request request = new Request.Builder().url(webSocketUrl).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                Log.d(TAG, "WebSocket 연결 성공");
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                runOnUiThread(() -> {
                    // 수신된 바이너리 데이터를 이미지로 변환
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes.toByteArray(), 0, bytes.size());
                    if (bitmap != null) {
                        // 기존 Bitmap을 재활용하여 메모리 누수를 방지합니다.
                        if (imageView.getDrawable() != null && imageView.getDrawable() instanceof BitmapDrawable) {
                            Bitmap oldBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            if (oldBitmap != null && !oldBitmap.isRecycled()) {
                                oldBitmap.recycle();
                            }
                        }
                        // ImageView에 새 이미지 설정
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Log.e(TAG, "이미지 변환 실패");
                    }
                });
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket 닫기 중: " + reason);
                webSocket.close(1000, null);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket 닫힘: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                Log.e(TAG, "WebSocket 오류: " + t.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(VideoStreamingActivity.this, "연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(VideoStreamingActivity.this, "재연결 시도 중...", Toast.LENGTH_SHORT).show();
                });

                // 재연결 시도 (예: 5초 후)
                new android.os.Handler().postDelayed(VideoStreamingActivity.this::connectWebSocket, 5000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "앱 종료");
        }
        client.dispatcher().executorService().shutdown();
    }
}