package com.example.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLaporan extends AppCompatActivity implements SpinnerBottomList.ItemClickListener {

    EditText nama, nik, umur, tgl, berat_badan, tinggi_badan, lingkar_kepala, catatan, jenis_obat, orang_tua_kandung;
    Button btn_simpan, btn_hapus;

    Spinner spin_berat_badan, spin_tinggi_badan, spin_lingkar_kepala, spin_jenis_imunisasi;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ProgressDialog progDailog;
    EditText berat_badan_new_type, tinggi_badan_new_type, lingkar_kepala_new_type, jenis_imunisasi_new_type, jenis_kelamin_new_type;
    TextView copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Bundle b = getIntent().getExtras();
        String id_ = b.getString("id");
        String nama_ = b.getString("nama");
        String umur_ = b.getString("umur");
        String nik_ = b.getString("nik");
        String tgl_periksa_ = b.getString("tgl_periksa");
        String berat_badan_ = b.getString("berat_badan");
        String ket_berat_badan_ = b.getString("ket_berat_badan");
        String tinggi_badan_ = b.getString("tinggi_badan");
        String ket_tinggi_badan_ = b.getString("ket_tinggi_badan");
        String lingkar_kepala_ = b.getString("lingkar_kepala");
        String ket_lingkar_kepala_ = b.getString("ket_lingkar_kepala");
        String jenis_imunisasi_ = b.getString("jenis_imunisasi");
        String catatan_ = b.getString("catatan");
        String obat_ = b.getString("obat");
        String orang_tua_kandung_ = b.getString("orang_tua_kandung");
        String jenis_kelamin_ = b.getString("jenis_kelamin");

        Toast.makeText(DetailLaporan.this, "jenis_kelamin_ " + jenis_kelamin_, Toast.LENGTH_SHORT).show();

        nama = (EditText) findViewById(R.id.nama);
        nama.setFocusable(false);
        nama.setEnabled(false);
        nama.setCursorVisible(false);
        nama.setText(nama_);
        nama.addTextChangedListener(watcher);
        nik = (EditText) findViewById(R.id.nik);
        nik.setFocusable(false);
        nik.setEnabled(false);
        nik.setCursorVisible(false);
        nik.setText(nik_);
        nik.addTextChangedListener(watcher);
        copy = findViewById(R.id.copy);
        umur = (EditText) findViewById(R.id.umur);
        umur.setText(umur_);
        umur.addTextChangedListener(watcher);
        orang_tua_kandung = (EditText) findViewById(R.id.orang_tua_kandung);
        orang_tua_kandung.setText(orang_tua_kandung_);
        orang_tua_kandung.setFocusable(false);
        orang_tua_kandung.setEnabled(false);
        orang_tua_kandung.setCursorVisible(false);
        orang_tua_kandung.addTextChangedListener(watcher);
        tgl = (EditText) findViewById(R.id.tgl);
        tgl.setFocusable(false);
        tgl.setEnabled(false);
        tgl.setCursorVisible(false);
        tgl.setText(tgl_periksa_);
        tgl.addTextChangedListener(watcher);
        berat_badan = (EditText) findViewById(R.id.berat_badan);
        berat_badan.setText(berat_badan_);
        berat_badan.addTextChangedListener(watcher);
        tinggi_badan = (EditText) findViewById(R.id.tinggi_badan);
        tinggi_badan.setText(tinggi_badan_);
        tinggi_badan.addTextChangedListener(watcher);
        lingkar_kepala = (EditText) findViewById(R.id.lingkar_kepala);
        lingkar_kepala.setText(lingkar_kepala_);
        lingkar_kepala.addTextChangedListener(watcher);
        catatan = (EditText) findViewById(R.id.catatan);
        catatan.setText(catatan_);
        catatan.addTextChangedListener(watcher);
        jenis_obat = (EditText) findViewById(R.id.jenis_obat);
        jenis_obat.setText(obat_);
        jenis_obat.addTextChangedListener(watcher);

        jenis_kelamin_new_type = (EditText) findViewById(R.id.jenis_kelamin_new_type);
        jenis_kelamin_new_type.setFocusable(false);
        jenis_kelamin_new_type.setEnabled(false);
        jenis_kelamin_new_type.setCursorVisible(false);
        jenis_kelamin_new_type.setText(tgl_periksa_);
        jenis_kelamin_new_type.setText(jenis_kelamin_);
        jenis_kelamin_new_type.addTextChangedListener(watcher);
        berat_badan_new_type = (EditText) findViewById(R.id.berat_badan_new_type);
        berat_badan_new_type.setFocusable(false);
        berat_badan_new_type.setEnabled(false);
        berat_badan_new_type.setCursorVisible(false);
        tinggi_badan_new_type = (EditText) findViewById(R.id.tinggi_badan_new_type);
        tinggi_badan_new_type.setFocusable(false);
        tinggi_badan_new_type.setEnabled(false);
        tinggi_badan_new_type.setCursorVisible(false);
        lingkar_kepala_new_type = (EditText) findViewById(R.id.lingkar_kepala_new_type);
        lingkar_kepala_new_type.setFocusable(false);
        lingkar_kepala_new_type.setEnabled(false);
        lingkar_kepala_new_type.setCursorVisible(false);
//      copy the clipboard
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(DetailLaporan.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });

        umur.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String jk = jenis_kelamin_new_type.getText().toString();
                    String usia = umur.getText().toString();
                    String bb = berat_badan.getText().toString();
                    String ket = doConfigureKeteranganBeratBadan(jk, usia, bb);
                    berat_badan_new_type.setText(ket);
                }
            }
        });



        berat_badan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String jk = jenis_kelamin_new_type.getText().toString();
                    String usia = umur.getText().toString();
                    String bb = berat_badan.getText().toString();
                    String ket = doConfigureKeteranganBeratBadan(jk, usia, bb);
                    berat_badan_new_type.setText(ket);
                }
            }
        });

        tinggi_badan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String jk = jenis_kelamin_new_type.getText().toString();
                    String usia = umur.getText().toString();
                    String tt = tinggi_badan.getText().toString();
                    String ket = doConfigureKeteranganTinggiBadan(jk, usia, tt);
                    tinggi_badan_new_type.setText(ket);
                }
            }
        });

        lingkar_kepala.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String jk = jenis_kelamin_new_type.getText().toString();
                    String usia = umur.getText().toString();
                    String lk = lingkar_kepala.getText().toString();
                    String ket = doConfigureKeteranganLingkarKepala(jk, usia, lk);
                    lingkar_kepala_new_type.setText(ket);
                }
            }
        });


//        spin_jenis_imunisasi = (Spinner) findViewById(R.id.jenis_imunisasi);
//        List<String> jenisImunisasi = new ArrayList<String>();
//        jenisImunisasi.add("HB-0");
//        jenisImunisasi.add("BCG");
//        jenisImunisasi.add("POLIO");
//        jenisImunisasi.add("DPT-HB-HIB");
//        jenisImunisasi.add("IPV");
//        jenisImunisasi.add("CAMPAK");

//        spin_berat_badan = (Spinner) findViewById(R.id.berat);
//        List<String> beratBadan = new ArrayList<String>();
//        beratBadan.add("Berat badan normal");
//        beratBadan.add("Berat badan tidak normal");

//        spin_tinggi_badan = (Spinner) findViewById(R.id.tinggi);
//        List<String> tinggiBadan = new ArrayList<String>();
//        tinggiBadan.add("Tinggi badan normal");
//        tinggiBadan.add("Tinggi badan tidak normal");

//        spin_lingkar_kepala = (Spinner) findViewById(R.id.lingkar);
//        List<String> lingkarKepala = new ArrayList<String>();
//        lingkarKepala.add("Lingkar kepala normal");
//        lingkarKepala.add("Lingkar kepala tidak normal");

//        ArrayAdapter<String> dataAdapterBB = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, beratBadan);
//        ArrayAdapter<String> dataAdapterTB = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tinggiBadan);
//        ArrayAdapter<String> dataAdapterLK = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lingkarKepala);
//        ArrayAdapter<String> dataAdapterJI = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jenisImunisasi);

//        dataAdapterBB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterTB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterLK.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterJI.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        spin_berat_badan.setAdapter(dataAdapterBB);
//        spin_berat_badan.setOnItemSelectedListener(new DetailLaporan.JenisSpinnerBB());
//        spin_tinggi_badan.setAdapter(dataAdapterTB);
//        spin_tinggi_badan.setOnItemSelectedListener(new DetailLaporan.JenisSpinnerTB());
//        spin_lingkar_kepala.setAdapter(dataAdapterLK);
//        spin_lingkar_kepala.setOnItemSelectedListener(new DetailLaporan.JenisSpinnerLK());
//        spin_jenis_imunisasi.setAdapter(dataAdapterJI);
//        spin_jenis_imunisasi.setOnItemSelectedListener(new DetailLaporan.JenisSpinnerJI());

