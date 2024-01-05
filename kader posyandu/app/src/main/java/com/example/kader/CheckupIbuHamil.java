package com.example.kader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckupIbuHamil extends AppCompatActivity implements SpinnerBottomList.ItemClickListener {

    EditText nik, nama, tgl_lahir, umur_hamil, tgl, berat_badan, tensi_darah, lila, tinggi_fundus, denyut_jantung, catatan, jenis_obat, alamat;
    Spinner tensi, lilaa, tinggi_fnds, denyut;
    Button btn_simpan;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ProgressDialog progDailog;
    String id_peserta;
    EditText tensi_darah_new_type, lila_new_type, tinggi_fundus_new_type, denyut_jantung_new_type;
    TextView copy;
    TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup_ibu_hamil);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        nama = (EditText) findViewById(R.id.nama);
        nama.setFocusable(false);
        nama.setEnabled(false);
        nama.setCursorVisible(false);
        nama.addTextChangedListener(watcher);
        nik = (EditText) findViewById(R.id.nik);
        count = findViewById(R.id.count);

        Bundle b = getIntent().getExtras();
        String nik_ = b.getString("nik");
        if(nik_.isEmpty()){
            nik.setText("");
            nik.setEnabled(true);
            nik.setFocusable(true);
            nik.setCursorVisible(true);
        }else{
            nik.setEnabled(false);
            nik.setFocusable(false);
            nik.setCursorVisible(false);
            nik.setText(nik_);
            doChecking();
        }
        nik.addTextChangedListener(watcher);

        copy = findViewById(R.id.copy);
        alamat = (EditText) findViewById(R.id.alamat);
        alamat.setFocusable(false);
        alamat.setEnabled(false);
        alamat.setCursorVisible(false);
        alamat.addTextChangedListener(watcher);
        tgl_lahir = (EditText) findViewById(R.id.tgl_lahir);
        tgl_lahir.setFocusable(false);
        tgl_lahir.setEnabled(false);
        tgl_lahir.setCursorVisible(false);
        tgl_lahir.addTextChangedListener(watcher);

        umur_hamil = (EditText) findViewById(R.id.umur_hamil);
        umur_hamil.addTextChangedListener(watcher);
        tgl = (EditText) findViewById(R.id.tgl);
        tgl.setFocusable(false);
        tgl.setEnabled(false);
        tgl.setCursorVisible(false);
        tgl.setText(dateFormat.format(date.getTime()));
        tgl.addTextChangedListener(watcher);
        berat_badan = (EditText) findViewById(R.id.berat_badan);
        berat_badan.addTextChangedListener(watcher);
        tensi_darah = (EditText) findViewById(R.id.tensi_darah);
        tensi_darah.addTextChangedListener(watcher);
        tinggi_fundus = (EditText) findViewById(R.id.tinggi_fundus);
        tinggi_fundus.addTextChangedListener(watcher);
        denyut_jantung = (EditText) findViewById(R.id.denyut_jantung);
        denyut_jantung.addTextChangedListener(watcher);
        catatan = (EditText) findViewById(R.id.catatan);
        catatan.addTextChangedListener(watcher);
        jenis_obat = (EditText) findViewById(R.id.jenis_obat);
        jenis_obat.addTextChangedListener(watcher);
        lila = (EditText) findViewById(R.id.lila);
        lila.addTextChangedListener(watcher);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);

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

        jenis_obat.setFocusable(false);
        jenis_obat.setInputType(InputType.TYPE_NULL);
        jenis_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "obat");
            }
        });
        jenis_obat.addTextChangedListener(watcher);

        nik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s=nik.getText().toString();
                int num=s.length();
                count.setText(""+(int)num);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//       copy the clipboard
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(CheckupIbuHamil.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });

        nik.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    int length = nik.getText().length();
                    if(length > 0){
                        doChecking();
                    } else {
                        Toast.makeText(CheckupIbuHamil.this, "format nik tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tinggi_fundus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    try{
                        String ttds = tinggi_fundus.getText().toString();
                        String usia_hamil = umur_hamil.getText().toString();
                        doCheckTinggiFundus(Integer.parseInt(ttds), Integer.parseInt(usia_hamil));
                    }catch (Exception e){
                        Toast.makeText(CheckupIbuHamil.this, "format penulisan lingkar lengan atas tidak sesuai", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CheckupIbuHamil.this, "format penulisan lingkar lengan atas tidak sesuai", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CheckupIbuHamil.this, "format penulisan tensi darah tidak sesuai", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CheckupIbuHamil.this, "format penulisan denyut jantung janin tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSimpan();
            }
        });
        btn_simpan.setEnabled(false);

        tensi_darah_new_type = (EditText) findViewById(R.id.tensi_darah_new_type);
        tensi_darah_new_type.setFocusable(false);
        tensi_darah_new_type.setInputType(InputType.TYPE_NULL);
//        tensi_darah_new_type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showBottomSheet(v, "tensi");
//            }
//        });
        tensi_darah_new_type.addTextChangedListener(watcher);

        lila_new_type = (EditText) findViewById(R.id.lila_new_type);
        lila_new_type.setFocusable(false);
        lila_new_type.setInputType(InputType.TYPE_NULL);
