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

public class RecyclerViewAdapterLaporanIbuHamil extends RecyclerView.Adapter<RecyclerViewAdapterLaporanIbuHamil.ViewHolder>{

    public View vi;
    private ArrayList<ModelLaporanIbuHamil> modelLaporanIbuHamil;
    Context context;
    Activity ac;

    public interface OnItemClickListener {
        void onItemClick(ModelLaporanIbuHamil item);
    }

    private final RecyclerViewAdapterLaporanIbuHamil.OnItemClickListener listener;

    public RecyclerViewAdapterLaporanIbuHamil(Context context, ArrayList<ModelLaporanIbuHamil> mod_laporan_ibu_hamil, RecyclerViewAdapterLaporanIbuHamil.OnItemClickListener listener) {
        this.modelLaporanIbuHamil = mod_laporan_ibu_hamil;
        this.context = context;
        this.ac = (Activity)context;
        this.listener = listener;
    }

    @Override
    public RecyclerViewAdapterLaporanIbuHamil.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laporan_ibu_hamil, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterLaporanIbuHamil.ViewHolder holder, final int position) {
        holder.bind(modelLaporanIbuHamil.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return modelLaporanIbuHamil.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, tanggal_lahir, tgl_periksa, nik, copy, alamat;
        CardView click_item;

        public ViewHolder(View itemView) {
            super(itemView);
            vi = itemView;
            click_item = (CardView) itemView.findViewById(R.id.click_item);
            nama = (TextView) itemView.findViewById(R.id.nama);

            tanggal_lahir = (TextView) itemView.findViewById(R.id.tanggal_lahir);
            tgl_periksa = (TextView) itemView.findViewById(R.id.tgl_periksa);
            nik = (TextView) itemView.findViewById(R.id.nik);
            alamat = (TextView) itemView.findViewById(R.id.alamat);
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

        public void bind(final ModelLaporanIbuHamil item, final RecyclerViewAdapterLaporanIbuHamil.OnItemClickListener listener)
        {
            nik.setText(item.getNik());

            nama.setText(item.getNama());

            alamat.setText(item.getAlamat());
            tanggal_lahir.setText(item.getTanggal_lahir());
            tgl_periksa.setText(item.getTanggal_periksa());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { listener.onItemClick(item); }});
            click_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), DetailLaporanIbuHamil.class);
                    Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("nama", item.getNama());
                    b.putString("nik", item.getNik());
                    b.putString("alamat", item.getAlamat());

                    b.putString("usia_hamil", item.getUsia_hamil());
                    b.putString("tanggal_periksa", item.getTanggal_periksa());
                    b.putString("berat_badan", item.getBerat_badan());
                    b.putString("tensi_darah", item.getTensi_darah());
                    b.putString("ket_tensi_darah", item.getKet_tensi_darah());
                    b.putString("lingkar_lengan_atas", item.getLingkar_lengan_atas());
                    b.putString("ket_lingkar_lengan_atas", item.getKet_lingkar_lengan_atas());
                    b.putString("denyut_jantung_bayi", item.getDenyut_jantung_bayi());
                    b.putString("ket_denyut_jantung_bayi", item.getKet_denyut_jantung_bayi());
                    b.putString("catatan", item.getCatatan());
                    b.putString("obat", item.getObat());
                    b.putString("tinggi_fundus", item.getTinggi_fundus());
                    b.putString("ket_tinggi_fundus", item.getKet_tinggi_fundus());
                    b.putString("tanggal_lahir", item.getTanggal_lahir());

                    intent.putExtras(b);
                    ((Activity) context).startActivityForResult(intent,1);
                }
            });
        }
    }

}
