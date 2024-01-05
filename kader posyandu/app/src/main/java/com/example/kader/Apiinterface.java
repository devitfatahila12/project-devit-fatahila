package com.example.kader;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Apiinterface {
    @Headers("Content-Type: application/json")
    @POST("account/register-kader")
    Call<Object> registerAkun(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/login-kader")
    Call<Object> loginAkun(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/lupapassword-kader")
    Call<Object> lupapassword(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/ambil-data-peserta")
    Call<Object> ambilDataPeserta(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/save-checkup-balita")
    Call<Object> saveCheckupBalita(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/list-checkup-balita")
    Call<Object> listCheckupBalita(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/update-checkup-balita")
    Call<Object> updateCheckupBalita(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/delete-checkup-balita")
    Call<Object> deleteCheckupBalita(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/search-checkup-balita")
    Call<Object> searchCheckupBalita(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/list-peserta")
    Call<Object> listPeserta(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/search-peserta")
    Call<Object> searchPeserta(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/detail-peserta-byid")
    Call<Object> detailPesertaById(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/save-checkup-lansia")
    Call<Object> saveCheckupLansia(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/list-checkup-lansia")
    Call<Object> listCheckupLansia(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/update-checkup-lansia")
    Call<Object> updateCheckupLansia(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/delete-checkup-lansia")
    Call<Object> deleteCheckupLansia(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/search-checkup-lansia")
    Call<Object> searchCheckupLansia(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/define-grafik/balita")
    Call<Object> defineGraphBalita(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/define-grafik/lansia")
    Call<Object> defineGraphLansia(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/save-checkup-hamil")
    Call<Object> saveCheckupHamil(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/list-checkup-hamil")
    Call<Object> listCheckupHamil(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/update-checkup-hamil")
    Call<Object> updateCheckupHamil(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/delete-checkup-hamil")
    Call<Object> deleteCheckupHamil(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/search-checkup-hamil")
    Call<Object> searchCheckupHamil(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/define-grafik/ibu-hamil")
    Call<Object> defineGraphIbuHamil(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/notifikasi")
    Call<Object> sendNotif(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/data-pengunjung")
    Call<Object> dataPengunjung(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("account/remove-account")
    Call<Object> removeAccount(@Body JsonObject body);
}


//codingan ini merupakan endpoint endpoint yang berupa request atau permintaan dari klien yang kemudian
//request tersebut akan diproses oleh server atau bagian backendnya
//fungsi API untuk menghubungkan antara front end dengan bagian backend

//PENJELASAN METODE POST UNTUK MENGHAPUS DAN MENGUPDATE
//Ya, metode POST dalam codingan tersebut bisa digunakan untuk menghapus data dari server. Meskipun metode HTTP POST sebagian besar digunakan untuk mengirimkan data baru ke server, namun implementasinya pada server dapat dirancang untuk melakukan operasi penghapusan (delete) data.
//
//Dalam banyak kasus, aplikasi dapat mengimplementasikan logika di sisi server yang memungkinkan metode POST untuk berfungsi sebagai mekanisme untuk menghapus data tertentu dari server. Pada implementasi server, endpoint "account/delete-checkup-lansia" akan menentukan tindakan apa yang akan dilakukan berdasarkan data yang diberikan dalam body permintaan (request) JSON.
//
//Untuk melakukan operasi penghapusan, server akan memproses data yang diterima dari body permintaan dan melakukan tindakan penghapusan yang sesuai. Pastikan untuk selalu berhati-hati dan mengamankan operasi penghapusan dengan menerapkan mekanisme otentikasi dan otorisasi yang tepat, agar hanya pengguna yang berwenang yang dapat melakukan aksi ini.
//
//Namun, perlu diingat bahwa konvensi yang lebih umum adalah menggunakan metode HTTP DELETE untuk operasi penghapusan. Biasanya, metode POST digunakan untuk operasi penyimpanan data baru atau pembaruan data yang sudah ada. Jika memungkinkan, lebih baik untuk menggunakan metode HTTP DELETE jika Anda ingin melakukan operasi penghapusan data.