package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Casnik;
import Simulacia.Model.Restauracia.Stol;
import Simulacia.Model.Restauracia.Zakaznik;

public class KoniecPlatenia extends UdalostRestauracia {

    private Stol obsluhovanyStol_;
    private Casnik obsluhujuciCasnik_;

    public KoniecPlatenia(double casUdalosti, SimulacneJadro simulacneJadro, Stol obsluhovanyStol, Casnik obsluhujuciCasnik) {
        super(casUdalosti, simulacneJadro);
        this.obsluhovanyStol_ = obsluhovanyStol;
        this.obsluhujuciCasnik_ = obsluhujuciCasnik;
    }

    @Override
    public void vykonaj() {
        if (obsluhujuciCasnik_ == null) {
            throw new RuntimeException("Casnik by tu mal byt!!!");
        }

        for (Zakaznik zakaznik: obsluhovanyStol_.getZakazniciPriStole()) {
            simulacia_.priemernyCasZakaznikaStravenyCakanimR_.pridaj(zakaznik.getCasStravenyCakanim());
        }

        restauracia_.ustanovCasnikaZaVolneho(obsluhujuciCasnik_, casUdalosti_);
        restauracia_.vratPouzivanyStol(obsluhovanyStol_);

        while (restauracia_.jeNejakyCasnikVolny() && restauracia_.suCinnostiPreCasnika()) {
            this.simulacia_.planujUdalost(restauracia_.vytvorCasnikovyUdalost(casUdalosti_));
        }
    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.KONIEC_PLATENIA;
    }

    @Override
    public String getDodatocneInfo() {
        return "Stôl č. " + obsluhovanyStol_.getIdStolu() + ", čašník č. " + obsluhujuciCasnik_.getIdCasnika();
    }
}
