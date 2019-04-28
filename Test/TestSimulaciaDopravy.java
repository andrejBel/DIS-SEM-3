import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.Enumeracie.TYP_LINKY;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Info.KonfiguraciaVozidiel;
import Model.VozidloKonfiguracia;
import OSPRNG.UniformContinuousRNG;
import Utils.Helper;
import org.junit.Before;
import org.junit.Test;
import simulation.SimulaciaDopravy;

import java.util.ArrayList;
import java.util.Arrays;

public class TestSimulaciaDopravy {

    SimulaciaDopravy _simulaciaDopravy;

    @Before
    public void initialize() {
        _simulaciaDopravy = new SimulaciaDopravy();
    }

    @Test
    public void spustiSimulaciu() {
        for (int maxPocetAutobusov = 3; maxPocetAutobusov < 13; maxPocetAutobusov++) {
            for (int rozdielMedziVozidlami = 30,iteracia= 0; rozdielMedziVozidlami < 60 * 20; rozdielMedziVozidlami += 30, iteracia++) {
                ArrayList<VozidloKonfiguracia> konfiguracie = new ArrayList<>();

                for (TYP_LINKY linka: TYP_LINKY.values()) {
                    double prijazdNaPrvuZastavku = 400;
                    int pocetgenerovanych = 0;
                    while (true) {
                        konfiguracie.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, linka, prijazdNaPrvuZastavku));
                        pocetgenerovanych++;
                        if (pocetgenerovanych >= maxPocetAutobusov) {
                            break;
                        }

                        prijazdNaPrvuZastavku += rozdielMedziVozidlami;
                        if (prijazdNaPrvuZastavku > (_simulaciaDopravy.getCasZaciatkuZapasu() - 600)) {
                            break;
                        }
                    }

                }

                KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel(PREVADZKA_LINIEK.PO_NASTUPENI_ODCHADZA, konfiguracie);
                _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
                //System.out.println(_simulaciaDopravy.getCsvFormatBehSimulacie());
                _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("intervaly.csv");
                System.out.println("Rozdiel medzi casmi: " + rozdielMedziVozidlami);
                System.out.println("Iteracia: " + iteracia);
                System.out.println("Max pocet busov: " + maxPocetAutobusov);
            }
        }
    }


    @Test
    public void simulaciaZoSuboruJedenVon() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie.csv", konfiguraciaVozidiel);
        for (int indexToRemove = 0; indexToRemove < konfiguraciaVozidiel.getKonfiguraciaVozidiel().size(); indexToRemove++) {
            KonfiguraciaVozidiel upravenaKonfiguracia = new KonfiguraciaVozidiel();
            upravenaKonfiguracia.setPrevadzkaLiniek(konfiguraciaVozidiel.getPrevadzkaLiniek());
            ArrayList<VozidloKonfiguracia> konfiguracie = new ArrayList<>(konfiguraciaVozidiel.getKonfiguraciaVozidiel());
            konfiguracie.remove(indexToRemove);

            upravenaKonfiguracia.setKonfiguraciaVozidiel(konfiguracie);
            _simulaciaDopravy.setKonfiguracia(upravenaKonfiguracia);
            _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
            _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("jedenVon.csv");
        }

    }

    @Test
    public void vozidlo17() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie.csv", konfiguraciaVozidiel);



        for (int casZaciatku = 100; casZaciatku <= 3400; casZaciatku += 50) {
            konfiguraciaVozidiel.getKonfiguraciaVozidiel().get(16).setCasPrijazduNaPrvuZastavku(casZaciatku);
            _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
            _simulaciaDopravy.simulate(150, Double.MAX_VALUE);
            _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("vozidlo17.csv");
            if (_simulaciaDopravy.getStatistikySimulacie()._percentoCestujuciochPrichadzajucichPoZaciatku <= 7.0) {
                System.out.println("Nasiel som");
            }

        }
    }

    @Test
    public void linkaBZaciatok() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie.csv", konfiguraciaVozidiel);
        int indexPrvehoC = -1;
        for (int index = 0; index < konfiguraciaVozidiel.getKonfiguraciaVozidiel().size(); index++) {
            if (konfiguraciaVozidiel.getKonfiguraciaVozidiel().get(index).getTypLinky() == TYP_LINKY.LINKA_B) {
                indexPrvehoC = index;
                break;
            }
        }


        for (int casZaciatku = 320; casZaciatku < 900; casZaciatku += 20) {
            konfiguraciaVozidiel.getKonfiguraciaVozidiel().get(indexPrvehoC).setCasPrijazduNaPrvuZastavku(casZaciatku);
            _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
            _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
            _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("zaciatokB.csv");
        }
    }

    @Test
    public void vymena2za3LinkaA() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie iba 1 typ.csv", konfiguraciaVozidiel);
        ArrayList<VozidloKonfiguracia> originalnaKonfuguracia = konfiguraciaVozidiel.getKonfiguraciaVozidiel();
        UniformContinuousRNG uniformContinuousRNG = new UniformContinuousRNG(-50.0, 50.0);
        for (int vonkajsiIndexA = 0; vonkajsiIndexA < 8; vonkajsiIndexA++) {
            for (int vnutornyIndexA = vonkajsiIndexA +1; vnutornyIndexA < 9; vnutornyIndexA++) {

                for (int pocetSkusaniCasu = 0; pocetSkusaniCasu < 50; pocetSkusaniCasu++) {
                    ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(originalnaKonfuguracia);
                    VozidloKonfiguracia naVyhodenie1 = novaKonfiguracia.remove(vonkajsiIndexA);
                    VozidloKonfiguracia naVyhodenie2 = novaKonfiguracia.remove(vnutornyIndexA - 1);
                    novaKonfiguracia.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_2, TYP_LINKY.LINKA_A, naVyhodenie1.getCasPrijazduNaPrvuZastavku() + uniformContinuousRNG.sample()));
                    novaKonfiguracia.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_2, TYP_LINKY.LINKA_A, naVyhodenie2.getCasPrijazduNaPrvuZastavku() + uniformContinuousRNG.sample()));
                    double cas3 = naVyhodenie1.getCasPrijazduNaPrvuZastavku() + naVyhodenie2.getCasPrijazduNaPrvuZastavku() / 2 + uniformContinuousRNG.sample();

                    novaKonfiguracia.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_2, TYP_LINKY.LINKA_A, cas3));

                    konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);
                    _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                    _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
                    _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("vymena2Za3LinkaA.csv");
                }
                System.out.println(String.valueOf(vonkajsiIndexA) + " " + String.valueOf(vnutornyIndexA));
            }
        }
    }

    @Test
    public void vymena2za3LinkaB() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie iba 1 typ.csv", konfiguraciaVozidiel);
        UniformContinuousRNG uniformContinuousRNG = new UniformContinuousRNG(-50.0, 50.0);
        ArrayList<VozidloKonfiguracia> originalnaKonfuguracia = konfiguraciaVozidiel.getKonfiguraciaVozidiel();

        for (int vonkajsiIndexB = 9; vonkajsiIndexB < 12; vonkajsiIndexB++) {
            for (int vnutornyIndexB = vonkajsiIndexB +1; vnutornyIndexB < 13; vnutornyIndexB++) {
                for (int pocetSkusaniCasu = 0; pocetSkusaniCasu < 100; pocetSkusaniCasu++) {
                    ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(originalnaKonfuguracia);
                    VozidloKonfiguracia naVyhodenie1 = novaKonfiguracia.remove(vonkajsiIndexB);
                    VozidloKonfiguracia naVyhodenie2 = novaKonfiguracia.remove(vnutornyIndexB - 1);
                    novaKonfiguracia.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_2, TYP_LINKY.LINKA_B, naVyhodenie1.getCasPrijazduNaPrvuZastavku() + uniformContinuousRNG.sample()));
                    novaKonfiguracia.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_2, TYP_LINKY.LINKA_B, naVyhodenie2.getCasPrijazduNaPrvuZastavku() + uniformContinuousRNG.sample()));
                    double cas3 = naVyhodenie1.getCasPrijazduNaPrvuZastavku() + naVyhodenie2.getCasPrijazduNaPrvuZastavku() / 2 + uniformContinuousRNG.sample();

                    novaKonfiguracia.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_2, TYP_LINKY.LINKA_B, cas3));

                    konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);
                    _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                    _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
                    _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("vymena2Za3LinkaB.csv");
                }

                System.out.println(String.valueOf(vonkajsiIndexB) + " " + String.valueOf(vnutornyIndexB));
            }

            //_simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
            //_simulaciaDopravy.simulate(100, Double.MAX_VALUE);
            //_simulaciaDopravy.zapisVysledokSimulacieDoSuboru("vymena2Za3LinkaB.csv");
        }

    }

    @Test
    public void vymena2za3LinkaC() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie iba 1 typ.csv", konfiguraciaVozidiel);
        UniformContinuousRNG uniformContinuousRNG = new UniformContinuousRNG(-50.0, 50.0);
        ArrayList<VozidloKonfiguracia> originalnaKonfuguracia = konfiguraciaVozidiel.getKonfiguraciaVozidiel();

        for (int vonkajsiIndexC = 13; vonkajsiIndexC < 19; vonkajsiIndexC++) {
            for (int vnutornyIndexC = vonkajsiIndexC +1; vnutornyIndexC < 20; vnutornyIndexC++) {
                for (int pocetSkusaniCasu = 0; pocetSkusaniCasu < 100; pocetSkusaniCasu++) {
                    ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(originalnaKonfuguracia);
                    VozidloKonfiguracia naVyhodenie1 = novaKonfiguracia.remove(vonkajsiIndexC);
                    VozidloKonfiguracia naVyhodenie2 = novaKonfiguracia.remove(vnutornyIndexC - 1);
                    novaKonfiguracia.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_2, TYP_LINKY.LINKA_C, naVyhodenie1.getCasPrijazduNaPrvuZastavku() + uniformContinuousRNG.sample()));
                    novaKonfiguracia.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_2, TYP_LINKY.LINKA_C, naVyhodenie2.getCasPrijazduNaPrvuZastavku() + uniformContinuousRNG.sample()));
                    double cas3 = naVyhodenie1.getCasPrijazduNaPrvuZastavku() + naVyhodenie2.getCasPrijazduNaPrvuZastavku() / 2 + uniformContinuousRNG.sample();

                    novaKonfiguracia.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_2, TYP_LINKY.LINKA_C, cas3));

                    konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);
                    _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                    _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
                    _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("vymena2Za3LinkaC.csv");
                }

                System.out.println(String.valueOf(vonkajsiIndexC) + " " + String.valueOf(vnutornyIndexC));
            }

            //_simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
            //_simulaciaDopravy.simulate(100, Double.MAX_VALUE);
            //_simulaciaDopravy.zapisVysledokSimulacieDoSuboru("vymena2Za3LinkaB.csv");
        }
    }

    @Test
    public void generatorPociatocnychRieseni() {



    }


}
