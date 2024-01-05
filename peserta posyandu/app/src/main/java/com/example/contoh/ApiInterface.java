package com.example.contoh;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("account/verify-password")
    Call<Object> verifyPassword(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/register")
    Call<Object> registerAkun(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/login")
    Call<Object> loginAkun(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/lupapassword")
    Call<Object> lupapassword(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/detail-peserta-byid")
    Call<Object> detailPesertaById(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/upload-kartu-keluarga")
    Call<Object> uploadKK(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/list-activity")
    Call<Object> listActivity(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/get-id-checkup-balita")
    Call<Object> getIDCheckupBalita(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/logout")
    Call<Object> logoutAkun(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/save-feedback")
    Call<Object> saveFeedback(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/define-grafik/ibu-hamil")
    Call<Object> defineGraphIbuHamil(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/define-grafik/lansia")
    Call<Object> defineGraphLansia(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/define-grafik/balita")
    Call<Object> defineGraphBalita(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/get-id-checkup-ibu-hamil")
    Call<Object> getIDCheckupIbuHamil(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/get-id-checkup-lansia")
    Call<Object> getIDCheckuplansia(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/update-profil")
    Call<Object> updateProfil(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/delete-profil")
    Call<Object> deleteProfil(@Body JsonObject body);

}