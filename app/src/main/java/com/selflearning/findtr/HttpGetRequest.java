package com.selflearning.findtr;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.selflearning.findtr.DataHolder.updateData;
import static com.selflearning.findtr.R.id.trainNumber;

public class HttpGetRequest extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    String result;
    JSONObject resultJsonObject;
    DateFormat df;

    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String returnString="";
        String inputLine;
        df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Connect to our url
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
            Log.v("Log Message",result);
        }
        catch(IOException e){
            Log.v("Url connection message",e.getMessage());
            result = null;
        }
        try {
            resultJsonObject = new JSONObject(result);
            JSONObject CurrentStatus = resultJsonObject.getJSONObject("CurrentStation");
            returnString=" Last Updated Staion:"+CurrentStatus.getString("StationName")+"\n Arrived at "+ CurrentStatus.getString("ActualArrival") +" Delay: "+ CurrentStatus.getString("DelayInArrival");
            JSONArray TrainRoute=resultJsonObject.getJSONArray("TrainRoute");
            returnString+=" \n Total stopping station: "+ TrainRoute.length()+" ";
            for(int i=0;i<TrainRoute.length();i++)
            {
                String aD=TrainRoute.getJSONObject(i).getString("DelayInArrival").toString();
                String dD= TrainRoute.getJSONObject(i).getString("DelayInDeparture").toString().toString();
                Boolean b=(aD.equals("00 M") && dD.equals("00 M"));
                Log.v("i value",""+ i +" if value "+ b);
                if(aD.equals("00 M") && dD.equals("00 M") ){
                    Log.v("Next Stoppage  ",TrainRoute.getJSONObject(i).getString("StationName"));
                    returnString+="\n Next Stoppage: "+ TrainRoute.getJSONObject(i).getString("StationName")+" ";
                    break;
                }
            }
            //JSONArray subArray=
            //return returnString;
        }
        catch (Exception e)
        {
            Log.v("Url connection message",e.getMessage());
        }

        DataHolder.updateData("00000",returnString);
        return returnString;
    }
    protected void onPostExecute(String result){
        super.onPostExecute(result);


    }
}
