package com.example.kader;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApuClient {
    public static final String BASE_URL = "http://192.168.247.201:4055/api/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

//Code tersebut adalah implementasi dari klien (client) untuk mengakses API menggunakan Retrofit library dengan
// format data JSON yang dikonversi menggunakan GsonConverterFactory.
//Konstanta BASE_URL merupakan URL dasar dari API yang akan diakses oleh klien.
// Perhatikan bahwa URL ini mencantumkan skema http dan alamat IP 192.168.43.163,
// serta port 8099. Bagian /api/v1/ menunjukkan jalur atau endpoint dari API yang akan diakses.