//        lila_new_type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showBottomSheet(v, "lla");
//            }
//        });
        lila_new_type.addTextChangedListener(watcher);

        tinggi_fundus_new_type = (EditText) findViewById(R.id.tinggi_fundus_new_type);
        tinggi_fundus_new_type.setFocusable(false);
        tinggi_fundus_new_type.setInputType(InputType.TYPE_NULL);
//        tinggi_fundus_new_type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showBottomSheet(v, "tfund");
//            }
//        });
        tinggi_fundus_new_type.addTextChangedListener(watcher);

        denyut_jantung_new_type = (EditText) findViewById(R.id.denyut_jantung_new_type);
        denyut_jantung_new_type.setFocusable(false);
        denyut_jantung_new_type.setInputType(InputType.TYPE_NULL);
//        denyut_jantung_new_type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showBottomSheet(v, "djt");
//            }
//        });
        denyut_jantung_new_type.addTextChangedListener(watcher);

        doCheckingInputData();
    }

    public void doCheckingInputData(){
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
                alamat.getText().toString().length() == 0 ||
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

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}
        @Override
        public void afterTextChanged(Editable s) {
            doCheckingInputData();
        }
    };

    public void showBottomSheet(View view, String type) {
        String[] id = new String[]{};
        if(type.equals("tensi")){
            id = new String[]{"Normal", "Tinggi", "Rendah"};
        }else if(type.equals("lla")){
            id = new String[]{"Normal", "Tidak Normal"};
        }else if(type.equals("tfund")){
            id = new String[]{"Normal", "Tidak Normal"};
        }else if(type.equals("obat")){
            id = new String[]{"Tablet tambah darah", "Vitamin A", "Tidak Ada"};
        }else if(type.equals("djt")){
            id = new String[]{"Normal", "Tidak Normal"};
        }else{
            id = new String[]{};
        }
        SpinnerBottomList addPhotoBottomDialogFragment = SpinnerBottomList.newInstance(CheckupIbuHamil.this,id,type);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), SpinnerBottomList.TAG);
    }

    @Override
    public void onItemClick(String item, String type) {
        if(type.equals("obat")){
            jenis_obat.setText(item);
        }
        if(type.equals("tensi")){
            tensi_darah_new_type.setText(item);
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

    public void doSimpan(){
        progDailog = new ProgressDialog(CheckupIbuHamil.this);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();

        try{
            JsonObject params = new JsonObject();
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
            params.addProperty("account_id",id_peserta);
            params.addProperty("tanggal_lahir",String.valueOf(tgl_lahir.getText()));
            params.addProperty("alamat",alamat.getText().toString());

            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.saveCheckupHamil(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    progDailog.dismiss();
                    try{
                        if (response.code()==200){
                            Toast.makeText(CheckupIbuHamil.this, "Data berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        } else  {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String errCOde = jsonObject.getString("err_code");
                            Toast.makeText(CheckupIbuHamil.this, "Data Gagal" + errCOde, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(CheckupIbuHamil.this, "Data Gagal" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    progDailog.dismiss();
                    Toast.makeText(CheckupIbuHamil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            progDailog.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(CheckupIbuHamil.this, "Simpan data gagal" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void doChecking(){
        progDailog = new ProgressDialog(CheckupIbuHamil.this);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();

        try{
            JsonObject params = new JsonObject();
            params.addProperty("nik",String.valueOf(nik.getText()));
            params.addProperty("type", "Ibu Hamil");
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.ambilDataPeserta(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    progDailog.dismiss();
                    try{
                        if (response.code()==200){
                            GsonBuilder builder = new GsonBuilder();
                            builder.serializeNulls();
                            Gson gson = builder.setPrettyPrinting().create();
                            JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                            JSONObject jsonobjectData = new JSONObject(jsonObject.getString("data"));
                            String nama_peserta, tanggal_lahir, alamat_peserta;

                            if(jsonobjectData.length() == 0) {
                                btn_simpan.setEnabled(false);
                                nama_peserta = "";
                                tanggal_lahir = "";
                                id_peserta = "";
                                alamat_peserta  = "";
                                Toast.makeText(CheckupIbuHamil.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            } else {
                                btn_simpan.setEnabled(true);
                                id_peserta = jsonobjectData.getString("id");
                                nama_peserta = jsonobjectData.getString("nama");
                                tanggal_lahir = jsonobjectData.getString("tanggal_lahir");
                                alamat_peserta = jsonobjectData.getString("alamat");
                                Toast.makeText(CheckupIbuHamil.this, "Data Berhasil ditemukan", Toast.LENGTH_SHORT).show();
                            }
                            tgl_lahir.setText(tanggal_lahir);
                            nama.setText(nama_peserta);
                            alamat.setText(alamat_peserta);
                        } else  {
                            Toast.makeText(CheckupIbuHamil.this, "Checking Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(CheckupIbuHamil.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    progDailog.dismiss();
                    Toast.makeText(CheckupIbuHamil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            progDailog.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(CheckupIbuHamil.this, "Checking nik gagal" + e.toString(), Toast.LENGTH_SHORT).show();
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
}