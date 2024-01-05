package com.example.contoh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_Profil extends AppCompatActivity implements View.OnClickListener, SpinnerBottomList.ItemClickListener{

    EditText nama, nik, alamat, tgl, jenis_kelamin_new_type, peserta_new_type, old_password, password, verified_password;
    private int mYear, mMonth, mDay, mHour, mMinute;
    TextView  count1, count2, count, count3, copy;
    TextView error_message;
    Button btn_simpan, btn_hapus, btn_date;
    loading mLoading;
    boolean passwordVisible1, passwordVisible2;
    private boolean passwordShown = false;
    SessionManager ssmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        ssmm = new SessionManager(Edit_Profil.this);
        mLoading = new loading(Edit_Profil.this);

        error_message = (TextView)findViewById(R.id.error_message);
        nama = (EditText) findViewById(R.id.nama);
        nik = (EditText) findViewById(R.id.nik);
        alamat = (EditText) findViewById(R.id.alamat);
        tgl = (EditText) findViewById(R.id.tgl);
        jenis_kelamin_new_type = (EditText) findViewById(R.id.jenis_kelamin_new_type);
        jenis_kelamin_new_type.setFocusable(false);
        jenis_kelamin_new_type.setInputType(InputType.TYPE_NULL);
        peserta_new_type = (EditText) findViewById(R.id.peserta_new_type);
        peserta_new_type.setFocusable(false);
        peserta_new_type.setInputType(InputType.TYPE_NULL);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_date.setOnClickListener(this);
        old_password = (EditText)findViewById(R.id.old_password);
        old_password.addTextChangedListener(watcher);
        password = (EditText)findViewById(R.id.password);
        password.addTextChangedListener(watcher);
        verified_password = (EditText)findViewById(R.id.verified_password);
        verified_password.addTextChangedListener(watcher);

      copy = (TextView) findViewById(R.id.copy);
      count = (TextView) findViewById(R.id.count);
        count1 = (TextView) findViewById(R.id.count1);
        count2 = (TextView) findViewById(R.id.count2);
        count3 = (TextView) findViewById(R.id.count3);


        Bundle b = getIntent().getExtras();
        String id_ = b.getString("id_temp");
        String nik_ = b.getString("nik_temp");
        String nama_ = b.getString("nama_temp");
        String jk_ = b.getString("jk_temp");
        String alamat_ = b.getString("alamat_temp");
        String tgl_lahir_ = b.getString("tgl_lahir_temp");
        String peserta_ = b.getString("kategori_peserta_temp");

        nama.setText(nama_);
        nama.addTextChangedListener(watcher);

        nik.setText(nik_);
        nik.addTextChangedListener(watcher);

        tgl.setText(tgl_lahir_);
        tgl.addTextChangedListener(watcher);

        alamat.setText(alamat_);
        alamat.addTextChangedListener(watcher);

        jenis_kelamin_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "jk");
            }
        });
        jenis_kelamin_new_type.setText(jk_);
        jenis_kelamin_new_type.addTextChangedListener(watcher);

        peserta_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "ps");
            }
        });
        peserta_new_type.setText(peserta_);
        peserta_new_type.addTextChangedListener(watcher);

        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        btn_simpan.setEnabled(false);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPassword(id_);
            }
        });

        btn_hapus = (Button) findViewById(R.id.btn_hapus);
        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHapus(id_);
            }
        });

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

        old_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right =2;
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=old_password.getRight()-old_password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=old_password.getSelectionEnd();
                        if (passwordVisible2){
                            // set drawable image here
                            old_password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_format_list_numbered_24, 0,R.drawable.ic_baseline_visibility, 0);
                            // for hide password
                            old_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible2 = false;
                        }else {
                            //  set drawable image here
                            old_password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_format_list_numbered_24, 0,R.drawable.ic_baseline_visibility_24, 0);
                            // for show password
                            old_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible2 = true;
                        }
                        old_password.setSelection(selection);
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

        old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String m=old_password.getText().toString();
                int no=m.length();
                count3.setText(""+(int)no);
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

        nik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String m=nik.getText().toString();
                int no=m.length();
                count.setText(""+(int)no);
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
                Toast.makeText(Edit_Profil.this, "Nik Berhasil Di Salin", Toast.LENGTH_SHORT).show();

            }
        });

        voidHandleButtonSimpan();
    }

    public void doHapus(String id) {
        // Create an AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Profil.this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah Anda yakin ingin menghapus data ini?");

        // Add the buttons for positive (yes) and negative (no) responses
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User clicked yes, proceed with deletion
                mLoading.show();
                try {
                    JsonObject params = new JsonObject();
                    params.addProperty("id", id);
                    ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
                    Call call = apiservice.deleteProfil(params);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            try {
                                if (response.code() == 200) {
                                    ssmm.logout();
                                    finish();
                                } else {
                                    mLoading.dismiss();
                                    Toast.makeText(Edit_Profil.this, "Data gagal dihapus", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                mLoading.dismiss();
                                Toast.makeText(Edit_Profil.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            mLoading.dismiss();
                            Toast.makeText(Edit_Profil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    mLoading.dismiss();
                    System.out.println("system error on global: " + e.getMessage());
                    Toast.makeText(Edit_Profil.this, "Hapus data gagal " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User clicked no, do nothing (cancel deletion)
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void verifyPassword(String id){
        mLoading.show();
        try {
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            params.addProperty("password",String.valueOf(old_password.getText()));
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call call = apiservice.verifyPassword(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        if (response.code()==200){
                            doSimpan(id);
                        }else{
                            mLoading.dismiss();
                            Toast.makeText(Edit_Profil.this, "Password lama tidak dapat di verifikasi", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        mLoading.dismiss();
                        Toast.makeText(Edit_Profil.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(Edit_Profil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(Edit_Profil.this, "Simpan data gagal " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void doSimpan(String id){
        try {
            JsonObject params = new JsonObject();
            params.addProperty("id",id);
            params.addProperty("peserta_posyandu",String.valueOf(peserta_new_type.getText()));
            params.addProperty("nama",String.valueOf(nama.getText()));
            params.addProperty("nik",String.valueOf(nik.getText()));
            params.addProperty("jenis_kelamin",String.valueOf(jenis_kelamin_new_type.getText()));
            params.addProperty("tanggal_lahir",String.valueOf(tgl.getText()));
            params.addProperty("alamat",String.valueOf(alamat.getText()));
            params.addProperty("password",String.valueOf(password.getText()));
            params.addProperty("umur","");
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call call = apiservice.updateProfil(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        mLoading.dismiss();
                        if (response.code()==200){
                            Toast.makeText(Edit_Profil.this, "data berhasil diubah", Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("id", id);
                            returnIntent.putExtra("result",1);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }else{
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String err_code = jsonObject.getString("err_code");
                            if(err_code.equals("10003")){
                                Toast.makeText(Edit_Profil.this, "Data Gagal di update: nik telah terdaftar", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Edit_Profil.this, "Data Gagal di update", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (Exception e){
                        mLoading.dismiss();
                        Toast.makeText(Edit_Profil.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(Edit_Profil.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(Edit_Profil.this, "Simpan data gagal " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void showBottomSheet(View view, String type) {
        String[] id = new String[]{};
        if(type.equals("jk")){
            id = new String[]{"Laki-Laki", "Perempuan"};
        }else if(type.equals("ps")){
            id = new String[]{"Ibu Hamil", "Lansia", "Balita"};
        }else{
            id = new String[]{};
        }
        SpinnerBottomList addPhotoBottomDialogFragment = SpinnerBottomList.newInstance(Edit_Profil.this,id,type);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), SpinnerBottomList.TAG);
    }

    @Override
    public void onItemClick(String item, String type) {
        if(type.equals("jk")){
            jenis_kelamin_new_type.setText(item);
        }
        if(type.equals("ps")){
            peserta_new_type.setText(item);
        }
    }

    public void voidHandleButtonSimpan(){
        if (nama.getText().toString().length() == 0 ||
        nik.getText().toString().length() == 0 ||
                jenis_kelamin_new_type.getText().toString().length() == 0 ||
                tgl.getText().toString().length() == 0 ||
                peserta_new_type.getText().toString().length() == 0 ||
                alamat.getText().toString().length() == 0 ||
                old_password.getText().toString().length() == 0 ||
                password.getText().toString().length() == 0 ||
                verified_password.getText().toString().length() == 0) {
            btn_simpan.setEnabled(false);
        } else {
            String pass1 = password.getText().toString();
            String pass2 = verified_password.getText().toString();
            if(!pass1.equals(pass2)){
                btn_simpan.setEnabled(false);
            }else{
                btn_simpan.setEnabled(true);
            }
        }
    }


    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        { }

        @Override
        public void afterTextChanged(Editable s) {
            voidHandleButtonSimpan();
        }
    };

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
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tgl.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}