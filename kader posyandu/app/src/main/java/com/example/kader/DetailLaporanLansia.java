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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLaporanLansia extends AppCompatActivity implements SpinnerBottomList.ItemClickListener {

    EditText nik, nama, umur, tgl, berat_badan, tensi_darah, asam_urat, kolestrol, catatan, jenis_obat;
    Spinner jenis, tensi, asam, kolestrl;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button btn_simpan, btn_hapus;
    ProgressDialog progDailog;
    EditText jenis_kelamin_new_type, tensi_darah_new_type, asam_urat_new_type, kolestrol_new_type;
    TextView copy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan_lansia);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Bundle b = getIntent().getExtras();
        String id_ = b.getString("id");
        String nama_ = b.getString("nama");
        String umur_ = b.getString("umur");
        String nik_ = b.getString("nik");
        String jenis_kelamin_ = b.getString("jenis_kelamin");
        String tgl_periksa_ = b.getString("tgl_periksa");
        String berat_badan_ = b.getString("berat_badan");

        String tensi_darah_ = b.getString("tensi_darah");
        String ket_tensi_darah_ = b.getString("ket_tensi_darah");
        String asam_urat_ = b.getString("asam_urat");
        String ket_asam_urat_ = b.getString("ket_asam_urat");
        String kolerstrol_ = b.getString("kolerstrol");
        String ket_kolerstrol_ = b.getString("ket_kolerstrol");
        String catatan_ = b.getString("catatan");
        String obat_ = b.getString("obat");

        nik = (EditText) findViewById(R.id.nik);
        nik.setFocusable(false);
        nik.setEnabled(false);
        nik.setCursorVisible(false);
        nik.setText(nik_);
        nik.addTextChangedListener(watcher);
        nama = (EditText) findViewById(R.id.nama);
        nama.setFocusable(false);
        nama.setEnabled(false);
        nama.setCursorVisible(false);
        nama.setText(nama_);
        nama.addTextChangedListener(watcher);
        umur = (EditText) findViewById(R.id.umur);
        umur.setText(umur_);
        umur.addTextChangedListener(watcher);

        copy = findViewById(R.id.copy);
        tgl = (EditText) findViewById(R.id.tgl);
        tgl.setFocusable(false);
        tgl.setEnabled(false);
        tgl.setCursorVisible(false);
        tgl.setText(tgl_periksa_);
        tgl.addTextChangedListener(watcher);
        berat_badan = (EditText) findViewById(R.id.berat_badan);
        berat_badan.setText(berat_badan_);
        berat_badan.addTextChangedListener(watcher);

        tensi_darah = (EditText) findViewById(R.id.tensi_darah);
        tensi_darah.setText(tensi_darah_);
        tensi_darah.addTextChangedListener(watcher);
        asam_urat = (EditText) findViewById(R.id.asam_urat);
        asam_urat.setText(asam_urat_);
        asam_urat.addTextChangedListener(watcher);
        kolestrol = (EditText) findViewById(R.id.kolestrol);
        kolestrol.setText(kolerstrol_);
        kolestrol.addTextChangedListener(watcher);
        catatan = (EditText) findViewById(R.id.catatan);
        catatan.setText(catatan_);
        catatan.addTextChangedListener(watcher);
        jenis_obat = (EditText) findViewById(R.id.jenis_obat);
        jenis_obat.setText(obat_);
        jenis_obat.addTextChangedListener(watcher);
        jenis_kelamin_new_type = (EditText) findViewById (R.id.jenis_kelamin_new_type);
        jenis_kelamin_new_type.setFocusable(false);
        jenis_kelamin_new_type.setEnabled(false);
        jenis_kelamin_new_type.setCursorVisible(false);
        tensi_darah_new_type = (EditText) findViewById (R.id.tensi_darah_new_type);
        tensi_darah_new_type.setFocusable(false);
        tensi_darah_new_type.setEnabled(false);
        tensi_darah_new_type.setCursorVisible(false);
        asam_urat_new_type = (EditText) findViewById (R.id.asam_urat_new_type);
        asam_urat_new_type.setFocusable(false);
        asam_urat_new_type.setEnabled(false);
        asam_urat_new_type.setCursorVisible(false);
        kolestrol_new_type = (EditText) findViewById (R.id.kolestrol_new_type);
        kolestrol_new_type.setFocusable(false);
        kolestrol_new_type.setEnabled(false);
        kolestrol_new_type.setCursorVisible(false);


        // ini ditambahin
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
                        Toast.makeText(DetailLaporanLansia.this, "format penulisan tensi darah tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//       ini ditambahin
        asam_urat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    try{
                        String value_asam_urat = asam_urat.getText().toString();
                        String jk = jenis_kelamin_new_type.getText().toString();
                        doCheckStatusAsamUrat(value_asam_urat, jk);
                    }catch (Exception e){
                        asam_urat_new_type.setText("");
                        Toast.makeText(DetailLaporanLansia.this, "format penulisan asam urat tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        ini ditambahin
        kolestrol.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    try{
                        String value_kolestrol = kolestrol.getText().toString();
                        doCheckStatusKolestrol(Integer.parseInt(value_kolestrol));
                    }catch (Exception e){
                        kolestrol_new_type.setText("");
                        Toast.makeText(DetailLaporanLansia.this, "format penulisan kolestrol tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        jenis = (Spinner) findViewById(R.id.jenis);
//        List<String> jeni_kelamin = new ArrayList<String>();
//        jeni_kelamin.add("Laki-Laki");
//        jeni_kelamin.add("Perempuan");

//        tensi = (Spinner) findViewById(R.id.tensi);
//        List<String> tensi_i = new ArrayList<String>();
//        tensi_i.add("Tensi normal");
//        tensi_i.add("Tensi tidak normal");

//        asam = (Spinner) findViewById(R.id.asam);
//        List<String> asam_i = new ArrayList<String>();
//        asam_i.add("Asam urat normal");
//        asam_i.add("Asam urat tidak normal");

//        kolestrl = (Spinner) findViewById(R.id.kolestrl);
//        List<String> kolestrol_i = new ArrayList<String>();
//        kolestrol_i.add("Kolestrol normal");
//        kolestrol_i.add("Kolestrol tidak normal");

//        ArrayAdapter<String> dataAdapterjenis = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jeni_kelamin);
//        ArrayAdapter<String> dataAdapterTensi = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tensi_i);
//        ArrayAdapter<String> dataAdapterAsam = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, asam_i);
//        ArrayAdapter<String> dataAdapterKolestrol = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kolestrol_i);

//        dataAdapterjenis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterTensi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterAsam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapterKolestrol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        jenis.setAdapter(dataAdapterjenis);
//        jenis.setOnItemSelectedListener(new JenisSpinnerJenis());
//        tensi.setAdapter(dataAdapterTensi);
//        tensi.setOnItemSelectedListener(new JenisSpinnerTensi());
//        asam.setAdapter(dataAdapterAsam);
//        asam.setOnItemSelectedListener(new JenisSpinnerAsamUrat());
//        kolestrl.setAdapter(dataAdapterKolestrol);
//        kolestrl.setOnItemSelectedListener(new JenisSpinnerKolestrol());

//        if (jenis_kelamin_ != null) {
//            int spinnerPosition = dataAdapterjenis.getPosition(jenis_kelamin_);
//            jenis.setSelection(spinnerPosition);
//        }
//        if (ket_tensi_darah_ != null) {
//            int spinnerPosition = dataAdapterTensi.getPosition(ket_tensi_darah_);
//            tensi.setSelection(spinnerPosition);
//        }
//        if (ket_asam_urat_ != null) {
//            int spinnerPosition = dataAdapterAsam.getPosition(ket_asam_urat_);
//            asam.setSelection(spinnerPosition);
//        }
//        if (kolerstrol_ != null) {
//            int spinnerPosition = dataAdapterKolestrol.getPosition(kolerstrol_);
//            kolestrl.setSelection(spinnerPosition);
//        }



//        copy the clipboard
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(DetailLaporanLansia.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });

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

        jenis_kelamin_new_type = (EditText) findViewById(R.id.jenis_kelamin_new_type);
        jenis_kelamin_new_type.setFocusable(false);
        jenis_kelamin_new_type.setInputType(InputType.TYPE_NULL);
        jenis_kelamin_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "jk");
            }
        });
        jenis_kelamin_new_type.setText(jenis_kelamin_);
        jenis_kelamin_new_type.addTextChangedListener(watcher);

        tensi_darah_new_type = (EditText) findViewById(R.id.tensi_darah_new_type);
        tensi_darah_new_type.setFocusable(false);
        tensi_darah_new_type.setInputType(InputType.TYPE_NULL);
        tensi_darah_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "tensi");
            }
        });
        tensi_darah_new_type.setText(ket_tensi_darah_);
        tensi_darah_new_type.addTextChangedListener(watcher);

        asam_urat_new_type = (EditText) findViewById(R.id.asam_urat_new_type);
        asam_urat_new_type.setFocusable(false);
        asam_urat_new_type.setInputType(InputType.TYPE_NULL);
        asam_urat_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "asam");
            }
        });
        asam_urat_new_type.setText(ket_asam_urat_);
        asam_urat_new_type.addTextChangedListener(watcher);

        jenis_obat.setFocusable(false);
        jenis_obat.setInputType(InputType.TYPE_NULL);
        jenis_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "obat");
            }
        });

        kolestrol_new_type = (EditText) findViewById(R.id.kolestrol_new_type);
        kolestrol_new_type.setFocusable(false);
        kolestrol_new_type.setInputType(InputType.TYPE_NULL);
        kolestrol_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "kolestrol");
            }
        });
        kolestrol_new_type.setText(ket_kolerstrol_);
        kolestrol_new_type.addTextChangedListener(watcher);

        voidHandleButtonSimpan();
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

    public void doCheckStatusKolestrol(int systole){
        if(systole >= 200 && systole <= 239){
            kolestrol_new_type.setText("Agak Tinggi");
        }else if(systole >= 120 && systole <= 200){
            kolestrol_new_type.setText("Normal");
        }else if(systole >= 240){
            kolestrol_new_type.setText("Tinggi");
        }else if(systole < 120){
            kolestrol_new_type.setText("Rendah");
        }else{
            kolestrol_new_type.setText("Rendah");
        }
    }

    public void doCheckStatusAsamUrat(String systole, String jk){
        Double prog = Double.valueOf(systole);
        if(jk.equals("Laki-Laki")){
            if(prog >= 2.0 && prog <= 8.5){
                asam_urat_new_type.setText("Normal");
            }else if(prog < 2.0){
                asam_urat_new_type.setText("Rendah");
            }else if(prog > 8.5){
                asam_urat_new_type.setText("Tinggi");
            }else{
                asam_urat_new_type.setText("");
            }
        }else if(jk.equals("Perempuan")){
            if(prog >= 2.0 && prog <= 8.0){
                asam_urat_new_type.setText("Normal");
            }else if(prog < 2.0){
                asam_urat_new_type.setText("Rendah");
            }else if(prog > 8.0){
                asam_urat_new_type.setText("Tinggi");
            }else{
                asam_urat_new_type.setText("");
            }
        }
    }

    public void voidHandleButtonSimpan(){
        if (nik.getText().toString().length() == 0 ||
                nama.getText().toString().length() == 0 ||
                jenis_kelamin_new_type.getText().toString().length() == 0 ||
                tensi_darah_new_type.getText().toString().length() == 0 ||
                asam_urat_new_type.getText().toString().length() == 0 ||
                kolestrol_new_type.getText().toString().length() == 0 ||
                umur.getText().toString().length() == 0 ||
                tgl.getText().toString().length() == 0 ||
                berat_badan.getText().toString().length() == 0 ||

                tensi_darah.getText().toString().length() == 0 ||
                asam_urat.getText().toString().length() == 0 ||
                kolestrol.getText().toString().length() == 0 ||
                catatan.getText().toString().length() == 0 ||
                jenis_obat.getText().toString().length() == 0) {
            btn_simpan.setEnabled(false);
        } else {
            btn_simpan.setEnabled(true);
        }
    }

    public void showBottomSheet(View view, String type) {
        String[] id = new String[]{};
        if(type.equals("jk")){
            id = new String[]{"Laki-Laki", "Perempuan"};
        }else if(type.equals("tensi")){
            id = new String[]{"Tensi normal", "Tensi tidak normal"};
        }else if(type.equals("obat")){
            id = new String[]{"Atorvastatin Calcium", "Propanolol HCI", "Allopurinol", "Tidak Ada"};
        }else if(type.equals("asam")){
            id = new String[]{"Asam urat normal", "Asam urat tidak normal"};
        }else if(type.equals("kolestrol")){
            id = new String[]{"Kolestrol normal", "Kolestrol tidak normal"};
        }else{
            id = new String[]{};
        }
        SpinnerBottomList addPhotoBottomDialogFragment = SpinnerBottomList.newInstance(DetailLaporanLansia.this,id,type);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), SpinnerBottomList.TAG);
    }

    @Override
    public void onItemClick(String item, String type) {
        if(type.equals("jk")){
            jenis_kelamin_new_type.setText(item);
        }
        if(type.equals("tensi")){
            tensi_darah_new_type.setText(item);
        }
        if(type.equals("obat")){
            jenis_obat.setText(item);
        }
        if(type.equals("asam")){
            asam_urat_new_type.setText(item);
        }
        if(type.equals("kolestrol")){
            kolestrol_new_type.setText(item);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailLaporanLansia.this);
        alertDialogBuilder.setMessage("Apakah Anda yakin ingin menghapus data ini?");
        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progDailog = new ProgressDialog(DetailLaporanLansia.this);
                progDailog.setMessage("Loading...");
                progDailog.setIndeterminate(false);
                progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDailog.setCancelable(false);
                progDailog.show();

                try {
                    JsonObject params = new JsonObject();
                    params.addProperty("id", id);
                    Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
                    Call call = apiservice.deleteCheckupLansia(params);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            progDailog.dismiss();
                            try {
                                if (response.code() == 200) {
                                    Toast.makeText(DetailLaporanLansia.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result", 1);
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                } else {
                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                    String errCode = jsonObject.getString("err_code");
                                    Toast.makeText(DetailLaporanLansia.this, "Data gagal dihapus" + errCode, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(DetailLaporanLansia.this, "Data gagal dihapus" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            progDailog.dismiss();
                            Toast.makeText(DetailLaporanLansia.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    progDailog.dismiss();
                    System.out.println("system error on global: " + e.getMessage());
                    Toast.makeText(DetailLaporanLansia.this, "Simpan data gagal" + e, Toast.LENGTH_SHORT).show();
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
        progDailog = new ProgressDialog(DetailLaporanLansia.this);
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
            params.addProperty("umur",String.valueOf(umur.getText()));
            params.addProperty("jenis_kelamin",String.valueOf(jenis_kelamin_new_type.getText()));
            params.addProperty("tgl_periksa",String.valueOf(tgl.getText()));
            params.addProperty("berat_badan",String.valueOf(berat_badan.getText()));

            params.addProperty("tensi_darah",String.valueOf(tensi_darah.getText()));
            params.addProperty("ket_tensi_darah",String.valueOf(tensi_darah_new_type.getText()));
            params.addProperty("asam_urat",String.valueOf(asam_urat.getText()));
            params.addProperty("ket_asam_urat",String.valueOf(asam_urat_new_type.getText()));
            params.addProperty("kolerstrol",String.valueOf(kolestrol.getText()));
            params.addProperty("ket_kolerstrol",String.valueOf(kolestrol_new_type.getText()));
            params.addProperty("catatan",String.valueOf(catatan.getText()));
            params.addProperty("obat",String.valueOf(jenis_obat.getText()));

            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.updateCheckupLansia(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    progDailog.dismiss();
                    try{
                        if (response.code()==200){
                            Toast.makeText(DetailLaporanLansia.this, "Data berhasil dirubah", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result",1);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        } else  {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String errCOde = jsonObject.getString("err_code");
                            Toast.makeText(DetailLaporanLansia.this, "Data gagal dirubah" + errCOde, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(DetailLaporanLansia.this, "Data gagal dirubah" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    progDailog.dismiss();
                    Toast.makeText(DetailLaporanLansia.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            progDailog.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(DetailLaporanLansia.this, "Simpan data gagal" + e, Toast.LENGTH_SHORT).show();
        }
    }

    class JenisSpinnerJenis implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class JenisSpinnerTensi implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class JenisSpinnerAsamUrat implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class JenisSpinnerKolestrol implements AdapterView.OnItemSelectedListener
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