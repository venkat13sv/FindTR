package com.selflearning.findtr;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.selflearning.findtr.DataHolder.updateData;
import static com.selflearning.findtr.R.id.trainNumber;

public class MainActivity extends AppCompatActivity {
    String myUrl,date;
    TextView tv1;
    EditText trainNumber;
    String msg="Fetching Data... Please wait..";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String fDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        Date cDate = new Date();
        fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        Log.v("Date Text",fDate);


        tv1=(TextView) findViewById(R.id.text_view_id);
        trainNumber=(EditText) findViewById(R.id.trainNumber);
        tv1.setText("Testing");
        Button button = (Button) findViewById(R.id.button1);
        //String regex = "[0-9]+";
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            doBackground();

            }
        });

    }

  public void doBackground()
  {
      tv1.setText(msg);
      String delegate = "hh:mm aaa";
      Log.v("time",DateFormat.format(delegate, Calendar.getInstance().getTime()).toString().replace(" ",""));

      if(trainNumber.getText().toString().equals(""))
          tv1.setText("Please Enter a Train Number");
      else {
          editor.putString("trNumber",trainNumber.getText().toString());

          Log.v("Main Activit",DataHolder.getStatus());
          myUrl = "https://indianrailapi.com/api/v2/livetrainstatus/apikey/6f446204319af149bd8b07908793cf56/trainnumber/"+trainNumber.getText().toString()+"/date/"+fDate.replace("-","")+"/";
          String result;
          try {
              HttpGetRequest getRequest = new HttpGetRequest();
              result = getRequest.execute(myUrl).get();
              Log.v("API Response",result);

              DataHolder.updateData(trainNumber.getText().toString(),result);
              editor.putString("trStatus",result);
              editor.commit();
              tv1.setText(result);
          } catch (Exception e) {

          }
      }
  }


}
