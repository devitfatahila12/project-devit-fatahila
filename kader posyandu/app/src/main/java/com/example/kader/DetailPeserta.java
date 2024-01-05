package com.example.kader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class DetailPeserta extends AppCompatActivity {

    TextView nik, nama, jk, alamat, tgl_lahir, kategori_peserta, kartu_keluarga, copy;
     Button periksa;
//    TextView hapus_akun;
    loading mLoading;
    public String kk, kp, nk;
    Boolean isRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_peserta);

        mLoading = new loading((DetailPeserta.this));

        Bundle b = getIntent().getExtras();
        String id_ = b.getString("id");
        if(id_.isEmpty()){
            Toast.makeText(DetailPeserta.this, "Detail data tidak ditemukan", Toast.LENGTH_SHORT).show();
            isRefresh = true;
            onBackPressed();
        }

//        hapus_akun = (TextView)findViewById(R.id.hapus_akun);
        nik = (TextView)findViewById(R.id.nik);
        nama = (TextView)findViewById(R.id.nama);
        jk = (TextView)findViewById(R.id.jk);
        periksa = (Button) findViewById(R.id.periksa);

        copy = (TextView)findViewById(R.id.copy);
        alamat = (TextView)findViewById(R.id.alamat);
        tgl_lahir = (TextView)findViewById(R.id.tgl_lahir);
        kategori_peserta = (TextView)findViewById(R.id.kategori_peserta);
        kartu_keluarga = (TextView)findViewById(R.id.kartu_keluarga);
        kartu_keluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetails mImgDtl = new ImageDetails(DetailPeserta.this, kk);
                mImgDtl.show();
            }
        });

        voidData(id_);

        //        copy the clipboard
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(DetailPeserta.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });

        periksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Berdasarkan jenisPeserta, Anda dapat menentukan apa yang harus dilakukan selanjutnya.
                // Misalnya, jika jenisPeserta adalah "Balita", tampilkan data di aktivitas PemeriksaanBalitaActivity.
                if (kp.equals("Balita")) {
                    Intent intent = new Intent(DetailPeserta.this, CheckUpBalita.class);
                    Bundle b = new Bundle();
                    b.putString("nik", nk);
                    intent.putExtras(b);
                    startActivity(intent);
                } else if (kp.equals("Ibu Hamil")) {
                    Intent intent = new Intent(DetailPeserta.this, CheckupIbuHamil.class);
                    Bundle b = new Bundle();
                    b.putString("nik", nk);
                    intent.putExtras(b);
                    startActivity(intent);
                } else if (kp.equals("Lansia")) {
                    Intent intent = new Intent(DetailPeserta.this, CheckupLansia.class);
                    Bundle b = new Bundle();
                    b.putString("nik", nk);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

//        hapus_akun.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialogConfrim(id_);
//            }
//        });
    }

//    public void showDialogConfrim(String id) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(DetailPeserta.this);
//        builder.setTitle("Konfirmasi Permintaan")
//                .setMessage("Apakah Anda yakin untuk melakukan penghapusan akun ini ? Tindakan ini tidak dapat dibatalkan!")
//                .setCancelable(false)
//                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        doHapusAkun(id);
//                    }
//                })
//                .setNegativeButton("Tidak", null)
//                .show();
//    }

//    public void doHapusAkun(String id){
//        mLoading.show();
//        try{
//            JsonObject params = new JsonObject();
//            params.addProperty("id",id);
//            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
//            Call call = apiservice.removeAccount(params);
//            call.enqueue(new Callback() {
//                @Override
//                public void onResponse(Call call, Response response) {
//                    mLoading.dismiss();
//                    try{
//                        if (response.code()==200){
//                            Toast.makeText(DetailPeserta.this, "Akun berhasil dihapus", Toast.LENGTH_SHORT).show();
//                            isRefresh = true;
//                            onBackPressed();
//                        } else  {
//                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
//                            String errCOde = jsonObject.getString("err_code");
//                            Toast.makeText(DetailPeserta.this, "Hapus akun gagal" + errCOde, Toast.LENGTH_SHORT).show();
//                        }
//                    }catch (Exception e){
//                        Toast.makeText(DetailPeserta.this, "Hapus akun gagal" + e.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call call, Throwable t) {
//                    mLoading.dismiss();
//                    Toast.makeText(DetailPeserta.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }catch (Exception e){
//            mLoading.dismiss();
//            System.out.println("system error on global: " +e.getMessage());
//            Toast.makeText(DetailPeserta.this, "Hapus akun gagal" + e, Toast.LENGTH_SHORT).show();
//        }
//    }

    public void voidData(String id){
        mLoading.show();
        try{
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.detailPesertaById(params);
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
                                Toast.makeText(DetailPeserta.this, "Detail data tidak ditemukan", Toast.LENGTH_SHORT).show();
                                isRefresh = true;
                                onBackPressed();
                            } else {
                                String nama_ = jsonobjectData.getString("nama");
                                String nik_ =  jsonobjectData.getString("nik");

                                String alamat_ = jsonobjectData.getString("alamat");
                                String tanggal_lahir_ =  jsonobjectData.getString("tanggal_lahir");
                                String jenis_kelamin_ = jsonobjectData.getString("jenis_kelamin");
                                String peserta_posyandu_ = jsonobjectData.getString("peserta_posyandu");
                                String kartu_keluarga_ = jsonobjectData.getString("kartu_keluarga");

                                kk = kartu_keluarga_;
                                nk = nik_;
                                nik.setText(nik_);
                                nama.setText(nama_);
                                jk.setText(jenis_kelamin_);

                                alamat.setText(alamat_);
                                tgl_lahir.setText(tanggal_lahir_);
                                kp = peserta_posyandu_;
                                kategori_peserta.setText(peserta_posyandu_);
                            }
                        } else  {
                            Toast.makeText(DetailPeserta.this, "Pengambilan data gagal", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(DetailPeserta.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(DetailPeserta.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(DetailPeserta.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",1);
        if(isRefresh){
            setResult(Activity.RESULT_OK,returnIntent);
        }else{
            setResult(Activity.RESULT_CANCELED,returnIntent);
        }
        finish();
    }
}