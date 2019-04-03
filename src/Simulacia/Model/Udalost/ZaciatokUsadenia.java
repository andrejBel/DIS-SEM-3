package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Casnik;
import Simulacia.Model.Restauracia.Stol;

public class ZaciatokUsadenia extends UdalostRestauracia {

    private Stol obsluhovanyStol_;
    private Casnik obsluhujuciCasnik_;

    public ZaciatokUsadenia(double casUdalosti, SimulacneJadro simulacneJadro, Stol obsluhovanyStol, Casnik obsluhujuciCasnik) {
        super(casUdalosti, simulacneJadro);
        this.obsluhovanyStol_ = obsluhovanyStol;
        this.obsluhujuciCasnik_ = obsluhujuciCasnik;
    }

    @Override
    public void vykonaj() {
        if (obsluhujuciCasnik_ == null) {
            throw new RuntimeException("Casnik by tu mal byt!!!");
        }

        obsluhovanyStol_.getZakazniciPriStole().forEach(zakaznik -> {
            zakaznik.setCasZaciatkuUsadenia(casUdalosti_);
        } );
        simulacia_.planujUdalost(new KoniecUsadenia(casUdalosti_ + simulacia_.generatorPrevzatiaObjednavky_.generuj(), simulacia_, obsluhovanyStol_, obsluhujuciCasnik_));

    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.ZACIATOK_USADENIA;
    }

    @Override
    public String getDodatocneInfo() {
        return "Stol č. " + obsluhovanyStol_.getIdStolu() + ", časník: " + obsluhujuciCasnik_.getIdCasnika();
    }

}
