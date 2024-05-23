package com.android.congestionobserver;

public class RadioData {
    Boolean isChecked;
    String name;


    public RadioData(Boolean isChecked, String name) {
        this.isChecked = isChecked;
        this.name = name;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
