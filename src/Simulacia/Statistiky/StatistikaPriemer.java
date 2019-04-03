package Simulacia.Statistiky;

import Simulacia.Jadro.SimulacneJadro;

public class StatistikaPriemer implements IStatistika {

    private double suma_ = 0;
    private long pocet_ = 0;
    private  String nazovStatistiky_;

    public StatistikaPriemer(String nazovStatistiky) {
        this.nazovStatistiky_ = nazovStatistiky;
        clear();
    }

    public void pridaj(double hodnota) {
        suma_ += hodnota;
        pocet_++;
    }

    public double getPriemer() {
        return pocet_ > 0 ? suma_ / ((double) pocet_) : 0.0;
    }

    @Override
    public void clear() {
        suma_ = 0.0;
        pocet_ = 0;
    }

    public String getNazovStatistiky() {
        return nazovStatistiky_;
    }

    @Override
    public String toString() {
        return nazovStatistiky_ + " " + getPriemer();
    }
}
