package com.example.contoh;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SpinnerBottomList extends BottomSheetDialogFragment
        implements View.OnClickListener{

    public static String type;
    public static Context context;
    public static String[] a;
    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;

    public static SpinnerBottomList newInstance(Context contextc, String[] ac, String types) {
        type = types;
        context = contextc;
        a = ac;
        return new SpinnerBottomList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spinner_bottom_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
        String judul;
        if(type.equals("jk")){
            judul = "PILIH JENIS KELAMIN";
        }else if(type.equals("jps")){
            judul = "PILIH KATEGORI PESERTA";
        }else {
            judul = "TIDAK DITENTUKAN";
        }

        TextView tvTitle = new TextView(context);
        tvTitle.setText(judul);
        tvTitle.setTextSize(16);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setPadding(50, 50, 50, 50);
        tvTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvTitle.setLayoutParams(lpTitle);
        layout.addView(tvTitle);

        int length = a.length;
        for(int i=0; i<length; i++){
            TextView tv = new TextView(context);
            tv.setText(a[i]);
            tv.setTextSize(16);
            tv.setPadding(50, 50, 50, 50);
            tv.setId(i+1);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            layout.addView(tv);

            int temp = getResources().getIdentifier(String.valueOf(i+1), "id", getActivity().getPackageName());
            view.findViewById(temp).setOnClickListener(this);
        }
//        view.findViewById(R.id.textView).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        TextView selected = (TextView) view;
        mListener.onItemClick(selected.getText().toString(), type);
        dismiss();
    }

    public interface ItemClickListener {
        void onItemClick(String item, String type);
    }
}
