package com.example.kader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanKunjungan extends AppCompatActivity {

    EditText peserta_balita, peserta_lansia, peserta_ibu_hamil, tgl1, tgl2;
    Button btn_tgl_awal, btn_tgl_akhir, btn_cari_data;
    loading mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kunjungan);

        mLoading = new loading(LaporanKunjungan.this);

        tgl1 = (EditText)findViewById(R.id.tgl1);
        tgl1.setFocusable(false);
        tgl1.setEnabled(false);
        tgl1.setCursorVisible(false);
        tgl2 = (EditText)findViewById(R.id.tgl2);
        tgl2.setFocusable(false);
        tgl2.setEnabled(false);
        tgl2.setCursorVisible(false);
        peserta_balita = (EditText) findViewById(R.id.peserta_balita);
        peserta_lansia = (EditText) findViewById(R.id.peserta_lansia);
        peserta_ibu_hamil = (EditText) findViewById(R.id.peserta_ibu_hamil);

        btn_tgl_awal = (Button) findViewById(R.id.btn_tgl_awal);
        btn_tgl_akhir = (Button) findViewById(R.id.btn_tgl_akhir);
        btn_cari_data = (Button)findViewById(R.id.btn_cari_data);

        btn_tgl_awal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LaporanKunjungan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                tgl1.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btn_tgl_akhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LaporanKunjungan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                tgl2.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btn_cari_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voidData(tgl1.getText().toString(), tgl2.getText().toString());
            }
        });
    }

    public void voidData(String tgl1, String tgl2){
        mLoading.show();
        try {
            JsonObject params = new JsonObject();
            params.addProperty("tgl_awal", tgl1);
            params.addProperty("tgl_akhir", tgl2);
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.dataPengunjung(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    mLoading.dismiss();
                    try{
                        GsonBuilder builder = new GsonBuilder();
                        builder.serializeNulls();
                        Gson gson = builder.setPrettyPrinting().create();
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        JSONObject jsonobjectData = new JSONObject(jsonObject.getString("data"));
                        String dataBalita = jsonobjectData.getString("dataBalita");
                        String dataLansia = jsonobjectData.getString("dataLansia");
                        String dataIbuHamil = jsonobjectData.getString("dataIbuHamil");
                        peserta_balita.setText(dataBalita);
                        peserta_lansia.setText(dataLansia);
                        peserta_ibu_hamil.setText(dataIbuHamil);
                    }catch (Exception e){
                        Toast.makeText(LaporanKunjungan.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(LaporanKunjungan.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(LaporanKunjungan.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}