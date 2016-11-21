package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.util.GregorianCalendar;

public class ChartActivity extends Activity {

    private final String TAG = Utils.class.getSimpleName();
    private OkHttpClient client;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent i = getIntent();

        mCalendar = Calendar.getInstance();

        String sym = i.getStringExtra("EXTRA_SYMBOL");

        String query = "select * from yahoo.finance.historicaldata where symbol ='" + sym + "' and startDate = " + getDate(7) + " and endDate = " + getDate(0);

        String url = "https://query.yahooapis.com/v1/public/yql";
        String call_val = "";
        String call = "callback";
        String env_val = "store://datatables.org/alltableswithkeys";
        String env = "env";
        String dia_val = "true";
        String dia = "diagnostics";
        String query_key = "q";
        String search_val = "json";
        String search = "format";
        Uri uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter(query_key, query)
                .appendQueryParameter(search, search_val)
                .appendQueryParameter(dia, dia_val)
                .appendQueryParameter(env, env_val)
                .appendQueryParameter(call, call_val)
                .build();
        Log.v("url", uri.toString());

        doGetRequest(uri.toString());
    }

    private String getDate(int offset) {

        int iToday = mCalendar.get(Calendar.DAY_OF_MONTH);
        int iMonth = mCalendar.get(Calendar.MONTH) + 1;

        if (iToday - offset < 0) {
            iMonth = iMonth - 1;

            Calendar mycal = new GregorianCalendar(Calendar.YEAR, iMonth, 1);
            int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28int iYear = 1999;
            int offsetDay = daysInMonth - (offset - iToday);
            return "'" + mCalendar.get(Calendar.YEAR) + "-" + iMonth + "-" + offsetDay + "'";
        }

        return "'" + mCalendar.get(Calendar.YEAR) + "-" + iMonth + "-" + (iToday - offset) + "'";

    }


    private void doGetRequest(String url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        // Error

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                                Toast.makeText(ChartActivity.this, "Error getting data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        // Read data on the worker thread
                        final String res = response.body().string();
                        Log.i(TAG, "onResponse: " + res);

                        // Run view-related code back on the main thread
                        ChartActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                processJson(res);
                            }
                        });
                    }
                });
    }


    private void processJson(String response) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        int val = 1;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.getJSONObject("query");
            JSONObject jsonObject3 = jsonObject1.getJSONObject("results");
            JSONArray jsonArray = jsonObject3.getJSONArray("quote");
            Log.v("array", jsonArray.length() + "");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                entries.add(new BarEntry((float) val, (int) Float.parseFloat(jsonObject2.getString("Adj_Close"))));
                val++;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateChart(entries);

    }


    private void updateChart(List<BarEntry> entries) {
        BarChart chart = (BarChart) findViewById(R.id.chart);
        BarDataSet set = new BarDataSet(entries, "BarDataSet");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
    }


}
