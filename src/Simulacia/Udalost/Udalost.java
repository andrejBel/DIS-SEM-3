package Simulacia.Udalost;

import Simulacia.Model.Udalost.TypUdalosti;
import Simulacia.Jadro.SimulacneJadro;

import java.util.Objects;

public abstract class Udalost implements Comparable<Udalost> {

    public double casUdalosti_;
    private int id_;
    private SimulacneJadro jadro_;


    public Udalost(double casUdalosti, SimulacneJadro simulacneJadro) {
        this.casUdalosti_ = casUdalosti;
        this.jadro_ = simulacneJadro;
    }

    public abstract void vykonaj();
    public abstract TypUdalosti getTypUdalosti();

    @Override
    public int compareTo(Udalost udalostDruha) {
        double rozdiel = this.casUdalosti_ - udalostDruha.casUdalosti_;
        if (rozdiel != 0.0) {
            return rozdiel > 0 ? 1 : -1;
        }
        long rozdielId = this.id_ - udalostDruha.id_;
        return rozdielId > 0 ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Udalost udalost = (Udalost) o;
        return (udalost.casUdalosti_ == casUdalosti_) && (id_ == udalost.id_);
    }

    @Override
    public int hashCode() {
        return Objects.hash(casUdalosti_, id_);
    }

    @Override
    public String toString() {
        return "Cas: " + casUdalosti_ + ", id_: " + id_ + ", info: " + getTypUdalosti().getNazovUdalosti();
    }

    public Udalost setCasUdalosti(double casUdalosti) {
        this.casUdalosti_ = casUdalosti;
        return this;
    }

    public SimulacneJadro getJadro() {
        return jadro_;
    }

    public void setId_(int id) {
        this.id_ = id;
    }

    public double getCasUdalosti() {
        return casUdalosti_;
    }

    public int getId() {
        return id_;
    }

    public abstract String getDodatocneInfo();

}
