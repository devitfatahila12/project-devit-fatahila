package com.example.kader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class notif extends AppCompatActivity {

    Button btn_kirim;
    EditText pesan;
    loading mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);



        mLoading = new loading(notif.this);

        pesan = (EditText)findViewById(R.id.pesan);

        btn_kirim = (Button) findViewById(R.id.btn_kirim);
        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doKirim();
            }
        });
    }

    public void doKirim(){
        mLoading.show();

        try {
            JsonObject params = new JsonObject();
            params.addProperty("title","Pemberitahuan Jadwal Posyandu");
            params.addProperty("group","notif_jadwal");
            params.addProperty("message",String.valueOf(pesan.getText()));
            params.addProperty("page_link","none");
            params.addProperty("device_id","");
            params.addProperty("data","");
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.sendNotif(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    mLoading.dismiss();
                    if (response.code()==200){
                        Toast.makeText(notif.this, "Kirim notif berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    } else  {
                        Toast.makeText(notif.this, "Kirim notif gagal", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(notif.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(notif.this, "kirim notifikasi gagal" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}