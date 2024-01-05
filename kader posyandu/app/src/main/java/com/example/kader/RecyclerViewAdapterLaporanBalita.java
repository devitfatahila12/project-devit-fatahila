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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterLaporanBalita extends RecyclerView.Adapter<RecyclerViewAdapterLaporanBalita.ViewHolder>{

    public View vi;
    private ArrayList<ModelLaporanBalita> modelLaporanBalita;
    Context context;
    Activity ac;

    public interface OnItemClickListener {
        void onItemClick(ModelLaporanBalita item);
    }

    private final OnItemClickListener listener;

    public RecyclerViewAdapterLaporanBalita(Context context, ArrayList<ModelLaporanBalita> mod_laporan_balita, OnItemClickListener listener) {
        this.modelLaporanBalita = mod_laporan_balita;
        this.context = context;
        this.ac = (Activity)context;
        this.listener = listener;
    }

    @Override
    public RecyclerViewAdapterLaporanBalita.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laporan_balita, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bind(modelLaporanBalita.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return modelLaporanBalita.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, tgl_periksa, tanggal_lahir, nik, copy, alamat;
        CardView click_item;

        public ViewHolder(View itemView) {
            super(itemView);
            vi = itemView;
            click_item = (CardView) itemView.findViewById(R.id.click_item);
            nama = (TextView)itemView.findViewById(R.id.nama_balita);
            nik = (TextView)itemView.findViewById(R.id.nik);

            tgl_periksa = (TextView)itemView.findViewById(R.id.tgl_periksa);
            tanggal_lahir = (TextView) itemView.findViewById(R.id.tanggal_lahir);
            copy = (TextView) itemView.findViewById(R.id.copy);
            alamat = (TextView) itemView.findViewById(R.id.alamat);

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

        public void bind(final ModelLaporanBalita item, final OnItemClickListener listener)
        {
            nik.setText(item.getNik());
            alamat.setText(item.getAlamat());
            nama.setText(item.getNama());

            tgl_periksa.setText(item.getTgl_periksa());
            tanggal_lahir.setText(item.getTanggal_lahir());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { listener.onItemClick(item); }});
            click_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), DetailLaporan.class);
                    Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("nama", item.getNama());
                    b.putString("nik", item.getNik());
                    b.putString("umur", item.getUmur());
                    b.putString("alamat", item.getAlamat());
                    b.putString("tgl_periksa", item.getTgl_periksa());
                    b.putString("berat_badan", item.getBerat_badan());
                    b.putString("ket_berat_badan", item.getKet_berat_badan());
                    b.putString("tinggi_badan", item.getTinggi_badan());
                    b.putString("ket_tinggi_badan", item.getKet_tinggi_badan());
                    b.putString("lingkar_kepala", item.getLingkar_kepala());
                    b.putString("ket_lingkar_kepala", item.getKet_lingkar_kepala());
                    b.putString("jenis_imunisasi", item.getJenis_imunisasi());
                    b.putString("catatan", item.getCatatan());
                    b.putString("obat", item.getObat());
                    b.putString("orang_tua_kandung", item.getOrang_tua_kandung());

                    b.putString("jenis_kelamin", item.getJenis_kelamin());
                    intent.putExtras(b);
                    ((Activity) context).startActivityForResult(intent,1);
                }
            });
        }
    }
}
