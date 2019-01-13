package com.selflearning.findtr;

/**
 * Created by venkat on 06-01-2019.
 */

public class DataHolder {
    public static String TrainNumber;
    public static String CurrentStatus="Please provide Train details in App.";
    public static  void updateData(String a,String b)
    {
        TrainNumber=a;
        CurrentStatus=b;
    }
    public static  String getStatus()
    {
        return CurrentStatus;
    }
}
