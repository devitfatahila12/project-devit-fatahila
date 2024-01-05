package com.example.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class MenuUtamaPeserta extends AppCompatActivity {
    CardView data_peserta,  laporan_balita,  laporan_lansia,
            laporan_hamil, chart_report,  kirim_notif, laporan_kunjungan;
    TextView nama_kader;
    Button logout;
    SessionManager ssmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama_peserta);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ssmm = new SessionManager(MenuUtamaPeserta.this);
        HashMap<String,String> user = ssmm.getUserDetails();

        nama_kader = (TextView) findViewById(R.id.nama_kader);
        nama_kader.setText(user.get(SessionManager.key_nama));

        data_peserta = (CardView) findViewById(R.id.data_peserta);
        data_peserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUtamaPeserta.this, DataPeserta.class));
            }
        });




        laporan_balita = (CardView) findViewById(R.id.laporan_balita);
        laporan_balita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuUtamaPeserta.this, LaporanBalita.class));
            }
        });



        laporan_lansia = (CardView) findViewById(R.id.laporan_lansia);
        laporan_lansia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUtamaPeserta.this, LaporanLansia.class));
            }
        });

        chart_report = (CardView) findViewById(R.id.chart_report);
        chart_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUtamaPeserta.this, ChartReport.class));
            }
        });

        logout = (Button) findViewById(R.id.logout_id);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOut();
            }
        });



        laporan_hamil = (CardView) findViewById(R.id.laporan_hamil);
        laporan_hamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUtamaPeserta.this, LaporanIbuHamil.class));
            }
        });

        kirim_notif = (CardView) findViewById(R.id.kirim_notif);
        kirim_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUtamaPeserta.this, notif.class));
            }
        });

        laporan_kunjungan = (CardView) findViewById(R.id.laporan_kunjungan);
        laporan_kunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUtamaPeserta.this, LaporanKunjungan.class));
            }
        });
    }




    public void showDialogOut() {
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