//        if (ket_berat_badan_ != null) {
//            int spinnerPosition = dataAdapterBB.getPosition(ket_berat_badan_);
//            spin_berat_badan.setSelection(spinnerPosition);
//        }
//        if (ket_tinggi_badan_ != null) {
//            int spinnerPosition = dataAdapterTB.getPosition(ket_tinggi_badan_);
//            spin_tinggi_badan.setSelection(spinnerPosition);
//        }
//        if (ket_lingkar_kepala_ != null) {
//            int spinnerPosition = dataAdapterLK.getPosition(ket_lingkar_kepala_);
//            spin_lingkar_kepala.setSelection(spinnerPosition);
//        }
//        if (jenis_imunisasi_ != null) {
//            int spinnerPosition = dataAdapterJI.getPosition(jenis_imunisasi_);
//            spin_jenis_imunisasi.setSelection(spinnerPosition);
//        }

        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        btn_simpan.setEnabled(false);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSimpan(id_);
            }
        });

        btn_hapus = (Button) findViewById(R.id.btn_hapus);
        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHapus(id_);
            }
        });

        berat_badan_new_type = (EditText) findViewById(R.id.berat_badan_new_type);
        berat_badan_new_type.setFocusable(false);
        berat_badan_new_type.setInputType(InputType.TYPE_NULL);
        berat_badan_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "bb");
            }
        });
        berat_badan_new_type.setText(ket_berat_badan_);
        berat_badan_new_type.addTextChangedListener(watcher);

        tinggi_badan_new_type = (EditText) findViewById(R.id.tinggi_badan_new_type);
        tinggi_badan_new_type.setFocusable(false);
        tinggi_badan_new_type.setInputType(InputType.TYPE_NULL);
        tinggi_badan_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "tb");
            }
        });
        tinggi_badan_new_type.setText(ket_tinggi_badan_);
        tinggi_badan_new_type.addTextChangedListener(watcher);

        lingkar_kepala_new_type = (EditText) findViewById(R.id.lingkar_kepala_new_type);
        lingkar_kepala_new_type.setFocusable(false);
        lingkar_kepala_new_type.setInputType(InputType.TYPE_NULL);
        lingkar_kepala_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "lkp");
            }
        });
        lingkar_kepala_new_type.setText(ket_lingkar_kepala_);
        lingkar_kepala_new_type.addTextChangedListener(watcher);

        jenis_imunisasi_new_type = (EditText) findViewById(R.id.jenis_imunisasi_new_type);
        jenis_imunisasi_new_type.setFocusable(false);
        jenis_imunisasi_new_type.setInputType(InputType.TYPE_NULL);
        jenis_imunisasi_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "jimun");
            }
        });
        jenis_imunisasi_new_type.setText(jenis_imunisasi_);
        jenis_imunisasi_new_type.addTextChangedListener(watcher);

        jenis_obat.setFocusable(false);
        jenis_obat.setInputType(InputType.TYPE_NULL);
        jenis_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet(view, "obat");
            }
        });

        voidHandleButtonSimpan();
    }


    public String doConfigureKeteranganBeratBadan(String jk, String usia, String bb){
        if(TextUtils.isEmpty(jk)){
            Toast.makeText(DetailLaporan.this, "JENIS KELAMIN BELUM DI INPUT", Toast.LENGTH_SHORT).show();
            return "";
        }
        if(TextUtils.isEmpty(usia)){
            Toast.makeText(DetailLaporan.this, "USIA BELUM DI INPUT", Toast.LENGTH_SHORT).show();
            return "";
        }
        if(TextUtils.isEmpty(bb)){
            Toast.makeText(DetailLaporan.this, "BERAT BADAN BELUM DI INPUT", Toast.LENGTH_SHORT).show();
            return "";
        }

        Double prog = Double.valueOf(bb);
        //validasi untuk lak-laki
        if(jk.equals("Laki-Laki")){
            if(usia.equals("0")){
                if(prog >= 2.5 && prog <= 4.4){
                    return "Normal";
                }
                if(prog < 2.5){
                    return "Kurus";
                }
                if(prog > 4.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("1")){
                if(prog >= 3.4 && prog <= 5.8){
                    return "Normal";
                }
                if(prog < 3.4){
                    return "Kurus";
                }
                if(prog > 5.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("2")){
                if(prog >= 4.3 && prog <= 7.1){
                    return "Normal";
                }
                if(prog < 4.3){
                    return "Kurus";
                }
                if(prog > 7.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("3")){
                if(prog >= 5.0 && prog <= 8.0){
                    return "Normal";
                }
                if(prog < 5.0){
                    return "Kurus";
                }
                if(prog > 8.0){
                    return "Gemuk";
                }
            }

            if(usia.equals("4")){
                if(prog >= 5.6 && prog <= 8.7){
                    return "Normal";
                }
                if(prog < 5.6){
                    return "Kurus";
                }
                if(prog > 8.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("5")){
                if(prog >= 6.0 && prog <= 9.3){
                    return "Normal";
                }
                if(prog < 6.0){
                    return "Kurus";
                }
                if(prog > 9.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("6")){
                if(prog >= 6.4 && prog <= 9.8){
                    return "Normal";
                }
                if(prog < 6.4){
                    return "Kurus";
                }
                if(prog > 9.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("7")){
                if(prog >= 6.7 && prog <= 10.3){
                    return "Normal";
                }
                if(prog < 6.7){
                    return "Kurus";
                }
                if(prog > 10.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("8")){
                if(prog >= 6.9 && prog <= 10.7){
                    return "Normal";
                }
                if(prog < 6.9){
                    return "Kurus";
                }
                if(prog > 10.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("9")){
                if(prog >= 7.1 && prog <= 11.0){
                    return "Normal";
                }
                if(prog < 7.1){
                    return "Kurus";
                }
                if(prog > 11.0){
                    return "Gemuk";
                }
            }

            if(usia.equals("10")){
                if(prog >= 7.4 && prog <= 11.4){
                    return "Normal";
                }
                if(prog < 7.4){
                    return "Kurus";
                }
                if(prog > 11.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("11")){
                if(prog >= 7.6 && prog <= 11.7){
                    return "Normal";
                }
                if(prog < 7.6){
                    return "Kurus";
                }
                if(prog > 11.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("12")){
                if(prog >= 7.7 && prog <= 12.0){
                    return "Normal";
                }
                if(prog < 7.7){
                    return "Kurus";
                }
                if(prog > 12.0){
                    return "Gemuk";
                }
            }

            if(usia.equals("13")){
                if(prog >= 7.9 && prog <= 12.3){
                    return "Normal";
                }
                if(prog < 7.9){
                    return "Kurus";
                }
                if(prog > 12.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("14")){
                if(prog >= 8.1 && prog <= 12.6){
                    return "Normal";
                }
                if(prog < 8.1){
                    return "Kurus";
                }
                if(prog > 12.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("15")){
                if(prog >= 8.3 && prog <= 12.8){
                    return "Normal";
                }
                if(prog < 8.3){
                    return "Kurus";
                }
                if(prog > 12.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("16")){
                if(prog >= 8.4 && prog <= 13.1){
                    return "Normal";
                }
                if(prog < 8.4){
                    return "Kurus";
                }
                if(prog > 13.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("17")){
                if(prog >= 8.6 && prog <= 13.4){
                    return "Normal";
                }
                if(prog < 8.6){
                    return "Kurus";
                }
                if(prog > 13.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("18")){
                if(prog >= 8.8 && prog <= 13.7){
                    return "Normal";
                }
                if(prog < 8.8){
                    return "Kurus";
                }
                if(prog > 13.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("19")){
                if(prog >= 8.9 && prog <= 13.9){
                    return "Normal";
                }
                if(prog < 8.9){
                    return "Kurus";
                }
                if(prog > 13.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("20")){
                if(prog >= 9.1 && prog <= 14.2){
                    return "Normal";
                }
                if(prog < 9.1){
                    return "Kurus";
                }
                if(prog > 14.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("21")){
                if(prog >= 9.2 && prog <= 14.5){
                    return "Normal";
                }
                if(prog < 9.2){
                    return "Kurus";
                }
                if(prog > 14.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("22")){
                if(prog >= 9.4 && prog <= 14.7){
                    return "Normal";
                }
                if(prog < 9.4){
                    return "Kurus";
                }
                if(prog > 14.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("23")){
                if(prog >= 9.5 && prog <= 15.0){
                    return "Normal";
                }
                if(prog < 9.5){
                    return "Kurus";
                }
                if(prog > 15.0){
                    return "Gemuk";
                }
            }

            if(usia.equals("24")){
                if(prog >= 9.7 && prog <=15.3){
                    return "Normal";
                }
                if(prog < 9.7){
                    return "Kurus";
                }
                if(prog > 15.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("25")){
                if(prog >= 9.8 && prog <= 15.5){
                    return "Normal";
                }
                if(prog < 9.8){
                    return "Kurus";
                }
                if(prog > 15.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("26")){
                if(prog >= 10.0 && prog <= 15.8){
                    return "Normal";
                }
                if(prog < 10.0){
                    return "Kurus";
                }
                if(prog > 15.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("27")){
                if(prog >= 10.1 && prog <= 16.1){
                    return "Normal";
                }
                if(prog < 10.1){
                    return "Kurus";
                }
                if(prog > 16.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("28")){
                if(prog >= 10.2 && prog <= 16.3){
                    return "Normal";
                }
                if(prog < 10.2){
                    return "Kurus";
                }
                if(prog > 16.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("29")){
                if(prog >= 10.4 && prog <= 16.6){
                    return "Normal";
                }
                if(prog < 10.4){
                    return "Kurus";
                }
                if(prog > 16.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("30")){
                if(prog >= 10.5 && prog <= 16.9){
                    return "Normal";
                }
                if(prog < 10.5){
                    return "Kurus";
                }
                if(prog > 16.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("31")){
                if(prog >= 10.7 && prog <= 17.1){
                    return "Normal";
                }
                if(prog < 10.7){
                    return "Kurus";
                }
                if(prog > 17.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("32")){
                if(prog >= 10.8 && prog <= 17.4){
                    return "Normal";
                }
                if(prog < 10.8){
                    return "Kurus";
                }
                if(prog > 17.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("33")){
                if(prog >= 10.9 && prog <= 17.6){
                    return "Normal";
                }
                if(prog < 10.9){
                    return "Kurus";
                }
                if(prog > 17.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("34")){
                if(prog >= 11.0 && prog <= 17.8){
                    return "Normal";
                }
                if(prog < 11.0){
                    return "Kurus";
                }
                if(prog > 17.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("35")){
                if(prog >= 11.2 && prog <= 18.1){
                    return "Normal";
                }
                if(prog < 11.2){
                    return "Kurus";
                }
                if(prog > 18.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("36")){
                if(prog >= 11.3 && prog <= 18.3){
                    return "Normal";
                }
                if(prog < 11.3){
                    return "Kurus";
                }
                if(prog > 18.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("37")){
                if(prog >= 11.4 && prog <= 18.6){
                    return "Normal";
                }
                if(prog < 11.4){
                    return "Kurus";
                }
                if(prog > 18.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("38")){
                if(prog >= 11.5 && prog <= 18.8){
                    return "Normal";
                }
                if(prog < 11.5){
                    return "Kurus";
                }
                if(prog > 18.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("39")){
                if(prog >= 11.6 && prog <= 19.0){
                    return "Normal";
                }
                if(prog < 11.6){
                    return "Kurus";
                }
                if(prog > 19.0){
                    return "Gemuk";
                }
            }

            if(usia.equals("40")){
                if(prog >= 11.8 && prog <= 19.3){
                    return "Normal";
                }
                if(prog < 11.8){
                    return "Kurus";
                }
                if(prog > 19.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("41")){
                if(prog >= 11.9 && prog <= 19.5){
                    return "Normal";
                }
                if(prog < 11.9){
                    return "Kurus";
                }
                if(prog > 19.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("42")){
                if(prog >= 12.0 && prog <= 19.7){
                    return "Normal";
                }
                if(prog < 12.0){
                    return "Kurus";
                }
                if(prog > 19.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("43")){
                if(prog >= 12.1 && prog <= 20.0){
                    return "Normal";
                }
                if(prog < 12.1){
                    return "Kurus";
                }
                if(prog > 20.0){
                    return "Gemuk";
                }
            }

            if(usia.equals("44")){
                if(prog >= 12.2 && prog <= 20.2){
                    return "Normal";
                }
                if(prog < 12.2){
                    return "Kurus";
                }
                if(prog > 20.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("45")){
                if(prog >= 12.4 && prog <= 20.5){
                    return "Normal";
                }
                if(prog < 12.4){
                    return "Kurus";
                }
                if(prog > 20.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("46")){
                if(prog >= 12.5 && prog <= 20.7){
                    return "Normal";
                }
                if(prog < 12.5){
                    return "Kurus";
                }
                if(prog > 20.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("47")){
                if(prog >= 12.6 && prog <= 20.9){
                    return "Normal";
                }
                if(prog < 12.6){
                    return "Kurus";
                }
                if(prog > 20.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("48")){
                if(prog >= 12.7 && prog <= 21.2){
                    return "Normal";
                }
                if(prog < 12.7){
                    return "Kurus";
                }
                if(prog > 21.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("49")){
                if(prog >= 12.8 && prog <= 21.4){
                    return "Normal";
                }
                if(prog < 12.8){
                    return "Kurus";
                }
                if(prog > 21.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("50")){
                if(prog >= 12.9 && prog <= 21.7){
                    return "Normal";
                }
                if(prog < 12.9){
                    return "Kurus";
                }
                if(prog > 21.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("51")){
                if(prog >= 13.1 && prog <= 21.9){
                    return "Normal";
                }
                if(prog < 13.1){
                    return "Kurus";
                }
                if(prog > 21.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("52")){
                if(prog >= 13.2 && prog <= 22.2){
                    return "Normal";
                }
                if(prog < 13.2){
                    return "Kurus";
                }
                if(prog > 22.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("53")){
                if(prog >= 13.3 && prog <= 22.4){
                    return "Normal";
                }
                if(prog < 13.3){
                    return "Kurus";
                }
                if(prog > 22.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("54")){
                if(prog >= 13.4 && prog <= 22.7){
                    return "Normal";
                }
                if(prog < 13.4){
                    return "Kurus";
                }
                if(prog > 22.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("55")){
                if(prog >= 13.5 && prog <= 22.9){
                    return "Normal";
                }
                if(prog < 13.5){
                    return "Kurus";
                }
                if(prog > 22.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("56")){
                if(prog >= 13.6 && prog <= 23.2){
                    return "Normal";
                }
                if(prog < 13.6){
                    return "Kurus";
                }
                if(prog > 23.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("57")){
                if(prog >= 13.7 && prog <= 23.4){
                    return "Normal";
                }
                if(prog < 13.7){
                    return "Kurus";
                }
                if(prog > 23.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("58")){
                if(prog >= 13.8 && prog <= 23.7){
                    return "Normal";
                }
                if(prog < 13.8){
                    return "Kurus";
                }
                if(prog > 23.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("59")){
                if(prog >= 14.0 && prog <= 23.9){
                    return "Normal";
                }
                if(prog < 14.0){
                    return "Kurus";
                }
                if(prog > 23.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("60")){
                if(prog >= 14.1 && prog <= 24.2){
                    return "Normal";
                }
                if(prog < 14.1){
                    return "Kurus";
                }
                if(prog > 24.2){
                    return "Gemuk";
                }
            }



        }

        //validasi untuk perempuan
        if(jk.equals("Perempuan")){
            if(usia.equals("0")){
                if(prog >= 2.4 && prog <= 4.2){
                    return "Normal";
                }
                if(prog < 2.4){
                    return "Kurus";
                }
                if(prog > 4.2){
                    return "Gemuk";
                }
            }
            if(usia.equals("1")){
                if(prog >= 3.2 && prog <= 5.5){
                    return "Normal";
                }
                if(prog < 3.2){
                    return "Kurus";
                }
                if(prog > 5.5){
                    return "Gemuk";
                }
            }
            if(usia.equals("2")){
                if(prog >= 3.9 && prog <= 6.6){
                    return "Normal";
                }
                if(prog < 3.9){
                    return "Kurus";
                }
                if(prog > 6.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("3")){
                if(prog >= 4.5 && prog <= 7.5){
                    return "Normal";
                }
                if(prog < 4.5){
                    return "Kurus";
                }
                if(prog > 7.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("4")){
                if(prog >= 5.0 && prog <= 8.2){
                    return "Normal";
                }
                if(prog < 5.0){
                    return "Kurus";
                }
                if(prog > 8.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("5")){
                if(prog >= 5.4 && prog <= 8.8){
                    return "Normal";
                }
                if(prog < 5.4){
                    return "Kurus";
                }
                if(prog > 8.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("6")){
                if(prog >= 5.7 && prog <= 9.3){
                    return "Normal";
                }
                if(prog < 5.7){
                    return "Kurus";
                }
                if(prog > 9.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("7")){
                if(prog >= 6.0 && prog <= 9.8){
                    return "Normal";
                }
                if(prog < 6.0){
                    return "Kurus";
                }
                if(prog > 9.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("8")){
                if(prog >= 6.3 && prog <= 10.2){
                    return "Normal";
                }
                if(prog < 6.3){
                    return "Kurus";
                }
                if(prog > 10.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("9")){
                if(prog >= 6.5 && prog <= 10.5){
                    return "Normal";
                }
                if(prog < 6.5){
                    return "Kurus";
                }
                if(prog > 10.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("10")){
                if(prog >= 6.7 && prog <= 10.9){
                    return "Normal";
                }
                if(prog < 6.7){
                    return "Kurus";
                }
                if(prog > 10.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("11")){
                if(prog >= 6.9 && prog <= 11.2){
                    return "Normal";
                }
                if(prog < 6.9){
                    return "Kurus";
                }
                if(prog > 11.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("12")){
                if(prog >= 7.0 && prog <= 11.5){
                    return "Normal";
                }
                if(prog < 7.0){
                    return "Kurus";
                }
                if(prog > 11.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("13")){
                if(prog >= 7.2 && prog <= 11.8){
                    return "Normal";
                }
                if(prog < 7.2){
                    return "Kurus";
                }
                if(prog > 11.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("14")){
                if(prog >= 7.4 && prog <= 12.1){
                    return "Normal";
                }
                if(prog < 7.4){
                    return "Kurus";
                }
                if(prog > 12.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("15")){
                if(prog >= 7.6 && prog <= 12.4){
                    return "Normal";
                }
                if(prog < 7.6){
                    return "Kurus";
                }
                if(prog > 12.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("16")){
                if(prog >= 7.7 && prog <= 12.6){
                    return "Normal";
                }
                if(prog < 7.7){
                    return "Kurus";
                }
                if(prog > 12.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("17")){
                if(prog >= 7.9 && prog <= 12.9){
                    return "Normal";
                }
                if(prog < 7.9){
                    return "Kurus";
                }
                if(prog > 12.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("18")){
                if(prog >= 8.1 && prog <= 13.2){
                    return "Normal";
                }
                if(prog < 8.1){
                    return "Kurus";
                }
                if(prog > 13.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("19")){
                if(prog >= 8.2 && prog <= 13.5){
                    return "Normal";
                }
                if(prog < 8.2){
                    return "Kurus";
                }
                if(prog > 13.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("20")){
                if(prog >= 8.4 && prog <= 13.7){
                    return "Normal";
                }
                if(prog < 8.4){
                    return "Kurus";
                }
                if(prog > 13.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("21")){
                if(prog >= 8.6 && prog <= 14.0){
                    return "Normal";
                }
                if(prog < 8.6){
                    return "Kurus";
                }
                if(prog > 14.0){
                    return "Gemuk";
                }
            }

            if(usia.equals("22")){
                if(prog >= 8.7 && prog <= 14.3){
                    return "Normal";
                }
                if(prog < 8.7){
                    return "Kurus";
                }
                if(prog > 14.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("23")){
                if(prog >= 8.9 && prog <= 14.6){
                    return "Normal";
                }
                if(prog < 8.9){
                    return "Kurus";
                }
                if(prog > 14.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("24")){
                if(prog >= 9.0 && prog <= 14.8){
                    return "Normal";
                }
                if(prog < 9.0){
                    return "Kurus";
                }
                if(prog > 14.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("25")){
                if(prog >= 9.2 && prog <= 15.1){
                    return "Normal";
                }
                if(prog < 9.2){
                    return "Kurus";
                }
                if(prog > 15.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("26")){
                if(prog >= 9.4 && prog <= 15.4){
                    return "Normal";
                }
                if(prog < 9.4){
                    return "Kurus";
                }
                if(prog > 15.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("27")){
                if(prog >= 9.5 && prog <= 15.7){
                    return "Normal";
                }
                if(prog < 9.5){
                    return "Kurus";
                }
                if(prog > 15.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("28")){
                if(prog >= 9.7 && prog <= 16.0){
                    return "Normal";
                }
                if(prog < 9.7){
                    return "Kurus";
                }
                if(prog > 16.0){
                    return "Gemuk";
                }
            }

            if(usia.equals("29")){
                if(prog >= 9.8 && prog <= 16.2){
                    return "Normal";
                }
                if(prog < 9.8){
                    return "Kurus";
                }
                if(prog > 16.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("30")){
                if(prog >= 10.0 && prog <= 16.5){
                    return "Normal";
                }
                if(prog < 10.0){
                    return "Kurus";
                }
                if(prog > 16.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("31")){
                if(prog >= 10.1 && prog <= 16.8){
                    return "Normal";
                }
                if(prog < 10.1){
                    return "Kurus";
                }
                if(prog > 16.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("32")){
                if(prog >= 10.3 && prog <= 17.1){
                    return "Normal";
                }
                if(prog < 10.3){
                    return "Kurus";
                }
                if(prog > 17.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("33")){
                if(prog >= 10.4 && prog <= 17.3){
                    return "Normal";
                }
                if(prog < 10.4){
                    return "Kurus";
                }
                if(prog > 17.3){
                    return "Gemuk";
                }
            }

            if(usia.equals("34")){
                if(prog >= 10.5 && prog <= 17.6){
                    return "Normal";
                }
                if(prog < 10.5){
                    return "Kurus";
                }
                if(prog > 17.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("35")){
                if(prog >= 10.7 && prog <= 17.9){
                    return "Normal";
                }
                if(prog < 10.7){
                    return "Kurus";
                }
                if(prog > 17.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("36")){
                if(prog >= 10.8 && prog <= 18.1){
                    return "Normal";
                }
                if(prog < 10.8){
                    return "Kurus";
                }
                if(prog > 18.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("37")){
                if(prog >= 10.9 && prog <= 18.4){
                    return "Normal";
                }
                if(prog < 10.9){
                    return "Kurus";
                }
                if(prog > 18.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("38")){
                if(prog >= 11.1 && prog <= 18.7){
                    return "Normal";
                }
                if(prog < 11.1){
                    return "Kurus";
                }
                if(prog > 18.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("39")){
                if(prog >= 11.2 && prog <= 19.0){
                    return "Normal";
                }
                if(prog < 11.2){
                    return "Kurus";
                }
                if(prog > 19.0){
                    return "Gemuk";
                }
            }

            if(usia.equals("40")){
                if(prog >= 11.3 && prog <= 19.2){
                    return "Normal";
                }
                if(prog < 11.3){
                    return "Kurus";
                }
                if(prog > 19.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("41")){
                if(prog >= 11.5 && prog <= 19.5){
                    return "Normal";
                }
                if(prog < 11.5){
                    return "Kurus";
                }
                if(prog > 19.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("42")){
                if(prog >= 11.6 && prog <= 19.8){
                    return "Normal";
                }
                if(prog < 11.6){
                    return "Kurus";
                }
                if(prog > 19.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("43")){
                if(prog >= 11.7 && prog <= 20.1){
                    return "Normal";
                }
                if(prog < 11.7){
                    return "Kurus";
                }
                if(prog > 20.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("44")){
                if(prog >= 11.8 && prog <= 20.4){
                    return "Normal";
                }
                if(prog < 11.8){
                    return "Kurus";
                }
                if(prog > 20.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("45")){
                if(prog >= 12.0 && prog <= 20.7){
                    return "Normal";
                }
                if(prog < 12.0){
                    return "Kurus";
                }
                if(prog > 20.7){
                    return "Gemuk";
                }
            }

            if(usia.equals("46")){
                if(prog >= 12.1 && prog <= 20.9){
                    return "Normal";
                }
                if(prog < 12.1){
                    return "Kurus";
                }
                if(prog > 20.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("47")){
                if(prog >= 12.2 && prog <= 21.2){
                    return "Normal";
                }
                if(prog < 12.2){
                    return "Kurus";
                }
                if(prog > 21.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("48")){
                if(prog >= 12.3 && prog <= 21.5){
                    return "Normal";
                }
                if(prog < 12.3){
                    return "Kurus";
                }
                if(prog > 21.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("49")){
                if(prog >= 12.4 && prog <= 21.8){
                    return "Normal";
                }
                if(prog < 12.4){
                    return "Kurus";
                }
                if(prog > 21.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("50")){
                if(prog >= 12.6 && prog <= 22.1){
                    return "Normal";
                }
                if(prog < 12.6){
                    return "Kurus";
                }
                if(prog > 22.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("51")){
                if(prog >= 12.7 && prog <= 22.4){
                    return "Normal";
                }
                if(prog < 12.7){
                    return "Kurus";
                }
                if(prog > 22.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("52")){
                if(prog >= 12.8 && prog <= 22.6){
                    return "Normal";
                }
                if(prog < 12.8){
                    return "Kurus";
                }
                if(prog > 22.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("53")){
                if(prog >= 12.9 && prog <= 22.9){
                    return "Normal";
                }
                if(prog < 12.9){
                    return "Kurus";
                }
                if(prog > 22.9){
                    return "Gemuk";
                }
            }

            if(usia.equals("54")){
                if(prog >= 13.0 && prog <= 23.2){
                    return "Normal";
                }
                if(prog < 13.0){
                    return "Kurus";
                }
                if(prog > 23.2){
                    return "Gemuk";
                }
            }

            if(usia.equals("55")){
                if(prog >= 13.2 && prog <= 23.5){
                    return "Normal";
                }
                if(prog < 13.2){
                    return "Kurus";
                }
                if(prog > 23.5){
                    return "Gemuk";
                }
            }

            if(usia.equals("56")){
                if(prog >= 13.3 && prog <= 23.8){
                    return "Normal";
                }
                if(prog < 13.3){
                    return "Kurus";
                }
                if(prog > 23.8){
                    return "Gemuk";
                }
            }

            if(usia.equals("57")){
                if(prog >= 13.4 && prog <= 24.1){
                    return "Normal";
                }
                if(prog < 13.4){
                    return "Kurus";
                }
                if(prog > 24.1){
                    return "Gemuk";
                }
            }

            if(usia.equals("58")){
                if(prog >= 13.5 && prog <= 24.4){
                    return "Normal";
                }
                if(prog < 13.5){
                    return "Kurus";
                }
                if(prog > 24.4){
                    return "Gemuk";
                }
            }

            if(usia.equals("59")){
                if(prog >= 13.6 && prog <= 24.6){
                    return "Normal";
                }
                if(prog < 13.6){
                    return "Kurus";
                }
                if(prog > 24.6){
                    return "Gemuk";
                }
            }

            if(usia.equals("60")){
                if(prog >= 13.7 && prog <= 24.9){
                    return "Normal";
                }
                if(prog < 13.7){
                    return "Kurus";
                }
                if(prog > 24.9){
                    return "Gemuk";
                }
            }


        }
        return null;
    }

    public String doConfigureKeteranganTinggiBadan(String jk, String usia, String tt){
        if(TextUtils.isEmpty(jk)){
            Toast.makeText(DetailLaporan.this, "JENIS KELAMIN BELUM DI INPUT", Toast.LENGTH_SHORT).show();
            return "";
        }
        if(TextUtils.isEmpty(usia)){
            Toast.makeText(DetailLaporan.this, "USIA BELUM DI INPUT", Toast.LENGTH_SHORT).show();
            return "";
        }
        if(TextUtils.isEmpty(tt)){
            Toast.makeText(DetailLaporan.this, "TINGGI BADAN BELUM DI INPUT", Toast.LENGTH_SHORT).show();
            return "";
        }

        Double prog = Double.valueOf(tt);
        //validasi untuk lak-laki
        if(jk.equals("Laki-Laki")){
            if(usia.equals("0")){
                if(prog >= 46.1 && prog <= 53.7){
                    return "Normal";
                }
                if(prog < 46.1){
                    return "Pendek";
                }
                if(prog > 53.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("1")){
                if(prog >= 50.8 && prog <= 58.6){
                    return "Normal";
                }
                if(prog < 50.8){
                    return "pendek";
                }
                if(prog > 58.6){
                    return "Tinggi";
                }
            }

            if(usia.equals("2")){
                if(prog >= 54.4 && prog <= 62.4){
                    return "Normal";
                }
                if(prog < 54.4){
                    return "pendek";
                }
                if(prog > 62.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("3")){
                if(prog >= 57.3 && prog <= 65.5){
                    return "Normal";
                }
                if(prog < 57.3){
                    return "pendek";
                }
                if(prog > 65.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("4")){
                if(prog >= 59.7 && prog <= 68.0){
                    return "Normal";
                }
                if(prog < 59.7){
                    return "pendek";
                }
                if(prog > 68.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("5")){
                if(prog >= 61.7 && prog <= 70.1){
                    return "Normal";
                }
                if(prog < 61.7){
                    return "pendek";
                }
                if(prog > 70.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("6")){
                if(prog >= 63.3 && prog <= 71.9){
                    return "Normal";
                }
                if(prog < 63.3){
                    return "pendek";
                }
                if(prog > 71.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("7")){
                if(prog >= 64.8 && prog <= 73.5){
                    return "Normal";
                }
                if(prog < 64.8){
                    return "pendek";
                }
                if(prog > 73.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("8")){
                if(prog >= 66.2 && prog <= 75.0){
                    return "Normal";
                }
                if(prog < 66.2){
                    return "pendek";
                }
                if(prog > 75.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("9")){
                if(prog >= 67.5 && prog <= 76.5){
                    return "Normal";
                }
                if(prog < 67.5){
                    return "pendek";
                }
                if(prog > 76.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("10")){
                if(prog >= 68.7 && prog <= 77.9){
                    return "Normal";
                }
                if(prog < 68.7){
                    return "pendek";
                }
                if(prog > 77.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("11")){
                if(prog >= 69.9 && prog <= 79.2){
                    return "Normal";
                }
                if(prog < 69.9){
                    return "pendek";
                }
                if(prog > 79.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("12")){
                if(prog >= 71.0 && prog <= 80.5){
                    return "Normal";
                }
                if(prog < 71.0){
                    return "pendek";
                }
                if(prog > 80.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("13")){
                if(prog >= 72.1 && prog <= 81.8){
                    return "Normal";
                }
                if(prog < 72.1){
                    return "pendek";
                }
                if(prog > 81.8){
                    return "Tinggi";
                }
            }

            if(usia.equals("14")){
                if(prog >= 73.1 && prog <= 83.0){
                    return "Normal";
                }
                if(prog < 73.1){
                    return "pendek";
                }
                if(prog > 83.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("15")){
                if(prog >= 74.1 && prog <= 84.2){
                    return "Normal";
                }
                if(prog < 74.1){
                    return "pendek";
                }
                if(prog > 84.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("16")){
                if(prog >= 75.0 && prog <= 85.4){
                    return "Normal";
                }
                if(prog < 75.0){
                    return "pendek";
                }
                if(prog > 85.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("17")){
                if(prog >= 76.0 && prog <= 86.5){
                    return "Normal";
                }
                if(prog < 76.0){
                    return "pendek";
                }
                if(prog > 86.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("18")){
                if(prog >= 76.9 && prog <= 87.7){
                    return "Normal";
                }
                if(prog < 76.9){
                    return "pendek";
                }
                if(prog > 87.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("19")){
                if(prog >= 77.7 && prog <= 88.8){
                    return "Normal";
                }
                if(prog < 77.7){
                    return "pendek";
                }
                if(prog > 88.8){
                    return "Tinggi";
                }
            }

            if(usia.equals("20")){
                if(prog >= 78.6 && prog <= 89.8){
                    return "Normal";
                }
                if(prog < 78.6){
                    return "pendek";
                }
                if(prog > 89.8){
                    return "Tinggi";
                }
            }

            if(usia.equals("21")){
                if(prog >= 79.4 && prog <= 90.9){
                    return "Normal";
                }
                if(prog < 79.4){
                    return "pendek";
                }
                if(prog > 90.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("22")){
                if(prog >= 80.2 && prog <= 91.9){
                    return "Normal";
                }
                if(prog < 80.2){
                    return "pendek";
                }
                if(prog > 91.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("23")){
                if(prog >= 81.0 && prog <= 92.9){
                    return "Normal";
                }
                if(prog < 81.0){
                    return "pendek";
                }
                if(prog > 92.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("24")){
                if(prog >= 81.0 && prog <= 93.2){
                    return "Normal";
                }
                if(prog < 81.0){
                    return "pendek";
                }
                if(prog > 93.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("25")){
                if(prog >= 81.7 && prog <= 94.2){
                    return "Normal";
                }
                if(prog < 81.7){
                    return "pendek";
                }
                if(prog > 94.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("26")){
                if(prog >= 82.5 && prog <= 95.2){
                    return "Normal";
                }
                if(prog < 82.5){
                    return "pendek";
                }
                if(prog > 95.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("27")){
                if(prog >= 83.1 && prog <= 96.1){
                    return "Normal";
                }
                if(prog < 83.1){
                    return "pendek";
                }
                if(prog > 96.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("28")){
                if(prog >= 83.8 && prog <= 97.0){
                    return "Normal";
                }
                if(prog < 83.8){
                    return "pendek";
                }
                if(prog > 97.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("29")){
                if(prog >= 84.5 && prog <= 97.9){
                    return "Normal";
                }
                if(prog < 84.5){
                    return "pendek";
                }
                if(prog > 97.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("30")){
                if(prog >= 85.1 && prog <= 98.7){
                    return "Normal";
                }
                if(prog < 85.1){
                    return "pendek";
                }
                if(prog > 98.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("31")){
                if(prog >= 85.7 && prog <= 99.6){
                    return "Normal";
                }
                if(prog < 85.7){
                    return "pendek";
                }
                if(prog > 99.6){
                    return "Tinggi";
                }
            }

            if(usia.equals("32")){
                if(prog >= 86.4 && prog <= 100.4){
                    return "Normal";
                }
                if(prog < 86.4){
                    return "pendek";
                }
                if(prog > 100.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("33")){
                if(prog >= 86.9 && prog <= 101.2){
                    return "Normal";
                }
                if(prog < 86.9){
                    return "pendek";
                }
                if(prog > 101.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("34")){
                if(prog >= 87.5 && prog <= 102.0){
                    return "Normal";
                }
                if(prog < 87.5){
                    return "pendek";
                }
                if(prog > 102.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("35")){
                if(prog >= 88.1 && prog <= 102.7){
                    return "Normal";
                }
                if(prog < 88.1){
                    return "pendek";
                }
                if(prog > 102.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("36")){
                if(prog >= 88.7 && prog <= 103.5){
                    return "Normal";
                }
                if(prog < 88.7){
                    return "pendek";
                }
                if(prog > 103.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("37")){
                if(prog >= 89.2 && prog <= 104.2){
                    return "Normal";
                }
                if(prog < 89.2){
                    return "pendek";
                }
                if(prog > 104.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("38")){
                if(prog >= 89.8 && prog <= 105.0){
                    return "Normal";
                }
                if(prog < 89.8){
                    return "pendek";
                }
                if(prog > 105.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("39")){
                if(prog >= 90.3 && prog <= 105.7){
                    return "Normal";
                }
                if(prog < 90.3){
                    return "pendek";
                }
                if(prog > 105.7){
                    return "Tinggi";
                }
            }


            if(usia.equals("40")){
                if(prog >= 90.9 && prog <= 106.4){
                    return "Normal";
                }
                if(prog < 90.9){
                    return "pendek";
                }
                if(prog > 106.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("41")){
                if(prog >= 91.4 && prog <= 107.1){
                    return "Normal";
                }
                if(prog < 91.4){
                    return "pendek";
                }
                if(prog > 107.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("42")){
                if(prog >= 91.9 && prog <= 107.8){
                    return "Normal";
                }
                if(prog < 91.9){
                    return "pendek";
                }
                if(prog > 107.8){
                    return "Tinggi";
                }
            }

            if(usia.equals("43")){
                if(prog >= 92.4 && prog <= 108.5){
                    return "Normal";
                }
                if(prog < 92.4){
                    return "pendek";
                }
                if(prog > 108.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("44")){
                if(prog >= 93.0 && prog <= 109.1){
                    return "Normal";
                }
                if(prog < 93.0){
                    return "pendek";
                }
                if(prog > 109.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("45")){
                if(prog >= 93.5 && prog <= 109.8){
                    return "Normal";
                }
                if(prog < 93.5){
                    return "pendek";
                }
                if(prog > 109.8){
                    return "Tinggi";
                }
            }

            if(usia.equals("46")){
                if(prog >= 94.0 && prog <= 110.4){
                    return "Normal";
                }
                if(prog < 94.0){
                    return "pendek";
                }
                if(prog > 110.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("47")){
                if(prog >= 94.4 && prog <= 111.1){
                    return "Normal";
                }
                if(prog < 94.4){
                    return "pendek";
                }
                if(prog > 111.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("48")){
                if(prog >= 94.9 && prog <= 111.7){
                    return "Normal";
                }
                if(prog < 94.9){
                    return "pendek";
                }
                if(prog > 111.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("49")){
                if(prog >= 95.4 && prog <= 112.4){
                    return "Normal";
                }
                if(prog < 95.4){
                    return "pendek";
                }
                if(prog > 112.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("50")){
                if(prog >= 95.9 && prog <= 113.0){
                    return "Normal";
                }
                if(prog < 95.9){
                    return "pendek";
                }
                if(prog > 113.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("51")){
                if(prog >= 96.4 && prog <= 113.6){
                    return "Normal";
                }
                if(prog < 96.4){
                    return "pendek";
                }
                if(prog > 113.6){
                    return "Tinggi";
                }
            }

            if(usia.equals("52")){
                if(prog >= 96.9 && prog <= 114.2){
                    return "Normal";
                }
                if(prog < 96.9){
                    return "pendek";
                }
                if(prog > 114.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("53")){
                if(prog >= 97.4 && prog <= 114.9){
                    return "Normal";
                }
                if(prog < 97.4){
                    return "pendek";
                }
                if(prog > 114.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("54")){
                if(prog >= 97.8 && prog <= 115.5 ){
                    return "Normal";
                }
                if(prog < 97.8){
                    return "pendek";
                }
                if(prog > 115.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("55")){
                if(prog >= 98.3 && prog <= 116.1){
                    return "Normal";
                }
                if(prog < 98.3){
                    return "pendek";
                }
                if(prog > 116.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("56")){
                if(prog >= 98.8 && prog <= 116.7){
                    return "Normal";
                }
                if(prog < 98.8){
                    return "pendek";
                }
                if(prog > 116.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("57")){
                if(prog >= 99.3 && prog <= 117.4){
                    return "Normal";
                }
                if(prog < 99.3){
                    return "pendek";
                }
                if(prog > 117.4){
                    return "Tinggi";
                }
            }


            if(usia.equals("58")){
                if(prog >= 99.7 && prog <= 118.0){
                    return "Normal";
                }
                if(prog < 99.7){
                    return "pendek";
                }
                if(prog > 118.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("59")){
                if(prog >= 100.2 && prog <= 118.6){
                    return "Normal";
                }
                if(prog < 100.2){
                    return "pendek";
                }
                if(prog > 118.6){
                    return "Tinggi";
                }
            }

            if(usia.equals("60")){
                if(prog >= 100.7 && prog <= 119.2){
                    return "Normal";
                }
                if(prog < 100.7){
                    return "pendek";
                }
                if(prog > 119.2){
                    return "Tinggi";
                }
            }


        }

        //validasi untuk perempuan
        if(jk.equals("Perempuan")){
            if(usia.equals("0")){
                if(prog >= 45.4 && prog <= 52.9){
                    return "Normal";
                }
                if(prog < 45.4){
                    return "pendek";
                }
                if(prog > 52.9){
                    return "Tinggi";
                }
            }
            if(usia.equals("1")){
                if(prog >= 49.8 && prog <= 57.6){
                    return "Normal";
                }
                if(prog < 49.8){
                    return "pendek";
                }
                if(prog > 57.6){
                    return "Tinggi";
                }
            }
            if(usia.equals("2")){
                if(prog >= 53.0 && prog <= 61.1){
                    return "Normal";
                }
                if(prog < 53.0){
                    return "pendek";
                }
                if(prog > 61.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("3")){
                if(prog >= 55.6 && prog <= 64.0){
                    return "Normal";
                }
                if(prog < 55.6){
                    return "pendek";
                }
                if(prog > 64.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("4")){
                if(prog >= 57.8 && prog <= 66.4){
                    return "Normal";
                }
                if(prog < 57.8){
                    return "pendek";
                }
                if(prog > 66.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("5")){
                if(prog >= 59.6 && prog <= 68.5){
                    return "Normal";
                }
                if(prog < 59.6){
                    return "pendek";
                }
                if(prog > 68.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("6")){
                if(prog >= 61.2 && prog <= 70.3){
                    return "Normal";
                }
                if(prog < 61.2){
                    return "pendek";
                }
                if(prog > 70.3){
                    return "Tinggi";
                }
            }

            if(usia.equals("7")){
                if(prog >= 62.7 && prog <= 71.9){
                    return "Normal";
                }
                if(prog < 62.7){
                    return "pendek";
                }
                if(prog > 71.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("8")){
                if(prog >= 64.0 && prog <= 73.5){
                    return "Normal";
                }
                if(prog < 64.0){
                    return "pendek";
                }
                if(prog > 73.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("9")){
                if(prog >= 65.3 && prog <= 75.0){
                    return "Normal";
                }
                if(prog < 65.3){
                    return "pendek";
                }
                if(prog > 75.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("10")){
                if(prog >= 66.5 && prog <= 76.4){
                    return "Normal";
                }
                if(prog < 66.5){
                    return "pendek";
                }
                if(prog > 76.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("11")){
                if(prog >= 67.7 && prog <= 77.8){
                    return "Normal";
                }
                if(prog < 67.7){
                    return "pendek";
                }
                if(prog > 77.8){
                    return "Tinggi";
                }
            }

            if(usia.equals("12")){
                if(prog >= 68.9 && prog <= 79.2){
                    return "Normal";
                }
                if(prog < 68.9){
                    return "pendek";
                }
                if(prog > 79.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("13")){
                if(prog >= 70.0 && prog <= 80.5){
                    return "Normal";
                }
                if(prog < 70.0){
                    return "pendek";
                }
                if(prog > 80.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("14")){
                if(prog >= 71.0 && prog <= 81.7){
                    return "Normal";
                }
                if(prog < 71.0){
                    return "pendek";
                }
                if(prog > 81.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("15")){
                if(prog >= 72.0 && prog <= 83.0){
                    return "Normal";
                }
                if(prog < 72.0){
                    return "pendek";
                }
                if(prog > 83.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("16")){
                if(prog >= 73.0 && prog <= 84.2){
                    return "Normal";
                }
                if(prog < 73.0){
                    return "pendek";
                }
                if(prog > 84.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("17")){
                if(prog >= 74.0 && prog <= 85.4){
                    return "Normal";
                }
                if(prog < 74.0){
                    return "pendek";
                }
                if(prog > 85.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("18")){
                if(prog >= 74.9 && prog <= 86.5){
                    return "Normal";
                }
                if(prog < 74.9){
                    return "pendek";
                }
                if(prog > 86.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("19")){
                if(prog >= 75.8 && prog <= 87.6){
                    return "Normal";
                }
                if(prog < 75.8){
                    return "pendek";
                }
                if(prog > 87.6){
                    return "Tinggi";
                }
            }

            if(usia.equals("20")){
                if(prog >= 76.7 && prog <= 88.7){
                    return "Normal";
                }
                if(prog < 76.7){
                    return "pendek";
                }
                if(prog > 88.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("21")){
                if(prog >= 77.5 && prog <= 89.8){
                    return "Normal";
                }
                if(prog < 77.5){
                    return "pendek";
                }
                if(prog > 89.8){
                    return "Tinggi";
                }
            }

            if(usia.equals("22")){
                if(prog >= 78.4 && prog <= 90.8){
                    return "Normal";
                }
                if(prog < 78.4){
                    return "pendek";
                }
                if(prog > 90.8){
                    return "Tinggi";
                }
            }

            if(usia.equals("23")){
                if(prog >= 79.2 && prog <= 91.9){
                    return "Normal";
                }
                if(prog < 79.2){
                    return "pendek";
                }
                if(prog > 91.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("24")){
                if(prog >= 79.3 && prog <= 92.2){
                    return "Normal";
                }
                if(prog < 79.3){
                    return "pendek";
                }
                if(prog > 92.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("25")){
                if(prog >= 80.0 && prog <= 93.1){
                    return "Normal";
                }
                if(prog < 80.0){
                    return "pendek";
                }
                if(prog > 93.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("26")){
                if(prog >= 80.8 && prog <= 94.1){
                    return "Normal";
                }
                if(prog < 80.8){
                    return "pendek";
                }
                if(prog > 94.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("27")){
                if(prog >= 81.5 && prog <= 95.0){
                    return "Normal";
                }
                if(prog < 81.5){
                    return "pendek";
                }
                if(prog > 95.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("28")){
                if(prog >= 82.2 && prog <= 96.0){
                    return "Normal";
                }
                if(prog < 82.2){
                    return "pendek";
                }
                if(prog > 96.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("29")){
                if(prog >= 82.9 && prog <= 96.9){
                    return "Normal";
                }
                if(prog < 82.9){
                    return "pendek";
                }
                if(prog > 96.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("30")){
                if(prog >= 83.6 && prog <= 97.7){
                    return "Normal";
                }
                if(prog < 83.6){
                    return "pendek";
                }
                if(prog > 97.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("31")){
                if(prog >= 84.3 && prog <= 98.6){
                    return "Normal";
                }
                if(prog < 84.3){
                    return "pendek";
                }
                if(prog > 98.6){
                    return "Tinggi";
                }
            }

            if(usia.equals("32")){
                if(prog >= 84.9 && prog <= 99.4){
                    return "Normal";
                }
                if(prog < 84.9){
                    return "pendek";
                }
                if(prog > 99.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("33")){
                if(prog >= 85.6 && prog <= 100.3){
                    return "Normal";
                }
                if(prog < 85.6){
                    return "pendek";
                }
                if(prog > 100.3){
                    return "Tinggi";
                }
            }

            if(usia.equals("34")){
                if(prog >= 86.2 && prog <= 101.1){
                    return "Normal";
                }
                if(prog < 86.2){
                    return "pendek";
                }
                if(prog > 101.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("35")){
                if(prog >= 86.8 && prog <= 101.9){
                    return "Normal";
                }
                if(prog < 86.8){
                    return "pendek";
                }
                if(prog > 101.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("36")){
                if(prog >= 87.4 && prog <= 102.7){
                    return "Normal";
                }
                if(prog < 87.4){
                    return "pendek";
                }
                if(prog > 102.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("37")){
                if(prog >= 88.0 && prog <= 103.4){
                    return "Normal";
                }
                if(prog < 88.0){
                    return "pendek";
                }
                if(prog > 103.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("38")){
                if(prog >= 88.6 && prog <= 104.2){
                    return "Normal";
                }
                if(prog < 88.6){
                    return "pendek";
                }
                if(prog > 104.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("39")){
                if(prog >= 89.2 && prog <= 105.0){
                    return "Normal";
                }
                if(prog < 89.2){
                    return "pendek";
                }
                if(prog > 105.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("40")){
                if(prog >= 89.8 && prog <= 105.7){
                    return "Normal";
                }
                if(prog < 89.8){
                    return "pendek";
                }
                if(prog > 105.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("41")){
                if(prog >= 90.4 && prog <= 106.4){
                    return "Normal";
                }
                if(prog < 90.4){
                    return "pendek";
                }
                if(prog > 106.4){
                    return "Tinggi";
                }
            }

            if(usia.equals("42")){
                if(prog >= 90.9 && prog <= 107.2){
                    return "Normal";
                }
                if(prog < 90.9){
                    return "pendek";
                }
                if(prog > 107.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("43")){
                if(prog >= 91.5 && prog <= 107.9){
                    return "Normal";
                }
                if(prog < 91.5){
                    return "pendek";
                }
                if(prog > 107.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("44")){
                if(prog >= 92.0 && prog <= 108.6){
                    return "Normal";
                }
                if(prog < 92.0){
                    return "pendek";
                }
                if(prog > 108.6){
                    return "Tinggi";
                }
            }

            if(usia.equals("45")){
                if(prog >= 92.5 && prog <= 109.3){
                    return "Normal";
                }
                if(prog < 92.5){
                    return "pendek";
                }
                if(prog > 109.3){
                    return "Tinggi";
                }
            }

            if(usia.equals("46")){
                if(prog >= 93.1 && prog <= 110.0){
                    return "Normal";
                }
                if(prog < 93.1){
                    return "pendek";
                }
                if(prog > 110.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("47")){
                if(prog >= 93.6 && prog <= 110.7){
                    return "Normal";
                }
                if(prog < 93.6){
                    return "pendek";
                }
                if(prog > 110.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("48")){
                if(prog >= 94.1 && prog <= 111.3){
                    return "Normal";
                }
                if(prog < 94.1){
                    return "pendek";
                }
                if(prog > 111.3){
                    return "Tinggi";
                }
            }

            if(usia.equals("49")){
                if(prog >= 94.6 && prog <= 112.0){
                    return "Normal";
                }
                if(prog < 94.6){
                    return "pendek";
                }
                if(prog > 112.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("50")){
                if(prog >= 95.1 && prog <= 112.7){
                    return "Normal";
                }
                if(prog < 95.1){
                    return "pendek";
                }
                if(prog > 112.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("51")){
                if(prog >= 95.6 && prog <= 113.3){
                    return "Normal";
                }
                if(prog < 95.6){
                    return "pendek";
                }
                if(prog > 113.3){
                    return "Tinggi";
                }
            }

            if(usia.equals("52")){
                if(prog >= 96.1 && prog <= 114.0){
                    return "Normal";
                }
                if(prog < 96.1){
                    return "pendek";
                }
                if(prog > 114.0){
                    return "Tinggi";
                }
            }

            if(usia.equals("53")){
                if(prog >= 96.6 && prog <= 114.6){
                    return "Normal";
                }
                if(prog < 96.6){
                    return "pendek";
                }
                if(prog > 114.6){
                    return "Tinggi";
                }
            }

            if(usia.equals("54")){
                if(prog >= 97.1 && prog <= 115.2){
                    return "Normal";
                }
                if(prog < 97.1){
                    return "pendek";
                }
                if(prog > 115.2){
                    return "Tinggi";
                }
            }

            if(usia.equals("55")){
                if(prog >= 97.6 && prog <= 115.9){
                    return "Normal";
                }
                if(prog < 97.6){
                    return "pendek";
                }
                if(prog > 115.9){
                    return "Tinggi";
                }
            }

            if(usia.equals("56")){
                if(prog >= 98.1 && prog <= 116.5){
                    return "Normal";
                }
                if(prog < 98.1){
                    return "pendek";
                }
                if(prog > 116.5){
                    return "Tinggi";
                }
            }

            if(usia.equals("57")){
                if(prog >= 98.5 && prog <= 117.1){
                    return "Normal";
                }
                if(prog < 98.5){
                    return "pendek";
                }
                if(prog > 117.1){
                    return "Tinggi";
                }
            }

            if(usia.equals("58")){
                if(prog >= 99.0 && prog <= 117.7){
                    return "Normal";
                }
                if(prog < 99.0){
                    return "pendek";
                }
                if(prog > 117.7){
                    return "Tinggi";
                }
            }

            if(usia.equals("59")){
                if(prog >= 99.5 && prog <= 118.3){
                    return "Normal";
                }
                if(prog < 99.5){
                    return "pendek";
                }
                if(prog > 118.3){
                    return "Tinggi";
                }
            }

            if(usia.equals("60")){
                if(prog >= 99.9 && prog <= 118.9){
                    return "Normal";
                }
                if(prog < 99.9){
                    return "pendek";
                }
                if(prog > 118.9){
                    return "Tinggi";
                }
            }

        }
        return null;
    }

    public String doConfigureKeteranganLingkarKepala(String jk, String usia, String lk){
        if(TextUtils.isEmpty(jk)){
            Toast.makeText(DetailLaporan.this, "JENIS KELAMIN BELUM DI INPUT", Toast.LENGTH_SHORT).show();
            return "";
        }
        if(TextUtils.isEmpty(usia)){
            Toast.makeText(DetailLaporan.this, "USIA BELUM DI INPUT", Toast.LENGTH_SHORT).show();
            return "";
        }
        if(TextUtils.isEmpty(lk)){
            Toast.makeText(DetailLaporan.this, "LINGKAR KEPALA BELUM DI INPUT", Toast.LENGTH_SHORT).show();
            return "";
        }

        Double prog = Double.valueOf(lk);
        //validasi untuk lak-laki
        if(jk.equals("Laki-Laki")){
            if(usia.equals("0")){
                if(prog >= 31.9 && prog <= 37.0){
                    return "Normal";
                }
                if(prog < 31.9){
                    return "Kurang dari normal";
                }
                if(prog > 37.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("1")){
                if(prog >= 34.9 && prog <= 39.6){
                    return "Normal";
                }
                if(prog < 34.9){
                    return "Kurang dari normal";
                }
                if(prog > 39.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("2")){
                if(prog >= 36.8 && prog <= 41.5){
                    return "Normal";
                }
                if(prog < 36.8){
                    return "Kurang dari normal";
                }
                if(prog > 41.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("3")){
                if(prog >= 38.1 && prog <= 42.9){
                    return "Normal";
                }
                if(prog < 38.1){
                    return "Kurang dari normal";
                }
                if(prog > 42.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("4")){
                if(prog >= 39.2 && prog <= 44.0){
                    return "Normal";
                }
                if(prog < 39.2){
                    return "Kurang dari normal";
                }
                if(prog > 44.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("5")){
                if(prog >= 40.1 && prog <= 45.0){
                    return "Normal";
                }
                if(prog < 40.1){
                    return "Kurang dari normal";
                }
                if(prog > 45.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("6")){
                if(prog >= 40.9 && prog <= 45.8){
                    return "Normal";
                }
                if(prog < 40.9){
                    return "Kurang dari normal";
                }
                if(prog > 45.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("7")){
                if(prog >= 41.5 && prog <= 46.4){
                    return "Normal";
                }
                if(prog < 41.5){
                    return "Kurang dari normal";
                }
                if(prog > 46.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("8")){
                if(prog >= 42.0 && prog <= 47.0){
                    return "Normal";
                }
                if(prog < 42.0){
                    return "Kurang dari normal";
                }
                if(prog > 47.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("9")){
                if(prog >= 42.5 && prog <= 47.5){
                    return "Normal";
                }
                if(prog < 42.5){
                    return "Kurang dari normal";
                }
                if(prog > 47.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("10")){
                if(prog >= 42.9 && prog <= 47.9){
                    return "Normal";
                }
                if(prog < 42.9){
                    return "Kurang dari normal";
                }
                if(prog > 47.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("11")){
                if(prog >= 43.2 && prog <= 48.3){
                    return "Normal";
                }
                if(prog < 43.2){
                    return "Kurang dari normal";
                }
                if(prog > 48.3){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("12")){
                if(prog >= 43.5 && prog <= 48.6){
                    return "Normal";
                }
                if(prog < 43.5){
                    return "Kurang dari normal";
                }
                if(prog > 48.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("13")){
                if(prog >= 43.8 && prog <= 48.9){
                    return "Normal";
                }
                if(prog < 43.8){
                    return "Kurang dari normal";
                }
                if(prog > 48.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("14")){
                if(prog >= 44.0 && prog <= 49.2){
                    return "Normal";
                }
                if(prog < 44.0){
                    return "Kurang dari normal";
                }
                if(prog > 49.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("15")){
                if(prog >= 44.2 && prog <= 49.4){
                    return "Normal";
                }
                if(prog < 44.2){
                    return "Kurang dari normal";
                }
                if(prog > 49.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("16")){
                if(prog >= 44.4 && prog <= 49.6){
                    return "Normal";
                }
                if(prog < 44.4){
                    return "Kurang dari normal";
                }
                if(prog > 49.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("17")){
                if(prog >= 44.6 && prog <= 49.8){
                    return "Normal";
                }
                if(prog < 44.6){
                    return "Kurang dari normal";
                }
                if(prog > 49.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("18")){
                if(prog >= 44.7 && prog <= 50.0){
                    return "Normal";
                }
                if(prog < 44.7){
                    return "Kurang dari normal";
                }
                if(prog > 50.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("19")){
                if(prog >= 44.9 && prog <= 50.2){
                    return "Normal";
                }
                if(prog < 44.9){
                    return "Kurang dari normal";
                }
                if(prog > 50.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("20")){
                if(prog >= 45.0 && prog <= 50.4){
                    return "Normal";
                }
                if(prog < 45.0){
                    return "Kurang dari normal";
                }
                if(prog > 50.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("21")){
                if(prog >= 45.2 && prog <= 50.5){
                    return "Normal";
                }
                if(prog < 45.2){
                    return "Kurang dari normal";
                }
                if(prog > 50.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("22")){
                if(prog >= 45.3 && prog <= 50.7){
                    return "Normal";
                }
                if(prog < 45.3){
                    return "Kurang dari normal";
                }
                if(prog > 50.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("23")){
                if(prog >= 45.4 && prog <= 50.8){
                    return "Normal";
                }
                if(prog < 45.4){
                    return "Kurang dari normal";
                }
                if(prog > 50.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("24")){
                if(prog >= 45.5 && prog <= 51.0){
                    return "Normal";
                }
                if(prog < 45.5){
                    return "Kurang dari normal";
                }
                if(prog > 51.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("25")){
                if(prog >= 45.6 && prog <= 51.1){
                    return "Normal";
                }
                if(prog < 45.6){
                    return "Kurang dari normal";
                }
                if(prog > 51.1){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("26")){
                if(prog >= 45.8 && prog <= 51.2){
                    return "Normal";
                }
                if(prog < 45.8){
                    return "Kurang dari normal";
                }
                if(prog > 51.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("27")){
                if(prog >= 45.9 && prog <= 51.4){
                    return "Normal";
                }
                if(prog < 45.9){
                    return "Kurang dari normal";
                }
                if(prog > 51.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("28")){
                if(prog >= 46.0 && prog <= 51.5){
                    return "Normal";
                }
                if(prog < 46.0){
                    return "Kurang dari normal";
                }
                if(prog > 51.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("29")){
                if(prog >= 46.1 && prog <= 51.6){
                    return "Normal";
                }
                if(prog < 46.1){
                    return "Kurang dari normal";
                }
                if(prog > 51.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("30")){
                if(prog >= 46.1 && prog <= 51.7){
                    return "Normal";
                }
                if(prog < 46.1){
                    return "Kurang dari normal";
                }
                if(prog > 51.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("31")){
                if(prog >= 46.2 && prog <= 51.8){
                    return "Normal";
                }
                if(prog < 46.2){
                    return "Kurang dari normal";
                }
                if(prog > 51.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("32")){
                if(prog >= 46.3 && prog <= 51.9){
                    return "Normal";
                }
                if(prog < 46.3){
                    return "Kurang dari normal";
                }
                if(prog > 51.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("33")){
                if(prog >= 46.4 && prog <= 52.0){
                    return "Normal";
                }
                if(prog < 46.4){
                    return "Kurang dari normal";
                }
                if(prog > 52.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("34")){
                if(prog >= 46.5 && prog <= 52.1){
                    return "Normal";
                }
                if(prog < 46.5){
                    return "Kurang dari normal";
                }
                if(prog > 52.1){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("35")){
                if(prog >= 46.6 && prog <= 52.2){
                    return "Normal";
                }
                if(prog < 46.6){
                    return "Kurang dari normal";
                }
                if(prog > 52.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("36")){
                if(prog >= 46.6 && prog <= 52.3){
                    return "Normal";
                }
                if(prog < 46.6){
                    return "Kurang dari normal";
                }
                if(prog > 52.3){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("37")){
                if(prog >= 46.7 && prog <= 52.4){
                    return "Normal";
                }
                if(prog < 46.7){
                    return "Kurang dari normal";
                }
                if(prog > 52.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("38")){
                if(prog >= 46.8 && prog <= 52.5){
                    return "Normal";
                }
                if(prog < 46.8){
                    return "Kurang dari normal";
                }
                if(prog > 52.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("39")){
                if(prog >= 46.8 && prog <= 52.5){
                    return "Normal";
                }
                if(prog < 46.8){
                    return "Kurang dari normal";
                }
                if(prog > 52.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("40")){
                if(prog >= 46.9 && prog <= 52.6){
                    return "Normal";
                }
                if(prog < 46.9){
                    return "Kurang dari normal";
                }
                if(prog > 52.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("41")){
                if(prog >= 46.9 && prog <= 52.7){
                    return "Normal";
                }
                if(prog < 46.9){
                    return "Kurang dari normal";
                }
                if(prog > 52.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("42")){
                if(prog >= 47.0 && prog <= 52.8){
                    return "Normal";
                }
                if(prog < 47.0){
                    return "Kurang dari normal";
                }
                if(prog > 52.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("43")){
                if(prog >= 47.0 && prog <= 52.8){
                    return "Normal";
                }
                if(prog < 47.0){
                    return "Kurang dari normal";
                }
                if(prog > 52.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("44")){
                if(prog >= 47.1 && prog <= 52.9){
                    return "Normal";
                }
                if(prog < 47.1){
                    return "Kurang dari normal";
                }
                if(prog > 52.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("45")){
                if(prog >= 47.1 && prog <= 53.0){
                    return "Normal";
                }
                if(prog < 47.1){
                    return "Kurang dari normal";
                }
                if(prog > 53.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("46")){
                if(prog >= 47.2 && prog <= 53.0){
                    return "Normal";
                }
                if(prog < 47.2){
                    return "Kurang dari normal";
                }
                if(prog > 47.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("47")){
                if(prog >= 47.2 && prog <= 53.1){
                    return "Normal";
                }
                if(prog < 47.2){
                    return "Kurang dari normal";
                }
                if(prog > 53.1){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("48")){
                if(prog >= 47.3 && prog <= 53.1){
                    return "Normal";
                }
                if(prog < 47.3){
                    return "Kurang dari normal";
                }
                if(prog > 53.1){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("49")){
                if(prog >= 47.3 && prog <= 53.2){
                    return "Normal";
                }
                if(prog < 47.3){
                    return "Kurang dari normal";
                }
                if(prog > 53.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("50")){
                if(prog >= 47.4 && prog <= 53.2){
                    return "Normal";
                }
                if(prog < 47.4){
                    return "Kurang dari normal";
                }
                if(prog > 53.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("51")){
                if(prog >= 47.4 && prog <= 53.3){
                    return "Normal";
                }
                if(prog < 47.4){
                    return "Kurang dari normal";
                }
                if(prog > 53.3){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("52")){
                if(prog >= 47.5 && prog <= 53.4){
                    return "Normal";
                }
                if(prog < 47.5){
                    return "Kurang dari normal";
                }
                if(prog > 53.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("53")){
                if(prog >= 47.5 && prog <= 53.4){
                    return "Normal";
                }
                if(prog < 47.5){
                    return "Kurang dari normal";
                }
                if(prog > 53.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("54")){
                if(prog >= 47.5 && prog <= 53.5){
                    return "Normal";
                }
                if(prog < 47.5){
                    return "Kurang dari normal";
                }
                if(prog > 53.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("55")){
                if(prog >= 47.6 && prog <= 53.5){
                    return "Normal";
                }
                if(prog < 47.6){
                    return "Kurang dari normal";
                }
                if(prog > 53.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("56")){
                if(prog >= 47.6 && prog <= 53.5){
                    return "Normal";
                }
                if(prog < 47.6){
                    return "Kurang dari normal";
                }
                if(prog > 53.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("57")){
                if(prog >= 47.6 && prog <= 53.6){
                    return "Normal";
                }
                if(prog < 47.6){
                    return "Kurang dari normal";
                }
                if(prog > 53.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("58")){
                if(prog >= 47.7 && prog <= 53.6){
                    return "Normal";
                }
                if(prog < 47.7){
                    return "Kurang dari normal";
                }
                if(prog > 53.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("59")){
                if(prog >= 47.7 && prog <= 53.7){
                    return "Normal";
                }
                if(prog < 47.7){
                    return "Kurang dari normal";
                }
                if(prog > 53.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("60")){
                if(prog >= 47.7 && prog <= 53.7){
                    return "Normal";
                }
                if(prog < 47.7){
                    return "Kurang dari normal";
                }
                if(prog > 53.7){
                    return "Lebih dari normal";
                }
            }
        }

        //validasi untuk perempuan
        if(jk.equals("Perempuan")){
            if(usia.equals("0")){
                if(prog >= 31.5 && prog <= 36.2){
                    return "Normal";
                }
                if(prog < 31.5){
                    return "Kurang dari normal";
                }
                if(prog > 36.2){
                    return "Lebih dari normal";
                }
            }
            if(usia.equals("1")){
                if(prog >= 34.2 && prog <= 38.9){
                    return "Normal";
                }
                if(prog < 34.2){
                    return "Kurang dari normal";
                }
                if(prog > 38.9){
                    return "Lebih dari normal";
                }
            }
            if(usia.equals("2")){
                if(prog >= 35.8 && prog <= 40.7){
                    return "Normal";
                }
                if(prog < 35.8){
                    return "Kurang dari normal";
                }
                if(prog > 40.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("3")){
                if(prog >= 37.1 && prog <= 42.0){
                    return "Normal";
                }
                if(prog < 37.1){
                    return "Kurang dari normal";
                }
                if(prog > 42.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("4")){
                if(prog >= 38.1 && prog <= 43.1){
                    return "Normal";
                }
                if(prog < 38.1){
                    return "Kurang dari normal";
                }
                if(prog > 43.1){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("5")){
                if(prog >= 38.9 && prog <= 44.0){
                    return "Normal";
                }
                if(prog < 38.9){
                    return "Kurang dari normal";
                }
                if(prog > 44.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("6")){
                if(prog >= 39.6 && prog <= 44.8){
                    return "Normal";
                }
                if(prog < 39.6){
                    return "Kurang dari normal";
                }
                if(prog > 44.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("7")){
                if(prog >= 40.2 && prog <= 45.5){
                    return "Normal";
                }
                if(prog < 40.2){
                    return "Kurang dari normal";
                }
                if(prog > 45.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("8")){
                if(prog >= 40.7 && prog <= 46.0){
                    return "Normal";
                }
                if(prog < 40.7){
                    return "Kurang dari normal";
                }
                if(prog > 46.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("9")){
                if(prog >= 41.2 && prog <= 46.5){
                    return "Normal";
                }
                if(prog < 41.2){
                    return "Kurang dari normal";
                }
                if(prog > 46.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("10")){
                if(prog >= 41.5 && prog <= 46.9){
                    return "Normal";
                }
                if(prog < 41.5){
                    return "Kurang dari normal";
                }
                if(prog > 46.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("11")){
                if(prog >= 41.9 && prog <= 47.3){
                    return "Normal";
                }
                if(prog < 41.9){
                    return "Kurang dari normal";
                }
                if(prog > 47.3){
                    return "Lebih dari normal";
                }
            }


            if(usia.equals("12")){
                if(prog >= 42.2 && prog <= 47.6){
                    return "Normal";
                }
                if(prog < 42.2){
                    return "Kurang dari normal";
                }
                if(prog > 47.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("13")){
                if(prog >= 42.4 && prog <= 47.9){
                    return "Normal";
                }
                if(prog < 42.4){
                    return "Kurang dari normal";
                }
                if(prog > 47.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("14")){
                if(prog >= 42.7 && prog <= 48.2){
                    return "Normal";
                }
                if(prog < 42.7){
                    return "Kurang dari normal";
                }
                if(prog > 48.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("15")){
                if(prog >= 42.9 && prog <= 48.4){
                    return "Normal";
                }
                if(prog < 42.9){
                    return "Kurang dari normal";
                }
                if(prog > 48.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("16")){
                if(prog >= 43.1 && prog <= 48.6){
                    return "Normal";
                }
                if(prog < 43.1){
                    return "Kurang dari normal";
                }
                if(prog > 48.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("17")){
                if(prog >= 43.3 && prog <= 48.8){
                    return "Normal";
                }
                if(prog < 43.3){
                    return "Kurang dari normal";
                }
                if(prog > 48.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("18")){
                if(prog >= 43.5 && prog <= 49.0){
                    return "Normal";
                }
                if(prog < 43.5){
                    return "Kurang dari normal";
                }
                if(prog > 49.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("19")){
                if(prog >= 43.6 && prog <= 49.2){
                    return "Normal";
                }
                if(prog < 43.6){
                    return "Kurang dari normal";
                }
                if(prog > 49.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("20")){
                if(prog >= 43.8 && prog <= 49.4){
                    return "Normal";
                }
                if(prog < 43.8){
                    return "Kurang dari normal";
                }
                if(prog > 49.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("22")){
                if(prog >= 44.1 && prog <= 49.7){
                    return "Normal";
                }
                if(prog < 44.1){
                    return "Kurang dari normal";
                }
                if(prog > 49.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("23")){
                if(prog >= 44.3 && prog <= 49.8){
                    return "Normal";
                }
                if(prog < 44.3){
                    return "Kurang dari normal";
                }
                if(prog > 49.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("24")){
                if(prog >= 44.4 && prog <= 50.0){
                    return "Normal";
                }
                if(prog < 44.4){
                    return "Kurang dari normal";
                }
                if(prog > 50.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("25")){
                if(prog >= 44.5 && prog <= 50.1){
                    return "Normal";
                }
                if(prog < 44.5){
                    return "Kurang dari normal";
                }
                if(prog > 50.1){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("26")){
                if(prog >= 44.7 && prog <= 50.3){
                    return "Normal";
                }
                if(prog < 44.7){
                    return "Kurang dari normal";
                }
                if(prog > 50.3){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("27")){
                if(prog >= 44.8 && prog <= 50.4){
                    return "Normal";
                }
                if(prog < 44.8){
                    return "Kurang dari normal";
                }
                if(prog > 50.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("28")){
                if(prog >= 44.9 && prog <= 50.5){
                    return "Normal";
                }
                if(prog < 44.9){
                    return "Kurang dari normal";
                }
                if(prog > 50.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("29")){
                if(prog >= 45.0 && prog <= 50.6){
                    return "Normal";
                }
                if(prog < 45.0){
                    return "Kurang dari normal";
                }
                if(prog > 50.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("30")){
                if(prog >= 45.1 && prog <= 50.7){
                    return "Normal";
                }
                if(prog < 45.1){
                    return "Kurang dari normal";
                }
                if(prog > 50.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("31")){
                if(prog >= 45.2 && prog <= 50.9){
                    return "Normal";
                }
                if(prog < 45.2){
                    return "Kurang dari normal";
                }
                if(prog > 50.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("32")){
                if(prog >= 45.3 && prog <= 51.0){
                    return "Normal";
                }
                if(prog < 45.3){
                    return "Kurang dari normal";
                }
                if(prog > 51.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("33")){
                if(prog >= 45.4 && prog <= 51.1){
                    return "Normal";
                }
                if(prog < 45.4){
                    return "Kurang dari normal";
                }
                if(prog > 51.1){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("34")){
                if(prog >= 45.5 && prog <= 51.2){
                    return "Normal";
                }
                if(prog < 45.5){
                    return "Kurang dari normal";
                }
                if(prog > 51.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("35")){
                if(prog >= 45.6 && prog <= 51.2){
                    return "Normal";
                }
                if(prog < 45.6){
                    return "Kurang dari normal";
                }
                if(prog > 51.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("36")){
                if(prog >= 45.7 && prog <= 51.3){
                    return "Normal";
                }
                if(prog < 45.7){
                    return "Kurang dari normal";
                }
                if(prog > 51.3){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("37")){
                if(prog >= 45.8 && prog <= 51.4){
                    return "Normal";
                }
                if(prog < 45.8){
                    return "Kurang dari normal";
                }
                if(prog > 51.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("38")){
                if(prog >= 45.8 && prog <= 51.5){
                    return "Normal";
                }
                if(prog < 45.8){
                    return "Kurang dari normal";
                }
                if(prog > 51.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("39")){
                if(prog >= 45.9 && prog <= 51.6){
                    return "Normal";
                }
                if(prog < 45.9){
                    return "Kurang dari normal";
                }
                if(prog > 51.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("40")){
                if(prog >= 46.0 && prog <= 51.7){
                    return "Normal";
                }
                if(prog < 46.0){
                    return "Kurang dari normal";
                }
                if(prog > 51.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("41")){
                if(prog >= 46.1 && prog <= 51.7){
                    return "Normal";
                }
                if(prog < 46.1){
                    return "Kurang dari normal";
                }
                if(prog > 51.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("42")){
                if(prog >= 46.1 && prog <= 51.8){
                    return "Normal";
                }
                if(prog < 46.1){
                    return "Kurang dari normal";
                }
                if(prog > 51.8){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("43")){
                if(prog >= 46.2 && prog <= 51.9){
                    return "Normal";
                }
                if(prog < 46.2){
                    return "Kurang dari normal";
                }
                if(prog > 51.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("44")){
                if(prog >= 46.3 && prog <= 51.9){
                    return "Normal";
                }
                if(prog < 46.3){
                    return "Kurang dari normal";
                }
                if(prog > 51.9){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("45")){
                if(prog >= 46.3 && prog <= 52.0){
                    return "Normal";
                }
                if(prog < 46.3){
                    return "Kurang dari normal";
                }
                if(prog > 52.0){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("46")){
                if(prog >= 46.4 && prog <= 52.1){
                    return "Normal";
                }
                if(prog < 46.4){
                    return "Kurang dari normal";
                }
                if(prog > 52.1){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("47")){
                if(prog >= 46.4 && prog <= 52.1){
                    return "Normal";
                }
                if(prog < 46.4){
                    return "Kurang dari normal";
                }
                if(prog > 52.1){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("48")){
                if(prog >= 46.5 && prog <= 52.2){
                    return "Normal";
                }
                if(prog < 46.5){
                    return "Kurang dari normal";
                }
                if(prog > 52.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("49")){
                if(prog >= 46.5 && prog <= 52.2){
                    return "Normal";
                }
                if(prog < 46.5){
                    return "Kurang dari normal";
                }
                if(prog > 52.2){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("50")){
                if(prog >= 46.6 && prog <= 52.3){
                    return "Normal";
                }
                if(prog < 46.6){
                    return "Kurang dari normal";
                }
                if(prog > 52.3){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("51")){
                if(prog >= 46.7 && prog <= 52.3){
                    return "Normal";
                }
                if(prog < 46.7){
                    return "Kurang dari normal";
                }
                if(prog > 52.3){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("52")){
                if(prog >= 46.7 && prog <= 52.4){
                    return "Normal";
                }
                if(prog < 46.7){
                    return "Kurang dari normal";
                }
                if(prog > 52.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("53")){
                if(prog >= 46.8 && prog <= 52.4){
                    return "Normal";
                }
                if(prog < 46.8){
                    return "Kurang dari normal";
                }
                if(prog > 52.4){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("54")){
                if(prog >= 46.8 && prog <= 52.5){
                    return "Normal";
                }
                if(prog < 46.8){
                    return "Kurang dari normal";
                }
                if(prog > 52.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("55")){
                if(prog >= 46.9 && prog <= 52.5){
                    return "Normal";
                }
                if(prog < 46.9){
                    return "Kurang dari normal";
                }
                if(prog > 52.5){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("56")){
                if(prog >= 46.9 && prog <= 52.6){
                    return "Normal";
                }
                if(prog < 46.9){
                    return "Kurang dari normal";
                }
                if(prog > 52.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("57")){
                if(prog >= 46.9 && prog <= 52.6){
                    return "Normal";
                }
                if(prog < 46.9){
                    return "Kurang dari normal";
                }
                if(prog > 52.6){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("58")){
                if(prog >= 47.0 && prog <= 52.7){
                    return "Normal";
                }
                if(prog < 47.0){
                    return "Kurang dari normal";
                }
                if(prog > 52.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("59")){
                if(prog >= 47.0 && prog <= 52.7){
                    return "Normal";
                }
                if(prog < 47.0){
                    return "Kurang dari normal";
                }
                if(prog > 52.7){
                    return "Lebih dari normal";
                }
            }

            if(usia.equals("60")){
                if(prog >= 47.1 && prog <= 52.8){
                    return "Normal";
                }
                if(prog < 47.1){
                    return "Kurang dari normal";
                }
                if(prog > 52.8){
                    return "Lebih dari normal";
                }
            }

        }
        return null;
    }




    public void showBottomSheet(View view, String type) {
        String[] id = new String[]{};
        if(type.equals("bb")){
            id = new String[]{"Berat badan normal", "Berat badan tidak normal"};
        }else if(type.equals("obat")){
            id = new String[]{"Vitamin A", "Vitamin B", "Vitamin C", "Vitamin D", "Tidak Ada"};
        }else if(type.equals("tb")){
            id = new String[]{"Tinggi badan normal", "Tinggi badan tidak normal"};
        }else if(type.equals("lkp")){
            id = new String[]{"Lingkar kepala normal", "Lingkar kepala tidak normal"};
        }else if (type.equals("jimun")){
            id = new String[]{"HB-0", "BCG", "POLIO", "DPT-HB-HIB", "IPV", "CAMPAK", "TIDAK ADA"};
        }else{
            id = new String[]{};
        }
        SpinnerBottomList addPhotoBottomDialogFragment = SpinnerBottomList.newInstance(DetailLaporan.this,id,type);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), SpinnerBottomList.TAG);
    }

    @Override
    public void onItemClick(String item, String type) {
        if(type.equals("bb")){
            berat_badan_new_type.setText(item);
        }
        if(type.equals("tb")){
            tinggi_badan_new_type.setText(item);
        }
        if(type.equals("obat")){
            jenis_obat.setText(item);
        }
        if(type.equals("lkp")){
            lingkar_kepala_new_type.setText(item);
        }
        if(type.equals("jimun")){
            jenis_imunisasi_new_type.setText(item);
        }
    }

    public void voidHandleButtonSimpan(){
        if (nama.getText().toString().length() == 0 ||
                berat_badan_new_type.getText().toString().length() == 0 ||
                tinggi_badan_new_type.getText().toString().length() == 0 ||
                lingkar_kepala_new_type.getText().toString().length() == 0 ||
                jenis_imunisasi_new_type.getText().toString().length() == 0 ||
                nik.getText().toString().length() == 0 ||
                umur.getText().toString().length() == 0 ||
                tgl.getText().toString().length() == 0 ||
                berat_badan.getText().toString().length() == 0 ||
                tinggi_badan.getText().toString().length() == 0 ||
                lingkar_kepala.getText().toString().length() == 0 ||
                catatan.getText().toString().length() == 0 ||
                jenis_obat.getText().toString().length() == 0 ||
                orang_tua_kandung.getText().toString().length() == 0 ||

                jenis_kelamin_new_type.getText().toString().length() == 0) {
            btn_simpan.setEnabled(false);
        } else {
            btn_simpan.setEnabled(true);
        }
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        { }

        @Override
        public void afterTextChanged(Editable s) {
            voidHandleButtonSimpan();
        }
    };



    public void doHapus(String id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailLaporan.this);
        alertDialogBuilder.setMessage("Apakah Anda yakin ingin menghapus data ini?");
        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progDailog = new ProgressDialog(DetailLaporan.this);
                progDailog.setMessage("Loading...");
                progDailog.setIndeterminate(false);
                progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDailog.setCancelable(false);
                progDailog.show();

                try {
                    JsonObject params = new JsonObject();
                    params.addProperty("id", id);
                    Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
                    Call call = apiservice.deleteCheckupBalita(params);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            progDailog.dismiss();
                            try {
                                if (response.code() == 200) {
                                    Toast.makeText(DetailLaporan.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", 1);
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                } else {
                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                    String errCode = jsonObject.getString("err_code");
                                    Toast.makeText(DetailLaporan.this, "Data gagal dihapus" + errCode, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(DetailLaporan.this, "Data gagal dihapus" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            progDailog.dismiss();
                            Toast.makeText(DetailLaporan.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    progDailog.dismiss();
                    System.out.println("system error on global: " + e.getMessage());
                    Toast.makeText(DetailLaporan.this, "Simpan data gagal" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing if the user clicks "Tidak"
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }


    public void doSimpan(String id){
        progDailog = new ProgressDialog(DetailLaporan.this);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();

        try {
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            params.addProperty("nama",String.valueOf(nama.getText()));
            params.addProperty("nik",String.valueOf(nik.getText()));
            params.addProperty("umur",String.valueOf(umur.getText()));
            params.addProperty("tgl_periksa",String.valueOf(tgl.getText()));
            params.addProperty("berat_badan",String.valueOf(berat_badan.getText()));
            params.addProperty("ket_berat_badan",String.valueOf(berat_badan_new_type.getText()));
            params.addProperty("tinggi_badan",String.valueOf(tinggi_badan.getText()));
            params.addProperty("ket_tinggi_badan",String.valueOf(tinggi_badan_new_type.getText()));
            params.addProperty("lingkar_kepala",String.valueOf(lingkar_kepala.getText()));
            params.addProperty("ket_lingkar_kepala",String.valueOf(lingkar_kepala_new_type.getText()));
            params.addProperty("jenis_imunisasi",String.valueOf(jenis_imunisasi_new_type.getText()));
            params.addProperty("catatan",String.valueOf(catatan.getText()));
            params.addProperty("obat",String.valueOf(jenis_obat.getText()));
            params.addProperty("orang_tua_kandung",String.valueOf(orang_tua_kandung.getText()));


            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.updateCheckupBalita(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    progDailog.dismiss();
                    try{
                        if (response.code()==200){
                            Toast.makeText(DetailLaporan.this, "Data berhasil dirubah", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result",1);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        } else  {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String errCOde = jsonObject.getString("err_code");
                            Toast.makeText(DetailLaporan.this, "Data gagal dirubah" + errCOde, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(DetailLaporan.this, "Data gagal dirubah" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    progDailog.dismiss();
                    Toast.makeText(DetailLaporan.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            progDailog.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(DetailLaporan.this, "Simpan data gagal" + e, Toast.LENGTH_SHORT).show();
        }

    }

    class JenisSpinnerBB implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class JenisSpinnerTB implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class JenisSpinnerLK implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class JenisSpinnerJI implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",1);
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }
}