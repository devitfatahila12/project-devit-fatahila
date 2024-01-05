package com.example.contoh;

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
import android.text.InputType;
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
        View.OnClickListener, SpinnerBottomList.ItemClickListener {

    EditText nama, nik, alamat, tgl, password, verified_password;
    Spinner jenis, peserta;
    Button btn_date, btn_register;
    ImageView imgGallery;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private final int GALLERY_REQ_CODE = 1000;
    String base64_image;
    TextView copy,count, count1, count2;
    EditText jenis_kelamin_new_type, peserta_new_type;
    loading mLoading;
    boolean passwordVisible1, passwordVisible2;
    private boolean passwordShown = false;
      TextView error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoading = new loading(RegisterActivity.this);

        nama = (EditText)findViewById(R.id.nama);
        error_message = (TextView)findViewById(R.id.error_message);
        nama.addTextChangedListener(watcher);
        nik = (EditText)findViewById(R.id.nik);
        nik.addTextChangedListener(watcher);

        alamat = (EditText)findViewById(R.id.alamat);
        copy = findViewById(R.id.copy);
        count = findViewById(R.id.count);
        count1 = findViewById(R.id.count1);
        count2  = findViewById(R.id.count2);
        alamat.addTextChangedListener(watcher);
        tgl = (EditText)findViewById(R.id.tgl);
        tgl.setFocusable(false);
        tgl.setEnabled(false);
        tgl.setCursorVisible(false);
        tgl.addTextChangedListener(watcher);
        password = (EditText)findViewById(R.id.password);
        password.addTextChangedListener(watcher);
//        jenis = (Spinner) findViewById(R.id.jenis);
//        peserta = (Spinner) findViewById(R.id.peserta);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_register = (Button) findViewById(R.id.btn_register);
        imgGallery = findViewById(R.id.imgGallery);
        Button btnGallery = findViewById(R.id.btnGallery);

//      codingan ini digunakan untuk membuka galeri yang ada di perangkat masing masing
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

         password = (EditText)findViewById(R.id.password);
        password.addTextChangedListener(watcher);

//Jika nilai yang dimasukkan dalam kolom kata sandi tidak sama dengan nilai yang dimasukkan dalam kolom
// kata sandi yang diverifikasi, maka pesan kesalahan "password dan verified password tidak sama"
// akan ditampilkan pada elemen tampilan dengan ID "error_message".
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

        //Jika nilai yang dimasukkan dalam kolom kata sandi tidak sama dengan nilai yang dimasukkan dalam kolom
// kata sandi yang diverifikasi, maka pesan kesalahan "password dan verified password tidak sama"
// akan ditampilkan pada elemen tampilan dengan ID "error_message".
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

        verified_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s=verified_password.getText().toString();
                int num=s.length();
                count2.setText(""+(int)num);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//      copy the clipboard
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


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });
        btn_register.setEnabled(false);
        btn_date.setOnClickListener(this);

//        List<String> jenis_kelamin = new ArrayList<String>();
//        jenis_kelamin.add("Perempuan");
//        jenis_kelamin.add("Laki-Laki");

//        List<String> peserta_posyandu = new ArrayList<String>();
//        peserta_posyandu.add("Ibu Hamil");
//        peserta_posyandu.add("Lansia");
//        peserta_posyandu.add("Balita");

//        ArrayAdapter<String> dataAdapter_jenis_kelamin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jenis_kelamin);
//        ArrayAdapter<String> dataAdapter_peserta_posyandu = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, peserta_posyandu);

