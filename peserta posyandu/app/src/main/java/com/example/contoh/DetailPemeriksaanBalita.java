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
import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemeriksaanBalita extends AppCompatActivity {

    TextView nik, nama, umur, tgl_periksa, berat_badan, ket_brt_bdn, tinggi_badan, ket_tinggi, lingkar,
            ket_lingkar, jenis, alamat, tgl_lahir, copy, obat, catatan, orang_tua_kandung;
    loading mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemeriksaan_balita);

        mLoading = new loading(DetailPemeriksaanBalita.this);
        Bundle b = getIntent().getExtras();
        String id_ = b.getString("link_id");

        nik = (TextView) findViewById(R.id.nik);
        nama = (TextView) findViewById(R.id.nama);
        umur = (TextView) findViewById(R.id.umur);
        copy = (TextView) findViewById(R.id.copy);
        tgl_periksa = (TextView) findViewById(R.id.tgl_periksa);
        berat_badan = (TextView) findViewById(R.id.berat_badan);
        ket_brt_bdn = (TextView) findViewById(R.id.ket_brt_bdn);
        tinggi_badan = (TextView) findViewById(R.id.tinggi_badan);
        ket_tinggi = (TextView) findViewById(R.id.ket_tinggi);
        lingkar = (TextView) findViewById(R.id.lingkar);
        ket_lingkar = (TextView) findViewById(R.id.ket_lingkar);
        jenis = (TextView) findViewById(R.id.jenis);
        obat = (TextView) findViewById(R.id.obat);
        alamat = (TextView) findViewById(R.id.alamat);
        catatan = (TextView) findViewById(R.id.catatan);
        tgl_lahir = (TextView) findViewById(R.id.tgl_lahir);
        orang_tua_kandung = (TextView) findViewById(R.id.orang_tua_kandung);

        VoidData(id_);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(DetailPemeriksaanBalita.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void VoidData(String id){
        mLoading.show();
        try{
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call call = apiservice.getIDCheckupBalita(params);
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
                                Toast.makeText(DetailPemeriksaanBalita.this, "Detail data tidak ditemukan", Toast.LENGTH_SHORT).show();
                            } else {
                                String nama_ = jsonobjectData.getString("nama");
                                String nik_ =  jsonobjectData.getString("nik");
                                String umur_ =  jsonobjectData.getString("umur");
                                String tgl_periksa_ = jsonobjectData.getString("tgl_periksa");
                                String berat_badan_ =  jsonobjectData.getString("berat_badan");
                                String ket_berat_badan_ = jsonobjectData.getString("ket_berat_badan");
                                String tinggi_badan_ = jsonobjectData.getString("tinggi_badan");
                                String ket_tinggi_badan_ = jsonobjectData.getString("ket_tinggi_badan");
                                String lingkar_kepala_ = jsonobjectData.getString("lingkar_kepala");
                                String ket_lingkar_kepala_ = jsonobjectData.getString("ket_lingkar_kepala");
                                String jenis_imunisasi_ = jsonobjectData.getString("jenis_imunisasi");
                                String alamat_ = jsonobjectData.getString("alamat");
                                String tanggal_lahir_ = jsonobjectData.getString("tanggal_lahir");
                                String obat_ = jsonobjectData.getString("obat");
                                String orang_tua_kandung_ = jsonobjectData.getString("orang_tua_kandung");
                                String catatan_ = jsonobjectData.getString("catatan");

                                nik.setText(nik_);
                                nama.setText(nama_);
                                umur.setText(umur_);
                                tgl_periksa.setText(tgl_periksa_);
                                berat_badan.setText(berat_badan_);
                                ket_brt_bdn.setText(ket_berat_badan_);
                                tinggi_badan.setText(tinggi_badan_);
                                ket_tinggi.setText(ket_tinggi_badan_);
                                lingkar.setText(lingkar_kepala_);
                                ket_lingkar.setText(ket_lingkar_kepala_);
                                jenis.setText(jenis_imunisasi_);
                                obat.setText(obat_);
                                alamat.setText(alamat_);
                                tgl_lahir.setText(tanggal_lahir_);
                                catatan.setText(catatan_);
                                orang_tua_kandung.setText(orang_tua_kandung_);
                            }
                        } else  {
                            Toast.makeText(DetailPemeriksaanBalita.this, "Pengambilan data gagal", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(DetailPemeriksaanBalita.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(DetailPemeriksaanBalita.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(DetailPemeriksaanBalita.this, "gagal mendapatkan data" + e, Toast.LENGTH_SHORT).show();
        }
    }
}