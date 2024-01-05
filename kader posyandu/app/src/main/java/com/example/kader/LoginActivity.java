package com.example.kader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String serviceCode = intent.getStringExtra(MyIntentService.SERVICE_CODE);
            String serviceData = intent.getStringExtra(MyIntentService.SERVICE_DATA);
            String serviceErrData = intent.getStringExtra(MyIntentService.SERVICE_ERR_DATA);
            OutData(serviceCode, serviceData, serviceErrData);
        }
    };

    EditText nik, password;
    Button btn_login, btn_lupa_password;
    SessionManager sessionManager;
    loading mLoading;
    boolean passwordVisible;
    TextView copy;
    TextView count;
    TextView count1;

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(MyIntentService.SERVICE_MESSAGE));
    }

    @Override
    protected void onStop(){
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoading = new loading(LoginActivity.this);
        sessionManager = new SessionManager(getApplicationContext());

        nik = (EditText)findViewById(R.id.nik);
        password = (EditText)findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_lupa_password = (Button) findViewById(R.id.btn_lupa_password);
        copy = findViewById(R.id.copy);
        count = findViewById(R.id.count);
        count1 = findViewById(R.id.count1);

        btn_login.setEnabled(false);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        btn_lupa_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this,LupaPassword.class));
            }
        });

        nik.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);

//        max character text
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

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(LoginActivity.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

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
                btn_login.setEnabled(false);
            } else {
                btn_login.setEnabled(true);
            }
        }
    };

    public void doLoginNew(){
        mLoading.show();
        try{
            Intent intent = new Intent(LoginActivity.this, MyIntentService.class);
            intent.putExtra("action", "login");
            intent.putExtra("nik", String.valueOf(nik.getText()));
            intent.putExtra("password", String.valueOf(password.getText()));
            startService(intent);
        }catch (Exception e){
            mLoading.dismiss();
            Toast.makeText(LoginActivity.this, "Internal server error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public  void doLogin(){
        mLoading.show();

        try {
            JsonObject params = new JsonObject();
            params.addProperty("nik",String.valueOf(nik.getText()));
            params.addProperty("password",String.valueOf(password.getText()));
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.loginAkun(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    mLoading.dismiss();
                    try{
                        if (response.code()==200){
                            GsonBuilder builder = new GsonBuilder();
                            builder.serializeNulls();
                            Gson gson = builder.setPrettyPrinting().create();
                            JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                            JSONObject jsonobjectData = new JSONObject(jsonObject.getString("data"));
                            String id =  jsonobjectData.getString("id");
                            String nama = jsonobjectData.getString("nama");

                            sessionManager.createSession(id, nama);
                            startActivity(new Intent(LoginActivity.this,MenuUtamaPeserta.class));
                            Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        } else  {
                            Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        System.out.println("system error on parsing json: " +e.toString());
                        Toast.makeText(LoginActivity.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(LoginActivity.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(LoginActivity.this, "Login gagal" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void OutData(String serviceCode, String serviceData, String serviceErrData){
        mLoading.dismiss();
        try{
            if (serviceCode.equals("200")){
                GsonBuilder builder = new GsonBuilder();
                builder.serializeNulls();
                Gson gson = builder.setPrettyPrinting().create();
                JSONObject jsonObject = new JSONObject(gson.toJson(String.valueOf(serviceData)));
                Toast.makeText(LoginActivity.this, "jsonObject " + jsonObject, Toast.LENGTH_SHORT).show();
//                JSONObject jsonobjectData = new JSONObject(jsonObject.getString("data"));
//                String id =  jsonobjectData.getString("id");
//                String nama = jsonobjectData.getString("nama");
//
//                sessionManager.createSession(id, nama);
//                startActivity(new Intent(LoginActivity.this,MenuUtamaPeserta.class));
//                Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
//                finish();
            } else  {
                Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            System.out.println("system error on parsing json: " +e.toString());
            Toast.makeText(LoginActivity.this, "Terjadi kesalahan saat pemrosesan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}