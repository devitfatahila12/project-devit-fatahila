package com.example.kader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LupaPassword extends AppCompatActivity {
    EditText nik, password;
    Button btn_simpan;
    loading mLoading;
    boolean passwordVisible;
    TextView copy;
    TextView count;
    TextView count1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoading = new loading(LupaPassword.this);

        nik = (EditText)findViewById(R.id.nik);
        password = (EditText)findViewById(R.id.password);
        copy = findViewById(R.id.copy);
        count = findViewById(R.id.count);
        count1 = findViewById(R.id.count1);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        btn_simpan.setEnabled(false);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSimpan();
            }
        });

        nik.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);

//        max character
        nik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s=nik.getText().toString();
                int num=s.length();
                count.setText(""+(int)num);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//       max character
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String m=password.getText().toString();
                int no=m.length();
                count1.setText(""+(int)no);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //   copy the clipboard
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(LupaPassword.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right =2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=password.getSelectionEnd();
                        if (passwordVisible){
                            // set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_visibility, 0);
                            // for hide password
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }else {
                            //  set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_visibility_24, 0);
                            // for show password
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}
        @Override
        public void afterTextChanged(Editable s) {
            if (nik.getText().toString().length() == 0 || password.getText().toString().length() == 0) {
                btn_simpan.setEnabled(false);
            } else {
                btn_simpan.setEnabled(true);
            }
        }
    };

    public  void doSimpan(){
        mLoading.show();

        try {
            JsonObject params = new JsonObject();
            params.addProperty("nik",String.valueOf(nik.getText()));
            params.addProperty("password",String.valueOf(password.getText()));
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.lupapassword(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    mLoading.dismiss();
                    if (response.code()==200){
                        Toast.makeText(LupaPassword.this, "Ubah Password Berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    } else  {
                        Toast.makeText(LupaPassword.this, " Ubah Password Gagal", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(LupaPassword.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(LupaPassword.this, "Login gagal" + e, Toast.LENGTH_SHORT).show();
        }
    }
}
