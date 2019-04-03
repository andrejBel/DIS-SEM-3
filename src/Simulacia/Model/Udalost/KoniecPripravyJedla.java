package Simulacia.Model.Udalost;

import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Casnik;
import Simulacia.Model.Restauracia.Enumeracie.StolStav;
import Simulacia.Model.Restauracia.Enumeracie.TypCinnosti;
import Simulacia.Model.Restauracia.Kuchar;
import Simulacia.Model.Restauracia.Stol;
import Simulacia.Model.Restauracia.Zakaznik;

public class KoniecPripravyJedla extends UdalostRestauracia {

    private Kuchar kuchar_;
    private Zakaznik obsluhovanyZakaznik_;

    public KoniecPripravyJedla(double casUdalosti, SimulacneJadro simulacneJadro, Kuchar kuchar, Zakaznik obsluhovanyZakaznik) {
        super(casUdalosti, simulacneJadro);
        this.kuchar_ = kuchar;
        this.obsluhovanyZakaznik_ = obsluhovanyZakaznik;
    }

    @Override
    public void vykonaj() {

        restauracia_.ustanovKucharaZaVolneho(kuchar_);

        obsluhovanyZakaznik_.setCasVyhotoveniaJedla(casUdalosti_);
        Stol stolZakaznika = obsluhovanyZakaznik_.getStolPridelenyZakaznikovy();
        stolZakaznika.zvysPocetPripravenychJedal();

        if (stolZakaznika.majuVsetciZakazniciNachystaneJedlo()) {

            if (restauracia_.jeNejakyCasnikVolny()) {
                // naplanuj odnesenie hned
                // casnik je volny a moze hned niest, nikto prioritny nie je
                Casnik casnikNaObsluhu = restauracia_.obsadCasnika(stolZakaznika, TypCinnosti.PRINESENIE_JEDAL);
                stolZakaznika.setStavStola_(StolStav.CASNIK_DONASA_JEDLO);
                this.simulacia_.planujUdalost(new ZaciatokOdnesenia(casUdalosti_, simulacia_, stolZakaznika, casnikNaObsluhu));
            } else {
                stolZakaznika.setCasZaradeniaDoFrontuOdneseniaJedal(casUdalosti_);
                restauracia_.pridajStolCakajuciNaDonesenieJedla(stolZakaznika);
            }
        }
        // kuchar sa musi priradit, ak ma co
        while (restauracia_.jeNejakyKucharVolny() && restauracia_.suCinnostiPreKuchara()) {
            Zakaznik zakaznikNaObsluhu = restauracia_.getZakaznikaZFrontuJedal();
            Kuchar kucharNaPracu = this.restauracia_.obsadKuchara(zakaznikNaObsluhu);
            this.simulacia_.planujUdalost(new ZaciatokPripravyJedla(casUdalosti_, simulacia_, kucharNaPracu, zakaznikNaObsluhu));
        }

    }

    @Override
    public TypUdalosti getTypUdalosti() {
        return TypUdalosti.KONIEC_PRIPRAVY_JEDAL;
    }

    @Override
    public String getDodatocneInfo() {
        return "Kuchar č. " + kuchar_.getIndexKuchara() + ", zakaznik č." + obsluhovanyZakaznik_.getIdZakaznika() + ", stol: " + obsluhovanyZakaznik_.getStolPridelenyZakaznikovy().getIdStolu();
    }
}
