package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Casnik;
import Simulacia.Model.Restauracia.Enumeracie.Pokrm;
import Simulacia.Model.Restauracia.Enumeracie.StolStav;
import Simulacia.Model.Restauracia.Kuchar;
import Simulacia.Model.Restauracia.Stol;
import Simulacia.Model.Restauracia.Zakaznik;
import Simulacia.SimulaciaRestauracia;

public class KoniecUsadenia extends UdalostRestauracia {

    private Stol obsluhovanyStol_;
    private Casnik obsluhujuciCasnik_;

    public KoniecUsadenia(double casUdalosti, SimulacneJadro simulacneJadro, Stol obsluhovanyStol, Casnik obsluhujuciCasnik) {
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
            zakaznik.setCasKoncaUsadenia(casUdalosti_);
        });
        obsluhovanyStol_.setStavStola_(StolStav.CAKAJUCI_NA_PRIPRAVU_JEDLA);

        restauracia_.ustanovCasnikaZaVolneho(obsluhujuciCasnik_, casUdalosti_);


        // planovanie kucharov

        for (Zakaznik zakaznik: obsluhovanyStol_.getZakazniciPriStole()) {

            // vyber pokrmu zakaznika
            double pKtoreJedlo = simulacia_.generatorVyberKtoreJedlo.generuj();
            Pokrm pokrmPreZakaznika = null;
            int dlzkaVyhotoveniaJedla = 0;

            if (pKtoreJedlo < 0.3) {
                pokrmPreZakaznika = Pokrm.CAESAR_SALAT;
                dlzkaVyhotoveniaJedla = simulacia_.generatorCeasarSalat.generuj();
            } else if (pKtoreJedlo < (0.3 + 0.35)) {
                pokrmPreZakaznika = Pokrm.PENNE_SALAT;
                dlzkaVyhotoveniaJedla = simulacia_.generatorPenneSalat.generuj();
            } else if (pKtoreJedlo < (0.3 + 0.35 + 0.2)) {
                pokrmPreZakaznika = Pokrm.CELOZRNNE_SPAGETY;
                dlzkaVyhotoveniaJedla = simulacia_.generatorCelozrnnaSpageta.generuj();
            } else {
                pokrmPreZakaznika = Pokrm.SYTY_SALAT;
                dlzkaVyhotoveniaJedla = simulacia_.DOBA_PRIPRAVY_SYTEHO_SALATU;
            }

            zakaznik.setPokrm(pokrmPreZakaznika);
            zakaznik.setDlzkaVyhotoveniaJedla(dlzkaVyhotoveniaJedla);

            if (restauracia_.jeNejakyKucharVolny()) {
                Kuchar kuchar = restauracia_.obsadKuchara(zakaznik);
                simulacia_.planujUdalost(new ZaciatokPripravyJedla(casUdalosti_, simulacia_, kuchar, zakaznik));
            } else {
                restauracia_.pridajZakaznikaDoFrontuJedal(zakaznik);
            }
        }


        while (restauracia_.jeNejakyCasnikVolny() && restauracia_.suCinnostiPreCasnika()) {
            this.simulacia_.planujUdalost(restauracia_.vytvorCasnikovyUdalost(casUdalosti_));
        }

    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.KONIEC_USADENIA;
    }

    @Override
    public String getDodatocneInfo() {
        return "Stol č. " + obsluhovanyStol_.getIdStolu() + ", časník: " + obsluhujuciCasnik_.getIdCasnika();
    }

}
