package com.example.kader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanLansia extends AppCompatActivity {

    public ArrayList<ModelLaporanLansia> modelLaporanLansia = new ArrayList<ModelLaporanLansia>();
    RecyclerView recycler_laporan_lansia;
    public RecyclerViewAdapterLaporanLansia adapter;
    loading mLoading;
    SearchView search;
    public int page = 1;
    public int totalPages;
    public boolean setSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_lansia);

        mLoading = new loading(LaporanLansia.this);

        recycler_laporan_lansia = (RecyclerView) findViewById(R.id.recycler_laporan_lansia);
        recycler_laporan_lansia.setLayoutManager(new GridLayoutManager(LaporanLansia.this,1));
        recycler_laporan_lansia.addItemDecoration(new SpacesItemDecoration(1));
        adapter = new RecyclerViewAdapterLaporanLansia(LaporanLansia.this, modelLaporanLansia, new RecyclerViewAdapterLaporanLansia.OnItemClickListener() {
            @Override
            public void onItemClick(ModelLaporanLansia item) {

            }
        });
        recycler_laporan_lansia.setAdapter(adapter);
        recycler_laporan_lansia.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if(!setSearch){
                    if(page != totalPages){
                        LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                        int numItems = recycler_laporan_lansia.getAdapter().getItemCount();
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
                    modelLaporanLansia.clear();
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
                        modelLaporanLansia.clear();
                        voidData(1);
                    }
                }
            }
        });

        voidData(page);
    }

    public void doSearch(String query){
        modelLaporanLansia.clear();
        mLoading.show();

        try{
            JsonObject params = new JsonObject();
            params.addProperty("search", query);
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.searchCheckupLansia(params);
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
                        ArrayList<ModelLaporanLansia> arrayListModelLaporanLansia = new ArrayList<ModelLaporanLansia>();

                        for (int i=0; i<listData.length(); i++) {
                            JSONObject c = listData.getJSONObject(i);
                            String id = c.getString("id");
                            String nama = c.getString("nama");
                            String nik = c.getString("nik");
                            String umur = c.getString("umur");
                            String jenis_kelamin = c.getString("jenis_kelamin");
                            String tgl_periksa = c.getString("tgl_periksa");
                            String berat_badan = c.getString("berat_badan");
                            String tinggi_badan = c.getString("tinggi_badan");
                            String tensi_darah = c.getString("tensi_darah");
                            String ket_tensi_darah = c.getString("ket_tensi_darah");
                            String asam_urat = c.getString("asam_urat");
                            String ket_asam_urat = c.getString("ket_asam_urat");
                            String kolerstrol = c.getString("kolerstrol");
                            String ket_kolerstrol = c.getString("ket_kolerstrol");
                            String catatan = c.getString("catatan");
                            String obat = c.getString("obat");
                            String tanggal_lahir = c.getString("tanggal_lahir");
                            String alamat = c.getString("alamat");
                            ModelLaporanLansia modelLaporanLansia = new ModelLaporanLansia();
                            modelLaporanLansia.setId(id);
                            modelLaporanLansia.setNama(nama);
                            modelLaporanLansia.setNik(nik);
                            modelLaporanLansia.setUmur(umur);
                            modelLaporanLansia.setJenis_kelamin((jenis_kelamin));
                            modelLaporanLansia.setAlamat(alamat);
                            modelLaporanLansia.setTgl_periksa(tgl_periksa);
                            modelLaporanLansia.setBerat_badan(berat_badan);
                            modelLaporanLansia.setTinggi_badan(tinggi_badan);
                            modelLaporanLansia.setTensi_darah(tensi_darah);
                            modelLaporanLansia.setKet_tensi_darah(ket_tensi_darah);
                            modelLaporanLansia.setAsam_urat(asam_urat);
                            modelLaporanLansia.setKet_asam_urat(ket_asam_urat);
                            modelLaporanLansia.setKolerstrol(kolerstrol);
                            modelLaporanLansia.setKet_kolerstrol(ket_kolerstrol);
                            modelLaporanLansia.setCatatan(catatan);
                            modelLaporanLansia.setObat(obat);
                            modelLaporanLansia.setTanggal_lahir(tanggal_lahir);

                            arrayListModelLaporanLansia.add(modelLaporanLansia);
                        }
                        modelLaporanLansia.addAll(arrayListModelLaporanLansia);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(LaporanLansia.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(LaporanLansia.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(LaporanLansia.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void voidData(int page){
        mLoading.show();

        try {
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            JsonObject params = new JsonObject();
            params.addProperty("page",page);
            Call call = apiservice.listCheckupLansia(params);
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
                        ArrayList<ModelLaporanLansia> arrayListModelLaporanLansia = new ArrayList<ModelLaporanLansia>();

                        for (int i=0; i<listData.length(); i++) {
                            JSONObject c = listData.getJSONObject(i);
                            String id = c.getString("id");
                            String nama = c.getString("nama");
                            String nik = c.getString("nik");
                            String umur = c.getString("umur");
                            String jenis_kelamin = c.getString("jenis_kelamin");
                            String tgl_periksa = c.getString("tgl_periksa");
                            String berat_badan = c.getString("berat_badan");
                            String tinggi_badan = c.getString("tinggi_badan");
                            String alamat = c.getString("alamat");
                            String tensi_darah = c.getString("tensi_darah");
                            String ket_tensi_darah = c.getString("ket_tensi_darah");
                            String asam_urat = c.getString("asam_urat");
                            String ket_asam_urat = c.getString("ket_asam_urat");
                            String kolerstrol = c.getString("kolerstrol");
                            String ket_kolerstrol = c.getString("ket_kolerstrol");
                            String catatan = c.getString("catatan");
                            String obat = c.getString("obat");
                            String tanggal_lahir = c.getString("tanggal_lahir");

                            ModelLaporanLansia modelLaporanLansia = new ModelLaporanLansia();
                            modelLaporanLansia.setId(id);
                            modelLaporanLansia.setNama(nama);
                            modelLaporanLansia.setNik(nik);
                            modelLaporanLansia.setUmur(umur);
                            modelLaporanLansia.setAlamat(alamat);
                            modelLaporanLansia.setJenis_kelamin((jenis_kelamin));
                            modelLaporanLansia.setTgl_periksa(tgl_periksa);
                            modelLaporanLansia.setBerat_badan(berat_badan);
                            modelLaporanLansia.setTinggi_badan(tinggi_badan);
                            modelLaporanLansia.setTensi_darah(tensi_darah);
                            modelLaporanLansia.setKet_tensi_darah(ket_tensi_darah);
                            modelLaporanLansia.setAsam_urat(asam_urat);
                            modelLaporanLansia.setKet_asam_urat(ket_asam_urat);
                            modelLaporanLansia.setKolerstrol(kolerstrol);
                            modelLaporanLansia.setKet_kolerstrol(ket_kolerstrol);
                            modelLaporanLansia.setCatatan(catatan);
                            modelLaporanLansia.setObat(obat);
                            modelLaporanLansia.setTanggal_lahir(tanggal_lahir);

                            arrayListModelLaporanLansia.add(modelLaporanLansia);
                        }
                        modelLaporanLansia.addAll(arrayListModelLaporanLansia);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(LaporanLansia.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(LaporanLansia.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(LaporanLansia.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                modelLaporanLansia.clear();
                voidData(1);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}