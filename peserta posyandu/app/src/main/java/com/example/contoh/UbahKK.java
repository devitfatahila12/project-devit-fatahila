package com.example.contoh;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahKK extends AppCompatActivity {

    String base64_image = null;
    private final int GALLERY_REQ_CODE = 1000;
    ImageView imgGallery;
    Button btn_simpan;
    loading mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kk);

         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoading = new loading(UbahKK.this);

        Bundle b = getIntent().getExtras();
        String id_ = b.getString("id");

        btn_simpan = (Button)findViewById(R.id.btn_simpan);
        imgGallery = (ImageView)findViewById(R.id.imgGallery);
        Button btnGallery = findViewById(R.id.btnGallery);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSimpan(id_);
            }
        });
    }

    public void doSimpan(String id){
        if(TextUtils.isEmpty(base64_image)){
            Toast.makeText(UbahKK.this,"gambar tidak dapat diambil dengan benar, silahkan coba kembali",Toast.LENGTH_SHORT).show();
        }else{
            mLoading.show();
            try{
                JsonObject params = new JsonObject();
                params.addProperty("id",id);
                params.addProperty("kk",base64_image);
                ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
                Call call = apiservice.uploadKK(params);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        mLoading.dismiss();
                        if (response.code()==200){
                            Toast.makeText(UbahKK.this, "upload berhasil", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("id", id);
                            returnIntent.putExtra("result",1);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        } else  {
                            Toast.makeText(UbahKK.this, "upload Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        mLoading.dismiss();
                        Toast.makeText(UbahKK.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                mLoading.dismiss();
                System.out.println("system error on global: " +e.getMessage());
                Toast.makeText(UbahKK.this, "upload Gagal" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            base64_image = null;
            if (requestCode==GALLERY_REQ_CODE){
                try {
                    ParcelFileDescriptor parcelFileDescriptor =
                            getContentResolver().openFileDescriptor(data.getData(), "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    imgGallery.setImageBitmap(image);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    base64_image = Base64.encodeToString(bytes, Base64.DEFAULT);
                } catch (IOException e) {
                    base64_image = null;
                    Toast.makeText(UbahKK.this,"error mengambil gambar" + e.toString(),Toast.LENGTH_SHORT).show();
                }
//                imgGallery.setImageURI(data.getData());
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                Bitmap bm=((BitmapDrawable)imgGallery.getDrawable()).getBitmap();
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//                byte[] bytes = byteArrayOutputStream.toByteArray();
//                base64_image = Base64.encodeToString(bytes, Base64.DEFAULT);

            }
        }
    }
}