package com.example.kader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLaporanIbuHamil extends AppCompatActivity implements SpinnerBottomList.ItemClickListener {

    EditText nik, nama, tgl_lahir, umur_hamil, tgl, berat_badan, tensi_darah, lila, tinggi_fundus, denyut_jantung, catatan, jenis_obat;
    Spinner tensi, lilaa, tinggi_fnds, denyut;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button btn_simpan, btn_hapus;
    ProgressDialog progDailog;
    EditText tensi_darah_new_type, lila_new_type, tinggi_fundus_new_type, denyut_jantung_new_type;
    TextView copy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan_ibu_hamil);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Bundle b = getIntent().getExtras();
        String id_ = b.getString("id");
        String nama_ = b.getString("nama");

        String nik_ = b.getString("nik");
        String usia_hamil_ = b.getString("usia_hamil");
        String tanggal_periksa_ = b.getString("tanggal_periksa");
        String berat_badan_ = b.getString("berat_badan");
        String tensi_darah_ = b.getString("tensi_darah");
        String ket_tensi_darah_ = b.getString("ket_tensi_darah");
        String lingkar_lengan_atas_ = b.getString("lingkar_lengan_atas");
        String ket_lingkar_lengan_atas_ = b.getString("ket_lingkar_lengan_atas");
        String denyut_jantung_bayi_ = b.getString("denyut_jantung_bayi");
        String ket_denyut_jantung_bayi_ = b.getString("ket_denyut_jantung_bayi");
        String catatan_ = b.getString("catatan");
        String obat_ = b.getString("obat");
        String tinggi_fundus_ = b.getString("tinggi_fundus");
        String ket_tinggi_fundus_ = b.getString("ket_tinggi_fundus");
        String tanggal_lahir_ = b.getString("tanggal_lahir");

        nama = (EditText) findViewById(R.id.nama);
        nik = (EditText) findViewById(R.id.nik);
        copy = findViewById(R.id.copy);
        tgl_lahir = (EditText) findViewById(R.id.tgl_lahir);


        umur_hamil = (EditText) findViewById(R.id.umur_hamil);
        berat_badan = (EditText) findViewById(R.id.berat_badan);
        tensi_darah = (EditText) findViewById(R.id.tensi_darah);
        tinggi_fundus = (EditText) findViewById(R.id.tinggi_fundus);
        denyut_jantung = (EditText) findViewById(R.id.denyut_jantung);
        catatan = (EditText) findViewById(R.id.catatan);
        jenis_obat = (EditText) findViewById(R.id.jenis_obat);
        lila = (EditText) findViewById(R.id.lila);
        tgl = (EditText)findViewById(R.id.tgl);

        tinggi_fundus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    try{
                        String ttds = tinggi_fundus.getText().toString();
                        String usia_hamil = umur_hamil.getText().toString();
                        doCheckTinggiFundus(Integer.parseInt(ttds), Integer.parseInt(usia_hamil));
                    }catch (Exception e){
                        Toast.makeText(DetailLaporanIbuHamil.this, "format penulisan lingkar lengan atas tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        lila.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    try{
                        String lla = lila.getText().toString();
                        doCheckLingkarLenganAtas(lla);
                    }catch (Exception e){
                        Toast.makeText(DetailLaporanIbuHamil.this, "format penulisan lingkar lengan atas tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tensi_darah.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    try{
                        String ts = tensi_darah.getText().toString();
                        String[] parts = ts.split("/");
                        int systole = Integer.parseInt(parts[0]);
                        int dyastole = Integer.parseInt(parts[1]);
                        doCheckStatusTensiDarah(systole, dyastole);
                    }catch (Exception e){
                        Toast.makeText(DetailLaporanIbuHamil.this, "format penulisan tensi darah tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        denyut_jantung.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    try{
                        String dyt = denyut_jantung.getText().toString();
                        doCheckDenyutJanin(Integer.parseInt(dyt));
                    }catch (Exception e){
                        Toast.makeText(DetailLaporanIbuHamil.this, "format penulisan denyut jantung janin tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        tensi = (Spinner) findViewById(R.id.tensi);
//        List<String> JenisTensi = new ArrayList<String>();
//        JenisTensi.add("Normal");
//        JenisTensi.add("Tinggi");
//        JenisTensi.add("Rendah");

//        lilaa = (Spinner) findViewById(R.id.lilaa);
//        List<String> JenisLilaa = new ArrayList<String>();
//        JenisLilaa.add("Normal");
//        JenisLilaa.add("Tidak Normal");

//        tinggi_fnds = (Spinner) findViewById(R.id.tinggi_fnds);
//        List<String> JenisFundus = new ArrayList<String>();
//        JenisFundus.add("Normal");
//        JenisFundus.add("Tidak Normal");

//        denyut = (Spinner) findViewById(R.id.denyut);
//        List<String> JenisDenyut = new ArrayList<String>();
//        JenisDenyut.add("Normal");
//        JenisDenyut.add("Tidak Normal");

//        ArrayAdapter<String> dataAdapterTensi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, JenisTensi);
//        ArrayAdapter<String> dataAdapterLilaa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, JenisLilaa);
//        ArrayAdapter<String> dataAdapterTinggi_fnds = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, JenisFundus);
//        ArrayAdapter<String> dataAdapterJenis_denyut = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, JenisDenyut);

//        dataAdapterTensi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterLilaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterTinggi_fnds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterJenis_denyut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        tensi.setAdapter(dataAdapterTensi);
//        tensi.setOnItemSelectedListener(new JenisSpinnerTensi());
//        lilaa.setAdapter(dataAdapterLilaa);
//        lilaa.setOnItemSelectedListener(new JenisSpinnerLilaa());
//        tinggi_fnds.setAdapter(dataAdapterTinggi_fnds);
//        tinggi_fnds.setOnItemSelectedListener(new JenisSpinnerTF());
//        denyut.setAdapter(dataAdapterJenis_denyut);
//        denyut.setOnItemSelectedListener(new JenisSpinnerDnyt());



//        copy the clipboard
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(DetailLaporanIbuHamil.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });

        nik.setText(nik_);
        nik.setFocusable(false);
        nik.setEnabled(false);
        nik.setCursorVisible(false);
        nik.addTextChangedListener(watcher);
        nama.setText(nama_);
        nama.setFocusable(false);
        nama.setEnabled(false);
        nama.setCursorVisible(false);
        nama.addTextChangedListener(watcher);

        umur_hamil.setText(usia_hamil_);
        umur_hamil.addTextChangedListener(watcher);
        tgl.setText(tanggal_periksa_);
        tgl.setFocusable(false);
        tgl.setEnabled(false);
        tgl.setCursorVisible(false);
        tgl.addTextChangedListener(watcher);
        berat_badan.setText(berat_badan_);
        berat_badan.addTextChangedListener(watcher);
        tensi_darah.setText(tensi_darah_);
        tensi_darah.addTextChangedListener(watcher);
        lila.setText(lingkar_lengan_atas_);
        lila.addTextChangedListener(watcher);
        tinggi_fundus.setText(tinggi_fundus_);
        tinggi_fundus.addTextChangedListener(watcher);
        denyut_jantung.setText(denyut_jantung_bayi_);
        denyut_jantung.addTextChangedListener(watcher);
        catatan.setText(catatan_);
        catatan.addTextChangedListener(watcher);
        jenis_obat.setText(obat_);
        jenis_obat.addTextChangedListener(watcher);
        tgl_lahir.setText(tanggal_lahir_);
        tgl_lahir.setFocusable(false);
        tgl_lahir.setEnabled(false);
        tgl_lahir.setCursorVisible(false);
        tgl_lahir.addTextChangedListener(watcher);

//        if (ket_tensi_darah_ != null) {
//            int spinnerPosition = dataAdapterTensi.getPosition(ket_tensi_darah_);
//            tensi.setSelection(spinnerPosition);
//        }
//        if (ket_lingkar_lengan_atas_ != null) {
//            int spinnerPosition = dataAdapterLilaa.getPosition(ket_lingkar_lengan_atas_);
//            lilaa.setSelection(spinnerPosition);
//        }
//        if (ket_tinggi_fundus_ != null) {
//            int spinnerPosition = dataAdapterTinggi_fnds.getPosition(ket_tinggi_fundus_);
//            tinggi_fnds.setSelection(spinnerPosition);
//        }
//        if (ket_denyut_jantung_bayi_ != null) {
//            int spinnerPosition = dataAdapterJenis_denyut.getPosition(ket_denyut_jantung_bayi_);
//            denyut.setSelection(spinnerPosition);
//        }

        jenis_obat.setFocusable(false);
//        jenis_obat.setInputType(InputType.TYPE_NULL);
        jenis_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "obat");
            }
        });
        jenis_obat.addTextChangedListener(watcher);

        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSimpan(id_);
            }
        });
        btn_simpan.setEnabled(false);

        btn_hapus = (Button)findViewById(R.id.btn_hapus);
        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHapus(id_);
            }
        });

        tensi_darah_new_type = (EditText) findViewById(R.id.tensi_darah_new_type);
        tensi_darah_new_type.setFocusable(false);
        tensi_darah_new_type.setEnabled(false);
        tensi_darah_new_type.setCursorVisible(false);
        tensi_darah_new_type.setInputType(InputType.TYPE_NULL);
        tensi_darah_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "tensi");
            }
        });
        tensi_darah_new_type.setText(ket_tensi_darah_);
        tensi_darah_new_type.addTextChangedListener(watcher);

        lila_new_type = (EditText) findViewById(R.id.lila_new_type);
        lila_new_type.setFocusable(false);
        lila_new_type.setEnabled(false);
        lila_new_type.setCursorVisible(false);
        lila_new_type.setInputType(InputType.TYPE_NULL);
        lila_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "lla");
            }
        });
        lila_new_type.setText(ket_lingkar_lengan_atas_);
        lila_new_type.addTextChangedListener(watcher);

        tinggi_fundus_new_type = (EditText) findViewById(R.id.tinggi_fundus_new_type);
        tinggi_fundus_new_type.setFocusable(false);
        tinggi_fundus_new_type.setEnabled(false);
        tinggi_fundus_new_type.setCursorVisible(false);
        tinggi_fundus_new_type.setInputType(InputType.TYPE_NULL);
        tinggi_fundus_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "tfund");
            }
        });
        tinggi_fundus_new_type.setText(ket_tinggi_fundus_);
        tinggi_fundus_new_type.addTextChangedListener(watcher);

        denyut_jantung_new_type = (EditText) findViewById(R.id.denyut_jantung_new_type);
        denyut_jantung_new_type.setFocusable(false);
        denyut_jantung_new_type.setEnabled(false);
        denyut_jantung_new_type.setCursorVisible(false);
        denyut_jantung_new_type.setInputType(InputType.TYPE_NULL);
        denyut_jantung_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "djt");
            }
        });
        denyut_jantung_new_type.setText(ket_denyut_jantung_bayi_);
        denyut_jantung_new_type.addTextChangedListener(watcher);

        HandleButtonSimpan();
    }



    public void doCheckTinggiFundus(int systole, int usia_hamil){
        if(usia_hamil == 1){
            if(systole >=2 && systole <=6){
                tinggi_fundus_new_type.setText("ideal");
            }else{
                tinggi_fundus_new_type.setText("kurang ideal");
            }
        }else if(usia_hamil == 2){
            if(systole >=6 && systole <=10){
                tinggi_fundus_new_type.setText("ideal");
            }else{
                tinggi_fundus_new_type.setText("kurang ideal");
            }
        }else if(usia_hamil == 3){
            if(systole >=11 && systole <=15){
                tinggi_fundus_new_type.setText("ideal");
            }else{
                tinggi_fundus_new_type.setText("kurang ideal");
            }
        }else if(usia_hamil == 4){
            if(systole >=15 && systole <=19){
                tinggi_fundus_new_type.setText("ideal");
            }else{
                tinggi_fundus_new_type.setText("kurang ideal");
            }
        }else if(usia_hamil == 5){
            if(systole >=19 && systole <=23){
                tinggi_fundus_new_type.setText("ideal");
            }else{
                tinggi_fundus_new_type.setText("kurang ideal");
            }
        }else if(usia_hamil == 6){
            if(systole >=24 && systole <=28){
                tinggi_fundus_new_type.setText("ideal");
            }else{
                tinggi_fundus_new_type.setText("kurang ideal");
            }
        }else if(usia_hamil == 7){
            if(systole >=28 && systole <=32){
                tinggi_fundus_new_type.setText("ideal");
            }else{
                tinggi_fundus_new_type.setText("kurang ideal");
            }
        }else if(usia_hamil == 8){
            if(systole >=32 && systole <=36){
                tinggi_fundus_new_type.setText("ideal");
            }else{
                tinggi_fundus_new_type.setText("kurang ideal");
            }
        }else if(usia_hamil == 9){
            if(systole >=37 && systole <=41){
                tinggi_fundus_new_type.setText("ideal");
            }else{
                tinggi_fundus_new_type.setText("kurang ideal");
            }
        }
    }

    public void doCheckLingkarLenganAtas(String systole){
        Double prog = Double.valueOf(systole);
        if(prog < 23.5){
            lila_new_type.setText("Status gizi kurang");
        }else if(prog >= 23.5){
            lila_new_type.setText("Status gizi normal");
        }else{
            lila_new_type.setText("");
        }
    }

    public void doCheckDenyutJanin(int systole){
        if(systole >= 120 && systole <= 160){
            denyut_jantung_new_type.setText("Normal");
        }else if(systole > 160){
            denyut_jantung_new_type.setText("Tidak Normal");
        }else if(systole < 120){
            denyut_jantung_new_type.setText("Tidak Normal");
        }else{
            denyut_jantung_new_type.setText("");
        }
    }

    public void doCheckStatusTensiDarah(int systole, int dyastole) {
        if((systole >= 0 && systole <= 109) && (dyastole >= 0 && dyastole <= 69))  {
            tensi_darah_new_type.setText("Tensi Darah Rendah");

        }else if((systole >= 110 && systole <= 120) && (dyastole >= 70 && dyastole <= 80)) {
            tensi_darah_new_type.setText("Normal");

        }else if((systole >= 121 && systole <= 139) && (dyastole >= 81 && dyastole <= 89)) {
            tensi_darah_new_type.setText("Pra Hipertensi");

        }else if((systole >= 140 && systole <= 159) && (dyastole >= 90 && dyastole <= 99)){
            tensi_darah_new_type.setText("Hipertensi Tingkat 1");

        }else if((systole >= 160 && systole <= 180) && (dyastole >= 100 && dyastole <= 120)){
            tensi_darah_new_type.setText("Hipertensi Tingkat 2");

        }else if((systole >= 190 && systole <= 1000) && (dyastole >= 121 && dyastole <= 1000)){
            tensi_darah_new_type.setText("Hipertensi Tingkat 3");


        }
    }


    public void showBottomSheet(View view, String type) {
        String[] id = new String[]{};
        if(type.equals("tensi")){
            id = new String[]{"Normal", "Tinggi", "Rendah"};
        }else if(type.equals("lla")){
            id = new String[]{"Normal", "Tidak Normal"};
        }else if(type.equals("obat")){
            id = new String[]{"Tablet tambah darah", "Vitamin A", "Tidak Ada"};
        }else if(type.equals("tfund")){
            id = new String[]{"Normal", "Tidak Normal"};
        }else if(type.equals("djt")){
            id = new String[]{"Normal", "Tidak Normal"};
        }else{
            id = new String[]{};
        }
        SpinnerBottomList addPhotoBottomDialogFragment = SpinnerBottomList.newInstance(DetailLaporanIbuHamil.this,id,type);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), SpinnerBottomList.TAG);
    }

    @Override
    public void onItemClick(String item, String type) {
        if(type.equals("tensi")){
            tensi_darah_new_type.setText(item);
        }
        if(type.equals("obat")){
            jenis_obat.setText(item);
        }
        if(type.equals("lla")){
            lila_new_type.setText(item);
        }
        if(type.equals("tfund")){
            tinggi_fundus_new_type.setText(item);
        }
        if(type.equals("djt")){
            denyut_jantung_new_type.setText(item);
        }
    }

    public void HandleButtonSimpan(){
        if (nama.getText().toString().length() == 0 ||
                tensi_darah_new_type.getText().toString().length() == 0 ||
                lila_new_type.getText().toString().length() == 0 ||
                tinggi_fundus_new_type.getText().toString().length() == 0 ||
                denyut_jantung_new_type.getText().toString().length() == 0 ||
                nik.getText().toString().length() == 0 ||

                umur_hamil.getText().toString().length() == 0 ||
                denyut_jantung.getText().toString().length() == 0 ||
                tgl.getText().toString().length() == 0 ||
                berat_badan.getText().toString().length() == 0 ||
                tensi_darah.getText().toString().length() == 0 ||
                lila.getText().toString().length() == 0 ||
                catatan.getText().toString().length() == 0 ||
                jenis_obat.getText().toString().length() == 0 ||
                tinggi_fundus.getText().toString().length() == 0 ||
                tgl_lahir.getText().toString().length() == 0) {
            btn_simpan.setEnabled(false);
        } else {
            btn_simpan.setEnabled(true);
        }
    }

    public void doHapus(String id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailLaporanIbuHamil.this);
        alertDialogBuilder.setMessage("Apakah Anda yakin ingin menghapus data ini?");
        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progDailog = new ProgressDialog(DetailLaporanIbuHamil.this);
                progDailog.setMessage("Loading...");
                progDailog.setIndeterminate(false);
                progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDailog.setCancelable(false);
                progDailog.show();

                try {
                    JsonObject params = new JsonObject();
                    params.addProperty("id", id);
                    Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
                    Call call = apiservice.deleteCheckupHamil(params);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            progDailog.dismiss();
                            try {
                                if (response.code() == 200) {
                                    Toast.makeText(DetailLaporanIbuHamil.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", 1);
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                } else {
                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                    String errCode = jsonObject.getString("err_code");
                                    Toast.makeText(DetailLaporanIbuHamil.this, "Data gagal dihapus" + errCode, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(DetailLaporanIbuHamil.this, "Data gagal dihapus" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            progDailog.dismiss();
                            Toast.makeText(DetailLaporanIbuHamil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    progDailog.dismiss();
                    System.out.println("system error on global: " + e.getMessage());
                    Toast.makeText(DetailLaporanIbuHamil.this, "Simpan data gagal" + e, Toast.LENGTH_SHORT).show();
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
        progDailog = new ProgressDialog(DetailLaporanIbuHamil.this);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();

        try{
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            params.addProperty("nama",String.valueOf(nama.getText()));
            params.addProperty("nik",String.valueOf(nik.getText()));

            params.addProperty("usia_hamil",String.valueOf(umur_hamil.getText()));
            params.addProperty("tgl_periksa",String.valueOf(tgl.getText()));
            params.addProperty("berat_badan",String.valueOf(berat_badan.getText()));
            params.addProperty("tensi_darah",String.valueOf(tensi_darah.getText()));
            params.addProperty("lingkar_lengan_atas",String.valueOf(lila.getText()));
            params.addProperty("tinggi_fundus",String.valueOf(tinggi_fundus.getText()));
            params.addProperty("denyut_jantung_bayi",String.valueOf(denyut_jantung.getText()));
            params.addProperty("catatan",String.valueOf(catatan.getText()));
            params.addProperty("obat",String.valueOf(jenis_obat.getText()));
            params.addProperty("ket_tensi_darah",String.valueOf(tensi_darah_new_type.getText()));
            params.addProperty("ket_lingkar_lengan_atas",String.valueOf(lila_new_type.getText()));
            params.addProperty("ket_tinggi_fundus",String.valueOf(tinggi_fundus_new_type.getText()));
            params.addProperty("ket_denyut_jantung_bayi",String.valueOf(denyut_jantung_new_type.getText()));

            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.updateCheckupHamil(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    progDailog.dismiss();
                    try{
                        if (response.code()==200){
                            Toast.makeText(DetailLaporanIbuHamil.this, "Data berhasil dirubah", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result",1);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        } else  {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String errCOde = jsonObject.getString("err_code");
                            Toast.makeText(DetailLaporanIbuHamil.this, "Data gagal dirubah" + errCOde, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(DetailLaporanIbuHamil.this, "Data gagal dirubah" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    progDailog.dismiss();
                    Toast.makeText(DetailLaporanIbuHamil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            progDailog.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(DetailLaporanIbuHamil.this, "Simpan data gagal" + e, Toast.LENGTH_SHORT).show();
        }
    }

    class JenisSpinnerTensi implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class JenisSpinnerLilaa implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class JenisSpinnerTF implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class JenisSpinnerDnyt implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}
        @Override
        public void afterTextChanged(Editable s) {
            HandleButtonSimpan();
        }
    };
}