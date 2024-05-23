package com.android.congestionobserver;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CongestionData {
    private List<DataItem> data;

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public static class DataItem {
        @SerializedName("period")
        private int period;

        @SerializedName("average")
        private int average;

        @SerializedName("status")
        private String status;

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public int getAverage() {
            return average;
        }

        public void setAverage(int average) {
            this.average = average;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}