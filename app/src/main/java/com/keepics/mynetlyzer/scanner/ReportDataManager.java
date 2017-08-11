package com.keepics.mynetlyzer.scanner;

import android.content.Context;

public class ReportDataManager {
    private static ReportDataManager instance;


    public String alternativeChannelString = "";
    public String alternativeBand = "";
    public static int band;

    public ReportDataManager(){
        instance = this;
    }

    public static ReportDataManager getInstance(){
        if(instance == null){
            instance = new ReportDataManager();
        }
        return instance;
    }

    public void initialize(Context ctx){

    }
}
