package com.example.missingapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private WebSocket webSocket;
    private OkHttpClient client;
    private String jwtToken;
    private int selectedTargetId = -1;
    private ImageView imageView;
    private TextView statusText, infoText;
    //private String webSocketUrl = "ws://server.com/"; // 실제 서버 URL로 변경

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI 요소 초기화
        imageView = findViewById(R.id.imageView);
        statusText = findViewById(R.id.status_text);
        infoText = findViewById(R.id.info_text);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        Button btnExplore = findViewById(R.id.btn_explore);

        // SharedPreferences에서 토큰 로드
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        jwtToken = sharedPreferences.getString("token", null);

        if (jwtToken == null) {
            Toast.makeText(this, "토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 탐색 버튼 클릭 이벤트
        btnExplore.setOnClickListener(v -> {
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            if (selectedRadioButtonId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                selectedTargetId = Integer.parseInt(selectedRadioButton.getTag().toString());

                // 상태 텍스트 업데이트
                statusText.setText("탐색중");
                infoText.setText("");

                // 웹소켓 연결을 통해 영상 수신 시작
                connectWebSocket();
            } else {
                Toast.makeText(this, "보호대상을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectWebSocket() {
        client = new OkHttpClient();
        Request request = new Request.Builder().url(webSocketUrl).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                Log.d(TAG, "WebSocket 연결 성공");
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                runOnUiThread(() -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes.toByteArray(), 0, bytes.size());
                    imageView.setImageBitmap(bitmap);
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