//        dataAdapter_jenis_kelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapter_peserta_posyandu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        jenis.setAdapter(dataAdapter_jenis_kelamin);
//        jenis.setOnItemSelectedListener(new JenisSpinnerClass());
//        peserta.setAdapter(dataAdapter_peserta_posyandu);
//        peserta.setOnItemSelectedListener(new PesertaSpinnerClass());

        jenis_kelamin_new_type = (EditText) findViewById(R.id.jenis_kelamin_new_type);
        jenis_kelamin_new_type.setFocusable(false);
        jenis_kelamin_new_type.setInputType(InputType.TYPE_NULL);
        jenis_kelamin_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "jk");
            }
        });
        jenis_kelamin_new_type.addTextChangedListener(watcher);

        peserta_new_type = (EditText) findViewById(R.id.peserta_new_type);
        peserta_new_type.setFocusable(false);
        peserta_new_type.setInputType(InputType.TYPE_NULL);
        peserta_new_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v, "jps");
            }
        });
        peserta_new_type.addTextChangedListener(watcher);
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
            doCheckButtonSimpan();
        }
    };

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

    public void showBottomSheet(View view, String type) {
        String[] id = new String[]{};
        if(type.equals("jk")){
            id = new String[]{"Laki-Laki", "Perempuan"};
        }else if(type.equals("jps")){
            id = new String[]{"Ibu Hamil", "Lansia", "Balita"};
        }else{
            id = new String[]{};
        }
        SpinnerBottomList addPhotoBottomDialogFragment = SpinnerBottomList.newInstance(RegisterActivity.this,id,type);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), SpinnerBottomList.TAG);
    }

    @Override
    public void onItemClick(String item, String type) {
        if(type.equals("jk")){
            jenis_kelamin_new_type.setText(item);
        }
        if(type.equals("jps")){
            peserta_new_type.setText(item);
        }
    }

    public  void doRegister(){
        mLoading.show();

        try {
            JsonObject params = new JsonObject();
            params.addProperty("nama",String.valueOf(nama.getText()));
            params.addProperty("nik",String.valueOf(nik.getText()));

            params.addProperty("alamat",String.valueOf(alamat.getText()));
            params.addProperty("tanggal_lahir",String.valueOf(tgl.getText()));
            params.addProperty("jenis_kelamin",String.valueOf(jenis_kelamin_new_type.getText()));
            params.addProperty("peserta_posyandu",String.valueOf(peserta_new_type.getText()));
            params.addProperty("kartu_keluarga",base64_image);
            params.addProperty("password",String.valueOf(password.getText()));
            ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
            Call call = apiservice.registerAkun(params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    mLoading.dismiss();
                    try{
                        if (response.code()==200){
                            Toast.makeText(RegisterActivity.this, "Register berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity (intent);
                            finish();
                        } else  {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String err_code = jsonObject.getString("err_code");
                            if(err_code.equals("10003")){
                                Toast.makeText(RegisterActivity.this, "Register Gagal : nik telah terdaftar", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(RegisterActivity.this, "Internal Server Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    mLoading.dismiss();
                    Toast.makeText(RegisterActivity.this, "Internal Server Error" + t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            mLoading.dismiss();
            System.out.println("system error on global: " +e.getMessage());
            Toast.makeText(RegisterActivity.this, "Register gagal" + e, Toast.LENGTH_SHORT).show();
        }
    }

//    kita hanya tertarik dengan hasil yang berhasil (resultCode RESULT_OK) dan
//    kita memeriksa apakah hasil tersebut dikembalikan oleh aktivitas galeri
//    (requestCode GALLERY_REQ_CODE). Jika ya, maka kode tersebut membuka file gambar,
//    menampilkannya di ImageView dengan ID imgGallery, mengompresinya menjadi bentuk byte array,
//    mengubahnya menjadi bentuk Base64, dan menyimpannya dalam variabel base64_image.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

// Memastikan hasil yang diterima adalah OK (berhasil)
        if (resultCode==RESULT_OK){
            // Inisialisasi variabel yang akan digunakan untuk menyimpan gambar dalam bentuk Base64
            base64_image = null;
            if (requestCode==GALLERY_REQ_CODE){

                try {
                    // Membuka file gambar dari data yang dikembalikan oleh aktivitas galeri
                    ParcelFileDescriptor parcelFileDescriptor =
                            getContentResolver().openFileDescriptor(data.getData(), "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

                    // Membaca file gambar menjadi objek Bitmap
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    // Menampilkan gambar di dalam ImageView dengan id imgGallery
                    imgGallery.setImageBitmap(image);

                    // Mengompresi gambar menjadi bentuk byte array
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();

                    // Mengubah byte array menjadi bentuk Base64 dan menyimpannya dalam variabel base64_image
                    base64_image = Base64.encodeToString(bytes, Base64.DEFAULT);
                } catch (IOException e) {
                    Toast.makeText(RegisterActivity.this,"error mengambil gambar" + e.toString(),Toast.LENGTH_SHORT).show();
                }
//                imgGallery.setImageURI(data.getData());
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                Bitmap bm=((BitmapDrawable)imgGallery.getDrawable()).getBitmap();
//                bm.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//                byte[] bytes = byteArrayOutputStream.toByteArray();
//                base64_image = Base64.encodeToString(bytes, Base64.DEFAULT);

            }
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

    class JenisSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class PesertaSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#00C9B8"));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }
}