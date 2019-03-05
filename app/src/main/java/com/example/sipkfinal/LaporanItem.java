package com.example.sipkfinal;

public class LaporanItem {

    private int mid_laporan;
    private String mjudul_keluhan,mnama_kategori,mwaktu;

    public LaporanItem(int id_laporan, String judul_keluhan, String nama_kategori, String waktu)
    {
        mid_laporan = id_laporan;
        mjudul_keluhan = judul_keluhan;
        mnama_kategori = nama_kategori;
        mwaktu = waktu;
    }

    public int getMid_laporan() {
        return mid_laporan;
    }

    public void setMid_laporan(int mid_laporan) {
        this.mid_laporan = mid_laporan;
    }

    public String getJudul_keluhan(){
        return mjudul_keluhan;
    }

    public String getNama_kategori(){
        return mnama_kategori;
    }

    public String getWaktu(){
        return mwaktu;
    }
}
