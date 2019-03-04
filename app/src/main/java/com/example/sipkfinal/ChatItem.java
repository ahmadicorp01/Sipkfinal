package com.example.sipkfinal;

public class ChatItem {
    private String cnama_pengguna,cpesan,cwaktu;

    public ChatItem(String nama, String tanggapan, String waktu){

        cnama_pengguna = nama;
        cpesan = tanggapan;
        cwaktu = waktu;
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
}
