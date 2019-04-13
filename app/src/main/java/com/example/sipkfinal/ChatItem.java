package com.example.sipkfinal;

public class ChatItem {
    private String cnama_pengguna,cpesan,cwaktu;
    private boolean kanan;
    public ChatItem(String nama_pengguna, String tanggapan, String waktu, boolean kanan){

        cnama_pengguna = nama_pengguna;
        cpesan = tanggapan;
        cwaktu = waktu;
        this.kanan = kanan;
    }

    public String getNama(){
        return cnama_pengguna;
    }

    public String getTanggapan(){
        return cpesan;
    }

    public String getWaktu(){
        return cwaktu;
    }

    public boolean getKanan(){
        return kanan;
    }
}
