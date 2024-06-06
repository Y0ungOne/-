package com.android.congestionobserver;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.missingapp.MainActivity;
import com.example.missingapp.R;
import com.example.missingapp.databinding.FragmentMonthBinding;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MonthFragment extends Fragment {
    private FragmentMonthBinding binding;

    // 실시간
    private static final String BASE_URL = "http://192.168.219.111:8080/monthly-congestion?cctvId=";

    private String cctvId = "1";

    private ArrayList<String> years = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMonthBinding.inflate(inflater, container, false);
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
        getCongestionData();
//
//
//        years.add("2010");
//        years.add("2011");
//        years.add("2012");
//        years.add("2013");
//        years.add("2014");
//        years.add("2015");
//        years.add("2016");
//        years.add("2017");
//        years.add("2018");
//        years.add("2019");
//        years.add("2020");
//        years.add("2021");
//        years.add("2022");
//        years.add("2023");
//        years.add("2024");
//
//        binding.spYear.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, years));


    }

    List<BarEntry> entries = new ArrayList<>();

    private void initBarChart() {

        // 데이터셋 생성
        BarDataSet dataSet = new BarDataSet(entries, "월간 인구 밀집도");
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
        description.setText("월간 인구 밀집도");
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
                String status = entry.getData().toString();
//                binding.tvNotify.setText("현재 월간 인구 밀집도는 " + entry.getY()+"("+status+")" + " 입니다. ");

                binding.tvNotify.setText(+ (int)entry.getX() + "월 인구 밀집도는 " + entry.getY()+"("+status+")" + " 입니다. ");

            }

            @Override
            public void onNothingSelected() {
                // 아무 값도 선택되지 않았을 때 호출됩니다.
            }
        });

        binding.rgCctvs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.rb_cctv1) {
                    cctvId = "1";
                } else if (i == R.id.rb_cctv2) {
                    cctvId = "2";
                } else if (i == R.id.rb_cctv3) {
                    cctvId = "3";
                } else if (i == R.id.rb_cctv4) {
                    cctvId = "4";
                }

                entries.clear();
                getCongestionData();
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
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int period = jsonArray.getJSONObject(i).getInt("period");
                        int average = jsonArray.getJSONObject(i).getInt("average");
                        String status = jsonArray.getJSONObject(i).getString("status");

                        Log.i("##INFO", "onPostExecute(): period = " + period + ", average = " + average + ", status = " + status);

                        entries.add(new BarEntry(i+1, (float) average, status));

                    }

                    initBarChart();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            } else {
                Log.e("##ERROR", "onPostExecute(): result is null");
            }
        }
    }
}