package com.example.contoh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemeriksaanLansia extends AppCompatActivity {
    loading mLoading;
    TextView nik, nama, tgl_lahir, alamat, copy, umur, jenis_kelamin, tgl_periksa, berat_badan, tensi, ket_tensi, asam, ket_asam, kolestrol, Ket_kolestrol,
        catatan, jenis_obat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemeriksaan_lansia);

        nik = (TextView) findViewById(R.id.nik);
        nama = (TextView) findViewById(R.id.nama);
        tgl_lahir = (TextView) findViewById(R.id.tgl_lahir);
        copy = (TextView) findViewById(R.id.copy);
        umur = (TextView) findViewById(R.id.umur);
        jenis_kelamin = (TextView) findViewById(R.id.jenis_kelamin);
        tgl_periksa = (TextView) findViewById(R.id.tgl_periksa);
        alamat = (TextView) findViewById(R.id.alamat);
        berat_badan = (TextView) findViewById(R.id.berat_badan);
        tensi = (TextView) findViewById(R.id.tensi);
        ket_tensi = (TextView) findViewById(R.id.ket_tensi);
        asam = (TextView) findViewById(R.id.asam);
        ket_asam = (TextView) findViewById(R.id.ket_asam);
        kolestrol = (TextView) findViewById(R.id.kolestrol);
        Ket_kolestrol = (TextView) findViewById(R.id.Ket_kolestrol);
        catatan = (TextView) findViewById(R.id.catatan);
        jenis_obat = (TextView) findViewById(R.id.jenis_obat);

        mLoading = new loading(DetailPemeriksaanLansia.this);
        Bundle b = getIntent().getExtras();
        String id_ = b.getString("link_id");

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(DetailPemeriksaanLansia.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });

        VoidData(id_);
    }

    public void VoidData(String id){
        mLoading.show();
        try{
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call call = apiservice.getIDCheckuplansia(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    mLoading.dismiss();
                    try{
                        if (response.code()==200){
                            GsonBuilder builder = new GsonBuilder();
                            builder.serializeNulls();
                            Gson gson = builder.setPrettyPrinting().create();
                            JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                            JSONObject jsonobjectData = new JSONObject(jsonObject.getString("data"));
                            if (jsonobjectData.length() == 0) {
                                Toast.makeText(DetailPemeriksaanLansia.this, "Detail data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            } else {
                                String nama_ = jsonobjectData.getString("nama");
                                String nik_ =  jsonobjectData.getString("nik");
                                String umur_ =  jsonobjectData.getString("umur");
                                String jenis_kelamin_ = jsonobjectData.getString("jenis_kelamin");
                                String tgl_periksa_ =  jsonobjectData.getString("tgl_periksa");
                                String berat_badan_ = jsonobjectData.getString("berat_badan");
                                String tensi_darah_ = jsonobjectData.getString("tensi_darah");
                                String ket_tensi_darah_ = jsonobjectData.getString("ket_tensi_darah");
                                String asam_urat_ = jsonobjectData.getString("asam_urat");
                                String ket_asam_urat_ = jsonobjectData.getString("ket_asam_urat");
                                String kolerstrol_ = jsonobjectData.getString("kolerstrol");
                                String ket_kolerstrol_ = jsonobjectData.getString("ket_kolerstrol");
                                String catatan_ = jsonobjectData.getString("catatan");
                                String obat_ = jsonobjectData.getString("obat");
                                String alamat_ = jsonobjectData.getString("alamat");
                                String tanggal_lahir_ = jsonobjectData.getString("tanggal_lahir");

                                nik.setText(nik_);
                                nama.setText(nama_);
                                tgl_lahir.setText(tanggal_lahir_);
                                umur.setText(umur_);
                                jenis_kelamin.setText(jenis_kelamin_);
                                tgl_periksa.setText(tgl_periksa_);
                                berat_badan.setText(berat_badan_);
                                tensi.setText(tensi_darah_);
                                alamat.setText(alamat_);
                                ket_tensi.setText(ket_tensi_darah_);
                                asam.setText(asam_urat_);
                                ket_asam.setText(ket_asam_urat_);
                                kolestrol.setText(kolerstrol_);
                                Ket_kolestrol.setText(ket_kolerstrol_);
                                catatan.setText(catatan_);
                                jenis_obat.setText(obat_);
                            }
                        } else  {
                            Toast.makeText(DetailPemeriksaanLansia.this, "Pengambilan data gagal", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(DetailPemeriksaanLansia.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(DetailPemeriksaanLansia.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(DetailPemeriksaanLansia.this, "gagal mendapatkan data" + e, Toast.LENGTH_SHORT).show();
        }
    }
}