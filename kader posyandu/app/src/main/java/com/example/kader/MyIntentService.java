package com.example.kader;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;

public class MyIntentService extends IntentService {

    public static final String SERVICE_CODE = "SERVICE_CODE";
    public static final String SERVICE_DATA = "SERVICE_DATA";
    public static final String SERVICE_ERR_DATA = "SERVICE_ERR_DATA";
    public static final String SERVICE_MESSAGE = "SERVICE_MESSAGE";

    public MyIntentService() { super("MyIntentService"); }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("action");
        if(action.equals("login")){
            String nik = intent.getStringExtra("nik");
            String password = intent.getStringExtra("password");
            doLogin(nik, password);
        }
    }

    public void doLogin(String nik, String password){
        try{
            JsonObject params = new JsonObject();
            params.addProperty("nik",nik);
            params.addProperty("password",password);
            Apiinterface apiService = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiService.loginAkun(params);
            Response response = call.execute();
            sendDataToUIR(response);
        }catch (Exception e){
            System.out.println("error call api" + e.toString());
            sendDataToUIE("A500");
        }
    }

    public void sendDataToUIR(Response response){
        Intent intent = new Intent(SERVICE_MESSAGE);
        intent.putExtra(SERVICE_CODE, String.valueOf(response.code()));
        intent.putExtra(SERVICE_DATA, String.valueOf(response.body()));
        try{
            intent.putExtra(SERVICE_ERR_DATA, String.valueOf(response.errorBody().string()));
        }catch (Exception e){
            intent.putExtra(SERVICE_ERR_DATA, "");
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void sendDataToUIE(String data){
        Intent intent = new Intent(SERVICE_MESSAGE);
        intent.putExtra(SERVICE_CODE, data);
        intent.putExtra(SERVICE_DATA, "");
        intent.putExtra(SERVICE_ERR_DATA, "");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}