package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Casnik;
import Simulacia.Model.Restauracia.Enumeracie.StolStav;
import Simulacia.Model.Restauracia.Enumeracie.TypCinnosti;
import Simulacia.Model.Restauracia.Stol;

public class KoniecJedenia extends UdalostRestauracia {

    private Stol obsluhovanyStol_;

    public KoniecJedenia(double casUdalosti, SimulacneJadro simulacneJadro, Stol obsluhovanyStol_) {
        super(casUdalosti, simulacneJadro);
        this.obsluhovanyStol_ = obsluhovanyStol_;
    }

    @Override
    public void vykonaj() {


        obsluhovanyStol_.getZakazniciPriStole().forEach(zakaznik -> {
            zakaznik.setCasKoncaJedenia(casUdalosti_);
        });

        if (restauracia_.jeNejakyCasnikVolny()) {
            Casnik casnikNaObsluhu = restauracia_.obsadCasnika(obsluhovanyStol_, TypCinnosti.PREVZATIE_PLATBY);

            obsluhovanyStol_.setStavStola_(StolStav.PLATENIE);
            simulacia_.planujUdalost(new ZaciatokPlatenia(casUdalosti_, simulacia_, obsluhovanyStol_, casnikNaObsluhu));
        } else {
            obsluhovanyStol_.setCasZaradeniaDoFrontuZaplatenie(casUdalosti_);
            restauracia_.pridajStolCakajuciNaZaplatenie(obsluhovanyStol_);
        }

    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.KONIEC_JEDENIA;
    }

    @Override
    public String getDodatocneInfo() {
        return "Stol č. " + obsluhovanyStol_.getIdStolu();
    }

}
