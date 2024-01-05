package com.example.kader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanBalita extends AppCompatActivity {

    public ArrayList<ModelLaporanBalita> modelLaporanBalita = new ArrayList<ModelLaporanBalita>();
    RecyclerView recycler_laporan_balita;
    public RecyclerViewAdapterLaporanBalita adapter;
    loading mLoading;
    SearchView search;
    public int page = 1;
    public int totalPages;
    public boolean setSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_balita);

        mLoading = new loading(LaporanBalita.this);

        recycler_laporan_balita = (RecyclerView) findViewById(R.id.recycler_laporan_balita);
        recycler_laporan_balita.setLayoutManager(new GridLayoutManager(LaporanBalita.this,1));
        recycler_laporan_balita.addItemDecoration(new SpacesItemDecoration(1));

//        codingan ini digunakan untuk apabila recycle view diklik maka akan masuk ke halaman selanjutnya atau
//        menampilkan detail laporan
        adapter = new RecyclerViewAdapterLaporanBalita(LaporanBalita.this, modelLaporanBalita, new RecyclerViewAdapterLaporanBalita.OnItemClickListener() {
            @Override
            public void onItemClick(ModelLaporanBalita item) {

            }
        });
        recycler_laporan_balita.setAdapter(adapter);
        recycler_laporan_balita.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if(!setSearch){
                    if(page != totalPages){
                        LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                        int numItems = recycler_laporan_balita.getAdapter().getItemCount();
                        boolean bool = (pos >= numItems - 1);
                        if(bool){
                            page = page + 1;
                            voidData(page);
                        }
                    }
                }
            }
        });

        search = (SearchView) findViewById(R.id.search);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 0){
                    setSearch = true;
                    doSearch(query.toString());
                } else {
                    setSearch = false;
                    modelLaporanBalita.clear();
                    voidData(1);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    CharSequence query = search.getQuery();
                    if(query.length() > 0){
                        setSearch = true;
                        doSearch(query.toString());
                    } else {
                        setSearch = false;
                        modelLaporanBalita.clear();
                        voidData(1);
                    }
                }
            }
        });
        voidData(page);
    }

    public void doSearch(String query){
        modelLaporanBalita.clear();
        mLoading.show();

        try {
            JsonObject params = new JsonObject();
            params.addProperty("search", query);
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.searchCheckupBalita(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    mLoading.dismiss();
                    try{
                        GsonBuilder builder = new GsonBuilder();
                        builder.serializeNulls();
                        Gson gson = builder.setPrettyPrinting().create();
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        JSONArray listData = jsonObject.getJSONArray("data");
                        ArrayList<ModelLaporanBalita> arrayListModelLaporanBalita = new ArrayList<ModelLaporanBalita>();

                        for (int i=0; i<listData.length(); i++) {
                            JSONObject c = listData.getJSONObject(i);
                            String id = c.getString("id");
                            String nama = c.getString("nama");
                            String nik = c.getString("nik");
                            String umur = c.getString("umur");
                            String alamat = c.getString("alamat");
                            String tgl_periksa = c.getString("tgl_periksa");
                            String berat_badan = c.getString("berat_badan");
                            String ket_berat_badan = c.getString("ket_berat_badan");
                            String tinggi_badan = c.getString("tinggi_badan");
                            String ket_tinggi_badan = c.getString("ket_tinggi_badan");
                            String lingkar_kepala = c.getString("lingkar_kepala");
                            String ket_lingkar_kepala = c.getString("ket_lingkar_kepala");
                            String jenis_imunisasi = c.getString("jenis_imunisasi");
                            String catatan = c.getString("catatan");
                            String obat = c.getString("obat");
                            String orang_tua_kandung = c.getString("orang_tua_kandung");

                            String tanggal_lahir = c.getString("tanggal_lahir");
                            String jenis_kelamin = c.getString("jenis_kelamin");

                            ModelLaporanBalita modelLaporanBalita = new ModelLaporanBalita();
                            modelLaporanBalita.setId(id);
                            modelLaporanBalita.setNama(nama);
                            modelLaporanBalita.setNik(nik);
                            modelLaporanBalita.setUmur(umur);
                            modelLaporanBalita.setTgl_periksa(tgl_periksa);
                            modelLaporanBalita.setBerat_badan(berat_badan);
                            modelLaporanBalita.setKet_berat_badan(ket_berat_badan);
                            modelLaporanBalita.setTinggi_badan(tinggi_badan);
                            modelLaporanBalita.setKet_tinggi_badan(ket_tinggi_badan);
                            modelLaporanBalita.setLingkar_kepala(lingkar_kepala);
                            modelLaporanBalita.setKet_lingkar_kepala(ket_lingkar_kepala);
                            modelLaporanBalita.setJenis_imunisasi(jenis_imunisasi);
                            modelLaporanBalita.setCatatan(catatan);
                            modelLaporanBalita.setObat(obat);
                            modelLaporanBalita.setAlamat(alamat);
                            modelLaporanBalita.setOrang_tua_kandung(orang_tua_kandung);

                            modelLaporanBalita.setTanggal_lahir(tanggal_lahir);
                            modelLaporanBalita.setJenis_kelamin(jenis_kelamin);

                            arrayListModelLaporanBalita.add(modelLaporanBalita);
                        }
                        modelLaporanBalita.addAll(arrayListModelLaporanBalita);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(LaporanBalita.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(LaporanBalita.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(LaporanBalita.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void voidData(int page){
        mLoading.show();

        try {
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            JsonObject params = new JsonObject();
            params.addProperty("page",page);
            Call call = apiservice.listCheckupBalita(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    mLoading.dismiss();
                    try{
                        GsonBuilder builder = new GsonBuilder();
                        builder.serializeNulls();
                        Gson gson = builder.setPrettyPrinting().create();
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        JSONObject jsonobjectData = new JSONObject(jsonObject.getString("data"));
                        String totalPage = jsonobjectData.getString("totalPage");
                        totalPages = Integer.parseInt(totalPage);
                        JSONArray listData = jsonobjectData.getJSONArray("rows");
                        ArrayList<ModelLaporanBalita> arrayListModelLaporanBalita = new ArrayList<ModelLaporanBalita>();

                        for (int i=0; i<listData.length(); i++) {
                            JSONObject c = listData.getJSONObject(i);
                            String id = c.getString("id");
                            String nama = c.getString("nama");
                            String nik = c.getString("nik");
                            String umur = c.getString("umur");
                            String tgl_periksa = c.getString("tgl_periksa");
                            String berat_badan = c.getString("berat_badan");
                            String ket_berat_badan = c.getString("ket_berat_badan");
                            String tinggi_badan = c.getString("tinggi_badan");
                            String ket_tinggi_badan = c.getString("ket_tinggi_badan");
                            String lingkar_kepala = c.getString("lingkar_kepala");
                            String ket_lingkar_kepala = c.getString("ket_lingkar_kepala");
                            String jenis_imunisasi = c.getString("jenis_imunisasi");
                            String catatan = c.getString("catatan");
                            String alamat = c.getString("alamat");
                            String obat = c.getString("obat");
                            String orang_tua_kandung = c.getString("orang_tua_kandung");

                            String tanggal_lahir = c.getString("tanggal_lahir");
                            String jenis_kelamin = c.getString("jenis_kelamin");

                            ModelLaporanBalita modelLaporanBalita = new ModelLaporanBalita();
                            modelLaporanBalita.setId(id);
                            modelLaporanBalita.setNama(nama);
                            modelLaporanBalita.setNik(nik);
                            modelLaporanBalita.setUmur(umur);
                            modelLaporanBalita.setTgl_periksa(tgl_periksa);
                            modelLaporanBalita.setBerat_badan(berat_badan);
                            modelLaporanBalita.setKet_berat_badan(ket_berat_badan);
                            modelLaporanBalita.setTinggi_badan(tinggi_badan);
                            modelLaporanBalita.setKet_tinggi_badan(ket_tinggi_badan);
                            modelLaporanBalita.setLingkar_kepala(lingkar_kepala);
                            modelLaporanBalita.setAlamat(alamat);
                            modelLaporanBalita.setKet_lingkar_kepala(ket_lingkar_kepala);
                            modelLaporanBalita.setJenis_imunisasi(jenis_imunisasi);
                            modelLaporanBalita.setCatatan(catatan);
                            modelLaporanBalita.setObat(obat);
                            modelLaporanBalita.setOrang_tua_kandung(orang_tua_kandung);
                            modelLaporanBalita.setTanggal_lahir(tanggal_lahir);
                            modelLaporanBalita.setJenis_kelamin(jenis_kelamin);

                            arrayListModelLaporanBalita.add(modelLaporanBalita);
                        }
                        modelLaporanBalita.addAll(arrayListModelLaporanBalita);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(LaporanBalita.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(LaporanBalita.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(LaporanBalita.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                modelLaporanBalita.clear();
                voidData(1);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}