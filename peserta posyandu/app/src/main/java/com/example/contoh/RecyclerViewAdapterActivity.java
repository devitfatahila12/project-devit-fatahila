package com.example.contoh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.apache.commons.lang3.StringUtils;

public class RecyclerViewAdapterActivity extends RecyclerView.Adapter<RecyclerViewAdapterActivity.ViewHolder>{

    public View vi;
    private ArrayList<modelActivity> modelActivity;
    Context context;
    Activity ac;

    public interface OnItemClickListener {
        void onItemClick(modelActivity item);
    }

    private final RecyclerViewAdapterActivity.OnItemClickListener listener;

    public RecyclerViewAdapterActivity(Context context, ArrayList<modelActivity> mod_activity, RecyclerViewAdapterActivity.OnItemClickListener listener) {
        this.modelActivity = mod_activity;
        this.context = context;
        this.ac = (Activity)context;
        this.listener = listener;
    }

    @Override
    public RecyclerViewAdapterActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aktivitas, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterActivity.ViewHolder holder, final int position) {
        holder.bind(modelActivity.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return modelActivity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_hasil, text_catatan, text_value_catatan, text_obat, text_value_obat, text_pengumuman, text_tgl, text_value_periksa;
        RatingBar rating;
        Button btn_lihat_details, btn_feedback;

        public ViewHolder(View itemView) {
            super(itemView);
            vi = itemView;
            title_hasil = (TextView) itemView.findViewById(R.id.title_hasil);
            text_catatan = (TextView) itemView.findViewById(R.id.text_catatan);
            text_value_catatan = (TextView) itemView.findViewById(R.id.text_value_catatan);
            text_tgl = (TextView) itemView.findViewById(R.id.text_tgl);
            text_value_periksa = (TextView) itemView.findViewById(R.id.text_value_periksa);
            text_obat = (TextView) itemView.findViewById(R.id.text_obat);
            text_value_obat = (TextView) itemView.findViewById(R.id.text_value_obat);
            text_pengumuman = (TextView) itemView.findViewById(R.id.text_pengumuman);
            btn_lihat_details = (Button) itemView.findViewById(R.id.btn_lihat_details);
//            rating = (RatingBar) itemView.findViewById(R.id.rating);
//            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                @Override
//                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                    if(rating <= 0){
//                        btn_feedback.setEnabled(false);
//                    }else{
//                        btn_feedback.setEnabled(true);
//                    }
//                }
//            });
//            btn_feedback = (Button) itemView.findViewById(R.id.feedback);
        }

        public void bind(final modelActivity item, final RecyclerViewAdapterActivity.OnItemClickListener listener) {
            text_catatan.setVisibility(View.GONE);
            text_value_catatan.setVisibility(View.GONE);
            text_obat.setVisibility(View.GONE);
            text_catatan.setVisibility(View.GONE);
            text_value_obat.setVisibility(View.GONE);
            text_pengumuman.setVisibility(View.GONE);
            text_tgl.setVisibility(View.GONE);
            text_value_periksa.setVisibility(View.GONE);
            btn_lihat_details.setVisibility(View.GONE);
//            rating.setVisibility(View.GONE);
//            btn_feedback.setVisibility(View.GONE);

            title_hasil.setText(item.getTitle());
            String group = item.getGroup();
            if(group.equals("notif_jadwal")){
                text_catatan.setVisibility(View.GONE);
                text_value_catatan.setVisibility(View.GONE);
                text_obat.setVisibility(View.GONE);
                text_catatan.setVisibility(View.GONE);
                text_tgl.setVisibility(View.GONE);
                text_value_periksa.setVisibility(View.GONE);
                text_value_obat.setVisibility(View.GONE);
                btn_lihat_details.setVisibility(View.GONE);
//                rating.setVisibility(View.GONE);
//                btn_feedback.setVisibility(View.GONE);
                text_pengumuman.setVisibility(View.VISIBLE);
                text_pengumuman.setText(item.getMessage());
            }else if(group.equals("notif_pemeriksaan")){
                try{
                    text_catatan.setVisibility(View.VISIBLE);
                    text_value_catatan.setVisibility(View.VISIBLE);
                    text_obat.setVisibility(View.VISIBLE);
                    text_catatan.setVisibility(View.VISIBLE);
                    text_tgl.setVisibility(View.VISIBLE);
                    text_value_periksa.setVisibility(View.VISIBLE);
                    text_value_obat.setVisibility(View.VISIBLE);
                    btn_lihat_details.setVisibility(View.VISIBLE);
//                    rating.setVisibility(View.VISIBLE);
//                    btn_feedback.setVisibility(View.VISIBLE);
//                    btn_feedback.setEnabled(false);
                    text_pengumuman.setVisibility(View.GONE);

                    JSONObject jsonobjectData = new JSONObject(item.getData());
                    String linkPage = item.getPage_link();
                    String link_id = jsonobjectData.getString("link_id");
                    String catatan = jsonobjectData.getString("catatan");
                    String obat = jsonobjectData.getString("obat");
                    String tgl_periksa = jsonobjectData.getString("tgl_periksa");

//                    String rating_svr = item.getRating();

//                    try{
//                        rating.setRating(Float.parseFloat(rating_svr));
//                    }catch (Exception e){
//                        rating_svr = "0";
//                        rating.setRating(Float.parseFloat(rating_svr));
//                    }

//                    if(Float.parseFloat(rating_svr) == 0){
//                        rating.setEnabled(true);
//                        btn_feedback.setVisibility(View.VISIBLE);
//                    }else{
//                        rating.setEnabled(false);
//                        btn_feedback.setVisibility(View.GONE);
//                    }

//                    btn_feedback.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            String rating_ = String.valueOf(rating.getRating());
//                            if(Float.parseFloat(rating_) == 0){
//                                Toast.makeText(context, "feedback masih kosong", Toast.LENGTH_SHORT).show();
//                            }else{
//                                doSimpanFeedback(link_id, rating_, linkPage, rating, btn_feedback);
//                            }
//                        }
//                    });

                    text_value_catatan.setText(catatan);
                    text_value_obat.setText(obat);
                    text_value_periksa.setText(tgl_periksa);

                    btn_lihat_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(linkPage.equals("laporan_balita")){
                                Intent intent = new Intent(context, DetailPemeriksaanBalita.class);
                                Bundle b = new Bundle();
                                b.putString("link_id", link_id);
                                intent.putExtras(b);
                                ((Activity) context).startActivity(intent);
                            }else if(linkPage.equals("laporan_lansia")){
                                Intent intent = new Intent(context, DetailPemeriksaanLansia.class);
                                Bundle b = new Bundle();
                                b.putString("link_id", link_id);
                                intent.putExtras(b);
                                ((Activity) context).startActivity(intent);
                            }else if(linkPage.equals("laporan_ibu_hamil")){
                                Intent intent = new Intent(context, DetailPemeriksaanIbuHamil.class);
                                Bundle b = new Bundle();
                                b.putString("link_id", link_id);
                                intent.putExtras(b);
                                ((Activity) context).startActivity(intent);
                            }
                        }
                    });
                }catch (Exception e){
                    System.out.println("error parsing json" + e.toString());
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { listener.onItemClick(item); }});
        }
    }

//    public void doSimpanFeedback(String id, String feedback, String linkPage, RatingBar rating, Button btnfeedback){
//        loading mLoading = new loading(context);
//        mLoading.show();
//        try{
//            JsonObject params = new JsonObject();
//            params.addProperty("id",id);
//            params.addProperty("type",linkPage);
//            params.addProperty("rating",feedback);
//            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
//            Call call = apiservice.saveFeedback(params);
//            call.enqueue(new Callback() {
//                @Override
//                public void onResponse(Call call, Response response) {
//                    mLoading.dismiss();
//                    if (response.code()==200){
//                        rating.setEnabled(false);
//                        btnfeedback.setVisibility(View.GONE);
//                        Toast.makeText(context, "feedback berhasil disimpan", Toast.LENGTH_SHORT).show();
//                    } else  {
//                        Toast.makeText(context, "feedback gagal disimpan", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call call, Throwable t) {
//                    mLoading.dismiss();
//                    Toast.makeText(context, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }catch (Exception e){
//            mLoading.dismiss();
//            System.out.println("system error on global: " +e.getMessage());
//            Toast.makeText(context, "feedback gagal disimpan" + e, Toast.LENGTH_SHORT).show();
//        }
//    }
}
