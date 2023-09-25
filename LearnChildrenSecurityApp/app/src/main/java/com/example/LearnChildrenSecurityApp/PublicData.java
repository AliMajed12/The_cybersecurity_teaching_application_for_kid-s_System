package com.example.LearnChildrenSecurityApp;

import android.app.Application;

public class PublicData extends Application {
    private static PublicData PublicData;
    public static  String UserName="";
    public static  String UserType="";
    public static  String UserPassword="";
    public static  String UserID="";
    public static  String  Email="";
    public static  String  LessonID="";
    public static  String  LessonName="";
    public static  String  LessonDetails="";
    public static  String  LessonImage="";

    public static  String EvaluateID="";


    public static PublicData getInstance(      )
    {
        return PublicData;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PublicData = this;
    }
}