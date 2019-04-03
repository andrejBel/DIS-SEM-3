package Simulacia.Statistiky;

import Simulacia.Jadro.SimulacneJadro;

public class StatistikaVazenyPriemer extends Statistika {

    private double poslednyCas_ = 0.0;
    private double poslednaHodnota_ = 0.0;
    public double suma_ = 0;
    public long pocet_ = 0;
    String nazovStatistiky_ = "";

    public StatistikaVazenyPriemer(SimulacneJadro jadro, String nazovStatistiky) {
        super(jadro);
        this.nazovStatistiky_ = nazovStatistiky;
    }

    public StatistikaVazenyPriemer(SimulacneJadro jadro) {
        super(jadro);
    }

    public void pridaj(double hodnota) {
        if (jadro_.getCasReplikacie() < jadro_.getKoncovyCasReplikacie()) {
            suma_ += poslednaHodnota_ * (jadro_.getCasReplikacie() - poslednyCas_);
            pocet_++;

            poslednyCas_ = jadro_.getCasReplikacie();
            poslednaHodnota_ = hodnota;
        }
    }


    public double getVazenyPriemer(double cas) {
        suma_ += poslednaHodnota_ * (cas - poslednyCas_);
        poslednyCas_ = cas;
        return cas > 0 ? (suma_ / cas) : 0.0;
    }

    public double getVazenyPriemer() {
        double cas = jadro_.getCasReplikacie();
        if (cas > jadro_.getKoncovyCasReplikacie()) {
            cas = jadro_.getKoncovyCasReplikacie();
        } else {
            suma_ += poslednaHodnota_ * (cas - poslednyCas_);
            poslednyCas_ = cas;
        }
        return cas > 0 ? (suma_ / cas) : 0.0;
    }

    @Override
    public void clear() {
        this.poslednyCas_ = 0.0;
        this.poslednaHodnota_ = 0.0;
        this.suma_ = 0;
        this.pocet_ = 0;
    }

    public void clear(double poslednaHodnota) {
        this.poslednyCas_ = 0.0;
        this.poslednaHodnota_ = poslednaHodnota;
        this.suma_ = 0;
        this.pocet_ = 0;
    }

    public String getNazovStatistiky() {
        return nazovStatistiky_;
    }

}
