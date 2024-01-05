package com.example.kader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class pin extends Dialog {
    public Context context;
    public String context_string;
    ImageView back_button, imageview_circle1, imageview_circle2, imageview_circle3, imageview_circle4, imageview_circle5, imageview_circle6;
    Button pin_code_button_1, pin_code_button_2, pin_code_button_3, pin_code_button_4, pin_code_button_5, pin_code_button_6,
            pin_code_button_7, pin_code_button_8, pin_code_button_9, pin_code_button_0, pin_code_button_d;
    int initfir = 1;
    TextView tvEnterCode1;
    ArrayList objArray = new ArrayList();
    OnMyDialogResult mDialogResult;

    public pin(Context context, String context_string) {
        super(context);
        this.context = context;
        this.context_string = context_string;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pin_dot);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = com.google.android.material.R.style.Animation_Material3_BottomSheetDialog;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setTitle(null);
        setCancelable(false);

        tvEnterCode1 = (TextView) findViewById(R.id.tvEnterCode1);
        if(context_string.equals("input_pin")){
            tvEnterCode1.setText("MASUKKAN PIN");
        }
        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        pin_code_button_1 = (Button) findViewById(R.id.pin_code_button_1);
        pin_code_button_2 = (Button) findViewById(R.id.pin_code_button_2);
        pin_code_button_3 = (Button) findViewById(R.id.pin_code_button_3);
        pin_code_button_4 = (Button) findViewById(R.id.pin_code_button_4);
        pin_code_button_5 = (Button) findViewById(R.id.pin_code_button_5);
        pin_code_button_6 = (Button) findViewById(R.id.pin_code_button_6);
        pin_code_button_7 = (Button) findViewById(R.id.pin_code_button_7);
        pin_code_button_8 = (Button) findViewById(R.id.pin_code_button_8);
        pin_code_button_9 = (Button) findViewById(R.id.pin_code_button_9);
        pin_code_button_0 = (Button) findViewById(R.id.pin_code_button_0);
        pin_code_button_d = (Button) findViewById(R.id.pin_code_button_d);

        imageview_circle1 = (ImageView) findViewById(R.id.imageview_circle1);
        imageview_circle2 = (ImageView) findViewById(R.id.imageview_circle2);
        imageview_circle3 = (ImageView) findViewById(R.id.imageview_circle3);
        imageview_circle4 = (ImageView) findViewById(R.id.imageview_circle4);
        imageview_circle5 = (ImageView) findViewById(R.id.imageview_circle5);
        imageview_circle6 = (ImageView) findViewById(R.id.imageview_circle6);

        pin_code_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(1, "add");
            }
        });
        pin_code_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(2, "add");
            }
        });
        pin_code_button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(3, "add");
            }
        });
        pin_code_button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(4, "add");
            }
        });
        pin_code_button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(5, "add");
            }
        });
        pin_code_button_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(6, "add");
            }
        });
        pin_code_button_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(7, "add");
            }
        });
        pin_code_button_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(8, "add");
            }
        });
        pin_code_button_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(9, "add");
            }
        });
        pin_code_button_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(0, "add");
            }
        });
        pin_code_button_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculate(-1,"remove");
            }
        });
    }

    public void onCalculate(int number, String type){
        if(initfir > 6){
            initfir = 6;
        }else if(initfir <= 0){
            initfir = 1;
        }
        if(number < 0){
            initfir = initfir - 1;
        }

        if(initfir == 1){
            if(type == "add"){
                imageview_circle1.setColorFilter(context.getResources().getColor(R.color.bgBottomNavigation));
                objArray.add(initfir-1, String.valueOf(number));
            }else{
                imageview_circle1.setColorFilter(null);
                objArray.remove(initfir-1);
            }
        }else if(initfir == 2){
            if(type == "add"){
                imageview_circle2.setColorFilter(context.getResources().getColor(R.color.bgBottomNavigation));
                objArray.add(initfir-1, String.valueOf(number));
            }else{
                imageview_circle2.setColorFilter(null);
                objArray.remove(initfir-1);
            }
        }
        else if(initfir == 3){
            if(type == "add"){
                imageview_circle3.setColorFilter(context.getResources().getColor(R.color.bgBottomNavigation));
                objArray.add(initfir-1, String.valueOf(number));
            }else{
                imageview_circle3.setColorFilter(null);
                objArray.remove(initfir-1);
            }
        }
        else if(initfir == 4){
            if(type == "add"){
                imageview_circle4.setColorFilter(context.getResources().getColor(R.color.bgBottomNavigation));
                objArray.add(initfir-1, String.valueOf(number));
            }else{
                imageview_circle4.setColorFilter(null);
                objArray.remove(initfir-1);
            }
        }
        else if(initfir == 5){
            if(type == "add"){
                imageview_circle5.setColorFilter(context.getResources().getColor(R.color.bgBottomNavigation));
                objArray.add(initfir-1, String.valueOf(number));
            }else{
                imageview_circle5.setColorFilter(null);
                objArray.remove(initfir-1);
            }
        }
        else if(initfir == 6){
            if(type == "add"){
                imageview_circle6.setColorFilter(context.getResources().getColor(R.color.bgBottomNavigation));
                objArray.add(initfir-1, String.valueOf(number));
                onFinishInput();
            }else{
                imageview_circle6.setColorFilter(null);
                objArray.remove(initfir-1);
            }
        }

        if(number >= 0){
            initfir = initfir + 1;
        }
    }

    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(String result);
    }

    public void onFinishInput(){
        StringBuilder sbf = new StringBuilder();
        for (int i=0; i<objArray.size()-1; i++) { sbf.append(objArray.get(i)); }
        sbf.append(objArray.get(objArray.size()-1));
        System.out.println("input akhir: " + String.valueOf(sbf));
        mDialogResult.finish(String.valueOf(sbf));
        dismiss();
    }
}