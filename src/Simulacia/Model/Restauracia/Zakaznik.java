package Simulacia.Model.Restauracia;

import Simulacia.Model.Restauracia.Enumeracie.Pokrm;

public class Zakaznik {

    private Stol stolPridelenyZakaznikovy_ = null;
    private long idZakaznika_;
    private double casVstupuDoRestauracie_ = 0.0;
    private double casZaciatkuUsadenia_ = 0.0;
    private double casKoncaUsadenia_ = 0.0;
    private int dlzkaVyhotoveniaJedla_ = 0;
    private double casVyhotoveniaJedla_ = 0.0;
    private double casZaciatkuJedenia_= 0.0;
    private double casKoncaJedenia_= 0.0;
    private double casZaciatkuPlatenia_= 0.0;
    private Pokrm pokrm_ = null;


    public Zakaznik(long idZakaznika) {
        this.idZakaznika_ = idZakaznika;
    }

    public Stol getStolPridelenyZakaznikovy() {
        return stolPridelenyZakaznikovy_;
    }

    public double getCasVstupuDoRestauracie() {
        return casVstupuDoRestauracie_;
    }

    public double getCasZaciatkuUsadenia() {
        return casZaciatkuUsadenia_;
    }

    public double getCasKoncaUsadenia() {
        return casKoncaUsadenia_;
    }

    public int getDlzkaVyhotoveniaJedla() {
        return dlzkaVyhotoveniaJedla_;
    }

    public double getCasVyhotoveniaJedla() {
        return casVyhotoveniaJedla_;
    }

    public double getCasZaciatkuJedenia() {
        return casZaciatkuJedenia_;
    }

    public double getCasKoncaJedenia() {
        return casKoncaJedenia_;
    }

    public double getCasZaciatkuPlatenia() {
        return casZaciatkuPlatenia_;
    }

    public Pokrm getPokrm() {
        return pokrm_;
    }

    public Zakaznik setStolPridelenyZakaznikovy(Stol stolPridelenyZakaznikovy) {
        this.stolPridelenyZakaznikovy_ = stolPridelenyZakaznikovy;
        return this;
    }

    public Zakaznik setCasVstupuDoRestauracie(double casVstupuDoRestauracie) {
        this.casVstupuDoRestauracie_ = casVstupuDoRestauracie;
        return this;
    }

    public Zakaznik setCasZaciatkuUsadenia(double casZaciatkuObsluhy) {
        this.casZaciatkuUsadenia_ = casZaciatkuObsluhy;
        return this;
    }

    public Zakaznik setCasKoncaUsadenia(double casKoncaUsadenia) {
        this.casKoncaUsadenia_ = casKoncaUsadenia;
        return this;
    }

    public void setDlzkaVyhotoveniaJedla(int dlzkaVyhotoveniaJedla) {
        this.dlzkaVyhotoveniaJedla_ = dlzkaVyhotoveniaJedla;
    }

    public Zakaznik setCasVyhotoveniaJedla(double casVyhotoveniaJedla) {
        this.casVyhotoveniaJedla_ = casVyhotoveniaJedla;
        return this;
    }

    public Zakaznik setCasZaciatkuJedenia(double casZaciatkuJedenia) {
        this.casZaciatkuJedenia_ = casZaciatkuJedenia;
        return this;
    }

    public Zakaznik setCasKoncaJedenia(double casKoncaJedenia) {
        this.casKoncaJedenia_ = casKoncaJedenia;
        return this;
    }

    public Zakaznik setCasZaciatkuPlatenia(double casZaciatkuPlatenia) {
        this.casZaciatkuPlatenia_ = casZaciatkuPlatenia;
        return this;
    }

    public Zakaznik setPokrm(Pokrm pokrm) {
        this.pokrm_ = pokrm;
        return this;
    }

    public double getCasStravenyCakanim() {
        return (this.casZaciatkuUsadenia_ - this.casVstupuDoRestauracie_) +
                (this.casZaciatkuJedenia_ - this.casKoncaUsadenia_) +
                (this.casZaciatkuPlatenia_ - this.casKoncaJedenia_);
    }

    public void clear() {
        this.stolPridelenyZakaznikovy_ = null;
        this.idZakaznika_ = -1;
        this.casVstupuDoRestauracie_ = 0.0;
        this.casZaciatkuUsadenia_ = 0.0;
        this.casKoncaUsadenia_ = 0.0;
        this.dlzkaVyhotoveniaJedla_ = 0;
        this.casVyhotoveniaJedla_ = 0.0;
        this.casZaciatkuJedenia_= 0.0;
        this.casKoncaJedenia_= 0.0;
        this.casZaciatkuPlatenia_= 0.0;
        this.pokrm_ = null;
    }

    public long getIdZakaznika() {
        return idZakaznika_;
    }

    @Override
    public String toString() {
        return "Zakaznik{" +
                "stolPridelenyZakaznikovy_=" + stolPridelenyZakaznikovy_ +
                ", pokrm_=" + pokrm_ + ", idZakaznika_=" + idZakaznika_ +
                '}';
    }
}
