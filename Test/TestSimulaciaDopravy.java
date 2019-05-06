import Model.Enumeracie.PREVADZKA_LINIEK;
import Model.Enumeracie.TYP_LINKY;
import Model.Enumeracie.TYP_VOZIDLA;
import Model.Info.BehSimulacieInfo;
import Model.Info.KonfiguraciaVozidiel;
import Model.VozidloKonfiguracia;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;
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
    public void simulacia() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie minibusy.csv", konfiguraciaVozidiel);
        _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
        _simulaciaDopravy.simulate(10000, Double.MAX_VALUE);

        BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();

        behSimulacieInfo.statistiky_.forEach(statistikaInfo -> {
            System.out.println(statistikaInfo.getNazovStatistiky() + Helper.DEFAULT_SEPARATOR + statistikaInfo.getHodnotaStatistiky());
        });
        behSimulacieInfo.statistikyZastavky_.forEach(statistikaInfo -> {
            System.out.println(statistikaInfo.getNazovStatistiky() + Helper.DEFAULT_SEPARATOR + statistikaInfo.getHodnotaStatistiky());
        });
        behSimulacieInfo.statistikyVozidla_.forEach(statistikaInfo -> {
            System.out.println(statistikaInfo.getNazovStatistiky() + Helper.DEFAULT_SEPARATOR + statistikaInfo.getHodnotaStatistiky());
        });
        System.out.println(_simulaciaDopravy.getKonfiguraciaVozidiel().getCsvFormat());
    }



    @Test
    public void simulaciaZoSuboruJedenVon() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie iba 1 typ po nastupeni caka final.csv", konfiguraciaVozidiel);
        for (int indexToRemove = 0; indexToRemove < konfiguraciaVozidiel.getKonfiguraciaVozidiel().size(); indexToRemove++) {
            KonfiguraciaVozidiel upravenaKonfiguracia = new KonfiguraciaVozidiel();
            upravenaKonfiguracia.setPrevadzkaLiniek(konfiguraciaVozidiel.getPrevadzkaLiniek());
            ArrayList<VozidloKonfiguracia> konfiguracie = new ArrayList<>(konfiguraciaVozidiel.getKonfiguraciaVozidiel());
            konfiguracie.remove(indexToRemove);

            upravenaKonfiguracia.setKonfiguraciaVozidiel(konfiguracie);
            _simulaciaDopravy.setKonfiguracia(upravenaKonfiguracia);
            _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
            BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();
            System.out.println("vozidlo von: " + Integer.valueOf(indexToRemove + 1) );
            System.out.println("cas cakania: " + behSimulacieInfo._priemernyCasCakaniaNaZastavke);
            System.out.println("percento: " + behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku);
            if (behSimulacieInfo._priemernyCasCakaniaNaZastavke < 600) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            //_simulaciaDopravy.zapisVysledokSimulacieDoSuboru("jedenVon.csv");
        }
    }

    @Test
    public void simulaciaZoSuboruZamenaZatyp2() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie iba 1 typ po nastupeni caka final.csv", konfiguraciaVozidiel);
        for (int indexToRemove = 0; indexToRemove < konfiguraciaVozidiel.getKonfiguraciaVozidiel().size(); indexToRemove++) {
            KonfiguraciaVozidiel upravenaKonfiguracia = new KonfiguraciaVozidiel();
            upravenaKonfiguracia.setPrevadzkaLiniek(konfiguraciaVozidiel.getPrevadzkaLiniek());
            ArrayList<VozidloKonfiguracia> konfiguracie = new ArrayList<>(konfiguraciaVozidiel.getKonfiguraciaVozidiel());
            if (konfiguracie.get(indexToRemove).getTypVozidla() == TYP_VOZIDLA.AUTOBUS_TYP_2) {
                continue;
            }
            konfiguracie.get(indexToRemove).setTypVozidla(TYP_VOZIDLA.AUTOBUS_TYP_2);

            upravenaKonfiguracia.setKonfiguraciaVozidiel(konfiguracie);
            _simulaciaDopravy.setKonfiguracia(upravenaKonfiguracia);
            _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
            konfiguracie.get(indexToRemove).setTypVozidla(TYP_VOZIDLA.AUTOBUS_TYP_1);
            BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();
            System.out.println("vozidlo zmena: " + Integer.valueOf(indexToRemove + 1) );
            System.out.println("cas cakania: " + behSimulacieInfo._priemernyCasCakaniaNaZastavke);
            System.out.println("percento: " + behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku);
            if (behSimulacieInfo._priemernyCasCakaniaNaZastavke < 600 && behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku < 7) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            //_simulaciaDopravy.zapisVysledokSimulacieDoSuboru("jedenVon.csv");
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
    public void generatorPociatocnychRieseniPoNastupeniOdchadza() {
        int pocetIteracii = 0;
        final int A = 0;
        final int B = 1;
        final int C = 2;
        int[] casPrichoduPrvehoZakaznika = {6, 324, 0};
        int[] casKedyZoberiePoslednehoCestujucehoZ1Zastavky = {3852, 4146, 3468};


        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        for (int pocetVozidielLinkaA = 11; pocetVozidielLinkaA <= 11; pocetVozidielLinkaA++) {
            for (int pocetVozidielZaciatokLinkaA = 1; pocetVozidielZaciatokLinkaA <= 3; pocetVozidielZaciatokLinkaA++) {

                for (int pocetVozidielLinkaB = 4; pocetVozidielLinkaB <= 4; pocetVozidielLinkaB++) {
                    for (int pocetVozidielZaciatokLinkaB = 1; pocetVozidielZaciatokLinkaB <= 2; pocetVozidielZaciatokLinkaB++) {

                        for (int pocetVozidielLinkaC = 7; pocetVozidielLinkaC <= 7; pocetVozidielLinkaC++) {
                            for (int pocetVozidielZaciatokLinkaC = 1; pocetVozidielZaciatokLinkaC <= 3; pocetVozidielZaciatokLinkaC++) {
                                int celkovyPocetVozidiel = pocetVozidielLinkaA + pocetVozidielLinkaB + pocetVozidielLinkaC;
                                if (celkovyPocetVozidiel < 21) {
                                    break;
                                }
                                ArrayList<VozidloKonfiguracia> konfiguracie = new ArrayList<>();

                                int indexVozidielA = 0;
                                int indexVozidielB = pocetVozidielLinkaA;
                                int indexVozidielC = indexVozidielB + pocetVozidielLinkaB;


                                for (int i = 0; i < pocetVozidielLinkaA; i++) {
                                    konfiguracie.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_A, 0));
                                }
                                for (int i = 0; i < pocetVozidielLinkaB; i++) {
                                    konfiguracie.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_B, 0));

                                }
                                for (int i = 0; i < pocetVozidielLinkaC; i++) {
                                    konfiguracie.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_C, 0));

                                }

                                for (int posunPociatocnehoCasu = 0; posunPociatocnehoCasu <= 180; posunPociatocnehoCasu+= 60) {

                                    for (int posunCasuMedziOdchodmiAutobusovZaciatok = 120; posunCasuMedziOdchodmiAutobusovZaciatok <= 480; posunCasuMedziOdchodmiAutobusovZaciatok+= 120) {

                                        for (int posunCasuMedziOdchodmiAutobusovPoZaciatku = 120; posunCasuMedziOdchodmiAutobusovPoZaciatku <= 840; posunCasuMedziOdchodmiAutobusovPoZaciatku+= 180) {

                                            konfiguracie.get(indexVozidielA).setCasPrijazduNaPrvuZastavku(casPrichoduPrvehoZakaznika[A] + posunPociatocnehoCasu);
                                            konfiguracie.get(indexVozidielB).setCasPrijazduNaPrvuZastavku(casPrichoduPrvehoZakaznika[B] + posunPociatocnehoCasu);
                                            konfiguracie.get(indexVozidielC).setCasPrijazduNaPrvuZastavku(casPrichoduPrvehoZakaznika[C] + posunPociatocnehoCasu);

                                            for (int indexVozidlaLinkaA = indexVozidielA + 1; indexVozidlaLinkaA < indexVozidielA + pocetVozidielZaciatokLinkaA; indexVozidlaLinkaA++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaA - 1);

                                                VozidloKonfiguracia konfiguraciaA = konfiguracie.get( indexVozidlaLinkaA);
                                                konfiguraciaA.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovZaciatok);
                                                if (konfiguraciaA.getTypLinky() != TYP_LINKY.LINKA_A) {
                                                    throw new RuntimeException("Chyba A");
                                                }
                                            }
                                            boolean prvyKratAPo = false;
                                            for (int indexVozidlaLinkaA = indexVozidielA + pocetVozidielZaciatokLinkaA; indexVozidlaLinkaA < pocetVozidielLinkaA; indexVozidlaLinkaA++) {

                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaA - 1);

                                                VozidloKonfiguracia konfiguraciaA = konfiguracie.get(indexVozidlaLinkaA);
                                                konfiguraciaA.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovPoZaciatku + (!prvyKratAPo ? 240 : 0));
                                                prvyKratAPo = true;
                                                if (konfiguraciaA.getTypLinky() != TYP_LINKY.LINKA_A) {
                                                    throw new RuntimeException("Chyba A");
                                                }
                                            }



                                            for (int indexVozidlaLinkaB = indexVozidielB + 1; indexVozidlaLinkaB < indexVozidielB + pocetVozidielZaciatokLinkaB; indexVozidlaLinkaB++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaB - 1);

                                                VozidloKonfiguracia konfiguraciaB = konfiguracie.get(indexVozidlaLinkaB);
                                                konfiguraciaB.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovZaciatok);
                                                if (konfiguraciaB.getTypLinky() != TYP_LINKY.LINKA_B) {
                                                    throw new RuntimeException("Chyba B");
                                                }
                                            }

                                            boolean prvyKratBpo = false;
                                            for (int indexVozidlaLinkaB = indexVozidielB + pocetVozidielZaciatokLinkaB; indexVozidlaLinkaB < indexVozidielB + pocetVozidielLinkaB; indexVozidlaLinkaB++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaB - 1);
                                                VozidloKonfiguracia konfiguraciaB = konfiguracie.get(indexVozidlaLinkaB);
                                                konfiguraciaB.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovPoZaciatku + (!prvyKratBpo ? 240 : 0));

                                                prvyKratBpo = true;
                                                if (konfiguraciaB.getTypLinky() != TYP_LINKY.LINKA_B) {
                                                    throw new RuntimeException("Chyba B");
                                                }
                                            }


                                            for (int indexVozidlaLinkaC = indexVozidielC + 1; indexVozidlaLinkaC < indexVozidielC + pocetVozidielZaciatokLinkaC; indexVozidlaLinkaC++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaC - 1);

                                                VozidloKonfiguracia konfiguraciaC = konfiguracie.get(indexVozidlaLinkaC);
                                                konfiguraciaC.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovZaciatok);
                                                if (konfiguraciaC.getTypLinky() != TYP_LINKY.LINKA_C) {
                                                    throw new RuntimeException("Chyba C");
                                                }

                                            }

                                            boolean prvyKratCPo = false;
                                            for (int indexVozidlaLinkaC = indexVozidielC + pocetVozidielZaciatokLinkaC; indexVozidlaLinkaC < indexVozidielC + pocetVozidielLinkaC; indexVozidlaLinkaC++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaC - 1);

                                                VozidloKonfiguracia konfiguraciaC = konfiguracie.get(indexVozidlaLinkaC);
                                                konfiguraciaC.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovPoZaciatku + (!prvyKratCPo ? 240 : 0));
                                                prvyKratCPo = true;
                                                if (konfiguraciaC.getTypLinky() != TYP_LINKY.LINKA_C) {
                                                    throw new RuntimeException("Chyba C");
                                                }

                                            }

                                            konfiguraciaVozidiel.setKonfiguraciaVozidiel(konfiguracie);
                                            konfiguraciaVozidiel.setPrevadzkaLiniek(PREVADZKA_LINIEK.PO_NASTUPENI_ODCHADZA);
                                            _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);

                                            _simulaciaDopravy.simulate(50, Helper.CASOVE_JEDNOTKY.HODINA.getPocetSekund() * 10);
                                            _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("generatorRieseniPoNastupeniOdchadza22Vozidiel.csv");

                                            System.out.println("Iteracia: " + pocetIteracii);
                                            pocetIteracii++;
                                        }
                                    }
                                }

                            }
                        }

                    }
                }
            }
        }
        System.out.println("Pocet iteracii: " + pocetIteracii);
    }

    @Test
    public void generatorPociatocnychRieseniPoNastupeniCaka() {
        int pocetIteracii = 0;
        final int A = 0;
        final int B = 1;
        final int C = 2;
        int[] casPrichoduPrvehoZakaznika = {6, 324, 0};
        int[] casKedyZoberiePoslednehoCestujucehoZ1Zastavky = {3852, 4146, 3468};


        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        for (int pocetVozidielLinkaA = 11; pocetVozidielLinkaA <= 11; pocetVozidielLinkaA++) {
            for (int pocetVozidielZaciatokLinkaA = 1; pocetVozidielZaciatokLinkaA <= 3; pocetVozidielZaciatokLinkaA++) {

                for (int pocetVozidielLinkaB = 4; pocetVozidielLinkaB <= 4; pocetVozidielLinkaB++) {
                    for (int pocetVozidielZaciatokLinkaB = 1; pocetVozidielZaciatokLinkaB <= 2; pocetVozidielZaciatokLinkaB++) {

                        for (int pocetVozidielLinkaC = 7; pocetVozidielLinkaC <= 7; pocetVozidielLinkaC++) {
                            for (int pocetVozidielZaciatokLinkaC = 1; pocetVozidielZaciatokLinkaC <= 3; pocetVozidielZaciatokLinkaC++) {
                                int celkovyPocetVozidiel = pocetVozidielLinkaA + pocetVozidielLinkaB + pocetVozidielLinkaC;
                                if (celkovyPocetVozidiel < 21) {
                                    break;
                                }
                                ArrayList<VozidloKonfiguracia> konfiguracie = new ArrayList<>();

                                int indexVozidielA = 0;
                                int indexVozidielB = pocetVozidielLinkaA;
                                int indexVozidielC = indexVozidielB + pocetVozidielLinkaB;


                                for (int i = 0; i < pocetVozidielLinkaA; i++) {
                                    konfiguracie.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_A, 0));
                                }
                                for (int i = 0; i < pocetVozidielLinkaB; i++) {
                                    konfiguracie.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_B, 0));

                                }
                                for (int i = 0; i < pocetVozidielLinkaC; i++) {
                                    konfiguracie.add(new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, TYP_LINKY.LINKA_C, 0));

                                }

                                for (int posunPociatocnehoCasu = 0; posunPociatocnehoCasu <= 180; posunPociatocnehoCasu+= 60) {

                                    for (int posunCasuMedziOdchodmiAutobusovZaciatok = 120; posunCasuMedziOdchodmiAutobusovZaciatok <= 480; posunCasuMedziOdchodmiAutobusovZaciatok+= 120) {

                                        for (int posunCasuMedziOdchodmiAutobusovPoZaciatku = 120; posunCasuMedziOdchodmiAutobusovPoZaciatku <= 840; posunCasuMedziOdchodmiAutobusovPoZaciatku+= 180) {

                                            konfiguracie.get(indexVozidielA).setCasPrijazduNaPrvuZastavku(casPrichoduPrvehoZakaznika[A] + posunPociatocnehoCasu);
                                            konfiguracie.get(indexVozidielB).setCasPrijazduNaPrvuZastavku(casPrichoduPrvehoZakaznika[B] + posunPociatocnehoCasu);
                                            konfiguracie.get(indexVozidielC).setCasPrijazduNaPrvuZastavku(casPrichoduPrvehoZakaznika[C] + posunPociatocnehoCasu);

                                            for (int indexVozidlaLinkaA = indexVozidielA + 1; indexVozidlaLinkaA < indexVozidielA + pocetVozidielZaciatokLinkaA; indexVozidlaLinkaA++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaA - 1);

                                                VozidloKonfiguracia konfiguraciaA = konfiguracie.get( indexVozidlaLinkaA);
                                                konfiguraciaA.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovZaciatok);
                                                if (konfiguraciaA.getTypLinky() != TYP_LINKY.LINKA_A) {
                                                    throw new RuntimeException("Chyba A");
                                                }
                                            }
                                            boolean prvyKratAPo = false;
                                            for (int indexVozidlaLinkaA = indexVozidielA + pocetVozidielZaciatokLinkaA; indexVozidlaLinkaA < pocetVozidielLinkaA; indexVozidlaLinkaA++) {

                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaA - 1);

                                                VozidloKonfiguracia konfiguraciaA = konfiguracie.get(indexVozidlaLinkaA);
                                                konfiguraciaA.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovPoZaciatku + (!prvyKratAPo ? 240 : 0));
                                                prvyKratAPo = true;
                                                if (konfiguraciaA.getTypLinky() != TYP_LINKY.LINKA_A) {
                                                    throw new RuntimeException("Chyba A");
                                                }
                                            }



                                            for (int indexVozidlaLinkaB = indexVozidielB + 1; indexVozidlaLinkaB < indexVozidielB + pocetVozidielZaciatokLinkaB; indexVozidlaLinkaB++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaB - 1);

                                                VozidloKonfiguracia konfiguraciaB = konfiguracie.get(indexVozidlaLinkaB);
                                                konfiguraciaB.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovZaciatok);
                                                if (konfiguraciaB.getTypLinky() != TYP_LINKY.LINKA_B) {
                                                    throw new RuntimeException("Chyba B");
                                                }
                                            }

                                            boolean prvyKratBpo = false;
                                            for (int indexVozidlaLinkaB = indexVozidielB + pocetVozidielZaciatokLinkaB; indexVozidlaLinkaB < indexVozidielB + pocetVozidielLinkaB; indexVozidlaLinkaB++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaB - 1);
                                                VozidloKonfiguracia konfiguraciaB = konfiguracie.get(indexVozidlaLinkaB);
                                                konfiguraciaB.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovPoZaciatku + (!prvyKratBpo ? 240 : 0));

                                                prvyKratBpo = true;
                                                if (konfiguraciaB.getTypLinky() != TYP_LINKY.LINKA_B) {
                                                    throw new RuntimeException("Chyba B");
                                                }
                                            }


                                            for (int indexVozidlaLinkaC = indexVozidielC + 1; indexVozidlaLinkaC < indexVozidielC + pocetVozidielZaciatokLinkaC; indexVozidlaLinkaC++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaC - 1);

                                                VozidloKonfiguracia konfiguraciaC = konfiguracie.get(indexVozidlaLinkaC);
                                                konfiguraciaC.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovZaciatok);
                                                if (konfiguraciaC.getTypLinky() != TYP_LINKY.LINKA_C) {
                                                    throw new RuntimeException("Chyba C");
                                                }

                                            }

                                            boolean prvyKratCPo = false;
                                            for (int indexVozidlaLinkaC = indexVozidielC + pocetVozidielZaciatokLinkaC; indexVozidlaLinkaC < indexVozidielC + pocetVozidielLinkaC; indexVozidlaLinkaC++) {
                                                VozidloKonfiguracia predchadzajucaKonfiguracia = konfiguracie.get(indexVozidlaLinkaC - 1);

                                                VozidloKonfiguracia konfiguraciaC = konfiguracie.get(indexVozidlaLinkaC);
                                                konfiguraciaC.setCasPrijazduNaPrvuZastavku(predchadzajucaKonfiguracia.getCasPrijazduNaPrvuZastavku() + posunCasuMedziOdchodmiAutobusovPoZaciatku + (!prvyKratCPo ? 240 : 0));
                                                prvyKratCPo = true;
                                                if (konfiguraciaC.getTypLinky() != TYP_LINKY.LINKA_C) {
                                                    throw new RuntimeException("Chyba C");
                                                }

                                            }

                                            konfiguraciaVozidiel.setKonfiguraciaVozidiel(konfiguracie);
                                            konfiguraciaVozidiel.setPrevadzkaLiniek(PREVADZKA_LINIEK.PO_NASTUPENI_CAKA);
                                            _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);

                                            _simulaciaDopravy.simulate(50, Helper.CASOVE_JEDNOTKY.HODINA.getPocetSekund() * 10);
                                            _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("generatorRieseniPoNastupeniCaka22Vozidiel.csv");

                                            System.out.println("Iteracia: " + pocetIteracii);
                                            pocetIteracii++;
                                        }
                                    }
                                }

                            }
                        }

                    }
                }
            }
        }
        System.out.println("Pocet iteracii: " + pocetIteracii);
    }

    @Test
    public void vylepseniePovodnejAjZmenaLinky() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie.csv", konfiguraciaVozidiel);

        int indexPrvehoAutobusu = 0;
        int indexPoslednehoAutobusu = konfiguraciaVozidiel.getKonfiguraciaVozidiel().size() - 1;

        ArrayList<VozidloKonfiguracia> konfiguraciaOriginalna = new ArrayList<>(konfiguraciaVozidiel.getKonfiguraciaVozidiel());

        int pocetAutobusov = indexPoslednehoAutobusu - indexPrvehoAutobusu + 1;
        System.out.println("Pocet autobusov: " + pocetAutobusov);

        double cakanieNajlepsejKonfiguracie = 0.0;
        konfiguraciaVozidiel.setKonfiguraciaVozidiel(konfiguraciaOriginalna);

        _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
        _simulaciaDopravy.simulate(1000, Double.MAX_VALUE);

        cakanieNajlepsejKonfiguracie = _simulaciaDopravy.getStatistikySimulacie()._priemernyCasCakaniaNaZastavke;
        System.out.println("Povodne cakanie: " + cakanieNajlepsejKonfiguracie);

        int maximalnyPocetIteracii = 100000;

        int iteracia = 0;

        UniformDiscreteRNG generatorVyberVozidla = new UniformDiscreteRNG(indexPrvehoAutobusu, indexPoslednehoAutobusu);
        ArrayList<VozidloKonfiguracia> doposialNajlepsiaKonfiguracia = new ArrayList<>(konfiguraciaOriginalna);

        while (iteracia <= maximalnyPocetIteracii) {
            System.out.println("Iteracia: " + iteracia);
            int indexMinibusu = generatorVyberVozidla.sample();
            System.out.println("Index vozidla: " + indexMinibusu);
            ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(doposialNajlepsiaKonfiguracia);
            cakanieNajlepsejKonfiguracie = Double.MAX_VALUE;

            VozidloKonfiguracia vozidloKtoremuCasBudememePosuvat = new VozidloKonfiguracia(novaKonfiguracia.get(indexMinibusu));
            novaKonfiguracia.set(indexMinibusu, vozidloKtoremuCasBudememePosuvat);

            for (TYP_LINKY typLinky: TYP_LINKY.values()) {
                for (int casPrijazduNaPrvu = 0; casPrijazduNaPrvu < 4000; casPrijazduNaPrvu += 60) {
                    vozidloKtoremuCasBudememePosuvat.setCasPrijazduNaPrvuZastavku(casPrijazduNaPrvu);
                    vozidloKtoremuCasBudememePosuvat.setTypLinky(typLinky);
                    konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);

                    _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                    _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
                    BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();
                    if (behSimulacieInfo._priemernyCasCakaniaNaZastavke < cakanieNajlepsejKonfiguracie && behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku < 7) {
                        cakanieNajlepsejKonfiguracie = behSimulacieInfo._priemernyCasCakaniaNaZastavke;
                        doposialNajlepsiaKonfiguracia = new ArrayList<>(novaKonfiguracia);
                        System.out.println("Nova konfiguracia, cakanie:  " + cakanieNajlepsejKonfiguracie);

                        _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("vychodzieHladanieKonfiguracieAjZmenaLinky.csv");
                    }
                    iteracia++;
                }

            }

        }


    }

    @Test
    public void vylepsenieVychodzejAjZmenaLinky() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie iba 1 typ po nastupeni caka final.csv", konfiguraciaVozidiel);

        int indexPrvehoAutobusu = 0;
        int indexPoslednehoAutobusu = konfiguraciaVozidiel.getKonfiguraciaVozidiel().size() - 1;

        ArrayList<VozidloKonfiguracia> konfiguraciaOriginalna = new ArrayList<>(konfiguraciaVozidiel.getKonfiguraciaVozidiel());

        int pocetAutobusov = indexPoslednehoAutobusu - indexPrvehoAutobusu + 1;
        System.out.println("Pocet autobusov: " + pocetAutobusov);

        double cakanieNajlepsejKonfiguracie = 0.0;
        konfiguraciaVozidiel.setKonfiguraciaVozidiel(konfiguraciaOriginalna);

        _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
        _simulaciaDopravy.simulate(1000, Double.MAX_VALUE);

        cakanieNajlepsejKonfiguracie = _simulaciaDopravy.getStatistikySimulacie()._priemernyCasCakaniaNaZastavke;
        System.out.println("Povodne cakanie: " + cakanieNajlepsejKonfiguracie);

        int maximalnyPocetIteracii = 100000;

        int iteracia = 0;

        UniformDiscreteRNG generatorVyberVozidla = new UniformDiscreteRNG(indexPrvehoAutobusu, indexPoslednehoAutobusu);
        ArrayList<VozidloKonfiguracia> doposialNajlepsiaKonfiguracia = new ArrayList<>(konfiguraciaOriginalna);

        while (iteracia <= maximalnyPocetIteracii) {
            System.out.println("Iteracia: " + iteracia);
            int indexMinibusu = generatorVyberVozidla.sample();
            System.out.println("Index vozidla: " + indexMinibusu);
            ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(konfiguraciaOriginalna);
            cakanieNajlepsejKonfiguracie = Double.MAX_VALUE;

            VozidloKonfiguracia vozidloKtoremuCasBudememePosuvat = new VozidloKonfiguracia(novaKonfiguracia.get(indexMinibusu));
            novaKonfiguracia.set(indexMinibusu, vozidloKtoremuCasBudememePosuvat);

            for (TYP_LINKY typLinky: TYP_LINKY.values()) {
                for (int casPrijazduNaPrvu = 0; casPrijazduNaPrvu < 4000; casPrijazduNaPrvu += 60) {
                    vozidloKtoremuCasBudememePosuvat.setCasPrijazduNaPrvuZastavku(casPrijazduNaPrvu);
                    vozidloKtoremuCasBudememePosuvat.setTypLinky(typLinky);
                    konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);

                    _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                    _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
                    BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();
                    if (behSimulacieInfo._priemernyCasCakaniaNaZastavke < cakanieNajlepsejKonfiguracie && behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku < 7) {
                        cakanieNajlepsejKonfiguracie = behSimulacieInfo._priemernyCasCakaniaNaZastavke;
                        doposialNajlepsiaKonfiguracia = new ArrayList<>(novaKonfiguracia);
                        System.out.println("Nova konfiguracia, cakanie:  " + cakanieNajlepsejKonfiguracie);

                        _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("vychodzieHladanieKonfiguraciePoCaseZmenaLinky.csv");
                    }
                    iteracia++;
                }

            }

        }


    }

    @Test
    public void nasadenieMinibusovPridanieMinibusov() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie.csv", konfiguraciaVozidiel);
        _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
        ArrayList<VozidloKonfiguracia> konfiguraciaOriginalna = konfiguraciaVozidiel.getKonfiguraciaVozidiel();

        double najZisk = -1;
        for (int pocetMinubusov = 1; pocetMinubusov <= 13; pocetMinubusov++) {


            VozidloKonfiguracia noveVozidlo = new VozidloKonfiguracia(TYP_VOZIDLA.MINIBUS, null, 0);
            VozidloKonfiguracia najVozidlo = new VozidloKonfiguracia(noveVozidlo);
            for (TYP_LINKY typLinky: TYP_LINKY.values()) {
                noveVozidlo.setTypLinky(typLinky);

                for (int pociatocnyCas = 0; pociatocnyCas <= 4000; pociatocnyCas+= 60) {
                    noveVozidlo.setCasPrijazduNaPrvuZastavku(pociatocnyCas);
                    ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(konfiguraciaOriginalna);
                    novaKonfiguracia.add(noveVozidlo);
                    konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);
                    _simulaciaDopravy.simulate(40, Double.MAX_VALUE);

                    BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();
                    if (najZisk < behSimulacieInfo._ziskMinibusov) {
                        najZisk = behSimulacieInfo._ziskMinibusov;
                        najVozidlo = new VozidloKonfiguracia(noveVozidlo);
                        System.out.println("Naj zisk: " + najZisk);
                        System.out.println("Konfig: " + najVozidlo.toString());
                    }
                    if (najZisk >= (pocetMinubusov * 2 * 8.0) ) {
                        break;
                    }

                }
                if (najZisk >= (pocetMinubusov * 2 * 8.0) ) {
                    break;
                }
            }
            System.out.println("MinibusNaPridanie : " + najVozidlo.toString());
            System.out.println("Naj zisk: " + najZisk);
            konfiguraciaOriginalna.add(najVozidlo);
            System.out.println(konfiguraciaVozidiel.getCsvFormat());
        }

    }




    @Test
    public void nasadenieMinibusovUprava() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie minibusy.csv", konfiguraciaVozidiel);

        int indexPrvehoMinibusu = -1;
        int indexPoslednehoMinibusu = -1;

        ArrayList<VozidloKonfiguracia> konfiguraciaOriginalna = new ArrayList<>(konfiguraciaVozidiel.getKonfiguraciaVozidiel());
        for (int indexMinibusu = 0; indexMinibusu < konfiguraciaOriginalna.size(); indexMinibusu++) {
            if (!konfiguraciaOriginalna.get(indexMinibusu).getTypVozidla().isAutobus()) {
                if (indexPrvehoMinibusu == -1) {
                    indexPrvehoMinibusu = indexMinibusu;
                }
                indexPoslednehoMinibusu = indexMinibusu;
            }
        }
        int pocetMinibusov = indexPoslednehoMinibusu - indexPrvehoMinibusu + 1;
        System.out.println("Pocet minibusov: " + pocetMinibusov);

        double ziskNajlepsejKonfiguracie = 0.0;
        konfiguraciaVozidiel.setKonfiguraciaVozidiel(konfiguraciaOriginalna);

        _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
        _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
        
         ziskNajlepsejKonfiguracie = _simulaciaDopravy.getStatistikySimulacie()._ziskMinibusov;
        System.out.println("Povodny zisk: " + ziskNajlepsejKonfiguracie);

         int maximalnyPocetIteracii = 100000;
         
         int iteracia = 1;

        UniformDiscreteRNG generatorVyberVozidla = new UniformDiscreteRNG(indexPrvehoMinibusu, indexPoslednehoMinibusu);


        ArrayList<VozidloKonfiguracia> doposialNajlepsiaKonfiguracia = new ArrayList<>(konfiguraciaOriginalna);

        for (int indexMinibusu = indexPrvehoMinibusu; indexMinibusu <= indexPoslednehoMinibusu; indexMinibusu++) {

            System.out.println("Index minibusu: " + indexMinibusu);
            ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(doposialNajlepsiaKonfiguracia);
            VozidloKonfiguracia vozidloKtoremuCasBudememePosuvat = new VozidloKonfiguracia(novaKonfiguracia.get(indexMinibusu));
            novaKonfiguracia.set(indexMinibusu, vozidloKtoremuCasBudememePosuvat);



            for (int casPrijazduNaPrvu = 60; casPrijazduNaPrvu < 4000; casPrijazduNaPrvu += 60) {
                for (TYP_LINKY typLinky: TYP_LINKY.values()) {
                    vozidloKtoremuCasBudememePosuvat.setCasPrijazduNaPrvuZastavku(casPrijazduNaPrvu);
                    vozidloKtoremuCasBudememePosuvat.setTypLinky(typLinky);
                    //vozidloKtoremuCasBudememePosuvat.setTypLinky(typLinky);
                    konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);
                    _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                    _simulaciaDopravy.simulate(100, Double.MAX_VALUE);
                    BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();
                    if (behSimulacieInfo._ziskMinibusov > ziskNajlepsejKonfiguracie) {
                        ziskNajlepsejKonfiguracie = behSimulacieInfo._ziskMinibusov;
                        doposialNajlepsiaKonfiguracia = new ArrayList<>(novaKonfiguracia);
                        System.out.println("Nova konfiguracia, zisk " + ziskNajlepsejKonfiguracie);

                        System.out.println(_simulaciaDopravy.getKonfiguraciaVozidiel().getCsvFormat());
                        //_simulaciaDopravy.zapisVysledokSimulacieDoSuboru("minibusyHladanieKonfiguracie.csv");

                    }
                }

            }
        }

        /*
        while (iteracia <= maximalnyPocetIteracii) {
             System.out.println("Iteracia: " + iteracia);
             int indexMinibusu = generatorVyberVozidla.sample();
             System.out.println("Index minibusu: " + indexMinibusu);
             ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(doposialNajlepsiaKonfiguracia); 
             VozidloKonfiguracia vozidloKtoremuCasBudememePosuvat = new VozidloKonfiguracia(novaKonfiguracia.get(indexMinibusu));
             novaKonfiguracia.set(indexMinibusu, vozidloKtoremuCasBudememePosuvat);

             for (int casPrijazduNaPrvu = 60; casPrijazduNaPrvu < 4000; casPrijazduNaPrvu += 60) {
                 vozidloKtoremuCasBudememePosuvat.setCasPrijazduNaPrvuZastavku(casPrijazduNaPrvu);

                for (TYP_LINKY typLinky: TYP_LINKY.values()) {
                    vozidloKtoremuCasBudememePosuvat.setTypLinky(typLinky);
                    konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);
                    _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                    _simulaciaDopravy.simulate(50, Double.MAX_VALUE);
                    BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();
                    if (behSimulacieInfo._ziskMinibusov > ziskNajlepsejKonfiguracie) {
                        ziskNajlepsejKonfiguracie = behSimulacieInfo._ziskMinibusov;
                        doposialNajlepsiaKonfiguracia = new ArrayList<>(novaKonfiguracia);
                        System.out.println("Nova konfiguracia, zisk " + ziskNajlepsejKonfiguracie);

                        System.out.println(_simulaciaDopravy.getKonfiguraciaVozidiel().getCsvFormat());
                        //_simulaciaDopravy.zapisVysledokSimulacieDoSuboru("minibusyHladanieKonfiguracie.csv");
                    }
                }
             }
            iteracia++;    
         }
         */
         
        
    }

    @Test
    public void pridanie1VozidlaNaLinku() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie iba 1 typ po nastupeni caka.csv", konfiguraciaVozidiel);
        konfiguraciaVozidiel.setPrevadzkaLiniek(PREVADZKA_LINIEK.PO_NASTUPENI_CAKA);
        ArrayList<VozidloKonfiguracia> originalnaKonfuguracia = konfiguraciaVozidiel.getKonfiguraciaVozidiel();

        double najnizsiCas = Double.MAX_VALUE;
        double najnizsiePercento = Double.MAX_VALUE;

        int iteracia = 0;
        for (TYP_LINKY typLinky: TYP_LINKY.values()) {
            System.out.println("Linka " + typLinky.getNazovLinky());
            for (int casPrijazdu = 0; casPrijazdu < 3000; casPrijazdu+= 60) {
                System.out.println("Iteracia: " + iteracia);
                VozidloKonfiguracia vozidloKonfiguracia = new VozidloKonfiguracia(TYP_VOZIDLA.AUTOBUS_TYP_1, typLinky, casPrijazdu);
                ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(originalnaKonfuguracia);
                novaKonfiguracia.add(vozidloKonfiguracia);
                konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);
                _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                _simulaciaDopravy.simulate(100, Double.MAX_VALUE);

                BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();
                if (najnizsiePercento > behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku) {
                    najnizsiCas = behSimulacieInfo._priemernyCasCakaniaNaZastavke;
                    najnizsiePercento = behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku;
                    System.out.println("najnizsiCas: " + najnizsiCas);
                    System.out.println("po zapase %: " + behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku);
                    System.out.println("Konfig: " + typLinky.getNazovLinky() + ", cas prijazdu: " + casPrijazdu + ", " + Helper.FormatujSimulacnyCas(casPrijazdu));

                }
                _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("pridanie1VozidlaZVychodzej1Typ.csv");
                iteracia++;
            }
        }
        System.out.println("Pocet iteracii: " + iteracia);
    }

    @Test
    public void vylepsenieVychodzejPoNastupeCaka() {
        KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel();
        _simulaciaDopravy.nacitajKonfiguraciuVozidiel("vychodzie iba 1 typ po nastupeni caka.csv", konfiguraciaVozidiel);

        int indexPrvehoAutobusu = 0;
        int indexPoslednehoAutobusu = konfiguraciaVozidiel.getKonfiguraciaVozidiel().size() - 1;

        ArrayList<VozidloKonfiguracia> konfiguraciaOriginalna = new ArrayList<>(konfiguraciaVozidiel.getKonfiguraciaVozidiel());

        int pocetAutobusov = indexPoslednehoAutobusu - indexPrvehoAutobusu + 1;
        System.out.println("Pocet autobusov: " + pocetAutobusov);

        double cakanieNajlepsejKonfiguracie = 0.0;
        konfiguraciaVozidiel.setKonfiguraciaVozidiel(konfiguraciaOriginalna);

        _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
        _simulaciaDopravy.simulate(100, Double.MAX_VALUE);

        cakanieNajlepsejKonfiguracie = _simulaciaDopravy.getStatistikySimulacie()._priemernyCasCakaniaNaZastavke;
        double najlespiePercento = _simulaciaDopravy.getStatistikySimulacie()._percentoCestujuciochPrichadzajucichPoZaciatku;
        System.out.println("Povodne cakanie: " + cakanieNajlepsejKonfiguracie);

        int iteracia = 0;

        UniformDiscreteRNG generatorVyberVozidla = new UniformDiscreteRNG(indexPrvehoAutobusu, indexPoslednehoAutobusu);
        ArrayList<VozidloKonfiguracia> doposialNajlepsiaKonfiguracia = new ArrayList<>(konfiguraciaOriginalna);

        for (int indexMinibusu = 0; indexMinibusu < indexPoslednehoAutobusu; indexMinibusu++) {
            System.out.println("Iteracia: " + iteracia);
            System.out.println("Index vozidla: " + indexMinibusu);
            ArrayList<VozidloKonfiguracia> novaKonfiguracia = new ArrayList<>(konfiguraciaOriginalna);
            VozidloKonfiguracia vozidloKtoremuCasBudememePosuvat = novaKonfiguracia.get(indexMinibusu);
            najlespiePercento = Double.MAX_VALUE;
            for (int casPrijazduNaPrvu = 0; casPrijazduNaPrvu < 3400; casPrijazduNaPrvu += 60) {
                vozidloKtoremuCasBudememePosuvat.setCasPrijazduNaPrvuZastavku(casPrijazduNaPrvu);
                konfiguraciaVozidiel.setKonfiguraciaVozidiel(novaKonfiguracia);
                _simulaciaDopravy.setKonfiguracia(konfiguraciaVozidiel);
                _simulaciaDopravy.simulate(40, Double.MAX_VALUE);
                BehSimulacieInfo behSimulacieInfo = _simulaciaDopravy.getStatistikySimulacie();
                if (behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku < najlespiePercento && behSimulacieInfo._priemernyCasCakaniaNaZastavke < 600) {
                    cakanieNajlepsejKonfiguracie = behSimulacieInfo._priemernyCasCakaniaNaZastavke;
                    najlespiePercento = behSimulacieInfo._percentoCestujuciochPrichadzajucichPoZaciatku;
                    doposialNajlepsiaKonfiguracia = new ArrayList<>(novaKonfiguracia);
                    System.out.println("Nova konfiguracia, cakanie:  " + cakanieNajlepsejKonfiguracie);
                    System.out.println("percento: " + najlespiePercento);
                    System.out.println("KonfiG: " + vozidloKtoremuCasBudememePosuvat);
                    _simulaciaDopravy.zapisVysledokSimulacieDoSuboru("vychodziePoNastupeniCakaUpravaCasov.csv");
                }
            }
            iteracia++;
        }





    }
}
