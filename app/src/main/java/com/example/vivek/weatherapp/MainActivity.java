package com.example.vivek.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText etPlaceName;
    Button btnSearch;
    TextView tvSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPlaceName = (EditText) findViewById(R.id.etPlaceName);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        tvSearchResults = (TextView) findViewById(R.id.tvSearchResults);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pn = etPlaceName.getText().toString();
                if (pn.length() == 0) {
                    etPlaceName.setError("Please enter place name");
                    etPlaceName.requestFocus();
                    return;
                }

                Task1 t1 = new Task1();
                t1.execute("http://api.openweathermap.org/data/2.5" + "/weather?units=metric&q=" + pn + "&appid=c6e315d09197cec231495138183954bd");
            }
        });

    }

    class Task1 extends AsyncTask<String, Void, Double> {

        double temp;

        @Override
        protected Double doInBackground(String... strings) {
            String line = "", json = "";
            try {
                URL u = new URL(strings[0]);    //website name
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.connect();

                InputStream is = c.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                while ((line = br.readLine()) != null) {
                    json = json + line + "\n";
                }

                if (json != null) {
                    JSONObject j = new JSONObject(json);    //data se object
                    JSONObject q = j.getJSONObject("main"); //object mein main object
                    temp = q.getDouble("temp");
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, " ", Toast.LENGTH_SHORT).show();
            }
            return temp;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            tvSearchResults.setText("Temp = " + aDouble);
        }
    }
}
