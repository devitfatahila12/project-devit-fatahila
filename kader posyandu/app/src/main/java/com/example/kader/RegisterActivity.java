package com.example.kader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.renderer.LineScatterCandleRadarRenderer;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener {
//    deklarasikan variabelnya
    EditText nama, nik, alamat, tgl, password, verified_password;
    ProgressDialog progDailog;
    Spinner jenis, peserta;
    Button btn_date, btn_register;
    ImageView imgGallery;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private final int GALLERY_REQ_CODE = 1000;
    String base64_image;
    boolean passwordVisible1, passwordVisible2;
    TextView copy;
    TextView count;
    TextView count1, count2;
    //COPY
    TextView error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//      untuk manggil id nya supaya id nya bisa dikenali dan baru bisa di program
        //COPY
        error_message = (TextView)findViewById(R.id.error_message);
        //COPY
        nama = (EditText)findViewById(R.id.nama);
        nama.addTextChangedListener(watcher);
        nik = (EditText)findViewById(R.id.nik);
//        addtextchangedlistener fungsinya untuk mengamati perubahan yang terjadi pada inputan edittext
//        menggunakan metode text watcher
        nik.addTextChangedListener(watcher);
        alamat = (EditText)findViewById(R.id.alamat);
        alamat.addTextChangedListener(watcher);
        tgl = (EditText)findViewById(R.id.tgl);
        tgl.setFocusable(false);
        tgl.setEnabled(false);
        tgl.setCursorVisible(false);
        tgl.addTextChangedListener(watcher);

        //COPY
        password = (EditText)findViewById(R.id.password);
        password.addTextChangedListener(watcher);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String pass1 = password.getText().toString();
                    String pass2 = verified_password.getText().toString();
                    if(!pass1.equals(pass2)){
                        error_message.setText("password dan verified password tidak sama");
                    }else{
                        error_message.setText("");
                    }
                }
            }
        });
        //COPY

        //COPY
        verified_password = (EditText)findViewById(R.id.verified_password);
        verified_password.addTextChangedListener(watcher);
        verified_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String pass1 = password.getText().toString();
                    String pass2 = verified_password.getText().toString();
                    if(!pass1.equals(pass2)){
                        error_message.setText("password dan verified password tidak sama");
                    }else{
                        error_message.setText("");
                    }
                }
            }
        });
        //COPY
        copy = findViewById(R.id.copy);
        count = findViewById(R.id.count);
        count1 = findViewById(R.id.count1);
        count2 = findViewById(R.id.count2);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setEnabled(false);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });

        btn_date = (Button) findViewById(R.id.btn_date);
        btn_date.setOnClickListener(this);


        verified_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String m=verified_password.getText().toString();
                int no=m.length();
                count2.setText(""+(int)no);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        max character
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

        //COPY
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right =2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=password.getSelectionEnd();
                        if (passwordVisible1){
                            // set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_format_list_numbered_24, 0,R.drawable.ic_baseline_visibility, 0);
                            // for hide password
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible1 = false;
                        }else {
                            //  set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_format_list_numbered_24, 0,R.drawable.ic_baseline_visibility_24, 0);
                            // for show password
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible1 = true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        verified_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right =2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=verified_password.getRight()-verified_password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=verified_password.getSelectionEnd();
                        if (passwordVisible2){
                            // set drawable image here
                            verified_password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_format_list_numbered_24, 0,R.drawable.ic_baseline_visibility, 0);
                            // for hide password
                            verified_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible2 = false;
                        }else {
                            //  set drawable image here
                            verified_password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_format_list_numbered_24, 0,R.drawable.ic_baseline_visibility_24, 0);
                            // for show password
                            verified_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible2 = true;
                        }
                        verified_password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        //COPY

        //  copy clipboard
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Text", nik.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                //  Show Toast
                Toast.makeText(RegisterActivity.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });
    }

//  codingan untuk menambahkan fungsi dari text watcher
    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}
        @Override
        public void afterTextChanged(Editable s) {
            //COPY
            doCheckButtonSimpan();
            //COPY
        }
    };

    //COPY
//    untuk mengetahui apabila panjang textnya sama dengan 0 atau ada salah satu inputan tidak diisi atau
//    bernilai kosong maka btn register bernilai false atau tidak bisa melakukan registrasi
    public void doCheckButtonSimpan(){
        if (nama.getText().toString().length() == 0 ||
                nik.getText().toString().length() == 0 ||
                alamat.getText().toString().length() == 0 ||
                tgl.getText().toString().length() == 0 ||
                password.getText().toString().length() == 0 ||
                verified_password.getText().length() == 0) {
            btn_register.setEnabled(false);
        } else {
            String pass1 = password.getText().toString();
            String pass2 = verified_password.getText().toString();
            if(!pass1.equals(pass2)){
                btn_register.setEnabled(false);
            }else{
                btn_register.setEnabled(true);
            }
        }
    }
    //COPY
// codingan untuk memanggil API yang ada di file Vs Code untuk melakukan registrasi
    public  void doRegister(){
        progDailog = new ProgressDialog(RegisterActivity.this);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();
        //fungsi params adalah untuk menyimpan data seperti nik, nama, alamat, tgl lahir dan password
        //atau juga wadah untuk menampung data yang kemudian akan dikirimkan dalam metode POST
        try {
            JsonObject params = new JsonObject();
            params.addProperty("nama",String.valueOf(nama.getText()));
            params.addProperty("nik",String.valueOf(nik.getText()));

            params.addProperty("alamat",String.valueOf(alamat.getText()));
            params.addProperty("tanggal_lahir",String.valueOf(tgl.getText()));
            params.addProperty("password",String.valueOf(password.getText()));
            Apiinterface apiservice = ApuClient.getClient().create(Apiinterface.class);
            Call call = apiservice.registerAkun(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    progDailog.dismiss();
                    try{
                        if (response.code()==200){
                            Toast.makeText(RegisterActivity.this, "Register berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity (intent);
                            finish();
                        } else  {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String errCOde = jsonObject.getString("err_code");
                            if (errCOde.equals("10003")) {
                                Toast.makeText(RegisterActivity.this, "Register Gagal - NIK telah terdaftar", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Register Gagal - General Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(RegisterActivity.this, "Register Gagal" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    progDailog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            progDailog.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(RegisterActivity.this, "Register gagal" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_date) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            tgl.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }
}