package Simulacia.Model.Udalost;

import Generatory.GeneratorExponencialne;
import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Casnik;
import Simulacia.Model.Restauracia.Enumeracie.StolStav;
import Simulacia.Model.Restauracia.Enumeracie.TypCinnosti;
import Simulacia.Model.Restauracia.Stol;
import Simulacia.Model.Restauracia.Zakaznik;

public class PrichodZakaznika extends UdalostRestauracia {

    GeneratorExponencialne generatorPrichoduZakaznikov_;
    int kolkoZakaznikovMaPrist_;

    public PrichodZakaznika(double casUdalosti, SimulacneJadro simulacneJadro) {
        super(casUdalosti, simulacneJadro);

    }

    public PrichodZakaznika(double casUdalosti, SimulacneJadro simulacneJadro, GeneratorExponencialne generatorPrichoduZakaznikov, int kolkoZakaznikovMaPrist) {
        super(casUdalosti, simulacneJadro);
        this.generatorPrichoduZakaznikov_ = generatorPrichoduZakaznikov;
        this.kolkoZakaznikovMaPrist_ = kolkoZakaznikovMaPrist;


    }

    @Override
    public void vykonaj() {

        if (casUdalosti_ < simulacia_.getKoncovyCasReplikacie()) {
            restauracia_.pridajPrichadzajucichZakaznikov(kolkoZakaznikovMaPrist_);
            Stol stolPreZakaznikov = restauracia_.getNajmensiVolnyStolPreSkupinuZakaznikov(kolkoZakaznikovMaPrist_);

            if (stolPreZakaznikov == null) {
                // nie je stolPreZakaznikov, zakaznici idu prec
                restauracia_.pridajZakaznikovCoOdisli(kolkoZakaznikovMaPrist_);
            } else {
                stolPreZakaznikov.clear();

                for (int indexZakaznika = 0; indexZakaznika < kolkoZakaznikovMaPrist_; indexZakaznika++) {
                    Zakaznik zakaznik = new Zakaznik(restauracia_.priradDalsieIdZakaznikovi());
                    zakaznik.setCasVstupuDoRestauracie(casUdalosti_);
                    stolPreZakaznikov.pridajZakaznika(zakaznik );
                }

                if (restauracia_.jeNejakyCasnikVolny()) {

                    Casnik casnikObsluhujuciStol = restauracia_.obsadCasnika(stolPreZakaznikov, TypCinnosti.PRIJATIE_OBJEDNAVKY_A_USADENIE);

                    stolPreZakaznikov.setStavStola_(StolStav.OBJEDNAVANIE);
                    simulacia_.planujUdalost(new ZaciatokUsadenia(casUdalosti_, simulacia_, stolPreZakaznikov, casnikObsluhujuciStol));

                } else {

                    stolPreZakaznikov.setCasZaradeniaDoFrontuUsadenie(casUdalosti_);
                    restauracia_.pridajStolCakajuciNaObjednavkuAUsadenie(stolPreZakaznikov);
                }

            }

            simulacia_.planujUdalost(this.setCasUdalosti(casUdalosti_ + generatorPrichoduZakaznikov_.generuj()));
        }


    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.PRICHOD_ZAKAZNIKA;
    }

    @Override
    public String getDodatocneInfo() {
        return "Kolko zakaznikov ma prist: " + kolkoZakaznikovMaPrist_;
    }

}
