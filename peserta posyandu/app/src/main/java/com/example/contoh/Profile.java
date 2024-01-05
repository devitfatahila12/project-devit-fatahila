package com.example.contoh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    TextView nik, nama, edit_profil, jk, alamat, tgl_lahir, kategori_peserta, kartu_keluarga, ubah_kartu_keluarga, copy;
    loading mLoading;
    SessionManager ssmm;
    public String kk;
    String id_temp, nik_temp, nama_temp, jk_temp, alamat_temp, tgl_lahir_temp, kategori_peserta_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mLoading = new loading(Profile.this);
        ssmm = new SessionManager(Profile.this);
        HashMap<String,String> user = ssmm.getUserDetails();
        String id = user.get(SessionManager.key_id);

        nik = (TextView)findViewById(R.id.nik);
        nama = (TextView)findViewById(R.id.nama);
        jk = (TextView)findViewById(R.id.jk);

        alamat = (TextView)findViewById(R.id.alamat);
        edit_profil = (TextView)findViewById(R.id.edit_profil);
        copy    = (TextView)findViewById(R.id.copy);
        tgl_lahir = (TextView)findViewById(R.id.tgl_lahir);
        kategori_peserta = (TextView)findViewById(R.id.kategori_peserta);
        kartu_keluarga = (TextView)findViewById(R.id.kartu_keluarga);
        kartu_keluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetails mImgDtl = new ImageDetails(Profile.this, kk);
                mImgDtl.show();
            }
        });

        edit_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edtProfile = new Intent(Profile.this, Edit_Profil.class);
                edtProfile.putExtra("id_temp", id_temp);
                edtProfile.putExtra("nik_temp", nik_temp);
                edtProfile.putExtra("nama_temp", nama_temp);
                edtProfile.putExtra("jk_temp", jk_temp);
                edtProfile.putExtra("alamat_temp", alamat_temp);
                edtProfile.putExtra("tgl_lahir_temp", tgl_lahir_temp);
                edtProfile.putExtra("kategori_peserta_temp", kategori_peserta_temp);
                ((Activity) Profile.this).startActivityForResult(edtProfile,1);
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(Profile.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });
        ubah_kartu_keluarga = (TextView)findViewById(R.id.ubah_kartu_keluarga);
        ubah_kartu_keluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, UbahKK.class);
                Bundle b = new Bundle();
                b.putString("id", id);
                intent.putExtras(b);
                ((Activity) Profile.this).startActivityForResult(intent,1);
            }
        });

        voidData(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String returnID = data.getStringExtra("id");
                voidData(returnID);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }

    public void voidData(String id){
        mLoading.show();
        try {
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
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
                            String nama_ = jsonobjectData.getString("nama");
                            String nik_ =  jsonobjectData.getString("nik");

                            String alamat_ = jsonobjectData.getString("alamat");
                            String tanggal_lahir_ =  jsonobjectData.getString("tanggal_lahir");
                            String jenis_kelamin_ = jsonobjectData.getString("jenis_kelamin");
                            String peserta_posyandu_ = jsonobjectData.getString("peserta_posyandu");
                            String kartu_keluarga_ = jsonobjectData.getString("kartu_keluarga");

                            kk = kartu_keluarga_;
                            nik.setText(nik_);
                            nama.setText(nama_);
                            jk.setText(jenis_kelamin_);

                            alamat.setText(alamat_);
                            tgl_lahir.setText(tanggal_lahir_);
                            kategori_peserta.setText(peserta_posyandu_);

                            id_temp = id;
                            nik_temp = nik_;
                            nama_temp = nama_;
                            jk_temp = jenis_kelamin_;
                            alamat_temp = alamat_;
                            tgl_lahir_temp = tanggal_lahir_;
                            kategori_peserta_temp = peserta_posyandu_;
                        } else  {
                            Toast.makeText(Profile.this, "Pengambilan data gagal", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(Profile.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(Profile.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(Profile.this, "Gagal menampilkan data profile" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        finish();
    }
}