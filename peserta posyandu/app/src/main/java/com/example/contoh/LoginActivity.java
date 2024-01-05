package com.example.contoh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.ActivityInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText nik, password;
    Button btn_login, btn_lupa_password;
    SessionManager sessionManager;
    TextView copy,count, count1;
    loading mLoading;
    String playerID;
    private boolean passwordShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mLoading = new loading(LoginActivity.this);
        sessionManager = new SessionManager(getApplicationContext());

        nik = (EditText)findViewById(R.id.nik);
        password = (EditText)findViewById(R.id.password);
        copy = findViewById(R.id.copy);
        count = findViewById(R.id.count);
        count1 = findViewById(R.id.count1);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_lupa_password = (Button) findViewById(R.id.btn_lupa_password);

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
        btn_login.setEnabled(false);

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

        //max character
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

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (passwordShown) {
                            passwordShown = false;
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_border_color_24, 0, R.drawable.eye_open, 0);
                        } else {
                            passwordShown = true;
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_border_color_24, 0, R.drawable.eye_close, 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        int delay = 0; // delay for 0 sec.
        int period = 3000; // repeat every 3 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                setPlayerID(timer);
            }
        }, delay, period);
    }

    public void setPlayerID(Timer timer){
        OSDeviceState device = OneSignal.getDeviceState();
        playerID = device.getUserId();
        if(TextUtils.isEmpty(playerID)){
            playerID = "dev";
        }
        if(!playerID.equals("dev")){
            timer.cancel();
        }
        System.out.println("player id " + playerID);
    }

    public  void doLogin(){
        mLoading.show();

        try {
            JsonObject params = new JsonObject();
            params.addProperty("nik",String.valueOf(nik.getText()));
            params.addProperty("password",String.valueOf(password.getText()));
            params.addProperty("device_id",playerID);
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
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
                            String peserta = jsonobjectData.getString("peserta_posyandu");

                            sessionManager.createSession(id, nama, peserta);
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
            Toast.makeText(LoginActivity.this, "Login gagal" + e, Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}