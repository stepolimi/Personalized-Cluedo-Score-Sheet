package com.example.cluedokiler.Statistics;

public class PersonalStats {

    private String statistic;
    private String value;

    public PersonalStats(String statistic, String value) {
        this.statistic = statistic;
        this.value = value;
    }

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
