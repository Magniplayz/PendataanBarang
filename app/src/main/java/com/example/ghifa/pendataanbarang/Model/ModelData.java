package com.example.ghifa.pendataanbarang.Model;

public class ModelData {

    String nama, date;
    String kode, harga, img;

    public ModelData(){}

    public ModelData(String nama, String date, String kode, String harga, String img) {
        this.nama = nama;
        this.date = date;
        this.kode = kode;
        this.harga = harga;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
