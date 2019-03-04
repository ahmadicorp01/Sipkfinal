package com.example.sipkfinal;

public class LaporanItem {

    private String mjudul_keluhan,mnama_kategori,mwaktu;

    public LaporanItem(String judul_keluhan, String nama_kategori, String waktu){

        mjudul_keluhan = judul_keluhan;
        mnama_kategori = nama_kategori;
        mwaktu = waktu;
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
