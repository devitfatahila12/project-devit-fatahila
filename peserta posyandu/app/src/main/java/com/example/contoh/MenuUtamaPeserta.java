package com.example.contoh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuUtamaPeserta extends AppCompatActivity {

    SessionManager ssmm;
    TextView nama_peserta;
    CardView logout, profile, activity, chart_report;
    loading mLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama_peserta);

         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoading = new loading(MenuUtamaPeserta.this);
        ssmm = new SessionManager(MenuUtamaPeserta.this);
        HashMap<String,String> user = ssmm.getUserDetails();
        String id = user.get(SessionManager.key_id);

        nama_peserta = (TextView) findViewById(R.id.nama_peserta);
        nama_peserta.setText(user.get(SessionManager.key_nama));

       logout = (CardView) findViewById(R.id.logout);
       logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               showDialogOut();
           }
       });

        profile = (CardView) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUtamaPeserta.this, Profile.class));
            }
        });

        activity = (CardView) findViewById(R.id.activity);
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUtamaPeserta.this, Activity.class));
            }
        });

        chart_report = (CardView) findViewById(R.id.chart_report);
        chart_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUtamaPeserta.this, ChartReport.class));
            }
        });
    }

    public void showDialogOut(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuUtamaPeserta.this);
        builder.setTitle("Konfirmasi Permintaan")
                .setMessage("Apakah Anda yakin untuk keluar ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ssmm.logout();
                        finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

 
}