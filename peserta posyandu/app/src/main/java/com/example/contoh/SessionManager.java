package com.example.contoh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int mode = 0;

    private static final String pref_name = "sesslog";
    private static final String is_login = "islogin";
    public static final String key_id = "keyid";
    public static final String key_nama = "keynama";
    public static final String key_peserta = "keypeserta";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(pref_name, mode);
        editor = pref.edit();
    }

    public void createSession(String id, String nama, String peserta){
        editor.putBoolean(is_login, true);
        editor.putString(key_id, id);
        editor.putString(key_nama, nama);
        editor.putString(key_peserta, peserta);
        editor.commit();
    }

    private boolean is_login() {
        return pref.getBoolean(is_login, false);
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(key_id, pref.getString(key_id, null));
        user.put(key_nama, pref.getString(key_nama, null));
        user.put(key_peserta, pref.getString(key_peserta, null));
        return user;
    }

    public void checkLogin(){
        if (!this.is_login())  {
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        } else {
            Intent i = new Intent(_context, MenuUtamaPeserta.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }
}
