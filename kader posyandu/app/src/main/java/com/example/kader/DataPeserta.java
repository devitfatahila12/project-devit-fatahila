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

public class DataPeserta extends AppCompatActivity {

    public ArrayList<ModelDataPeserta> modelDataPeserta = new ArrayList<ModelDataPeserta>();
    RecyclerView recycler_data_peserta;
    SearchView search;
    public RecyclerViewAdapterDataPeserta adapter;
    loading mLoading;
    public int page = 1;
    public int totalPages;
    public boolean setSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_peserta);

        mLoading = new loading(DataPeserta.this);

        recycler_data_peserta = (RecyclerView) findViewById(R.id.recycler_data_peserta);
        recycler_data_peserta.setLayoutManager(new GridLayoutManager(DataPeserta.this,1));
        recycler_data_peserta.addItemDecoration(new SpacesItemDecoration(1));
        adapter = new RecyclerViewAdapterDataPeserta(DataPeserta.this, modelDataPeserta, new RecyclerViewAdapterDataPeserta.OnItemClickListener() {
            @Override
            public void onItemClick(ModelDataPeserta item) {

            }
        });
        recycler_data_peserta.setAdapter(adapter);
        recycler_data_peserta.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if(!setSearch){
                    if(page != totalPages){
                        LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                        int numItems = recycler_data_peserta.getAdapter().getItemCount();
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
                    modelDataPeserta.clear();
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
                        modelDataPeserta.clear();
                        voidData(1);
                    }
                }
            }
        });

        voidData(page);
    }

    public void doSearch(String query){
        modelDataPeserta.clear();
        mLoading.show();
        try{
            JsonObject params = new JsonObject();
            params.addProperty("search", query);
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.searchPeserta(params);
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
                        ArrayList<ModelDataPeserta> arrayListModelDataPeserta = new ArrayList<ModelDataPeserta>();

                        for (int i=0; i<listData.length(); i++) {
                            JSONObject c = listData.getJSONObject(i);
                            String id = c.getString("id");
                            String nama = c.getString("nama");
                            String nik = c.getString("nik");

                            String alamat = c.getString("alamat");
                            String tanggal_lahir = c.getString("tanggal_lahir");
                            String jenis_kelamin = c.getString("jenis_kelamin");
                            String peserta_posyandu = c.getString("peserta_posyandu");
                            String password = c.getString("password");
                            String created_dt = c.getString("created_dt");

                            ModelDataPeserta modelDataPeserta = new ModelDataPeserta();
                            modelDataPeserta.setId(id);
                            modelDataPeserta.setNama(nama);
                            modelDataPeserta.setNik(nik);

                            modelDataPeserta.setAlamat(alamat);
                            modelDataPeserta.setTanggal_lahir(tanggal_lahir);
                            modelDataPeserta.setJenis_kelamin(jenis_kelamin);
                            modelDataPeserta.setPeserta_posyandu(peserta_posyandu);
                            modelDataPeserta.setPassword(password);
                            modelDataPeserta.setCreated_dt(created_dt);

                            arrayListModelDataPeserta.add(modelDataPeserta);
                        }
                        modelDataPeserta.addAll(arrayListModelDataPeserta);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(DataPeserta.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(DataPeserta.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(DataPeserta.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void voidData(int page){
        mLoading.show();
        try{
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            JsonObject params = new JsonObject();
            params.addProperty("page",page);
            Call call = apiservice.listPeserta(params);
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
                        ArrayList<ModelDataPeserta> arrayListModelDataPeserta = new ArrayList<ModelDataPeserta>();

                        for (int i=0; i<listData.length(); i++) {
                            JSONObject c = listData.getJSONObject(i);
                            String id = c.getString("id");
                            String nama = c.getString("nama");
                            String nik = c.getString("nik");

                            String alamat = c.getString("alamat");
                            String tanggal_lahir = c.getString("tanggal_lahir");
                            String jenis_kelamin = c.getString("jenis_kelamin");
                            String peserta_posyandu = c.getString("peserta_posyandu");
                            String password = c.getString("password");
                            String created_dt = c.getString("created_dt");

                            ModelDataPeserta modelDataPeserta = new ModelDataPeserta();
                            modelDataPeserta.setId(id);
                            modelDataPeserta.setNama(nama);
                            modelDataPeserta.setNik(nik);

                            modelDataPeserta.setAlamat(alamat);
                            modelDataPeserta.setTanggal_lahir(tanggal_lahir);
                            modelDataPeserta.setJenis_kelamin(jenis_kelamin);
                            modelDataPeserta.setPeserta_posyandu(peserta_posyandu);
                            modelDataPeserta.setPassword(password);
                            modelDataPeserta.setCreated_dt(created_dt);

                            arrayListModelDataPeserta.add(modelDataPeserta);
                        }
                        modelDataPeserta.addAll(arrayListModelDataPeserta);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(DataPeserta.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(DataPeserta.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(DataPeserta.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                modelDataPeserta.clear();
                voidData(1);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}