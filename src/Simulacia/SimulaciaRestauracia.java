package Simulacia;

import GUI.CAS_UPDATU;
import Generatory.*;
import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.*;
import Simulacia.Model.Restauracia.Enumeracie.StolStav;
import Simulacia.Model.Restauracia.Info.*;
import Simulacia.Model.Udalost.*;
import Simulacia.Statistiky.IStatistika;
import Simulacia.Statistiky.StatistikaIntervalSpolahlivosti;
import Simulacia.Statistiky.StatistikaPriemer;
import Simulacia.Udalost.FrontUdalosti;
import Simulacia.Udalost.Udalost;
import Utils.Helper;
import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class SimulaciaRestauracia extends SimulacneJadro {


    private Restauracia restauracia_;
    private StavObjektov stavObjektov_;

    // GENERATORY !!!

    // GENERATOR prichody
    private GeneratorExponencialne generatorPrichodZakaznikaPocet1 = new GeneratorExponencialne(3600.0 / 10.0);
    private GeneratorExponencialne generatorPrichodZakaznikaPocet2 = new GeneratorExponencialne(3600.0 / 8.0);
    private GeneratorExponencialne generatorPrichodZakaznikaPocet3 = new GeneratorExponencialne(3600.0 / 6.0);
    private GeneratorExponencialne generatorPrichodZakaznikaPocet4 = new GeneratorExponencialne(3600.0 / 5.0);
    private GeneratorExponencialne generatorPrichodZakaznikaPocet5 = new GeneratorExponencialne(3600.0 / 3.0);
    private GeneratorExponencialne generatorPrichodZakaznikaPocet6 = new GeneratorExponencialne(3600.0 / 4.0);

    List<GeneratorExponencialne> generatoryPrichodovZakaznika = Arrays.asList(
            generatorPrichodZakaznikaPocet1,
            generatorPrichodZakaznikaPocet2,
            generatorPrichodZakaznikaPocet3,
            generatorPrichodZakaznikaPocet4,
            generatorPrichodZakaznikaPocet5,
            generatorPrichodZakaznikaPocet6
    );

    // GENERATOR objednanie
    public GeneratorRovnomerneSpojite generatorPrevzatiaObjednavky_ = new GeneratorRovnomerneSpojite(45.0, 120.0);

    // generator jedla
    public GeneratorRovnomerneSpojite generatorVyberKtoreJedlo = new GeneratorRovnomerneSpojite();

    public GeneratorRovnomerneDiskretne generatorCeasarSalat = new GeneratorRovnomerneDiskretne(380, 440);

    public GeneratorEmpirickeDiskretne generatorPenneSalat = new GeneratorEmpirickeDiskretne(
            new EmpirickeDiskretneParameter(185, 330, 0.15),
            new EmpirickeDiskretneParameter(331, 630, 0.5),
            new EmpirickeDiskretneParameter(631, 930, 0.35)
    );

    public GeneratorEmpirickeDiskretne generatorCelozrnnaSpageta = new GeneratorEmpirickeDiskretne(
            new EmpirickeDiskretneParameter(290, 356, 0.2),
            new EmpirickeDiskretneParameter(357, 540, 0.43),
            new EmpirickeDiskretneParameter(541, 600, 0.37)
    );

    public final int DOBA_PRIPRAVY_SYTEHO_SALATU = 180;

    // generator dlzka jedenia
    public GeneratorTrojuholnikove generatorDlzkaJedenia_ = new GeneratorTrojuholnikove(3.0 * 60.0, 15.0 * 60.0, 30.0 * 60.0);

    // generator platenie
    public GeneratorRovnomerneSpojite generatorDlzkaPlatenia_ = new GeneratorRovnomerneSpojite(43.0, 97.0);

    // generator presun a donesenie jedla
    public GeneratorRovnomerneSpojite generatorDonesenieJedal_ = new GeneratorRovnomerneSpojite(23.0, 80.0);


    // Statistiky
    //replikacne
    public StatistikaPriemer priemernyCasZakaznikaStravenyCakanimR_ = new StatistikaPriemer("Priemerný čas zákaznika stráveny čakaním");


    // globalne
    public StatistikaIntervalSpolahlivosti priemernyCasZakaznikaStravenyCakanimS_ = new StatistikaIntervalSpolahlivosti("Priemerný čas zákaznika strávený čakaním");
    public StatistikaIntervalSpolahlivosti priemernyPocetZakaznikovS_ = new StatistikaIntervalSpolahlivosti("Priemerný počet zákazníkov");
    public StatistikaIntervalSpolahlivosti priemernyPocetOdchadzajucichZakaznikovS_ = new StatistikaIntervalSpolahlivosti("Priemerný počet zákazníkov čo odišli");
    public StatistikaIntervalSpolahlivosti percentoOdchadzajucichZakaznikovS_ = new StatistikaIntervalSpolahlivosti("% zákazníkov odchádzajúcich bez objednania") ;
    public StatistikaPriemer casniciKolkoPercentNepracovaliS_ = new StatistikaPriemer("Koľko % pracovnej doby nepracovali čašníci");
    public StatistikaPriemer kuchariKolkoPercentNepracovaliS_ = new StatistikaPriemer("Koľko % pracovnej doby nepracovali kuchári");


    public StatistikaPriemer priemernyCasUzavretiaResturacieS_ = new StatistikaPriemer("Priemerný čas zatvorenia reštaurácie");


    private List<IStatistika> statistikyReplikacne = Arrays.asList(
            priemernyCasZakaznikaStravenyCakanimR_
    );

    private List<IStatistika> statistikySimulacne = Arrays.asList(
            priemernyCasZakaznikaStravenyCakanimS_,
            priemernyPocetZakaznikovS_,
            priemernyPocetOdchadzajucichZakaznikovS_,
            percentoOdchadzajucichZakaznikovS_,
            priemernyCasUzavretiaResturacieS_
    );


    // Grafy zavislosti
    boolean grafZavislostiCasnici_ = false;
    boolean grafZavislostiKuchari_ = false;

    public static final double POSUN_CASU_NA_OTVORENIE_RESTAURACIE = 11 * Helper.CASOVE_JEDNOTKY.HODINA.getPocetSekund();

    public SimulaciaRestauracia(double koncovyCasReplikacie, long jednotkovyPosunSimulacnehoCasu, long spomalenieCasu, long pocetReplikacii, long pocetSimulacii, boolean zrychlenaSimulacia, boolean krokovanieSimulacie, boolean chladenie,boolean planujPosunCasu, int pocetCasnikov, int pocetKucharov) {
        super(koncovyCasReplikacie, jednotkovyPosunSimulacnehoCasu, spomalenieCasu, pocetReplikacii, pocetSimulacii,  zrychlenaSimulacia, krokovanieSimulacie, chladenie, planujPosunCasu);
        this.restauracia_ = new Restauracia(this, pocetCasnikov, pocetKucharov );
        this.stavObjektov_ = new StavObjektov();
    }

    public SimulaciaRestauracia(long jednotkovyPosunSimulacnehoCasu, long spomalenieCasu, long pocetReplikacii, boolean zrychlenaSimulacia, boolean krokovanieSimulacie, boolean chladenie, boolean planujPosunCasu, int pocetCasnikov, int pocetKucharov) {
        this(Restauracia.DENNA_OTVARACIA_DOBA,jednotkovyPosunSimulacnehoCasu, spomalenieCasu, pocetReplikacii, 1, zrychlenaSimulacia, krokovanieSimulacie, chladenie, planujPosunCasu, pocetCasnikov, pocetKucharov);
    }

    @Override
    public void predSimulaciou() {
        super.predSimulaciou();
        restauracia_.predSimulaciou();

        statistikySimulacne.forEach(IStatistika::clear);
        
        casniciKolkoPercentNepracovaliS_.clear();

        kuchariKolkoPercentNepracovaliS_.clear();

        refreshGui(CAS_UPDATU.PRED_SIMULACIOU);
    }

    @Override
    public void predReplikaciou(Integer cisloReplikacie) {
        super.predReplikaciou(cisloReplikacie);
        restauracia_.predReplikaciou(cisloReplikacie);

        for (int indexGeneratora = 0; indexGeneratora < generatoryPrichodovZakaznika.size(); indexGeneratora++) {
            GeneratorExponencialne generatorPrichodu = generatoryPrichodovZakaznika.get(indexGeneratora);
            int pocetZakaznikov = indexGeneratora + 1;
            this.planujUdalost(new PrichodZakaznika(generatorPrichodu.generuj(), this, generatorPrichodu, pocetZakaznikov));
        }

        // STATISTiKY
        statistikyReplikacne.forEach(IStatistika::clear);

        refreshGui(CAS_UPDATU.PRED_REPLIKACIOU);
        }

    @Override
    public void poReplikacii(Integer cisloReplikacie) {
        restauracia_.poReplikacii(cisloReplikacie);
        priemernyCasZakaznikaStravenyCakanimS_.pridaj(priemernyCasZakaznikaStravenyCakanimR_.getPriemer());
        priemernyPocetZakaznikovS_.pridaj(restauracia_.getPocetZakaznikov());
        priemernyPocetOdchadzajucichZakaznikovS_.pridaj(restauracia_.getPocetZakaznikovCoOdisli());
        percentoOdchadzajucichZakaznikovS_.pridaj(((double) restauracia_.getPocetZakaznikovCoOdisli()/ (double) restauracia_.getPocetZakaznikov()) * 100);

        //System.out.println("Cislo replikacie: " + cisloReplikacie);
        //statistikySimulacne.forEach(System.out::println);

        ArrayList<Casnik> casnici = restauracia_.getVsetciCasnici();
        StatistikaPriemer priemerCasnici = new StatistikaPriemer("");
        for (Casnik casnik : casnici) {
            priemerCasnici.pridaj(casnik.kolkoPercentCasuJeVolny(this.getCasReplikacie()));
        }
        casniciKolkoPercentNepracovaliS_.pridaj(priemerCasnici.getPriemer());

        ArrayList<Kuchar> kuchari = restauracia_.getVsetciKuchari();
        StatistikaPriemer priemerKuchari = new StatistikaPriemer("");
        for (Kuchar kuchar: kuchari) {
            priemerKuchari.pridaj(kuchar.kolkoPercentCasuJeVolny(this.getCasReplikacie()));
        }
        kuchariKolkoPercentNepracovaliS_.pridaj(priemerKuchari.getPriemer());

        priemernyCasUzavretiaResturacieS_.pridaj(this.getCasReplikacie() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE);
        refreshGui(CAS_UPDATU.PO_REPLIKACII);
    }

    @Override
    public void poSimulacii(int cisloSimulacie) {
        restauracia_.poSimulacii();
        System.out.println("Po simulacii: ");
        System.out.println("Pocet casnikov: " + restauracia_.getPocetVsetkychCasnikov());
        System.out.println("Pocet kucharov: " + restauracia_.getPocetVsetkychKucharov());
        System.out.println(priemernyCasZakaznikaStravenyCakanimS_.toString());
        refreshGui(CAS_UPDATU.PO_SIMULACII);

        PoBehuReplikacieInfo poBehuReplikacieInfo = this.getStatistikyPoBehuReplikacie();
        for (StatistikaInfo statistikaInfo: poBehuReplikacieInfo.statistiky_) {
            System.out.println(statistikaInfo.getNazovStatistiky() + ": " + statistikaInfo.getHodnotaStatistiky());
        }
        //zapisReplikacneStatistiky();

        if (grafZavislostiCasnici_ && cisloSimulacie <= this.getPocetSimulacii()) {
            this.restauracia_.zvysPocetCasnikov();
        }
        if (grafZavislostiKuchari_ && cisloSimulacie <= this.getPocetSimulacii()) {
            this.restauracia_.zvysPocetKucharov();
        }
    }


    public SimulaciaRestauracia setPocetCasnikov(int pocetCasnikov) {
        this.restauracia_.setPocetCasnikov(pocetCasnikov);
        return this;
    }

    public SimulaciaRestauracia setPocetKucharov(int pocetKucharov) {
        this.restauracia_.setPocetKucharov(pocetKucharov);
        return this;
    }

    public Restauracia getRestauracia() {
        return restauracia_;
    }

     public StavObjektov getAktualnyStavObjektovSimulacie() {

        // Kalendar udalosti

         ArrayList<UdalostInfo> kalendarUdalosti =  stavObjektov_.getKalendarUdalosti();
        kalendarUdalosti.clear();
        FrontUdalosti frontUdalosti = getFrontUdalosti();
        for (Udalost udalost: frontUdalosti.getFrontUdalosti()) {
            kalendarUdalosti.add(new UdalostInfo(udalost.getId(), udalost.getCasUdalosti(), udalost.getTypUdalosti().getNazovUdalosti(), udalost.getDodatocneInfo()));
        }
        kalendarUdalosti.sort(UdalostInfo.GetComparator());

        // statistiky

         stavObjektov_.setStatistikyReplikacie(this.getStatistikyVRamciReplikacie());

        // Casnici

         ArrayList<CasnikInfo> casniciInfo = stavObjektov_.getCasniciInfo();
        casniciInfo.clear();
        for (Casnik casnik : restauracia_.getVsetciCasnici()) {
            CasnikInfo casnikInfo = new CasnikInfo();
            casnikInfo.setIdCasnika(casnik.getIdCasnika());
            ObservableList<String> casnikZoznamInformacii = casnikInfo.getInfo();
            casnikZoznamInformacii.add("Cas volny: " + Helper.FormatujSimulacnyCas(casnik.kolkoJeCasnikVolny(this.getCasReplikacie())));
            if (casnik.getObsadeny()) {
                casnikZoznamInformacii.add("Stav: obsadeny");
                casnikZoznamInformacii.add("Druh cinnosti: " + casnik.getVykonavanaCinnost().getPopisCinnosti());
                casnikZoznamInformacii.add("Obsluhuje stol: " + casnik.getObsluhovanyStol().getIdStolu());
            } else {
                casnikZoznamInformacii.add("Stav: volny");

            }
            casniciInfo.add(casnikInfo);

        }

        // Kuchari
        ArrayList<KucharInfo> kuchariInfo = stavObjektov_.getKuchariInfo();
        kuchariInfo.clear();
        for (Kuchar kuchar: restauracia_.getVsetciKuchari()) {
            KucharInfo kucharInfo = new KucharInfo();
            kucharInfo.setIdKuchara(kuchar.getIndexKuchara());
            ObservableList<String> kucharZoznamInformacii = kucharInfo.getInfo();

            kucharZoznamInformacii.add("Cas obsadeny: " + Helper.FormatujSimulacnyCas(kuchar.kolkoJeKucharObsadeny(this.getCasReplikacie())));
            if (kuchar.getObsadeny()) {
                kucharZoznamInformacii.add("Stav: obsadeny");
                kucharZoznamInformacii.add("Pripravuje jedlo: " + kuchar.getZakaznikKtoremuJedloSaRobi().getPokrm().getPopis());
                kucharZoznamInformacii.add("Pre zakaznika: " + kuchar.getZakaznikKtoremuJedloSaRobi().getIdZakaznika() + " zo stola č. " + kuchar.getZakaznikKtoremuJedloSaRobi().getStolPridelenyZakaznikovy().getIdStolu());
                kucharZoznamInformacii.add("Zaciatok pripravy jedla: " + Helper.FormatujSimulacnyCas(kuchar.getCasZaciatkuPripravyJedla() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE) + ", koniec pripravy jedla: " + Helper.FormatujSimulacnyCas(kuchar.getCasKoncaPripravyJedla() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE));
                kucharZoznamInformacii.add( "Kolko percent je hotovych: " + Helper.FormatujDouble(kuchar.kolkoPercentPripravovanehoJedlaJeHotoveho(this.getCasReplikacie()), 2) + "%");
            } else {
                kucharZoznamInformacii.add("Stav: volny");
            }

            kuchariInfo.add(kucharInfo);
        }

        // Stoly
        ArrayList<StolInfo> stolyInfo = stavObjektov_.getStolyInfo();
        stolyInfo.clear();
        for (Stol stol: restauracia_.getVsetkyStoly()) {
            StolInfo stolInfo = new StolInfo();
            stolInfo.setIdStola(stol.getIdStolu());
            ObservableList<String> stolZoznamInformacii = stolInfo.getInfo();
            ArrayList<Zakaznik> zakazniciPriStole = stol.getZakazniciPriStole();

            stolZoznamInformacii.add("Maximalny pocet miest: " + stol.getMaximalnyPocetMiest());
            if (zakazniciPriStole.size() > 0) {
                if (stol.getStavStola() == StolStav.VOLNY) {
                    throw new RuntimeException("Stol nemoze byt volny ak ma zakaznikov");
                }
                stolZoznamInformacii.add("Stav: " + stol.getStavStola().getNazov());
                stolZoznamInformacii.add("Pocet zakaznikov: " + zakazniciPriStole.size());

                for (Zakaznik zakaznik: zakazniciPriStole) {
                    String zakaznikInfo = "Z. č. " + zakaznik.getIdZakaznika() + " č.v: " + Helper.FormatujSimulacnyCas(zakaznik.getCasVstupuDoRestauracie() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE);
                    double casVyhotoveniaJedla = zakaznik.getCasVyhotoveniaJedla();
                    if (casVyhotoveniaJedla > 0.0) {
                        zakaznikInfo += ", jedlo pripravené: " + Helper.FormatujSimulacnyCas(casVyhotoveniaJedla + POSUN_CASU_NA_OTVORENIE_RESTAURACIE);
                    } else {
                        zakaznikInfo += ", jedlo nepripravené";
                    }
                    stolZoznamInformacii.add(zakaznikInfo);
                }
            } else {
                stolZoznamInformacii.add("Stav: volny");
            }

            stolyInfo.add(stolInfo);
        }

        // Front cakajucich na obsluhu
        ArrayList<String> frontCakajucichNaObsluhuInfo = stavObjektov_.getFrontStolyCakajuceNaObsluhu().getInformacieOFronte();
        frontCakajucichNaObsluhuInfo.clear();
        for (Stol stol: restauracia_.getStolyCakajuceNaObjednavkuAUsadenie()) {
            frontCakajucichNaObsluhuInfo.add(
                                    "Stol: " + stol.getIdStolu() +
                                    ", počet zákaznikov: " + stol.getZakazniciPriStole().size() +
                                    " č.zaradenia: " + Helper.FormatujSimulacnyCas(stol.getCasZaradeniaDoFrontuUsadenie() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE)
            );
        }

        // front jedal
        ArrayList<String> fronJedalInfo = stavObjektov_.getFrontZakazniciCakajuciNaObsluhu().getInformacieOFronte();
        fronJedalInfo.clear();
        for (Zakaznik zakaznik: restauracia_.getFrontJedal()) {
            fronJedalInfo.add(
                            "Č. z: " + zakaznik.getIdZakaznika() +
                            ", č.zaradenia: " + Helper.FormatujSimulacnyCas(zakaznik.getCasKoncaUsadenia() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE) + // cas konca usadenia je zaroven cas, kedy bol zaradeny do frontu na objednanie
                            ", stôl: " + zakaznik.getStolPridelenyZakaznikovy().getIdStolu() +
                            ", pokrm: " + zakaznik.getPokrm().getPopis()
            );
        }

        // front cakajucich na donesenie jedla
         ArrayList<String> frontCakajucichNaDonesenieJedla = stavObjektov_.getFrontStolyCakajuceNaDonesenieJedla().getInformacieOFronte();
         frontCakajucichNaDonesenieJedla.clear();
         for (Stol stol: restauracia_.getStolyCakajuceNaDonesenieJedla()) {
             frontCakajucichNaDonesenieJedla.add("Stol: " + stol.getIdStolu() +
                     ", počet zákaznikov: " + stol.getZakazniciPriStole().size() +
                     ", č. zaradenia: " + Helper.FormatujSimulacnyCas(stol.getCasZaradeniaDoFrontuOdneseniaJedal() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE)
                     );
         }

         // front cakajucich na zaplatenie

         ArrayList<String> frontCakajucichNaZaplatenie = stavObjektov_.getFrontStolyCakajuceNaZaplatenie().getInformacieOFronte();
         frontCakajucichNaZaplatenie.clear();
         for (Stol stol: restauracia_.getStolyCakajuceNaZaplatenie()) {
             frontCakajucichNaZaplatenie.add("Stol: " + stol.getIdStolu() +
                     ", počet zákaznikov" + stol.getZakazniciPriStole().size() +
                     ", č. zaradenia: " + Helper.FormatujSimulacnyCas(stol.getCasZaradeniaDoFrontuZaplatenie() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE)
             );
         }

        return stavObjektov_;
    }


    public BehReplikacieInfo getStatistikyVRamciReplikacie() {
        BehReplikacieInfo behReplikacieInfo = new BehReplikacieInfo();
        ObservableList<StatistikaInfo> statistiky = behReplikacieInfo.statistiky_;

        statistiky.add(new StatistikaInfo("Číslo replikácie", this.getCisloReplikacie() + ""));
        statistiky.add(new StatistikaInfo("Simulačný čas", Helper.FormatujSimulacnyCas(this.getCasSimulacie())));
        statistiky.add(new StatistikaInfo("Čas replikácie", Helper.FormatujSimulacnyCas(this.getCasReplikacie() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE)));

        statistiky.add(new StatistikaInfo("Zákazníci",  ""));
        statistiky.add(new StatistikaInfo(priemernyCasZakaznikaStravenyCakanimR_.getNazovStatistiky(), Helper.FormatujDouble(priemernyCasZakaznikaStravenyCakanimR_.getPriemer()) ));
        statistiky.add(new StatistikaInfo("Celkový počet zákazníkov", this.getRestauracia().getPocetZakaznikov() + ""));
        statistiky.add(new StatistikaInfo("Zákazníci čo odišli", this.getRestauracia().getPocetZakaznikovCoOdisli() + ""));
        double percentaZakaznikovCoOdisli = ((double) restauracia_.getPocetZakaznikovCoOdisli() / Math.max(1, (double) restauracia_.getPocetZakaznikov()) ) * 100;
        statistiky.add(new StatistikaInfo("Koľko percent zákazníkov odišlo",  String.format("%.2f" , percentaZakaznikovCoOdisli) + " %"));


        statistiky.add(new StatistikaInfo("Čašníci",  ""));

        statistiky.add(new StatistikaInfo(this.getRestauracia().priemernyPocetVolnychCasnikovR_.getNazovStatistiky(), Helper.FormatujDouble(this.getRestauracia().priemernyPocetVolnychCasnikovR_.getVazenyPriemer()) ));
        statistiky.add(new StatistikaInfo("Počet voľných čašníkov", this.getRestauracia().getPocetVolnychCasnikov() + ""));
        statistiky.add(new StatistikaInfo("Počet pracujúcich čašníkov", this.getRestauracia().getPocetPracujucichCasnikov() + ""));

        int indexCasnika = 1;
        for (Casnik casnik: this.restauracia_.getVsetciCasnici()) {
            statistiky.add(new StatistikaInfo("Čašník č. " + indexCasnika + " nepracoval", Helper.FormatujDouble(casnik.kolkoPercentCasuJeVolny(this.getCasReplikacie())) + "%"));
            indexCasnika++;
        }


        statistiky.add(new StatistikaInfo("Kuchári",  ""));
        statistiky.add(new StatistikaInfo(this.getRestauracia().priemernyPocetVolnychKucharovR_.getNazovStatistiky(), Helper.FormatujDouble(this.getRestauracia().priemernyPocetVolnychKucharovR_.getVazenyPriemer()) ));
        statistiky.add(new StatistikaInfo("Počet voľných kuchárov", this.getRestauracia().getPocetVolnychKucharov() + ""));
        statistiky.add(new StatistikaInfo("Počet pracujúcich kuchárov", this.getRestauracia().getPocetPracujucichKucharov() + ""));
        int indexKuchara = 1;
        for (Kuchar kuchar: this.restauracia_.getVsetciKuchari()) {
            statistiky.add(new StatistikaInfo("Kuchár č. " + indexKuchara + " nepracoval", Helper.FormatujDouble(kuchar.kolkoPercentCasuJeVolny(this.getCasReplikacie())) + "%"));
            indexKuchara++;
        }


        statistiky.add(new StatistikaInfo("Stoly",  ""));
        TreeMap<Integer, ArrayList<Stol>> volneStoly = this.restauracia_.getVolneStoly();
        for (Map.Entry<Integer, ArrayList<Stol>> stoly: volneStoly.entrySet()) {
            statistiky.add(new StatistikaInfo("Počet voľných stolov kapacity " + stoly.getKey(), stoly.getValue().size() + ""));
        }

        for (Map.Entry<Integer, Stol.StatistikaStolov> statistikyStolov: restauracia_.priemernyPocetVolnychStolovJednotlivychKapacitR_.entrySet()) {
            Stol.StatistikaStolov statistikaStolov = statistikyStolov.getValue();
            statistiky.add(new StatistikaInfo(statistikaStolov.getNazovStatistiky(), Helper.FormatujDouble(statistikaStolov.getVazenyPriemer()) ));
        }

        statistiky.add(new StatistikaInfo("Fronty", ""));
        statistiky.add(new StatistikaInfo("Front stolov čakajúcich na objednanie", restauracia_.getStolyCakajuceNaObjednavkuAUsadenie().size() + ""));
        statistiky.add(new StatistikaInfo("Front zákazníkov čakajúcich na prípravu jedla", restauracia_.getFrontJedal().size() + ""));
        statistiky.add(new StatistikaInfo("Front stolov čakajúcich na donesenie jedla", restauracia_.getStolyCakajuceNaDonesenieJedla().size() + ""));
        statistiky.add(new StatistikaInfo("Front stolov čakajúcich na zaplatenie", restauracia_.getStolyCakajuceNaZaplatenie().size() + ""));


        return behReplikacieInfo;
    }

    public PoBehuReplikacieInfo getStatistikyPoBehuReplikacie() {
        PoBehuReplikacieInfo poBehuReplikacieInfo = new PoBehuReplikacieInfo(this.getCisloReplikacie(), this.getPocetReplikacii(), this.getCasReplikacie(), this.priemernyCasZakaznikaStravenyCakanimS_.getPriemer(), this.restauracia_.getPocetVsetkychCasnikov(), this.restauracia_.getPocetVsetkychKucharov());
        ObservableList<StatistikaInfo> statistiky = poBehuReplikacieInfo.statistiky_;

        statistiky.add(new StatistikaInfo("Číslo replikácie", this.getCisloReplikacie() + ""));
        if (this.getCasReplikacie() != 0.0) {
            // statistiky.add(new StatistikaInfo("Čas replikácie", Helper.FormatujSimulacnyCas (this.getCasReplikacie() + POSUN_CASU_NA_OTVORENIE_RESTAURACIE)));
        }

        statistiky.add(new StatistikaInfo(priemernyCasUzavretiaResturacieS_.getNazovStatistiky(), Helper.FormatujSimulacnyCas(priemernyCasUzavretiaResturacieS_.getPriemer())));

        statistiky.add(new StatistikaInfo(priemernyCasZakaznikaStravenyCakanimS_.getNazovStatistiky(), priemernyCasZakaznikaStravenyCakanimS_.getIntervalSpolahlivosti()));
        statistiky.add(new StatistikaInfo( priemernyPocetZakaznikovS_.getNazovStatistiky(), Helper.FormatujDouble(priemernyPocetZakaznikovS_.getPriemer())));
        statistiky.add(new StatistikaInfo( priemernyPocetOdchadzajucichZakaznikovS_.getNazovStatistiky(), Helper.FormatujDouble(priemernyPocetOdchadzajucichZakaznikovS_.getPriemer())));

        statistiky.add(new StatistikaInfo(percentoOdchadzajucichZakaznikovS_.getNazovStatistiky(),  percentoOdchadzajucichZakaznikovS_.getIntervalSpolahlivosti()));

        statistiky.add(new StatistikaInfo(casniciKolkoPercentNepracovaliS_.getNazovStatistiky(),  Helper.FormatujDouble(casniciKolkoPercentNepracovaliS_.getPriemer()) + "%"));
        statistiky.add(new StatistikaInfo(kuchariKolkoPercentNepracovaliS_.getNazovStatistiky(),  Helper.FormatujDouble(kuchariKolkoPercentNepracovaliS_.getPriemer()) + "%"));



        statistiky.add(new StatistikaInfo(restauracia_.priemernyPocetVolnychCasnikovS_.getNazovStatistiky(), Helper.FormatujDouble(restauracia_.priemernyPocetVolnychCasnikovS_.getPriemer())));
        statistiky.add(new StatistikaInfo(restauracia_.priemernyPocetVolnychKucharovS_.getNazovStatistiky(), Helper.FormatujDouble(restauracia_.priemernyPocetVolnychKucharovS_.getPriemer())));
        for (Map.Entry<Integer, StatistikaPriemer> statistika :restauracia_.priemernyPocetVolnychStolovJednotlivychKapacitS_.entrySet()) {
            statistiky.add(new StatistikaInfo(statistika.getValue().getNazovStatistiky(), Helper.FormatujDouble(statistika.getValue().getPriemer())));
        }



        return poBehuReplikacieInfo;
    }


    public SimulaciaRestauracia setGrafZavislostiCasnici(boolean grafZavislostiCasnici) {
        this.grafZavislostiCasnici_ = grafZavislostiCasnici;
        return this;
    }

    public SimulaciaRestauracia setGrafZavislostiKuchari(boolean grafZavislostiKuchari) {
        this.grafZavislostiKuchari_ = grafZavislostiKuchari;
        return this;
    }

    private void zapisReplikacneStatistiky() {
        final String separator = ";";

        PoBehuReplikacieInfo statistiky = getStatistikyPoBehuReplikacie();
        final String cestaKSuboru = "StatistikaCasnici_" + statistiky.pocetCasnikov_ + "Kuchari_" + statistiky.pocetKucharov_ + "Replikacie_" + statistiky.cisloReplikacie_ + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cestaKSuboru)))
        {
            for (StatistikaInfo  info: statistiky.statistiky_) {
                writer.write(info.getNazovStatistiky() + separator + info.getHodnotaStatistiky() + separator);
                writer.newLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
