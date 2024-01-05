package com.example.kader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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

public class LaporanIbuHamil extends AppCompatActivity {

    public ArrayList<ModelLaporanIbuHamil> modelLaporanIbuHamil = new ArrayList<ModelLaporanIbuHamil>();
    RecyclerView recycler_laporan_hamil;
    public RecyclerViewAdapterLaporanIbuHamil adapter;
    loading mLoading;
    SearchView search;
    public int page = 1;
    public int totalPages;
    public boolean setSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_ibu_hamil);

        mLoading = new loading(LaporanIbuHamil.this);

        recycler_laporan_hamil = (RecyclerView) findViewById(R.id.recycler_laporan_hamil);
        recycler_laporan_hamil.setLayoutManager(new GridLayoutManager(LaporanIbuHamil.this,1));
        recycler_laporan_hamil.addItemDecoration(new SpacesItemDecoration(1));
        adapter = new RecyclerViewAdapterLaporanIbuHamil(LaporanIbuHamil.this, modelLaporanIbuHamil, new RecyclerViewAdapterLaporanIbuHamil.OnItemClickListener() {
            @Override
            public void onItemClick(ModelLaporanIbuHamil item) {

            }
        });
        recycler_laporan_hamil.setAdapter(adapter);
        recycler_laporan_hamil.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if(!setSearch){
                    if(page != totalPages){
                        LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                        int numItems = recycler_laporan_hamil.getAdapter().getItemCount();
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
                    modelLaporanIbuHamil.clear();
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
                        modelLaporanIbuHamil.clear();
                        voidData(1);
                    }
                }
            }
        });

        voidData(1);
    }

    public void doSearch(String query){
        modelLaporanIbuHamil.clear();
        mLoading.show();

        try{
            JsonObject params = new JsonObject();
            params.addProperty("search", query);
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.searchCheckupHamil(params);
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
                        ArrayList<ModelLaporanIbuHamil> arrayListModelLaporanHamil = new ArrayList<ModelLaporanIbuHamil>();

                        for (int i=0; i<listData.length(); i++) {
                            JSONObject c = listData.getJSONObject(i);
                            String id = c.getString("id");
                            String nama = c.getString("nama");
                            String nik = c.getString("nik");

                            String usia_hamil = c.getString("usia_hamil");
                            String tgl_periksa = c.getString("tanggal_periksa");
                            String alamat = c.getString("alamat");
                            String berat_badan = c.getString("berat_badan");
                            String tensi_darah = c.getString("tensi_darah");
                            String ket_tensi_darah = c.getString("ket_tensi_darah");
                            String lingkar_lengan_atas = c.getString("lingkar_lengan_atas");
                            String ket_lingkar_lengan_atas = c.getString("ket_lingkar_lengan_atas");
                            String denyut_jantung_bayi = c.getString("denyut_jantung_bayi");
                            String ket_denyut_jantung_bayi = c.getString("ket_denyut_jantung_bayi");
                            String tinggi_fundus = c.getString("tinggi_fundus");
                            String ket_tinggi_fundus = c.getString("ket_tinggi_fundus");
                            String catatan = c.getString("catatan");
                            String obat = c.getString("obat");
                            String tanggal_lahir = c.getString("tanggal_lahir");

                            ModelLaporanIbuHamil modLapIbuHamil = new ModelLaporanIbuHamil();
                            modLapIbuHamil.setId(id);
                            modLapIbuHamil.setNama(nama);
                            modLapIbuHamil.setNik(nik);

                            modLapIbuHamil.setUsia_hamil(usia_hamil);
                            modLapIbuHamil.setTanggal_periksa(tgl_periksa);
                            modLapIbuHamil.setAlamat(alamat);
                            modLapIbuHamil.setBerat_badan(berat_badan);
                            modLapIbuHamil.setTensi_darah(tensi_darah);
                            modLapIbuHamil.setKet_tensi_darah(ket_tensi_darah);
                            modLapIbuHamil.setLingkar_lengan_atas(lingkar_lengan_atas);
                            modLapIbuHamil.setKet_lingkar_lengan_atas(ket_lingkar_lengan_atas);
                            modLapIbuHamil.setDenyut_jantung_bayi(denyut_jantung_bayi);
                            modLapIbuHamil.setKet_denyut_jantung_bayi(ket_denyut_jantung_bayi);
                            modLapIbuHamil.setTinggi_fundus(tinggi_fundus);
                            modLapIbuHamil.setKet_tinggi_fundus(ket_tinggi_fundus);
                            modLapIbuHamil.setCatatan(catatan);
                            modLapIbuHamil.setObat(obat);
                            modLapIbuHamil.setTanggal_lahir(tanggal_lahir);

                            arrayListModelLaporanHamil.add(modLapIbuHamil);
                        }
                        modelLaporanIbuHamil.addAll(arrayListModelLaporanHamil);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(LaporanIbuHamil.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(LaporanIbuHamil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(LaporanIbuHamil.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void voidData(int page){
        mLoading.show();

        try {
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            JsonObject params = new JsonObject();
            params.addProperty("page",page);
            Call call = apiservice.listCheckupHamil(params);
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
                        ArrayList<ModelLaporanIbuHamil> arrayListModelLaporanHamil = new ArrayList<ModelLaporanIbuHamil>();

                        for (int i=0; i<listData.length(); i++) {
                            JSONObject c = listData.getJSONObject(i);
                            String id = c.getString("id");
                            String nama = c.getString("nama");
                            String nik = c.getString("nik");

                            String usia_hamil = c.getString("usia_hamil");
                            String tgl_periksa = c.getString("tanggal_periksa");
                            String berat_badan = c.getString("berat_badan");
                            String tensi_darah = c.getString("tensi_darah");
                            String ket_tensi_darah = c.getString("ket_tensi_darah");
                            String lingkar_lengan_atas = c.getString("lingkar_lengan_atas");
                            String ket_lingkar_lengan_atas = c.getString("ket_lingkar_lengan_atas");
                            String denyut_jantung_bayi = c.getString("denyut_jantung_bayi");
                            String ket_denyut_jantung_bayi = c.getString("ket_denyut_jantung_bayi");
                            String tinggi_fundus = c.getString("tinggi_fundus");
                            String ket_tinggi_fundus = c.getString("ket_tinggi_fundus");
                            String alamat = c.getString("alamat");
                            String catatan = c.getString("catatan");
                            String obat = c.getString("obat");
                            String tanggal_lahir = c.getString("tanggal_lahir");

                            ModelLaporanIbuHamil modLapIbuHamil = new ModelLaporanIbuHamil();
                            modLapIbuHamil.setId(id);
                            modLapIbuHamil.setNama(nama);
                            modLapIbuHamil.setNik(nik);

                            modLapIbuHamil.setUsia_hamil(usia_hamil);
                            modLapIbuHamil.setTanggal_periksa(tgl_periksa);
                            modLapIbuHamil.setAlamat(alamat);
                            modLapIbuHamil.setBerat_badan(berat_badan);
                            modLapIbuHamil.setTensi_darah(tensi_darah);
                            modLapIbuHamil.setKet_tensi_darah(ket_tensi_darah);
                            modLapIbuHamil.setLingkar_lengan_atas(lingkar_lengan_atas);
                            modLapIbuHamil.setKet_lingkar_lengan_atas(ket_lingkar_lengan_atas);
                            modLapIbuHamil.setDenyut_jantung_bayi(denyut_jantung_bayi);
                            modLapIbuHamil.setKet_denyut_jantung_bayi(ket_denyut_jantung_bayi);
                            modLapIbuHamil.setTinggi_fundus(tinggi_fundus);
                            modLapIbuHamil.setKet_tinggi_fundus(ket_tinggi_fundus);
                            modLapIbuHamil.setCatatan(catatan);
                            modLapIbuHamil.setObat(obat);
                            modLapIbuHamil.setTanggal_lahir(tanggal_lahir);

                            arrayListModelLaporanHamil.add(modLapIbuHamil);
                        }
                        modelLaporanIbuHamil.addAll(arrayListModelLaporanHamil);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(LaporanIbuHamil.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(LaporanIbuHamil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(LaporanIbuHamil.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                modelLaporanIbuHamil.clear();
                voidData(1);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}