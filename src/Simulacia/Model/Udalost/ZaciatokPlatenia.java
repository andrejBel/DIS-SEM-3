package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Casnik;
import Simulacia.Model.Restauracia.Stol;

public class ZaciatokPlatenia extends UdalostRestauracia {

    private Stol obsluhovanyStol_;
    private Casnik obsluhujuciCasnik_;

    public ZaciatokPlatenia(double casUdalosti, SimulacneJadro simulacneJadro, Stol obsluhovanyStol, Casnik obsluhujuciCasnik) {
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
            zakaznik.setCasZaciatkuPlatenia(casUdalosti_);
        });

        this.simulacia_.planujUdalost(new KoniecPlatenia(casUdalosti_ + simulacia_.generatorDlzkaPlatenia_.generuj(), simulacia_, obsluhovanyStol_, obsluhujuciCasnik_));

    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.ZACIATOK_PLATENIA;
    }

    @Override
    public String getDodatocneInfo() {
        return "Stol č. " + obsluhovanyStol_.getIdStolu() + ", časník: " + obsluhujuciCasnik_.getIdCasnika();
    }

}
