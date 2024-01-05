package com.example.aplikasirumahadat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterGrid extends RecyclerView.Adapter<AdapterGrid.GridViewHolder> {

    private ArrayList<ModelPahlawan> dataPahlawan;

    public AdapterGrid(ArrayList<ModelPahlawan> dataPahlawan) {
        this.dataPahlawan = dataPahlawan;
    }

    public  interface onItemClickCallBack {
        void onItemClicked(ModelPahlawan data);

    }

    private onItemClickCallBack callBack;

    public void setOnItemClickCallBack(onItemClickCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_pahlawan, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        ModelPahlawan pahlawan = dataPahlawan.get(position);



        Glide
                .with(holder.itemView.getContext())
                .load(pahlawan.getFoto())
                .into(holder.ivGridPahlawan);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onItemClicked(dataPahlawan.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataPahlawan.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGridPahlawan;


        public GridViewHolder(View itemView) {
            super(itemView);

            ivGridPahlawan = itemView.findViewById(R.id.iv_grid_pahlawan);

        }
    }
}
