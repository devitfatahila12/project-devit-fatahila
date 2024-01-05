package com.example.aplikasirumahadat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    private TextView tvNama, tvTentang;
    private ImageView ivFoto;
    private String yNama, yTentang, yFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();

        Intent terima = getIntent();
        yNama = terima.getStringExtra("xNama");
        yTentang = terima.getStringExtra("xTentang");
        yFoto = terima.getStringExtra("xFoto");

        tvNama.setText(yNama);
        tvTentang.setText(yTentang);

        Glide.with(DetailActivity.this)
                .load(yFoto)
                .into(ivFoto);

    }

    private void initView(){
        tvNama = findViewById(R.id.tv_nama_pahlawan);
        tvTentang = findViewById(R.id.tv_tentang_pahlawan);
        ivFoto = findViewById(R.id.iv_foto);
    }
}