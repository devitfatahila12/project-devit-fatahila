package com.example.kader;

public class ModelLaporanBalita {
    private String id, nama, nik, umur, tgl_periksa, berat_badan, ket_berat_badan, tinggi_badan,
            ket_tinggi_badan, lingkar_kepala, ket_lingkar_kepala, jenis_imunisasi, catatan, obat,
            tanggal_lahir, jenis_kelamin, alamat, orang_tua_kandung;


//  codingan ini digunakan untuk menyimpan nilai atau atribut
    public void setId(String id){
        this.id = id;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public void setOrang_tua_kandung(String orang_tua_kandung){
        this.orang_tua_kandung = orang_tua_kandung;
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

    public void setTgl_periksa(String tgl_periksa){
        this.tgl_periksa = tgl_periksa;
    }

    public void setBerat_badan(String berat_badan){
        this.berat_badan = berat_badan;
    }

    public void setKet_berat_badan(String ket_berat_badan){ this.ket_berat_badan = ket_berat_badan; }

    public void setTinggi_badan(String tinggi_badan){
        this.tinggi_badan = tinggi_badan;
    }

    public void setKet_tinggi_badan(String ket_tinggi_badan){ this.ket_tinggi_badan = ket_tinggi_badan; }

    public void setLingkar_kepala(String lingkar_kepala){
        this.lingkar_kepala = lingkar_kepala;
    }

    public void setKet_lingkar_kepala(String ket_lingkar_kepala){ this.ket_lingkar_kepala = ket_lingkar_kepala; }

    public void setJenis_imunisasi(String jenis_imunisasi){ this.jenis_imunisasi = jenis_imunisasi; }

    public void setCatatan(String catatan){
        this.catatan = catatan;
    }

    public void setObat(String obat){
        this.obat = obat;
    }


    public void setTanggal_lahir(String tanggal_lahir){ this.tanggal_lahir = tanggal_lahir; }

    public void setJenis_kelamin(String jenis_kelamin){
        this.jenis_kelamin = jenis_kelamin;
    }


//    codingan ini digunakan untuk mengambil nilai atau value
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

    public String getTgl_periksa(){
        return tgl_periksa;
    }

    public String getBerat_badan(){
        return berat_badan;
    }

    public String getKet_berat_badan(){
        return ket_berat_badan;
    }

    public String getAlamat(){
        return alamat;
    }

    public String getTinggi_badan(){
        return tinggi_badan;
    }

    public String getKet_tinggi_badan(){
        return ket_tinggi_badan;
    }

    public String getLingkar_kepala(){
        return lingkar_kepala;
    }

    public String getKet_lingkar_kepala(){
        return ket_lingkar_kepala;
    }

    public String getJenis_imunisasi(){
        return jenis_imunisasi;
    }

    public String getCatatan(){
        return catatan;
    }

    public String getOrang_tua_kandung(){
        return orang_tua_kandung;
    }

    public String getObat(){
        return obat;
    }



    public String getTanggal_lahir(){
        return tanggal_lahir;
    }

    public String getJenis_kelamin(){
        return jenis_kelamin;
    }
}
