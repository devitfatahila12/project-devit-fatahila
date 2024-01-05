package com.example.kader;

public class ModelDataPeserta {
    private String id, nama, nik, alamat, tanggal_lahir, jenis_kelamin, peserta_posyandu, password, created_dt;

    public void setId(String id){
        this.id = id;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public void setNik(String nik){
        this.nik = nik;
    }



    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public void setTanggal_lahir(String tanggal_lahir){
        this.tanggal_lahir = tanggal_lahir;
    }

    public void setJenis_kelamin(String jenis_kelamin){
        this.jenis_kelamin = jenis_kelamin;
    }

    public void setPeserta_posyandu(String peserta_posyandu){ this.peserta_posyandu = peserta_posyandu; }

    public void setPassword(String password){
        this.password = password;
    }

    public void setCreated_dt(String created_dt){
        this.created_dt = created_dt;
    }

    public String getId(){
        return id;
    }

    public String getNama(){
        return nama;
    }

    public String getNik(){
        return nik;
    }



    public String getAlamat(){
        return alamat;
    }

    public String getTanggal_lahir(){
        return tanggal_lahir;
    }

    public String getJenis_kelamin(){
        return jenis_kelamin;
    }

    public String getPeserta_posyandu(){
        return peserta_posyandu;
    }

    public String getPassword(){
        return password;
    }

    public String getCreated_dt(){
        return created_dt;
    }
}
