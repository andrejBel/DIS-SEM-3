package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Casnik;
import Simulacia.Model.Restauracia.Stol;

public class ZaciatokOdnesenia extends UdalostRestauracia {

    private Stol obsluhovanyStol_;
    private Casnik obsluhujuciCasnik_;

    public ZaciatokOdnesenia(double casUdalosti, SimulacneJadro simulacneJadro, Stol obsluhovanyStol, Casnik obsluhujuciCasnik) {
        super(casUdalosti, simulacneJadro);
        this.obsluhovanyStol_ = obsluhovanyStol;
        this.obsluhujuciCasnik_ = obsluhujuciCasnik;
    }

    @Override
    public void vykonaj() {
        if (obsluhujuciCasnik_ == null) {
            throw new RuntimeException("Nejaky casnik musi byt prideleny!!!");
        }

        this.simulacia_.planujUdalost(new KoniecOdneseniaZaciatokJedenia(casUdalosti_ + simulacia_.generatorDonesenieJedal_.generuj(), simulacia_, obsluhovanyStol_, obsluhujuciCasnik_));

    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.ZACIATOK_ODNESENIA_JEDAL;
    }

    @Override
    public String getDodatocneInfo() {
        return "Stol č. " + obsluhovanyStol_.getIdStolu() + ", časník: " + obsluhujuciCasnik_.getIdCasnika();
    }
}
