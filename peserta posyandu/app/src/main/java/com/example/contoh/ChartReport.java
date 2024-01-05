package com.example.contoh;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartReport extends AppCompatActivity {

    loading mLoading;
    TextView text_hasil;
    SessionManager ssmm;
    BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_report);

        mLoading = new loading(ChartReport.this);
        ssmm = new SessionManager(ChartReport.this);
        HashMap<String,String> user = ssmm.getUserDetails();
        String id = user.get(SessionManager.key_id);
        String peserta = user.get(SessionManager.key_peserta);

        text_hasil = (TextView)findViewById(R.id.text_hasil);
        chart = (BarChart) findViewById(R.id.chart);

        if(peserta.equals("Balita")){
            doGraphBalita(id);
        }
        if(peserta.equals("Lansia")){
            doGraphLansia(id);
        }
        if(peserta.equals("Ibu Hamil")){
            doGraphIbuHamil(id);
        }
    }

    public void doGraphBalita(String id){
        mLoading.show();
        try{
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call call = apiservice.defineGraphBalita(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        if (response.code()==200){
                            GsonBuilder builder = new GsonBuilder();
                            builder.serializeNulls();
                            Gson gson = builder.setPrettyPrinting().create();
                            JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                            JSONObject jsonobjectData = new JSONObject(jsonObject.getString("data"));
                            JSONArray xAxis = jsonobjectData.getJSONArray("xAxis");
                            if(xAxis.length() <= 0){
                                mLoading.dismiss();
                                chart.setVisibility(View.GONE);
                                text_hasil.setVisibility(View.VISIBLE);
                            } else {
                                chart.setVisibility(View.VISIBLE);
                                text_hasil.setVisibility(View.GONE);
                                final ArrayList<String> xAxisLabel = new ArrayList<>();
                                for(int i=0; i<xAxis.length(); i++){
                                    xAxisLabel.add(String.valueOf(xAxis.getString(i)));
                                }

                                JSONObject yAxis = new JSONObject(jsonobjectData.getString("yAxis"));
                                JSONArray xAxis_berat_badan = yAxis.getJSONArray("berat_badan");
                                BarDataSet barDataSetBeratBadan = new BarDataSet(getBarEntriesBeratBadan(xAxis_berat_badan), "Berat Badan");
                                barDataSetBeratBadan.setColor(Color.parseColor("#648cc4"));
                                barDataSetBeratBadan.setValueTextColor(Color.BLACK);
                                barDataSetBeratBadan.setValueTextSize(16f);

                                JSONArray xAxis_tinggi_badan = yAxis.getJSONArray("tinggi_badan");
                                BarDataSet barDataSetTinggiBadan = new BarDataSet(getBarEntriesTinggiBadan(xAxis_tinggi_badan), "Tinggi Badan");
                                barDataSetTinggiBadan.setColor(Color.parseColor("#2ed92b"));
                                barDataSetTinggiBadan.setValueTextColor(Color.BLACK);
                                barDataSetTinggiBadan.setValueTextSize(16f);

                                JSONArray xAxis_lingkar_kepala = yAxis.getJSONArray("lingkar_kepala");
                                BarDataSet barDataSetLingkarkepala = new BarDataSet(getBarEntriesLingkarKepala(xAxis_lingkar_kepala), "Lingkar Kepala");
                                barDataSetLingkarkepala.setColor(Color.parseColor("#914747"));
                                barDataSetLingkarkepala.setValueTextColor(Color.BLACK);
                                barDataSetLingkarkepala.setValueTextSize(16f);

                                BarData data = new BarData(barDataSetBeratBadan, barDataSetTinggiBadan, barDataSetLingkarkepala);
                                chart.setData(data);
                                chart.getDescription().setEnabled(false);
                                chart.getXAxis().setCenterAxisLabels(true);
                                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                chart.getXAxis().setGranularity(1);
                                chart.getXAxis().setAxisMinimum(0);
                                chart.getXAxis().setAxisMaximum(xAxis.length() + 2);
                                chart.getXAxis().setGranularityEnabled(true);
                                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                                chart.setDragEnabled(true);
                                chart.setVisibleXRangeMaximum(3);
                                float barSpace = 0.1f;
                                float groupSpace = 0.4f;
                                data.setBarWidth(0.10f);
                                chart.animateXY(2000, 2000);
                                chart.groupBars(0, groupSpace, barSpace);
                                chart.invalidate();
                                mLoading.dismiss();
                            }
                        } else  {
                            mLoading.dismiss();
                            Toast.makeText(ChartReport.this, "chart gagal dibentuk", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        mLoading.dismiss();
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(ChartReport.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(ChartReport.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(ChartReport.this, "chart gagal dibentuk" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void doGraphLansia(String id){
        mLoading.show();
        try{
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call call = apiservice.defineGraphLansia(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        if (response.code()==200) {
                            GsonBuilder builder = new GsonBuilder();
                            builder.serializeNulls();
                            Gson gson = builder.setPrettyPrinting().create();
                            JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                            JSONObject jsonobjectData = new JSONObject(jsonObject.getString("data"));
                            JSONArray xAxis = jsonobjectData.getJSONArray("xAxis");
                            if(xAxis.length() <= 0){
                                mLoading.dismiss();
                                chart.setVisibility(View.GONE);
                                text_hasil.setVisibility(View.VISIBLE);
                            } else {
                                chart.setVisibility(View.VISIBLE);
                                text_hasil.setVisibility(View.GONE);
                                final ArrayList<String> xAxisLabel = new ArrayList<>();
                                for(int i=0; i<xAxis.length(); i++){
                                    xAxisLabel.add(String.valueOf(xAxis.getString(i)));
                                }

                                JSONObject yAxis = new JSONObject(jsonobjectData.getString("yAxis"));
                                JSONArray xAxis_berat_badan = yAxis.getJSONArray("berat_badan");
                                BarDataSet barDataSetBeratBadan = new BarDataSet(getBarEntriesBeratBadan(xAxis_berat_badan), "Berat Badan");
                                barDataSetBeratBadan.setColor(Color.parseColor("#648cc4"));
                                barDataSetBeratBadan.setValueTextColor(Color.BLACK);
                                barDataSetBeratBadan.setValueTextSize(16f);

//                                JSONArray xAxis_tinggi_badan = yAxis.getJSONArray("tinggi_badan");
//                                BarDataSet barDataSetTinggiBadan = new BarDataSet(getBarEntriesTinggiBadan(xAxis_tinggi_badan), "Tinggi Badan");
//                                barDataSetTinggiBadan.setColor(Color.parseColor("#2ed92b"));
//                                barDataSetTinggiBadan.setValueTextColor(Color.BLACK);
//                                barDataSetTinggiBadan.setValueTextSize(16f);

//                                JSONArray xAxis_tensi_darah = yAxis.getJSONArray("tensi_darah");
//                                BarDataSet barDataSetTensiDarah = new BarDataSet(getBarEntriesTensiDarah(xAxis_tensi_darah), "Tensi Darah");
//                                barDataSetTensiDarah.setColor(Color.parseColor("#292227"));
//                                barDataSetTensiDarah.setValueTextColor(Color.BLACK);
//                                barDataSetTensiDarah.setValueTextSize(16f);

                                JSONArray xAxis_asam_urat = yAxis.getJSONArray("asam_urat");
                                BarDataSet barDataSetAsamUrat = new BarDataSet(getBarEntriesAsamUrat(xAxis_asam_urat), "Asam Urat");
                                barDataSetAsamUrat.setColor(Color.parseColor("#f5520c"));
                                barDataSetAsamUrat.setValueTextColor(Color.BLACK);
                                barDataSetAsamUrat.setValueTextSize(16f);

                                JSONArray xAxis_kolestrol = yAxis.getJSONArray("kolerstrol");
                                BarDataSet barDataSetKolestrol = new BarDataSet(getBarEntriesKolestrol(xAxis_kolestrol), "Kolestrol");
                                barDataSetKolestrol.setColor(Color.parseColor("#de0d0d"));
                                barDataSetKolestrol.setValueTextColor(Color.BLACK);
                                barDataSetKolestrol.setValueTextSize(16f);

                                BarData data = new BarData(barDataSetBeratBadan, barDataSetAsamUrat, barDataSetKolestrol);
                                chart.setData(data);
                                chart.getDescription().setEnabled(false);
                                chart.getXAxis().setCenterAxisLabels(true);
                                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                chart.getXAxis().setGranularity(1);
                                chart.getXAxis().setAxisMinimum(0);
                                chart.getXAxis().setAxisMaximum(xAxis.length() + 2);
                                chart.getXAxis().setGranularityEnabled(true);
                                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                                chart.setDragEnabled(true);
                                chart.setVisibleXRangeMaximum(3);
                                float barSpace = 0.1f;
                                float groupSpace = 0.4f;
                                data.setBarWidth(0.10f);
                                chart.animateXY(2000, 2000);
                                chart.groupBars(0, groupSpace, barSpace);
                                chart.invalidate();
                                mLoading.dismiss();
                            }
                        } else {
                            mLoading.dismiss();
                            Toast.makeText(ChartReport.this, "chart gagal dibentuk", Toast.LENGTH_SHORT).show();

                        }
                    }catch (Exception e){
                        mLoading.dismiss();
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(ChartReport.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(ChartReport.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(ChartReport.this, "chart gagal dibentuk" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void doGraphIbuHamil(String id){
        mLoading.show();
        try {
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call call = apiservice.defineGraphIbuHamil(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        if (response.code()==200){
                            GsonBuilder builder = new GsonBuilder();
                            builder.serializeNulls();
                            Gson gson = builder.setPrettyPrinting().create();
                            JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                            JSONObject jsonobjectData = new JSONObject(jsonObject.getString("data"));
                            JSONArray xAxis = jsonobjectData.getJSONArray("xAxis");
                            if(xAxis.length() <= 0){
                                mLoading.dismiss();
                                chart.setVisibility(View.GONE);
                                text_hasil.setVisibility(View.VISIBLE);
                            } else {
                                chart.setVisibility(View.VISIBLE);
                                text_hasil.setVisibility(View.GONE);
                                final ArrayList<String> xAxisLabel = new ArrayList<>();
                                for(int i=0; i<xAxis.length(); i++){
                                    xAxisLabel.add(String.valueOf(xAxis.getString(i)));
                                }

                                JSONObject yAxis = new JSONObject(jsonobjectData.getString("yAxis"));
                                JSONArray xAxis_berat_badan = yAxis.getJSONArray("berat_badan");
                                BarDataSet barDataSetBeratBadan = new BarDataSet(getBarEntriesBeratBadan(xAxis_berat_badan), "Berat Badan");
                                barDataSetBeratBadan.setColor(Color.parseColor("#648cc4"));
                                barDataSetBeratBadan.setValueTextColor(Color.BLACK);
                                barDataSetBeratBadan.setValueTextSize(16f);

                                JSONArray xAxis_denyut_jantung_bayi = yAxis.getJSONArray("denyut_jantung_bayi");
                                BarDataSet barDataSetDenyutJantungBayi = new BarDataSet(getBarEntriesDenyutJantungbayi(xAxis_denyut_jantung_bayi), "Denyut Jantung Bayi");
                                barDataSetDenyutJantungBayi.setColor(Color.parseColor("#2ed92b"));
                                barDataSetDenyutJantungBayi.setValueTextColor(Color.BLACK);
                                barDataSetDenyutJantungBayi.setValueTextSize(16f);

                                JSONArray xAxis_tinggi_fundus = yAxis.getJSONArray("tinggi_fundus");
                                BarDataSet barDataSetTinggiFundus = new BarDataSet(getBarEntriesTinggiFundus(xAxis_tinggi_fundus), "Tinggi Fundus");
                                barDataSetTinggiFundus.setColor(Color.parseColor("#914747"));
                                barDataSetTinggiFundus.setValueTextColor(Color.BLACK);
                                barDataSetTinggiFundus.setValueTextSize(16f);

                                BarData data = new BarData(barDataSetBeratBadan, barDataSetDenyutJantungBayi, barDataSetTinggiFundus);
                                chart.setData(data);
                                chart.getDescription().setEnabled(false);
                                chart.getXAxis().setCenterAxisLabels(true);
                                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                chart.getXAxis().setGranularity(1);
                                chart.getXAxis().setAxisMinimum(0);
                                chart.getXAxis().setAxisMaximum(xAxis.length() + 2);
                                chart.getXAxis().setGranularityEnabled(true);
                                chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
                                chart.setDragEnabled(true);
                                chart.setVisibleXRangeMaximum(3);
                                float barSpace = 0.1f;
                                float groupSpace = 0.4f;
                                data.setBarWidth(0.10f);
                                chart.animateXY(2000, 2000);
                                chart.groupBars(0, groupSpace, barSpace);
                                chart.invalidate();
                                mLoading.dismiss();
                            }
                        } else  {
                            mLoading.dismiss();
                            Toast.makeText(ChartReport.this, "chart gagal dibentuk", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        mLoading.dismiss();
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(ChartReport.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(ChartReport.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(ChartReport.this, "chart gagal dibentuk" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<BarEntry> getBarEntriesDenyutJantungbayi(JSONArray xAxis_denyut_jantung_bayi) {
        ArrayList barEntriesDenyutJantungBayi = new ArrayList<>();
        for(int i=0; i<xAxis_denyut_jantung_bayi.length(); i++){
            try{
                barEntriesDenyutJantungBayi.add(new BarEntry(Float.valueOf(i+1), Float.valueOf(String.valueOf(xAxis_denyut_jantung_bayi.getString(i)))));
            }catch (Exception e){
                continue;
            }
        }
        return barEntriesDenyutJantungBayi;
    }

    public ArrayList<BarEntry> getBarEntriesTinggiFundus(JSONArray xAxis_tinggi_fundus) {
        ArrayList barEntriesTinggiFundus = new ArrayList<>();
        for(int i=0; i<xAxis_tinggi_fundus.length(); i++){
            try{
                barEntriesTinggiFundus.add(new BarEntry(Float.valueOf(i+1), Float.valueOf(String.valueOf(xAxis_tinggi_fundus.getString(i)))));
            }catch (Exception e){
                continue;
            }
        }
        return barEntriesTinggiFundus;
    }

    public ArrayList<BarEntry> getBarEntriesKolestrol(JSONArray xAxis_kolestrol) {
        ArrayList barEntriesKolestrol = new ArrayList<>();
        for(int i=0; i<xAxis_kolestrol.length(); i++){
            try{
                barEntriesKolestrol.add(new BarEntry(Float.valueOf(i+1), Float.valueOf(String.valueOf(xAxis_kolestrol.getString(i)))));
            }catch (Exception e){
                continue;
            }
        }
        return barEntriesKolestrol;
    }

    public ArrayList<BarEntry> getBarEntriesAsamUrat(JSONArray xAxis_asam_urat) {
        ArrayList barEntriesAsamUrat = new ArrayList<>();
        for(int i=0; i<xAxis_asam_urat.length(); i++){
            try{
                barEntriesAsamUrat.add(new BarEntry(Float.valueOf(i+1), Float.valueOf(String.valueOf(xAxis_asam_urat.getString(i)))));
            }catch (Exception e){
                continue;
            }
        }
        return barEntriesAsamUrat;
    }

    public ArrayList<BarEntry> getBarEntriesTensiDarah(JSONArray xAxis_tensi_darah) {
        ArrayList barEntriesTensiDarah = new ArrayList<>();
        for(int i=0; i<xAxis_tensi_darah.length(); i++){
            try{
                barEntriesTensiDarah.add(new BarEntry(Float.valueOf(i+1), Float.valueOf(String.valueOf(xAxis_tensi_darah.getString(i)))));
            }catch (Exception e){
                continue;
            }
        }
        return barEntriesTensiDarah;
    }

    public ArrayList<BarEntry> getBarEntriesLingkarKepala(JSONArray xAxis_lingkar_kepala) {
        ArrayList barEntriesLingkarKepala = new ArrayList<>();
        for(int i=0; i<xAxis_lingkar_kepala.length(); i++){
            try{
                barEntriesLingkarKepala.add(new BarEntry(Float.valueOf(i+1), Float.valueOf(String.valueOf(xAxis_lingkar_kepala.getString(i)))));
            }catch (Exception e){
                continue;
            }
        }
        return barEntriesLingkarKepala;
    }

    public ArrayList<BarEntry> getBarEntriesBeratBadan(JSONArray xAxis_berat_badan) {
        ArrayList barEntriesBeratBadan = new ArrayList<>();
        for(int i=0; i<xAxis_berat_badan.length(); i++){
            try{
                barEntriesBeratBadan.add(new BarEntry(Float.valueOf(i+1), Float.valueOf(String.valueOf(xAxis_berat_badan.getString(i)))));
            }catch (Exception e){
                continue;
            }
        }
        return barEntriesBeratBadan;
    }

    public ArrayList<BarEntry> getBarEntriesTinggiBadan(JSONArray xAxis_tinggi_badan) {
        ArrayList barEntriesTinggiBadan = new ArrayList<>();
        for(int i=0; i<xAxis_tinggi_badan.length(); i++){
            try{
                barEntriesTinggiBadan.add(new BarEntry(Float.valueOf(i+1), Float.valueOf(String.valueOf(xAxis_tinggi_badan.getString(i)))));
            }catch (Exception e){
                continue;
            }
        }
        return barEntriesTinggiBadan;
    }
}