package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Casnik;
import Simulacia.Model.Restauracia.Enumeracie.StolStav;
import Simulacia.Model.Restauracia.Stol;

public class KoniecOdneseniaZaciatokJedenia extends UdalostRestauracia {

    private Stol obsluhovanyStol_;
    private Casnik obsluhujuciCasnik_;

    public KoniecOdneseniaZaciatokJedenia(double casUdalosti, SimulacneJadro simulacneJadro, Stol obsluhovanyStol, Casnik obsluhujuciCasnik) {
        super(casUdalosti, simulacneJadro);
        this.obsluhovanyStol_ = obsluhovanyStol;
        this.obsluhujuciCasnik_ = obsluhujuciCasnik;
    }

    @Override
    public void vykonaj() {

        if (obsluhujuciCasnik_ == null) {
            throw new RuntimeException("Casnik by tu mal byt!!!");
        }

        // uvolnenie
        restauracia_.ustanovCasnikaZaVolneho(obsluhujuciCasnik_, casUdalosti_);

        // kazdy zakaznik zacne jest ked mu prinesu jedlo
        obsluhovanyStol_.getZakazniciPriStole().forEach(zakaznik -> {
            zakaznik.setCasZaciatkuJedenia(casUdalosti_);
        });
        obsluhovanyStol_.setStavStola_(StolStav.JEDENIE);
        int pocetZakaznkikovPriStole = obsluhovanyStol_.getZakazniciPriStole().size();
        double kedyDojedia = 0.0;
        for (int i = 0; i < pocetZakaznkikovPriStole; i++) {
            kedyDojedia = Math.max(kedyDojedia, simulacia_.generatorDlzkaJedenia_.generuj());
        }


        this.simulacia_.planujUdalost(new KoniecJedenia(casUdalosti_ + kedyDojedia, simulacia_, obsluhovanyStol_));

        while (restauracia_.jeNejakyCasnikVolny() && restauracia_.suCinnostiPreCasnika()) {
            this.simulacia_.planujUdalost(restauracia_.vytvorCasnikovyUdalost(casUdalosti_));
        }

    }


    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.KONIEC_ODNESENIA_JEDAL;
    }

    @Override
    public String getDodatocneInfo() {
        return "Stol č. " + obsluhovanyStol_.getIdStolu() + ", časník: " + obsluhujuciCasnik_.getIdCasnika();
    }

}
