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

public class DetailPemeriksaanIbuHamil extends AppCompatActivity {

    loading mLoading;
    TextView nik, nama, tgl_lahir, alamat, umur_hamil, tgl,berat_badan,tensi, ket_tensi, lingkar, ket_lingkar, tinggi_fundus,
            ket_tinggi_fundus, denyut, ket_denyut, catatan, jenis_obat, copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemeriksaan_ibu_hamil);

        mLoading = new loading(DetailPemeriksaanIbuHamil.this);
        Bundle b = getIntent().getExtras();
        String id_ = b.getString("link_id");

        nik = (TextView) findViewById(R.id.nik);
        nama = (TextView)findViewById(R.id.nama);
        tgl_lahir = (TextView)findViewById(R.id.tgl_lahir );
        alamat = (TextView)findViewById(R.id.alamat);
        umur_hamil = (TextView)findViewById(R.id.umur_hamil);
        tgl = (TextView)findViewById(R.id.tgl);
        berat_badan = (TextView)findViewById(R.id.berat_badan);
        tensi = (TextView)findViewById(R.id.tensi);
        copy = (TextView)findViewById(R.id.copy);
        ket_tensi = (TextView)findViewById(R.id.ket_tensi );
        lingkar = (TextView)findViewById(R.id.lingkar);
        ket_lingkar = (TextView)findViewById(R.id.ket_lingkar);
        tinggi_fundus = (TextView)findViewById(R.id.tinggi_fundus);
        ket_tinggi_fundus = (TextView)findViewById(R.id.ket_tinggi_fundus);
        denyut = (TextView)findViewById(R.id.denyut);
        ket_denyut = (TextView)findViewById(R.id.ket_denyut);
        catatan = (TextView)findViewById(R.id.catatan );
        jenis_obat = (TextView)findViewById(R.id.jenis_obat);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(DetailPemeriksaanIbuHamil.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

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
            Call call = apiservice.getIDCheckupIbuHamil(params);
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
                                Toast.makeText(DetailPemeriksaanIbuHamil.this, "Detail data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            } else {
                                String nama_ = jsonobjectData.getString("nama");
                                String nik_ =  jsonobjectData.getString("nik");
                                String alamat_ =  jsonobjectData.getString("alamat");
                                String usia_hamil_ = jsonobjectData.getString("usia_hamil");
                                String tanggal_periksa_ =  jsonobjectData.getString("tanggal_periksa");
                                String berat_badan_ = jsonobjectData.getString("berat_badan");
                                String tensi_darah_ = jsonobjectData.getString("tensi_darah");
                                String ket_tensi_darah_ = jsonobjectData.getString("ket_tensi_darah");
                                String lingkar_lengan_atas_ = jsonobjectData.getString("lingkar_lengan_atas");
                                String ket_lingkar_lengan_atas_ = jsonobjectData.getString("ket_lingkar_lengan_atas");
                                String denyut_jantung_bayi_ = jsonobjectData.getString("denyut_jantung_bayi");
                                String ket_denyut_jantung_bayi_ = jsonobjectData.getString("ket_denyut_jantung_bayi");
                                String tinggi_fundus_ = jsonobjectData.getString("tinggi_fundus");
                                String ket_tinggi_fundus_ = jsonobjectData.getString("ket_tinggi_fundus");
                                String catatan_ = jsonobjectData.getString("catatan");
                                String obat_ = jsonobjectData.getString("obat");
                                String tanggal_lahir_ = jsonobjectData.getString("tanggal_lahir");

                                nik.setText(nik_);
                                nama.setText(nama_);
                                tgl_lahir.setText(tanggal_lahir_);
                                alamat.setText(alamat_);
                                umur_hamil.setText(usia_hamil_);
                                tgl.setText(tanggal_periksa_);
                                berat_badan.setText(berat_badan_);
                                tensi.setText(tensi_darah_);
                                ket_tensi.setText(ket_tensi_darah_);
                                lingkar.setText(lingkar_lengan_atas_);
                                ket_lingkar.setText(ket_lingkar_lengan_atas_);
                                tinggi_fundus.setText(tinggi_fundus_);
                                ket_tinggi_fundus.setText(ket_tinggi_fundus_);
                                denyut.setText(denyut_jantung_bayi_);
                                ket_denyut.setText(ket_denyut_jantung_bayi_);
                                catatan.setText(catatan_);
                                jenis_obat.setText(obat_);
                            }
                        } else  {
                            Toast.makeText(DetailPemeriksaanIbuHamil.this, "Pengambilan data gagal", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(DetailPemeriksaanIbuHamil.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(DetailPemeriksaanIbuHamil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(DetailPemeriksaanIbuHamil.this, "gagal mendapatkan data" + e, Toast.LENGTH_SHORT).show();
        }
    }
}