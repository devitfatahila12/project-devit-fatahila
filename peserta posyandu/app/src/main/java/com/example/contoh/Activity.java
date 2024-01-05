package com.example.contoh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity extends AppCompatActivity {

    public ArrayList<modelActivity> modelActivity = new ArrayList<modelActivity>();
    RecyclerView recycler_activity;
    RecyclerViewAdapterActivity adapter;
    loading mLoading;
    public int page = 1;
    public int totalPages;
    SessionManager ssmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

        mLoading = new loading(Activity.this);
        ssmm = new SessionManager(Activity.this);
        HashMap<String,String> user = ssmm.getUserDetails();
        String id = user.get(SessionManager.key_id);

        if(TextUtils.isEmpty(id)){
            ssmm.checkLogin();
            finish();
        }

        recycler_activity = (RecyclerView) findViewById(R.id.recycler_activity);
        recycler_activity.setLayoutManager(new GridLayoutManager(Activity.this,1));
        recycler_activity.addItemDecoration(new SpacesItemDecoration(1));
        adapter = new RecyclerViewAdapterActivity(Activity.this, modelActivity, new RecyclerViewAdapterActivity.OnItemClickListener() {
            @Override
            public void onItemClick(modelActivity item) {

            }
        });
        recycler_activity.setAdapter(adapter);
        recycler_activity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if(page != totalPages){
                    LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                    int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                    int numItems = recycler_activity.getAdapter().getItemCount();
                    boolean bool = (pos >= numItems - 1);
                    if(bool){
                        page = page + 1;
                        voidData(id, page);
                    }
                }
            }
        });

        voidData(id, page);
    }

    public void voidData(String id, int page){
        mLoading.show();
        try{
            JsonObject params = new JsonObject();
            params.addProperty("account_id",id);
            params.addProperty("page",page);
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call call = apiservice.listActivity(params);
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
                        ArrayList<modelActivity> arrayListModelActivity = new ArrayList<modelActivity>();

                        for (int i=0; i<listData.length(); i++) {
                            JSONObject c = listData.getJSONObject(i);
                            String id = c.getString("id");
                            String account_id = c.getString("account_id");
                            String group = c.getString("group");
                            String title = c.getString("title");
                            String message = c.getString("message");
                            String page_link = c.getString("page_link");
                            String data = c.getString("data");
                            String created_dt = c.getString("created_dt");
//                            String rating = c.getString("rating");

                            modelActivity modActvy = new modelActivity();
                            modActvy.setId(id);
                            modActvy.setAccount_id(account_id);
                            modActvy.setGroup(group);
                            modActvy.setTitle(title);
                            modActvy.setMessage(message);
                            modActvy.setPage_link(page_link);
                            modActvy.setData(data);
                            modActvy.setCreated_dt(created_dt);
//                            modActvy.setRating(rating);

                            arrayListModelActivity.add(modActvy);
                        }
                        modelActivity.addAll(arrayListModelActivity);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(Activity.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(Activity.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(Activity.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}