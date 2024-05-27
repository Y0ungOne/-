package com.android.congestionobserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.missingapp.MainActivity;
import com.example.missingapp.databinding.FragmentLiveBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LiveFragment extends Fragment {
    private FragmentLiveBinding binding;
    private RadioAdapter radioAdapter;
    private ArrayList<RadioData> floorList = new ArrayList<>();


    // 실시간
    private static final String BASE_URL = "http://223.130.152.183:8080/live-congestion?cctvId=";
    private String cctvId = "1";
    public String status = "원할";
    private int currentFloor = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLiveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setEvent();
        // 뒤로 가기 버튼 클릭 리스너 추가
        binding.imBack.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish(); // 현재 액티비티를 종료하여 뒤로가기 스택을 정리
        });
    }

    private void init() {
        floorList.add(new RadioData(true, "1층"));
        floorList.add(new RadioData(false, "2층"));
        floorList.add(new RadioData(false, "3층"));
        floorList.add(new RadioData(false, "4층"));
        floorList.add(new RadioData(false, "5층"));
        floorList.add(new RadioData(false, "6층"));
        floorList.add(new RadioData(false, "7층"));
        floorList.add(new RadioData(false, "8층"));
        floorList.add(new RadioData(false, "9층"));
        floorList.add(new RadioData(false, "10층"));


        radioAdapter = new RadioAdapter(floorList, requireContext(), new RadioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                for (int i = 0; i < floorList.size(); i++) {
                    if (i == position) {
                        floorList.get(i).setChecked(true);
                    } else {
                        floorList.get(i).setChecked(false);
                    }
                }
                currentFloor = position + 1;
                radioAdapter.notifyDataSetChanged();

                binding.tvNotify.setText("현재 " + floorList.get(position).getName() + " 입니다");

                getCongestionData();
            }
        });

        binding.reFloor.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.reFloor.setAdapter(radioAdapter);

        getCongestionData();
    }

    List<BarEntry> entries = new ArrayList<>();

    private void initBarChart() {

        // 데이터셋 생성
        BarDataSet dataSet = new BarDataSet(entries, "실시간 인구 밀집도");
        setupDataSet(dataSet);

        // BarData 객체 생성 및 데이터 설정
        BarData barData = new BarData(dataSet);
        binding.chBar.setData(barData);

        // X축 설정
        XAxis xAxis = binding.chBar.getXAxis();
        setupXAxis(xAxis);

        // Y축 설정
        YAxis leftAxis = binding.chBar.getAxisLeft();
        YAxis rightAxis = binding.chBar.getAxisRight();
        setupYAxis(leftAxis, rightAxis);

        // 범례 설정
        Legend legend = binding.chBar.getLegend();
        setupLegend(legend);

        // 차트 설명 설정
        Description description = new Description();
        setupDescription(description);
        binding.chBar.setDescription(description);

        // 차트 스타일 설정
        setupChartStyle();
        binding.chBar.animateY(1000);
        binding.chBar.setScaleEnabled(false);
        binding.chBar.setExtraTopOffset(30f);
    }

    private void setupDataSet(BarDataSet dataSet) {
        dataSet.setColor(Color.parseColor("#1976D2")); // 막대 색상 설정
        dataSet.setValueTextColor(Color.RED); // 데이터 값 텍스트 색상 설정
    }

    private void setupXAxis(XAxis xAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
    }

    private void setupYAxis(YAxis leftAxis, YAxis rightAxis) {
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextColor(Color.BLUE);
        rightAxis.setEnabled(false);
    }

    private void setupLegend(Legend legend) {
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(20f);
        legend.setTextColor(Color.BLACK);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }

    private void setupDescription(Description description) {
        description.setText("실시간 인구 밀집도");
    }

    private void setupChartStyle() {
        BarChart barChart = binding.chBar;
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawBorders(false);
        Description description = barChart.getDescription();
        description.setEnabled(false);
    }


    private void setEvent() {
        binding.chBar.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // 차트의 값이 선택되었을 때 호출됩니다.
                BarEntry entry = (BarEntry) e;

                binding.tvNotify.setText("현재 " + currentFloor+"층 " +(int)entry.getX() + "번 구역은 " +entry.getY()+"("+status+")" + " 입니다");
            }

            @Override
            public void onNothingSelected() {
                // 아무 값도 선택되지 않았을 때 호출됩니다.
            }
        });
    }

    private void getCongestionData() {
        // 서버에서 혼잡도 데이터를 가져오는 로직
        // 서버에서 가져온 데이터를 바탕으로 혼잡도를 계산하여 화면에 표시

        new FetchDataTask().execute(BASE_URL+cctvId);
    }


    private class FetchDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String urlString = strings[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                } else {
                    Log.e("##ERROR", "doInBackground: responseCode = " + responseCode);
                }
            } catch (IOException e) {
                Log.e("##ERROR", "doInBackground: error = " + e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {

                        reader.close();
                    } catch (IOException e) {
                        Log.e("##ERROR", "doInBackground: error = " + e.getMessage());
                    }
                }
            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // 여기서 결과를 처리합니다. 예를 들어, UI 업데이트 등.
                Log.i("##INFO", "onPostExecute(): result = " + result);

                JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
                status = jsonObject.get("status").getAsString();
                Log.i("##INFO", "onPostExecute(): status = "+status);




                switch (status) {
                    case "normal":
//                        binding.tvNotify.setText("현재 " + floorList.get(0).getName() + " E구역은 원활 구역 입니다");
                        status = "원할";
                        break;

                    case "confusion" :
//                        binding.tvNotify.setText("현재 " + floorList.get(1).getName() + " E구역은 혼잡 구역 입니다");
                        status = "혼잡";
                        break;

                    case "not many people" :
//                        binding.tvNotify.setText("현재 " + floorList.get(2).getName() + " E구역은 여유 구역 입니다");
                        status = "여유";
                        break;
                }

                entries.clear();
                entries.add(new BarEntry(1, 6, result));
                entries.add(new BarEntry(2, 8, result));

                initBarChart();
            } else {
                Log.e("##ERROR", "onPostExecute(): result is null");
            }


        }
    }
}