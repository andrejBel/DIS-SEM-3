package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Kuchar;
import Simulacia.Model.Restauracia.Zakaznik;

public class ZaciatokPripravyJedla extends UdalostRestauracia {

    private Kuchar kuchar_;
    private Zakaznik obsluhovanyZakaznik_;

    public ZaciatokPripravyJedla(double casUdalosti, SimulacneJadro simulacneJadro, Kuchar kuchar, Zakaznik obsluhovanyZakaznik) {
        super(casUdalosti, simulacneJadro);
        this.kuchar_ = kuchar;
        this.obsluhovanyZakaznik_ = obsluhovanyZakaznik;
    }

    @Override
    public void vykonaj() {
        if (obsluhovanyZakaznik_.getPokrm() == null) {
            throw new RuntimeException("Jedlo musi byt priradene");
        }

        int casPripravyPokrmu = obsluhovanyZakaznik_.getDlzkaVyhotoveniaJedla();

        if (casPripravyPokrmu <= 0) {
            throw new RuntimeException("Tu by si nemal byt");
        }

        simulacia_.planujUdalost(new KoniecPripravyJedla(casUdalosti_ + casPripravyPokrmu, simulacia_, kuchar_, obsluhovanyZakaznik_));

    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.ZACIATOK_PRIPRAVY_JEDAL;
    }

    @Override
    public String getDodatocneInfo() {
        return "Kuchar č. " + kuchar_.getIndexKuchara() + ", zakaznik č." + obsluhovanyZakaznik_.getIdZakaznika() + ", stol: " + obsluhovanyZakaznik_.getStolPridelenyZakaznikovy().getIdStolu();
    }

}
