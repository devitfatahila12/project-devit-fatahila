package com.example.kader;

public class ModelLaporanLansia {
    private String id, nama, nik, umur, jenis_kelamin, tgl_periksa, berat_badan, tinggi_badan,
            tensi_darah, ket_tensi_darah, asam_urat, ket_asam_urat, kolerstrol, ket_kolerstrol,
            catatan, obat, tanggal_lahir, alamat;

    public void setId(String id){
        this.id = id;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public void setNik(String nik){
        this.nik = nik;
    }

    public void setUmur(String umur){
        this.umur = umur;
    }

    public void setJenis_kelamin(String jenis_kelamin){
        this.jenis_kelamin = jenis_kelamin;
    }

    public void setTgl_periksa(String tgl_periksa){
        this.tgl_periksa = tgl_periksa;
    }

    public void setBerat_badan(String berat_badan){
        this.berat_badan = berat_badan;
    }

    public void setTinggi_badan(String tinggi_badan){
        this.tinggi_badan = tinggi_badan;
    }

    public void setTensi_darah(String tensi_darah){
        this.tensi_darah = tensi_darah;
    }

    public void setKet_tensi_darah(String ket_tensi_darah){ this.ket_tensi_darah = ket_tensi_darah; }

    public void setAsam_urat(String asam_urat){ this.asam_urat = asam_urat; }

    public void setKet_asam_urat(String ket_asam_urat){
        this.ket_asam_urat = ket_asam_urat;
    }

    public void setKolerstrol(String kolerstrol){ this.kolerstrol = kolerstrol; }

    public void setKet_kolerstrol(String ket_kolerstrol){ this.ket_kolerstrol = ket_kolerstrol; }

    public void setCatatan(String catatan){ this.catatan = catatan; }

    public void setObat(String obat){
        this.obat = obat;
    }

    public void setTanggal_lahir(String tanggal_lahir){
        this.tanggal_lahir = tanggal_lahir;
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

    public String getUmur(){
        return umur;
    }

    public String getAlamat(){
        return alamat;
    }

    public String getJenis_kelamin(){
        return jenis_kelamin;
    }

    public String getTgl_periksa(){
        return tgl_periksa;
    }

    public String getBerat_badan(){
        return berat_badan;
    }

    public String getTinggi_badan(){
        return tinggi_badan;
    }

    public String getTensi_darah(){
        return tensi_darah;
    }

    public String getKet_tensi_darah(){
        return ket_tensi_darah;
    }

    public String getAsam_urat(){
        return asam_urat;
    }

    public String getKet_asam_urat(){
        return ket_asam_urat;
    }

    public String getKolerstrol(){
        return kolerstrol;
    }

    public String getKet_kolerstrol(){ return ket_kolerstrol; }

    public String getCatatan(){
        return catatan;
    }

    public String getObat(){
        return obat;
    }

    public String getTanggal_lahir(){
        return tanggal_lahir;
    }
}
