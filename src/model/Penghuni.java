package model;

public class Penghuni {
    // PILAR - encapsulation
    private int idPenghuni;
    private String namaPenghuni;
    private String noTelp;
    private int idKamar;
    private String asal;
    private String tglMasuk;
    private String statusPenghuni;
    private String namaOrtu;
    private String telpOrtu;
    
    public Penghuni(){}
    
    public Penghuni(int idPenghuni, String namaPenghuni, String noTelp, int idKamar, String asal,
        String tglMasuk, String statusPenghuni, String namaOrtu, String telpOrtu){
        this.idPenghuni = idPenghuni;
        this.namaPenghuni = namaPenghuni;
        this.noTelp = noTelp;
        this.idKamar = idKamar;
        this.asal = asal;
        this.tglMasuk = tglMasuk;
        this.statusPenghuni = statusPenghuni;
        this.namaOrtu = namaOrtu;
        this.telpOrtu = telpOrtu;
    }
    
    public int getIdPenghuni() { return idPenghuni; }
    public String getNama() {return namaPenghuni;}
    public String getNoTelepon()  { return noTelp; }
    public int getIdKamar()      { return idKamar; }
    public String getAsal() {return asal;}
    public String getTglMasuk() {return tglMasuk;}
    public String getStatusPenghuni() {return statusPenghuni;}
    public String getNamaOrtu() {return namaOrtu;}
    public String getTelpOrtu() {return telpOrtu;}
    
    public void setIdPenghuni(int idPenghuni) { this.idPenghuni = idPenghuni; }
    public void setNama(String namaPenghuni) { this.namaPenghuni = namaPenghuni; }
    public void setNoTelepon(String noTelp) {this.noTelp = noTelp;}
    public void setIdKamar(int idKamar) {this.idKamar = idKamar;}
    public void setAsal(String asal) {this.asal = asal;}
    public void setTglMasuk(String tglMasuk) {this.tglMasuk = tglMasuk;}
    public void setStatusPenghuni(String statusPenghuni) {this.statusPenghuni = statusPenghuni;}
    public void setNamaOrtu(String namaOrtu) {this.namaOrtu = namaOrtu;}
    public void setTelpOrtu(String telpOrtu) {this.telpOrtu = telpOrtu;}
    
    @Override
    public String toString(){return namaPenghuni;}
    
    
    
}