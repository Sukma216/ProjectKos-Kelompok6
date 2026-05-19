/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class Pembayaran {
    // PILAR - encapsulation
    private int idPembayaran;
    private int idPenghuni;
    private String namaPenghuni;
    private int idKamar;
    private String bulan;
    private String tanggal;
    private double jumlah;
    private String metodeBayar;
    private String status;
    private String keterangan;
    
    
    public Pembayaran(){}
    public Pembayaran(int idPembayaran, int idPenghuni, String namaPenghuni,int idKamar, String bulan, String tanggal, double jumlah, String status, String metodeBayar, String keterangan){
        this.idPembayaran = idPembayaran;
        this.idPenghuni = idPenghuni;
        this.namaPenghuni = namaPenghuni;
        this.idKamar = idKamar;
        this.bulan = bulan;
        this.tanggal = tanggal;
        this.jumlah = jumlah;
        this.metodeBayar = metodeBayar;
        this.status = status;
        this.keterangan = keterangan;
        
    }
   
    public int getIdPembayaran() {return idPembayaran;}
    public int getIdPenghuni(){return idPenghuni;}
    public String getNamaPenghuni(){return namaPenghuni;}
    public int getIdKamar() {return idKamar;}
    public String getBulan(){return bulan;}
    public String getTanggal(){return tanggal;}
    public double getJumlah(){return jumlah;}
    public String getMetodeBayar(){return metodeBayar;}
    public String getStatus(){return status;}
    public String getKeterangan(){return keterangan;}
    
    // setter
    public void setIdPembayaran(int idPembayaran){
        this.idPembayaran = idPembayaran;
    }
    public void setIdPenghuni(int idPenghuni){
        this.idPenghuni = idPenghuni;
    }
    public void setNamaPenghuni(String namaPenghuni){
        this.namaPenghuni = namaPenghuni;
    }
    public void setIdKamar(int idKamar){
        this.idKamar = idKamar;
    }
    public void setBulan(String bulan){
        this.bulan = bulan;
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
    public void setJumlah(double jumlah){
        this.jumlah = jumlah;
    }
    public void setMeotdeBayar(String metodeBayar){
        this.metodeBayar = metodeBayar;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setKeterangan(String keterangan){
        this.keterangan = keterangan;
    }
    
}
