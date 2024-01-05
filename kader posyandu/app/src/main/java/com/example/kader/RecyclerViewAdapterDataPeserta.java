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

public class RecyclerViewAdapterDataPeserta extends RecyclerView.Adapter<RecyclerViewAdapterDataPeserta.ViewHolder>{

    public View vi;
    private ArrayList<ModelDataPeserta> modelDataPeserta;
    Context context;
    Activity ac;

    public interface OnItemClickListener {
        void onItemClick(ModelDataPeserta item);
    }

    private final RecyclerViewAdapterDataPeserta.OnItemClickListener listener;

    public RecyclerViewAdapterDataPeserta(Context context, ArrayList<ModelDataPeserta> mod_data_peserta, RecyclerViewAdapterDataPeserta.OnItemClickListener listener) {
        this.modelDataPeserta = mod_data_peserta;
        this.context = context;
        this.ac = (Activity)context;
        this.listener = listener;
    }

    @Override
    public RecyclerViewAdapterDataPeserta.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_peserta, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterDataPeserta.ViewHolder holder, final int position) {
        holder.bind(modelDataPeserta.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return modelDataPeserta.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nik_peserta, nama_peserta, jenis_peserta, tgl_lahir, alamat, copy;
        CardView click_item;

        public ViewHolder(View itemView) {
            super(itemView);
            vi = itemView;
            click_item = (CardView) itemView.findViewById(R.id.click_item);
            nik_peserta = (TextView)itemView.findViewById(R.id.nik_peserta);
            nama_peserta = (TextView)itemView.findViewById(R.id.nama_peserta);
            jenis_peserta = (TextView)itemView.findViewById(R.id.jenis_peserta);
            tgl_lahir = (TextView)itemView.findViewById(R.id.tgl_lahir);
            alamat = (TextView)itemView.findViewById(R.id.alamat);
            copy = (TextView)itemView.findViewById(R.id.copy);

            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  copy to clipboard
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Text", nik_peserta.getText().toString());
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(context, "Nik berhasil disalin", Toast.LENGTH_SHORT).show();
                    //  Show Toast


                }
            });
        }

        public void bind(final ModelDataPeserta item, final RecyclerViewAdapterDataPeserta.OnItemClickListener listener)
        {
            nik_peserta.setText(item.getNik());
            nama_peserta.setText(item.getNama());
            jenis_peserta.setText(item.getPeserta_posyandu());
            tgl_lahir.setText(item.getTanggal_lahir());
            alamat.setText(item.getAlamat());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { listener.onItemClick(item); }});
            click_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), DetailPeserta.class);
                    Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    intent.putExtras(b);
                    ((Activity) context).startActivityForResult(intent,1);
                }
            });
        }
    }
}
