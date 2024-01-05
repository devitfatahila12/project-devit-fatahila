package com.example.aplikasirumahadat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvPahlawan;
    private ArrayList<ModelPahlawan> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvPahlawan = findViewById(R.id.rv_pahlawan);
        rvPahlawan.setHasFixedSize(true);

        data.addAll(DataPahlawan.ambilDataPahlawan());
        tampilDataCard();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_right, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){


           case  R.id.menu_profile:
               tampilProfil();
               break;
       }
        return super.onOptionsItemSelected(item);
    }

    private void tampilDataCard(){
        rvPahlawan.setLayoutManager(new LinearLayoutManager(this));
        AdapterCard colokanCard = new AdapterCard(data);
        rvPahlawan.setAdapter(colokanCard);

        colokanCard.setOnItemClickCallBack(new AdapterGrid.onItemClickCallBack() {
            @Override
            public void onItemClicked(ModelPahlawan data) {
                Intent pindah = new Intent(MainActivity.this, DetailActivity.class);
                pindah.putExtra("xnama", data.getNama());
                pindah.putExtra("xTentang", data.getTentang());
                pindah.putExtra("xFoto", data.getFoto());
                startActivity(pindah);
            }
        });
    }

    private void tampilDataGrid(){
        rvPahlawan.setLayoutManager(new GridLayoutManager(this,2));
        AdapterGrid colokanGrid = new AdapterGrid(data);
        rvPahlawan.setAdapter(colokanGrid);

        colokanGrid.setOnItemClickCallBack(new AdapterGrid.onItemClickCallBack() {
            @Override
            public void onItemClicked(ModelPahlawan data) {
                Toast.makeText(MainActivity.this, "Nama Pahlawan" + data.getNama(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tampilProfil() {
        Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
        startActivity(intent);
    }

}
