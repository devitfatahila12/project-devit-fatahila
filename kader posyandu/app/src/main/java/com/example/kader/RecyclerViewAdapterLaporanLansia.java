package com.example.kader;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterLaporanLansia extends RecyclerView.Adapter<RecyclerViewAdapterLaporanLansia.ViewHolder>{

    public View vi;
    private ArrayList<ModelLaporanLansia> modelLaporanLansia;
    Context context;
    Activity ac;

    public interface OnItemClickListener {
        void onItemClick(ModelLaporanLansia item);
    }

    private final RecyclerViewAdapterLaporanLansia.OnItemClickListener listener;

    public RecyclerViewAdapterLaporanLansia(Context context, ArrayList<ModelLaporanLansia> mod_laporan_lansia, RecyclerViewAdapterLaporanLansia.OnItemClickListener listener) {
        this.modelLaporanLansia = mod_laporan_lansia;
        this.context = context;
        this.ac = (Activity)context;
        this.listener = listener;
    }

    @Override
    public RecyclerViewAdapterLaporanLansia.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laporan_lansia, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterLaporanLansia.ViewHolder holder, final int position) {
        holder.bind(modelLaporanLansia.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return modelLaporanLansia.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama_lansia, tgl_periksa, jenis_kelamin, tanggal_lahir, nik, copy, alamat;
        CardView click_item;

        public ViewHolder(View itemView) {
            super(itemView);
            vi = itemView;
            click_item = (CardView)itemView.findViewById(R.id.click_item);
            nama_lansia = (TextView)itemView.findViewById(R.id.nama_lansia);
            jenis_kelamin = (TextView)itemView.findViewById(R.id.jenis_kelamin);
            tgl_periksa = (TextView)itemView.findViewById(R.id.tgl_periksa);
            tanggal_lahir = (TextView) itemView.findViewById(R.id.tanggal_lahir);
            alamat = (TextView) itemView.findViewById(R.id.alamat);
            nik = (TextView) itemView.findViewById(R.id.nik);
            copy = (TextView) itemView.findViewById(R.id.copy);

            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  copy to clipboard
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                    clipboardManager.setPrimaryClip(clipData);

                    //  Show Toast
                    Toast.makeText(context, "Nik berhasil disalin", Toast.LENGTH_SHORT).show();

                }
            });
        }

        public void bind(final ModelLaporanLansia item, final RecyclerViewAdapterLaporanLansia.OnItemClickListener listener)
        {
            nik.setText(item.getNik());
            nama_lansia.setText(item.getNama());
            jenis_kelamin.setText(item.getJenis_kelamin());
            tgl_periksa.setText(item.getTgl_periksa());
            alamat.setText(item.getAlamat());
            tanggal_lahir.setText(item.getTanggal_lahir());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { listener.onItemClick(item); }});

            click_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), DetailLaporanLansia.class);
                    Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("nama", item.getNama());
                    b.putString("nik", item.getNik());
                    b.putString("umur", item.getUmur());
                    b.putString("alamat", item.getAlamat());
                    b.putString("jenis_kelamin", item.getJenis_kelamin());
                    b.putString("tgl_periksa", item.getTgl_periksa());
                    b.putString("berat_badan", item.getBerat_badan());
                    b.putString("tinggi_badan", item.getTinggi_badan());
                    b.putString("tensi_darah", item.getTensi_darah());
                    b.putString("ket_tensi_darah", item.getKet_tensi_darah());
                    b.putString("asam_urat", item.getAsam_urat());
                    b.putString("ket_asam_urat", item.getKet_asam_urat());
                    b.putString("kolerstrol", item.getKolerstrol());
                    b.putString("ket_kolerstrol", item.getKet_kolerstrol());
                    b.putString("catatan", item.getCatatan());
                    b.putString("obat", item.getObat());
                    intent.putExtras(b);
                    ((Activity) context).startActivityForResult(intent,1);
                }
            });
        }
    }
}
